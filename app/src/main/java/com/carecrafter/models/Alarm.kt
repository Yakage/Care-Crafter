package com.carecrafter.models

data class Alarm(
    var user_id: String,
    var title: String,
    var message: String,
    var time: String,
    var date: String,
)
