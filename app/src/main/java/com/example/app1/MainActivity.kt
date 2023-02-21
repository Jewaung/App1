package com.example.app1

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.PersistableBundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var firstName: String? = null
    private var middleName: String? = null
    private var lastName: String? = null
    private var imagePath: String? = null

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
            val fn = savedInstanceState.getString("FIRST_NAME")
            if (fn != null)
                firstName = fn
            val mn = savedInstanceState.getString("MIDDLE_NAME")
            if (mn != null)
                middleName = mn
            val ln = savedInstanceState.getString("LAST_NAME")
            if (ln != null)
                lastName = ln
            val imagePath = savedInstanceState.getString("IMAGE_PATH")
            if (imagePath != null) {
                val image = BitmapFactory.decodeFile(imagePath)
                if (image != null)
                    profilePicture!!.setImageBitmap(image)
            }
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val fn = savedInstanceState.getString("FIRST_NAME")
        if (fn != null)
            firstName = fn
        val mn = savedInstanceState.getString("MIDDLE_NAME")
        if (mn != null)
            middleName = mn
        val ln = savedInstanceState.getString("LAST_NAME")
        if (ln != null)
            lastName = ln
        val path = savedInstanceState.getString("IMAGE_PATH")
        if (path != null) {
            imagePath = path
            val image = BitmapFactory.decodeFile(path)
            if (image != null)
                profilePicture!!.setImageBitmap(image)
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
                firstName = firstEdit!!.text.toString()
                middleName = middleEdit!!.text.toString()
                lastName = lastEdit!!.text.toString()
                if (firstName == "" || middleName == "" || lastName =="" || imagePath == null) {
                    Toast.makeText(this@MainActivity, "All fields are required", Toast.LENGTH_SHORT).show()
                    return
                }
                loginIntent!!.putExtra("FIRST_NAME", firstName)
                loginIntent!!.putExtra("LAST_NAME", lastName)
                startActivity(loginIntent)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("FIRST_NAME", firstName)
        outState.putString("MIDDLE_NAME", middleName)
        outState.putString("LAST_NAME", lastName)
        outState.putString("IMAGE_PATH", imagePath)
    }
    private val openCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
        if (result.resultCode == RESULT_OK) {
            profilePicture = findViewById(R.id.image)
            val image = result.data!!.getParcelableExtra("data", Bitmap::class.java)
            profilePicture!!.setImageBitmap(image)
            cameraButton!!.text = "Retake?"
            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
                val path = saveImage(image)
                imagePath = path
            }
        }
    }

    private fun saveImage(imageBitmap: Bitmap?): String {
        val dirRoot = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val localDir = File("$dirRoot/saved_images")
        localDir.mkdirs()
        val time = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val name = "Image_$time.jpg"
        val file = File(localDir, name)
        if (file.exists())
            file.delete()
        try {
            val output = FileOutputStream(file)
            imageBitmap!!.compress(Bitmap.CompressFormat.JPEG, 90, output)
            output.flush()
            output.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return file.absolutePath

    }
}