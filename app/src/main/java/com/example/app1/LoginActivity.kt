package com.example.app1

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity(){
    var textFullName: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        textFullName = findViewById(R.id.login_confirmed)

        val intentFromMain = intent

        var first = intentFromMain.getStringExtra("FIRST_NAME")
        var last = intentFromMain.getStringExtra("LAST_NAME")

        textFullName!!.text = first + " " + last + " is logged in!"
    }
}