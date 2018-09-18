package com.macrohard.cooklit.model.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button

import com.macrohard.cooklit.R


class FirstTimeActivity : AppCompatActivity() {

    internal var startAppButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val basicSharedPreference = getSharedPreferences(SHARED_PREFERENCE_APP_BASICS, Context.MODE_PRIVATE)
        if (!basicSharedPreference.getBoolean(SHARED_PREFERENCE_FIRST_TIME, true)) {
            val myKitchenIntent = Intent(this@FirstTimeActivity, MyKitchenActivity)
            startActivity(myKitchenIntent)
            finish()
            return
        }

        val sharedPreferencesEditor = getSharedPreferences(SHARED_PREFERENCE_APP_BASICS, Context.MODE_PRIVATE).edit()
        sharedPreferencesEditor.putBoolean(SHARED_PREFERENCE_FIRST_TIME, true)
        sharedPreferencesEditor.putBoolean(SHARED_PREFERENCE_TUTORIAL_DONE, false)
        sharedPreferencesEditor.apply()

        setContentView(R.layout.activity_first_time)

        startAppButton = findViewById(R.id.button_startUpCookLit)
        startAppButton.setOnClickListener { startTour() }
    }

    private fun startTour() {
        val sharedPreferencesEditor = getSharedPreferences(SHARED_PREFERENCE_APP_BASICS, Context.MODE_PRIVATE).edit()
        sharedPreferencesEditor.putBoolean(SHARED_PREFERENCE_FIRST_TIME, false)
        sharedPreferencesEditor.apply()

        val intent = Intent(this, MyKitchenActivity::class.java)
        startActivity(intent)
    }

    companion object {

        var SHARED_PREFERENCE_APP_BASICS = "appBasics"
        var SHARED_PREFERENCE_FIRST_TIME = "firstTime"
        var SHARED_PREFERENCE_TUTORIAL_DONE = "tutorialCompleted"
    }


}
