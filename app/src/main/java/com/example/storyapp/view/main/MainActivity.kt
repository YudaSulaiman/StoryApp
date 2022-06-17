package com.example.storyapp.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.model.SystemPreferences
import com.example.storyapp.model.UserModel
import com.example.storyapp.view.StoryModelFactory
import com.example.storyapp.view.addstory.AddStoryActivity
import com.example.storyapp.view.maps.MapsActivity
import com.example.storyapp.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userModel: UserModel
    private lateinit var mSystemPreferences: SystemPreferences

    val viewModel: MainViewModel by viewModels{
        StoryModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.progressBar.visibility = View.INVISIBLE

        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStory.addItemDecoration(itemDecoration)

        mSystemPreferences = SystemPreferences(this@MainActivity)
        userModel = mSystemPreferences.getUser()

        getData()
        setupViewModel()
        setupAction()
    }

    private fun getData(){
        val adapter = StoryListAdapter()
        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        viewModel.getStory("Bearer " + userModel.token.toString()).observe(this) {
            adapter.submitData(lifecycle, it)
            Log.i("RESULT MAIN", it.toString())
        }
    }

    private fun setupViewModel() {

        if (userModel.isLogin) {
            binding.tvTest.text = getString(R.string.greeting, userModel.name)
        } else {
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        }
    }

    private fun setupAction() {
        binding.buttonLogout.setOnClickListener{
            val systemPreferences = SystemPreferences(this)
            systemPreferences.setUser(UserModel(name = null, token = null, userId = null, isLogin = false))
            Log.d("TOKEN", userModel.token.toString())
            AlertDialog.Builder(this).apply {
                setTitle("Logout " + getString(R.string.success))
                setPositiveButton("OK") { _, _ ->
                    val intent = Intent(this@MainActivity, WelcomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }
        }

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this@MainActivity, AddStoryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean{
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_maps -> {
                val moveToSettingActivity = Intent(this, MapsActivity::class.java)
                startActivity(moveToSettingActivity)
                true
            }
            else -> true
        }
    }
}