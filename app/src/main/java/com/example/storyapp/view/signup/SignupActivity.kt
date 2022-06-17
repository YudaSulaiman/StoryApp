package com.example.storyapp.view.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.storyapp.R
import com.example.storyapp.data.Fetch
import com.example.storyapp.databinding.ActivitySignupBinding
import com.example.storyapp.view.ViewModelFactory

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    val viewModel: SignupViewModel by viewModels {
        ViewModelFactory.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = View.INVISIBLE

        setupAction()
    }

    private fun setupAction(){
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (binding.emailEditTextLayout.error == null && binding.passwordEditTextLayout.error == null && name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()){
                viewModel.userSignup(name, email, password).observe(this@SignupActivity){ result ->
                    if (result != null) {
                        when (result) {
                            is Fetch.Loading -> {
                                binding.progressBar.visibility = View.VISIBLE
                            }
                            is Fetch.Success -> {
                                binding.progressBar.visibility = View.GONE
                                val data = result.data
                                Toast.makeText(
                                    this,
                                    "Status : $data",
                                    Toast.LENGTH_SHORT
                                ).show()
                                AlertDialog.Builder(this).apply {
                                    setTitle("Signup " + getString(R.string.success))
                                    setPositiveButton("OK") { _, _ ->
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
}