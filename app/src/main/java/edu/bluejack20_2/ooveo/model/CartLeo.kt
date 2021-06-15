package edu.bluejack20_2.ooveo.model

import com.google.firebase.firestore.DocumentReference

data class CartLeo(
    var id : String,
    var userID : DocumentReference ,
    var merchantID : DocumentReference,
)
