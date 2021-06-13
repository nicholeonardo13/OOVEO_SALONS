package edu.bluejack20_2.ooveo.model

import com.google.firebase.Timestamp
import java.util.*
import kotlin.collections.ArrayList

data class StylistModel(
    var id : String,
    var name : String,
    var gender : String,
    var profilePicture : String,
    var merchantID : String,
//    var schedule : ArrayList<String>?,
//    var scheduleTimestamp : Timestamp?
)
