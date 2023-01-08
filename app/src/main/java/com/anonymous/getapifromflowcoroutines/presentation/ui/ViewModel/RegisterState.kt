package com.anonymous.getapifromflowcoroutines.presentation.ui.ViewModel

data class RegisterState(
    val isLoading : Boolean = false,
    val data : Boolean = false ,
    val error : String = ""
)
