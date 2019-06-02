package com.example.sequencegame

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import kotlinx.android.synthetic.main.activity_start.*

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class StartActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        showNextActivity()
    }

    fun showNextActivity(){
        val thread = Thread(){
            run {
                Thread.sleep(2000)
                val sharedPref = this.getSharedPreferences("SharedData", Context.MODE_PRIVATE)
                val name = sharedPref.getString("UserLogin", "")
                val password = sharedPref.getString("UserPassword", "")
                var intent = Intent()
                if (name == "" && password == "") {
                    intent = Intent(this, LoginActivity::class.java)
                }
                else {
                    intent = Intent(this, MainActivity::class.java)
                }

                startActivity(intent)
                finish()

            }
        }
        thread.start()
    }

}
