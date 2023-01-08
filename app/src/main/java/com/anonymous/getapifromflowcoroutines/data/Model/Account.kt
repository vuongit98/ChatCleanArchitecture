package com.anonymous.getapifromflowcoroutines.data.Model

import java.io.Serializable

data class Account(
     var uid : String = "",
     var email : String = "" ,
     var password : String ="" ,
     var address : String ="" ,
     var phone  : String ="",
     var gender : Int = 0
):Serializable
