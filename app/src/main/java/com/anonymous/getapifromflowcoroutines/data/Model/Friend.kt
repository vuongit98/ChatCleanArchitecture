package com.anonymous.getapifromflowcoroutines.data.Model

import java.io.Serializable

data class Friend(
    val account: Account?= null,
    val status : Int = 0 // 0: chưa gửi, 1 : person request add , 2: chap nhan, 3: person receiver request
):Serializable
