package com.example.leafai

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabDots: TabLayout
    private lateinit var btnNext: Button
    private lateinit var onboardingAdapter: OnboardingAdapter

    // Store answers as user selects (initially empty strings)
    private val answers = mutableListOf<String?>("", "", "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        viewPager = findViewById(R.id.viewPager)
        tabDots = findViewById(R.id.tabDots)
        btnNext = findViewById(R.id.btnNext)

        val onboardingItems = listOf(
            OnboardingItem(
                question = "What country are you from?",
                lottieFile = "n.json",
                options = listOf("Uganda", "Kenya", "Tanzania", "Somaliland", "Ethiopia","Malawi", "Zambia", "Zimbabwe",)
            ),
            OnboardingItem(
                question = "What region?",
                lottieFile = "farmer.json",
                options = listOf("Central", "North", "West", "East")
            ),
            OnboardingItem(
                question = "What crop are you harvesting?",
                lottieFile = "tree.json",
                options = listOf("Coffee", "Tea", "Cotton")
            )
        )

        onboardingAdapter = OnboardingAdapter(onboardingItems, answers)
        viewPager.adapter = onboardingAdapter

        TabLayoutMediator(tabDots, viewPager) { _, _ -> }.attach()

        btnNext.setOnClickListener {
            if (viewPager.currentItem < onboardingItems.lastIndex) {
                viewPager.currentItem += 1
            } else {
                val prefs = PreferenceManager.getDefaultSharedPreferences(this)
                with(prefs.edit()) {
                    putString("country", answers[0])
                    putString("region", answers[1])
                    putString("default_crop", answers[2])
                    apply()
                }

                startActivity(Intent(this, SignInActivity::class.java))
                finish()

            }
        }
    }
}
