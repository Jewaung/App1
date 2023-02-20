package com.example.app1

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var firstName: String? = null
    private var middleName: String? = null
    private var lastName: String? = null

    private var firstEdit: EditText? = null
    private var middleEdit: EditText? = null
    private var lastEdit: EditText? = null

    private var cameraButton: Button? = null
    private var profilePicture: ImageView? = null

    private var submitButton: Button? = null

    private var loginIntent: Intent? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firstEdit = findViewById(R.id.first_data)
        middleEdit = findViewById(R.id.middle_data)
        lastEdit = findViewById(R.id.last_data)

        cameraButton = findViewById(R.id.button_camera)
        profilePicture = findViewById(R.id.image)

        submitButton = findViewById(R.id.button_submit)

        cameraButton!!.setOnClickListener(this)
        submitButton!!.setOnClickListener(this)

        loginIntent = Intent(this, LoginActivity::class.java)

        if (savedInstanceState != null) {
            //names and picture
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.button_camera -> {
               val cameraApp = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
               try {
                   openCamera.launch(cameraApp)
               } catch (ex:ActivityNotFoundException) {
                   cameraButton!!.text = "Try again"
               }
            }
            R.id.button_submit -> {
                //save names and start new activity
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

    }
    private val openCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
        if (result.resultCode == RESULT_OK) {
            profilePicture = findViewById(R.id.image)
            val image = result.data!!.getParcelableExtra("data", Bitmap::class.java)
            profilePicture!!.setImageBitmap(image)
            cameraButton!!.text = "Retake?"
        }
    }
}