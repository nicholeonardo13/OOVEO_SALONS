package edu.bluejack20_2.ooveo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import edu.bluejack20_2.ooveo.homes.HomeActivity
import java.sql.Timestamp
import java.text.SimpleDateFormat

class AppointmentCreatedActivity : AppCompatActivity() {
    private lateinit var backButton: TextView
    private lateinit var viewAppointmentDetailButton: Button
    private lateinit var locationTv: TextView
    private lateinit var dateTv: TextView
    private lateinit var timeTv: TextView
    private lateinit var bookingCodeTv: TextView
    private lateinit var checkinTimeTv: TextView
    private lateinit var mAuth: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_created)

        init()
        mAuth = FirebaseAuth.getInstance()

        //Get data dari hasil intent
        val location = intent.getStringExtra("location").toString()
        val d = Timestamp.valueOf(intent.getStringExtra("date"))

        val time = intent.getStringExtra("time").toString()
        val bookingCode = intent.getStringExtra("bookingCode").toString()

        val stylistID = intent.getStringExtra("stylistID").toString()
        val merchantID = intent.getStringExtra("merchantID").toString()
        val serviceID = intent.getStringExtra("serviceID").toString()
        val userID = intent.getStringExtra("userID").toString()

        val paymentStatus = intent.getStringExtra("payment_status").toString()
        val request = intent.getStringExtra("request").toString()

        locationTv.text = location
        var fmt = SimpleDateFormat("EEE, d MMM YYYY")
        dateTv.text = fmt.format(d)
        timeTv.text = time
        bookingCodeTv.text = getString(R.string.booking_code) + " " + bookingCode
        checkinTimeTv.text = getString(R.string.check_in_time) + " " + time


        //View Appointment Detail
        viewAppointmentDetailButton.setOnClickListener(View.OnClickListener {

            val intent = Intent(this, AppointmentDetailActivity::class.java)
            intent.putExtra("location", location)
            intent.putExtra("date", d.toString())
            intent.putExtra("time", time)
            intent.putExtra("bookingCode", bookingCode)

            intent.putExtra("stylistID", stylistID)
            intent.putExtra("merchantID", merchantID)
            intent.putExtra("serviceID", serviceID)
            intent.putExtra("userID", mAuth.currentUser.uid)
            intent.putExtra("payment_status", paymentStatus)
            intent.putExtra("request", request)
            startActivity(intent)
            finish()

        })

        //UNTUK back button
        backButton.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        })
    }

    private fun init() {
        backButton = findViewById(R.id.tvAppointmentCreatedBacktoHome)
        viewAppointmentDetailButton = findViewById(R.id.btnAppointmentCreatedViewAppDetail)

        locationTv = findViewById(R.id.tvAppointmentCreatedLocation)
        dateTv = findViewById(R.id.tvAppointmentCreatedDate)
        timeTv = findViewById(R.id.tvAppointmentCreatedTime)
        bookingCodeTv = findViewById(R.id.tvAppointmentCreatedBookingCode)
        checkinTimeTv = findViewById(R.id.tvAppointmentCreatedChecinTime)
    }


}