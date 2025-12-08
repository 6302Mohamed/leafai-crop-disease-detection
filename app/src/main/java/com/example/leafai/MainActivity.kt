package com.example.leafai

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import androidx.camera.view.PreviewView
import androidx.activity.OnBackPressedCallback
import com.example.leafai.weather.WeatherService
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.ByteOrder

class MainActivity : AppCompatActivity() {

    private lateinit var previewView: PreviewView
    private lateinit var imageView: ImageView
    private lateinit var progressBar: ProgressBar

    private lateinit var buttonSelectImage: Button
    private lateinit var buttonCaptureImage: Button
    private lateinit var buttonDetect: Button
    private lateinit var buttonAskExpert: Button

    private lateinit var textConfidence: TextView
    private lateinit var textDisease: TextView
    private lateinit var textSymptoms: TextView
    private lateinit var textCauses: TextView
    private lateinit var textTreatment: TextView
    private lateinit var textPrevention: TextView

    private lateinit var textWeatherDesc: TextView
    private lateinit var textWeatherTemp: TextView

    private var selectedBitmap: Bitmap? = null
    private var interpreter: Interpreter? = null

    private val cropClassMap = mapOf(
        "Coffee" to listOf("Healthy leaves", "Red Rust", "Phoma"),
        "Cotton" to listOf("Bacterial blight", "Curl virus", "Fussarium wilt", "Healthy"),
        "Tea" to listOf("Not Suffering from Red Rust", "Suffering from Red Rust")
    )

    private val cropModelMap = mapOf(
        "Coffee" to "coffee_leaf_model.tflite",
        "Cotton" to "cotton_model.tflite",
        "Tea" to "model.tflite"
    )

    data class DiseaseInfo(
        val symptoms: List<String>,
        val treatment: String,
        val causes: String,
        val prevention: String
    )

