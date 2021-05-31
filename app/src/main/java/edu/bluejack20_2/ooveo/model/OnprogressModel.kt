package edu.bluejack20_2.ooveo.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference

class OnprogressModel {
    lateinit var image: DocumentReference
    lateinit var date: Timestamp
    lateinit var location: DocumentReference
    lateinit var start_time: String
    lateinit var end_time: String
    lateinit var serviceName: DocumentReference
    lateinit var status: String

    constructor(image: DocumentReference, date: Timestamp,
                location: DocumentReference, start_time: String, end_time: String,
                serviceName: DocumentReference, status: String) {
        this.image = image
        this.date = date
        this.location = location
        this.start_time = start_time
        this.end_time = end_time
        this.serviceName = serviceName
        this.status = status
    }
}