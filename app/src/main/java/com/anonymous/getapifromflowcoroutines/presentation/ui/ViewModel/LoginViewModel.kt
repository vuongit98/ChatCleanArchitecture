package com.anonymous.getapifromflowcoroutines.presentation.ui.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anonymous.getapifromflowcoroutines.Utils.Resource
import com.anonymous.getapifromflowcoroutines.domain.Usecases.usecases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class LoginViewModel @Inject constructor(val usercase : usecases) : ViewModel() {
    private val loginFlow = MutableStateFlow(LoginState(true))
    public var _loginFlow : StateFlow<LoginState> = loginFlow
  

     fun login(email : String , pass : String) {
        viewModelScope.launch{
            val result = usercase.loginUsecase(email,pass)
            result.collect{
                when(it) {

                    is Resource.Loading ->{

                        loginFlow.value = LoginState(true, null, "")
                    }
                    is Resource.Success -> {
                        loginFlow.value = LoginState(false,it.data,"")
                    }
                    is Resource.Error ->{
                        loginFlow.value = LoginState(false,null, it.message.toString())
                    }
                }
            }
        }
    }
}