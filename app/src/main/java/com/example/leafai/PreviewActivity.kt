package com.example.leafai

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.ByteOrder

class PreviewActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var detectButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var textResult: TextView
    private var interpreter: Interpreter? = null
    private var bitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)

        imageView = findViewById(R.id.previewImage)
        detectButton = findViewById(R.id.buttonDetect)
        progressBar = findViewById(R.id.progressBar)
        textResult = findViewById(R.id.textResult)

        val imageUri = intent.getStringExtra("imageUri")
        imageUri?.let {
            val uri = Uri.parse(it)
            contentResolver.openInputStream(uri)?.use { stream ->
                bitmap = BitmapFactory.decodeStream(stream)
                imageView.setImageBitmap(bitmap)
            }
        }

        loadModel()

        detectButton.setOnClickListener {
            detectDisease()
        }
    }

    private fun loadModel() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val crop = prefs.getString("default_crop", "Coffee") ?: "Coffee"
        val modelName = when (crop) {
            "Coffee" -> "coffee_leaf_model.tflite"
            "Cotton" -> "cotton_model.tflite"
            "Tea" -> "model.tflite"
            else -> "coffee_leaf_model.tflite"
        }

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
        val b = bitmap ?: return
        progressBar.visibility = ProgressBar.VISIBLE
        val resized = Bitmap.createScaledBitmap(b, 224, 224, true)

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

        val output = Array(1) { FloatArray(3) } // Adjust if your model classes differ
        interpreter?.run(input, output)

        val probs = output[0]
        val maxIdx = probs.indices.maxByOrNull { probs[it] } ?: -1
        val confidence = "%.2f%%".format(probs.getOrElse(maxIdx) { 0f } * 100)

        runOnUiThread {
            textResult.text = "Class: $maxIdx, Confidence: $confidence"
            progressBar.visibility = ProgressBar.GONE
        }
    }
}
