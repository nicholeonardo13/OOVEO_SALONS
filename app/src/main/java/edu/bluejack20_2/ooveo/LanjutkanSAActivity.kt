package edu.bluejack20_2.ooveo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class LanjutkanSAActivity : AppCompatActivity() {

    private lateinit var scheduleID : String
    private lateinit var merchantID : String
    private lateinit var serviceID : String
    private lateinit var stylistID : String
    private lateinit var userID : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lanjutkan_s_a)


        scheduleID = intent.extras?.getString("id").toString()
        merchantID = intent.extras?.getString("merchantID").toString()
        serviceID = intent.extras?.getString("serviceID").toString()
        stylistID = intent.extras?.getString("stylistID").toString()
        userID = intent.extras?.getString("userID").toString()

        Log.e("COBA :  :  : ", scheduleID + " " + merchantID + " " + serviceID + " " + stylistID + " " + userID)
    }
}