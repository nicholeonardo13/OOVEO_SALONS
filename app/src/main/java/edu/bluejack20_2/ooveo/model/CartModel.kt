package edu.bluejack20_2.ooveo.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference

class CartModel {
    lateinit var id: String
    lateinit var image: DocumentReference
    lateinit var date: Timestamp
    lateinit var location: DocumentReference
    lateinit var start_time: String
    lateinit var end_time: String
    lateinit var serviceName: DocumentReference
    lateinit var status: String
    lateinit var bookingCode: String
    lateinit var payment_status: String

    constructor(id: String, image: DocumentReference, date: Timestamp,
                location: DocumentReference, start_time: String, end_time: String,
                serviceName: DocumentReference, status: String, bookingCode: String,
    payment_status: String) {
        this.id = id
        this.image = image
        this.date = date
        this.location = location
        this.start_time = start_time
        this.end_time = end_time
        this.serviceName = serviceName
        this.status = status
        this.bookingCode = bookingCode
        this.payment_status = payment_status
    }
}