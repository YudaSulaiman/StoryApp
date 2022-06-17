package com.example.storyapp.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.storyapp.R
import com.example.storyapp.data.Fetch
import com.example.storyapp.data.response.LoginResult
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.model.SystemPreferences
import com.example.storyapp.model.UserModel
import com.example.storyapp.view.ViewModelFactory
import com.example.storyapp.view.main.MainActivity


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var userModel: UserModel
    private lateinit var mSystemPreferences: SystemPreferences

    val viewModel: LoginViewModel by viewModels{
        ViewModelFactory.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = View.INVISIBLE

        mSystemPreferences = SystemPreferences(this)
        userModel = mSystemPreferences.getUser()
        if (userModel.isLogin) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        setupAction()
    }

    private fun setupAction(){
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (binding.emailEditTextLayout.error == null && binding.passwordEditTextLayout.error == null &&
                email.isNotEmpty() && password.isNotEmpty()){
                viewModel.userLogin(email, password).observe(this@LoginActivity){ result ->
                    if (result != null) {
                        when (result) {
                            is Fetch.Loading -> {
                                binding.progressBar.visibility = View.VISIBLE
                            }
                            is Fetch.Success -> {
                                binding.progressBar.visibility = View.GONE
                                val data = result.data
                                inputData(data)
                            }
                            is Fetch.Error -> {
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(
                                    this,
                                    "Password atau email salah",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.field_invalid),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun inputData(data: LoginResult){
        val systemPreferences = SystemPreferences(this@LoginActivity)
        systemPreferences.setUser(UserModel(name = data.name, token = data.token, userId = data.userId,isLogin = true))
        AlertDialog.Builder(this).apply {
            setTitle("Login " + getString(R.string.success))
            setPositiveButton("OK") { _, _ ->
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            create()
            show()
        }
    }
}