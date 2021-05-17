package edu.bluejack20_2.ooveo.model

data class User(
    val role: String,
    val name: String,
    val phoneNumber: String,
    val email: String,
    val gender: String,
    val dOB: String,
    val password: String
)