    private val diseaseInfoMap = mapOf(
        "Healthy leaves" to DiseaseInfo(
            listOf("No spots or lesions", "Normal green color", "Uniform leaf shape"),
            "No treatment needed.", "Normal healthy leaf condition.", "Maintain good crop care."
        ),
        "Red Rust" to DiseaseInfo(
            listOf("Orange-red powdery spots", "Premature leaf drop", "Yellowing around spots"),
            "Apply recommended fungicides.",
            "Fungal infection caused by Hemileia vastatrix.",
            "Regular monitoring and fungicide sprays."
        ),
        "Phoma" to DiseaseInfo(
            listOf("Brown lesions with dark margins", "Leaf necrosis", "Leaf curling"),
            "Use appropriate fungicides.",
            "Fungal pathogen Phoma spp.",
            "Crop rotation and sanitation."
        ),
        "Bacterial blight" to DiseaseInfo(
            listOf("Water-soaked leaf spots", "Angular lesions on leaves", "Defoliation"),
            "Use bactericides and resistant varieties.",
            "Bacterial infection.",
            "Crop hygiene and resistant strains."
        ),
        "Curl virus" to DiseaseInfo(
            listOf("Leaf curling", "Yellowing between veins", "Stunted growth"),
            "Remove infected plants.",
            "Viral infection transmitted by whiteflies.",
            "Control whiteflies and plant resistant varieties."
        ),
        "Fussarium wilt" to DiseaseInfo(
            listOf("Wilting of leaves", "Yellowing of lower leaves", "Brown streaks in stems"),
            "Fungicide soil treatments.",
            "Soil-borne fungal infection Fusarium oxysporum.",
            "Crop rotation and soil sterilization."
        ),
        "Healthy" to DiseaseInfo(
            listOf("No spots", "Normal leaf structure", "Healthy color"),
            "None required.", "Healthy plant.", "Maintain good crop health."
        ),
        "Not Suffering from Red Rust" to DiseaseInfo(
            listOf("No rust spots", "No leaf yellowing", "No defoliation"),
            "No treatment needed.",
            "Healthy leaf condition.",
            "Good cultural practices."
        ),
        "Suffering from Red Rust" to DiseaseInfo(
            listOf("Yellow to orange powdery spots", "Yellow halos around spots", "Leaf drop"),
            "Fungicide applications.",
            "Fungal infection caused by Hemileia vastatrix.",
            "Fungicide sprays and resistant cultivars."
        )
    )

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val inputStream = contentResolver.openInputStream(it)
            selectedBitmap = BitmapFactory.decodeStream(inputStream)
            imageView.setImageBitmap(selectedBitmap)
            imageView.visibility = ImageView.VISIBLE
            previewView.visibility = PreviewView.GONE
        }
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val byteArray = result.data?.getByteArrayExtra("imageBitmap")
            byteArray?.let {
                val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                selectedBitmap = bitmap
                imageView.setImageBitmap(bitmap)
                imageView.visibility = ImageView.VISIBLE
                previewView.visibility = PreviewView.GONE
                detectDisease()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        previewView = findViewById(R.id.previewView)
        imageView = findViewById(R.id.imageView)
        progressBar = findViewById(R.id.progressBar)

        buttonSelectImage = findViewById(R.id.buttonSelectImage)
        buttonCaptureImage = findViewById(R.id.buttonCaptureImage)
        buttonDetect = findViewById(R.id.buttonDetect)
        buttonAskExpert = findViewById(R.id.buttonAskExpert)

        textConfidence = findViewById(R.id.textConfidence)
        textDisease = findViewById(R.id.textDisease)
        textSymptoms = findViewById(R.id.textSymptoms)
        textCauses = findViewById(R.id.textCauses)
        textTreatment = findViewById(R.id.textTreatment)
        textPrevention = findViewById(R.id.textPrevention)

        textWeatherDesc = findViewById(R.id.textWeatherDesc)
        textWeatherTemp = findViewById(R.id.textWeatherTemp)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 100)
        }

        buttonSelectImage.setOnClickListener { galleryLauncher.launch("image/*") }

        buttonCaptureImage.setOnClickListener {
            cameraLauncher.launch(Intent(this, CameraActivity::class.java))
        }

        buttonDetect.setOnClickListener { detectDisease() }

        buttonAskExpert.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://gemini.google.com/app")))
        }

        // <-- HERE IS THE ADDED ONBACKPRESSED CALLBACK
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                moveTaskToBack(true)
            }
        })

        loadModel()
        loadWeather()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Camera permission denied. The app may not work properly.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun loadModel() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val crop = prefs.getString("default_crop", "Coffee") ?: "Coffee"
        val modelName = cropModelMap[crop] ?: return

        assets.openFd(modelName).use { fd ->
            fd.createInputStream().use { inputStream ->
                val modelBytes = inputStream.readBytes()
                val modelBuffer = ByteBuffer.allocateDirect(modelBytes.size).apply {
                    order(ByteOrder.nativeOrder())
                    put(modelBytes)
                    rewind()
                }
                interpreter = Interpreter(modelBuffer)
            }
        }
    }

    private fun detectDisease() {
        val bitmap = selectedBitmap ?: return

        progressBar.visibility = ProgressBar.VISIBLE

        val resized = Bitmap.createScaledBitmap(bitmap, 224, 224, true)

        val input = ByteBuffer.allocateDirect(4 * 224 * 224 * 3).apply {
            order(ByteOrder.nativeOrder())
            for (y in 0 until 224) {
                for (x in 0 until 224) {
                    val pixel = resized.getPixel(x, y)
                    putFloat(((pixel shr 16) and 0xFF) / 255.0f)
                    putFloat(((pixel shr 8) and 0xFF) / 255.0f)
                    putFloat((pixel and 0xFF) / 255.0f)
                }
            }
        }

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val crop = prefs.getString("default_crop", "Coffee") ?: "Coffee"
        val output = Array(1) { FloatArray(cropClassMap[crop]?.size ?: 1) }

        interpreter?.run(input, output)

        val probs = output[0]
        val maxIdx = probs.indices.maxByOrNull { probs[it] } ?: -1
        val className = cropClassMap[crop]?.getOrNull(maxIdx) ?: "Unknown"
        val confidence = "%.2f%%".format(probs.getOrElse(maxIdx) { 0f } * 100)

        runOnUiThread {
            textConfidence.text = confidence
            textDisease.text = className

            val info = diseaseInfoMap[className]
            textSymptoms.text = info?.symptoms?.joinToString("\n") ?: "N/A"
            textCauses.text = info?.causes ?: "N/A"
            textTreatment.text = info?.treatment ?: "N/A"
            textPrevention.text = info?.prevention ?: "N/A"

            progressBar.visibility = ProgressBar.GONE
        }
    }

    private fun loadWeather() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val city = prefs.getString("region", "Kampala") ?: "Kampala"
        val apiKey = "a83ee1b30c24cf84757bd173badf021e"

        runOnUiThread {
            textWeatherDesc.text = "Loading weather..."
            textWeatherTemp.text = ""
        }

        WeatherService.fetchWeather(city, apiKey) { response ->
            runOnUiThread {
                if (response != null) {
                    val desc = response.weather.firstOrNull()?.description?.replaceFirstChar { it.uppercase() } ?: "N/A"
                    val temp = response.main.temp
                    textWeatherDesc.text = desc
                    textWeatherTemp.text = "Temperature: %.1f°C".format(temp)
                } else {
                    textWeatherDesc.text = "Failed to load weather"
                    textWeatherTemp.text = ""
                }
            }
        }
    }
}
