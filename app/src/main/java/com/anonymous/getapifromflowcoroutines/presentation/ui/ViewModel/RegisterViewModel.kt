package com.anonymous.getapifromflowcoroutines.presentation.ui.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anonymous.getapifromflowcoroutines.Utils.Resource
import com.anonymous.getapifromflowcoroutines.data.Model.Account
import com.anonymous.getapifromflowcoroutines.domain.Usecases.usecases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class RegisterViewModel @Inject constructor(val usercase: usecases) : ViewModel() {

    val registerFlow = MutableStateFlow(RegisterState(isLoading = true))
    var _registerFlow : StateFlow<RegisterState> = registerFlow

    fun register (account: Account) {
        viewModelScope.launch {
            val result = usercase.registerUsecase(account)
            result.collect{
                when(it){

                    is Resource.Loading ->{
                        registerFlow.value = RegisterState(isLoading = true)
                    }
                    is Resource.Success -> {
                        registerFlow.value = RegisterState(data = it.data!!)
                    }
                    is Resource.Error -> {
                        registerFlow.value = RegisterState(error = "An error is unexpected ")
                    }
                }
            }
        }
    }
}