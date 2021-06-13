package edu.bluejack20_2.ooveo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import edu.bluejack20_2.ooveo.homes.HomeActivity

class AppointmentCreatedActivity : AppCompatActivity() {
    private lateinit var backButton: TextView
    private lateinit var viewAppointmentDetailButton: Button
    private lateinit var locationTv: TextView
    private lateinit var dateTv: TextView
    private lateinit var timeTv: TextView
    private lateinit var bookingCodeTv: TextView
    private lateinit var checkinTimeTv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_created)

        init()

        //Get data dari hasil intent
//        val location = intent.getStringExtra("location").toString()
//        val date = intent.getStringExtra("date").toString()
//        val time = intent.getStringExtra("time").toString()
        val bookingCode = intent.getStringExtra("bookingCode").toString()
//
//        locationTv.text = location
//        location   dateTv.text = date
//        timeTv.text = time
        bookingCodeTv.text = getString(R.string.booking_code) + bookingCode
//        checkinTimeTv.text = getString(R.string.check_in_time) + time


        //View Appointment Detail
        viewAppointmentDetailButton.setOnClickListener(View.OnClickListener {

        })

        //UNTUK back button
        backButton.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        })
    }

    private fun init(){
        backButton = findViewById(R.id.tvAppointmentCreatedBacktoHome)
        viewAppointmentDetailButton = findViewById(R.id.btnAppointmentCreatedViewAppDetail)

        locationTv = findViewById(R.id.tvAppointmentCreatedLocation)
        dateTv = findViewById(R.id.tvAppointmentCreatedDate)
        timeTv = findViewById(R.id.tvAppointmentCreatedTime)
        bookingCodeTv = findViewById(R.id.tvAppointmentCreatedBookingCode)
        checkinTimeTv = findViewById(R.id.tvAppointmentCreatedChecinTime)
    }
}