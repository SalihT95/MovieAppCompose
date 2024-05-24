package com.turkoglu.composedeneme.presentation.login

data class LoginState(
    var username : String ="",
    var password : String = "",
    var isRemember : Boolean= false
)
