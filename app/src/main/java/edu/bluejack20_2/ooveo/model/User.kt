package edu.bluejack20_2.ooveo.model

data class User(
    val id: String,
    val role: String,
    val name: String,
    val phone: String,
    val email: String,
    val gender: String,
    val dob: String,
    val password: String,
    val profilePicture: String,
    val mode: String
)