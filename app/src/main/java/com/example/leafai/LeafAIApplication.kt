package com.example.leafai

import android.app.Application
import com.google.firebase.FirebaseApp

class LeafAIApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize Firebase once for the whole app
        FirebaseApp.initializeApp(this)
    }
}
