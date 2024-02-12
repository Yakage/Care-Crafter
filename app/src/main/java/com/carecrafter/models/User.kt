package com.carecrafter.models

data class User(
    var userId: Int,
    var name: String? = null,
    var email: String? = null,
    var age: Int,
    var height: Float,
    var gender: String? = null,
    var weight: Float,
    var password: String? = null,
    var confirmPassword: String? = null,
    var role: String? = null,
    var status: String? = null)


