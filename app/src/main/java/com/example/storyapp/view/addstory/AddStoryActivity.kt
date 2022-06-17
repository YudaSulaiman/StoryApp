package com.example.storyapp.view.addstory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.storyapp.R
import com.example.storyapp.data.Fetch
import com.example.storyapp.databinding.ActivityAddStoryBinding
import com.example.storyapp.model.SystemPreferences
import com.example.storyapp.model.UserModel
import com.example.storyapp.view.ViewModelFactory
import com.example.storyapp.utils.createTempFile
import com.example.storyapp.view.main.MainActivity
import com.example.storyapp.utils.reduceFileImage
import com.example.storyapp.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var userModel: UserModel
    private lateinit var mSystemPreferences: SystemPreferences

    val viewModel: CameraViewModel by viewModels{
        ViewModelFactory.getInstance()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = View.INVISIBLE

        mSystemPreferences = SystemPreferences(this)
        userModel = mSystemPreferences.getUser()

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        binding.cameraButton.setOnClickListener { startTakePhoto() }
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.uploadButton.setOnClickListener { uploadImage() }
    }

    private fun uploadImage(){

        val desc = binding.formDescription.text.toString()

        if (getFile != null && desc.isNotEmpty()) {

            val file = reduceFileImage(getFile as File)

            val description = desc.toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )
            Log.i("IMAGEMULTIPART", imageMultipart.toString())
            Log.i("DESCRIPTION", description.toString())

            viewModel.addStory("Bearer " + userModel.token.toString(), imageMultipart, description).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Fetch.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is Fetch.Success -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                this,
                                "UPLOAD " + getString(R.string.success),
                                Toast.LENGTH_LONG
                            ).show()
                            AlertDialog.Builder(this).apply {
                                setTitle("Story " + getString(R.string.success))
                                setPositiveButton("OK") { _, _ ->
                                    val intent = Intent(this@AddStoryActivity, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()
                                }
                                create()
                                show()
                            }
                        }
                        is Fetch.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                this,
                                result.error,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        } else {
            Toast.makeText(this@AddStoryActivity, getString(R.string.desc_warning), Toast.LENGTH_SHORT).show()
        }
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@AddStoryActivity,
                "com.example.storyapp.view.addstory",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private var getFile: File? = null

    private lateinit var currentPhotoPath: String
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile

            val result =  BitmapFactory.decodeFile(myFile.path)
//            Silakan gunakan kode ini jika mengalami perubahan rotasi
//            val result = rotateBitmap(
//                BitmapFactory.decodeFile(myFile.path),
//                true
//            )

            binding.ivAddstory.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@AddStoryActivity)
            getFile = myFile

            binding.ivAddstory.setImageURI(selectedImg)
        }
    }

    companion object {

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}