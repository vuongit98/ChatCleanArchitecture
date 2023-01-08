package com.anonymous.getapifromflowcoroutines.presentation.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.coroutineScope
import com.anonymous.getapifromflowcoroutines.data.Model.Account
import com.anonymous.getapifromflowcoroutines.databinding.ActivityRegisterBinding
import com.anonymous.getapifromflowcoroutines.presentation.ui.ViewModel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    lateinit var viewBinding : ActivityRegisterBinding
    val viewModelRegister : RegisterViewModel by viewModels()
    var checkGender : Int = 0 ;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.btnMale.setOnClickListener {
            viewBinding.btnFemale.isChecked = false
            checkGender = 1 ;
        }
        viewBinding.btnFemale.setOnClickListener {
            viewBinding.btnMale.isChecked = false
            checkGender =0 ;
        }
        lifecycle.coroutineScope.launchWhenStarted {
            viewModelRegister._registerFlow.collectLatest{
                if (it.isLoading == true) {
                    Toast.makeText(this@RegisterActivity, "Loading....", Toast.LENGTH_SHORT).show()
                }
                else if (it.error.isNotBlank() == false) {
                    Toast.makeText(this@RegisterActivity, "Register successfully....", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    finish()
                }
                else {
                    Toast.makeText(this@RegisterActivity, "${it.error}", Toast.LENGTH_SHORT).show()

                }
            }
        }
        viewBinding.btnRegister.setOnClickListener {
            val strEmail = viewBinding.edtEmail.text.toString().trim()
            val strPass  = viewBinding.edtPassword.text.toString().trim()
            val strPhone = viewBinding.edtPhone.text.toString().trim()
            val strAddress  = viewBinding.edtAddress.text.toString().trim()
            val user = Account("",strEmail,strPass,strAddress,strPhone,checkGender)
            viewModelRegister.register(user)
        }
    }
}