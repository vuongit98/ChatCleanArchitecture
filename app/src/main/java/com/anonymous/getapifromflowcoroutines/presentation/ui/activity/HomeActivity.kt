package com.anonymous.getapifromflowcoroutines.presentation.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.anonymous.getapifromflowcoroutines.databinding.ActivityHomeBinding

import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    lateinit var viewBinding : ActivityHomeBinding
    companion object {
        val uid = FirebaseAuth.getInstance().uid
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

    }
}