package com.anonymous.getapifromflowcoroutines.presentation.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import com.anonymous.getapifromflowcoroutines.data.Model.Account
import com.anonymous.getapifromflowcoroutines.databinding.ActivityLoginBinding
import com.anonymous.getapifromflowcoroutines.presentation.ui.ViewModel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    lateinit var viewBinding: ActivityLoginBinding
    val viewModeLogin : LoginViewModel by viewModels()
    companion object{
        var accountLogin : Account? = null;
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        lifecycle.coroutineScope.launchWhenCreated {
            viewModeLogin._loginFlow.collectLatest{
                if (it.isLoading) {
                    Toast.makeText(this@LoginActivity, "Loading...", Toast.LENGTH_SHORT).show()
                }
                if(it.error.isNotBlank()){
                    Toast.makeText(this@LoginActivity, "${it.error}", Toast.LENGTH_SHORT).show()
                }
                it.results?.let {
                    Toast.makeText(this@LoginActivity, "${it}",Toast.LENGTH_SHORT).show()
                    accountLogin = it ;

                    startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                }
            }
        }

        viewBinding.btnLogin.setOnClickListener {
            val strEmail = viewBinding.edtEmail.text.toString()
            val strPass  = viewBinding.edtPass.text.toString()
            lifecycleScope.launch {
                viewModeLogin.login(strEmail, strPass)
                Log.d("viewModeLogin", "Clicked ")
            }
        }
        viewBinding.txtForgot.setOnClickListener {

        }
        viewBinding.txtCreate.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            finish()
        }
    }
}