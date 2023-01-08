package com.anonymous.getapifromflowcoroutines.presentation.ui.ViewModel

import com.anonymous.getapifromflowcoroutines.data.Model.Account

data class LoginState (
    val isLoading :Boolean = false,
    val results : Account? = null,
    val error : String = ""

        )