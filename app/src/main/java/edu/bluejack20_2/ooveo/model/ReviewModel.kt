package edu.bluejack20_2.ooveo.model

import com.google.firebase.firestore.DocumentReference

data class ReviewModel(
    var id : String,
    var rating : Long ,
    var feedback : String,
    var cartID : DocumentReference,
)
