package edu.bluejack20_2.ooveo

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.DateFormat
import java.time.Instant
import java.util.*
import kotlin.collections.HashMap

class AppointmentCartActivity : AppCompatActivity() {
    private lateinit var bookButton: Button
    private lateinit var requestEdt: EditText

    private lateinit var db: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth

    private lateinit var locationTv: TextView
    private lateinit var timeTv: TextView
    private lateinit var dateTv: TextView
    private lateinit var serviceNameTv: TextView
    private lateinit var stylistNameTv: TextView
    private lateinit var rangeTimeTv: TextView
    private lateinit var totalTv: TextView

    private lateinit var bookingCode: String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_cart)
        init();

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val requestTxt = requestEdt.text.toString()

//        DAPETIN DULU DATA NYA DARI INTENT

//        val location = intent.getStringExtra("location").toString()
        //SET TEXTVIEW SAMA IMAGE NYA
//        locationTv.text =
//        dateTv.text =
//        timeTv.text =
//        serviceNameTv.text =
//        stylistNameTv.text =
//        rangeTimeTv.text =
//        totalTv.text =



        //KALO UDH TARIK GAMBAR, SHOW GAMBARNYA PAKE INI
//        val requestOption = RequestOptions()
//            .placeholder(R.drawable.ic_launcher_background)
//            .error(R.drawable.ic_launcher_background)
//
//        if(activity?.applicationContext != null) {
//            Glide.with(activity!!.applicationContext)
//                .applyDefaultRequestOptions(requestOption)
//                .load(user.profilePicture)
//                .into(ivProfilePicture)
//        }

        bookButton.setOnClickListener(View.OnClickListener {
            //tampilin pop up dulu, baru kalo YES, add ke DB
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.confirm_booking))
            builder.setMessage(getString(R.string.confirmBook_message))
            builder.setPositiveButton(
                getString(R.string.yes_proceed_booking)
            ) { _: DialogInterface, _: Int ->

                val length = 6
                bookingCode = getRandomString(length)

                Log.wtf("Booking Code", bookingCode)
                //Disini berarti mau masukin ke cart
                addToCart(requestTxt)
                Log.wtf("YES BOOKING", "Add ke cart")
                val intent = Intent(this, AppointmentCreatedActivity::class.java)
                //KIRIM DATA PAKE INTENT
                //        intent.putExtra("location", location)
                //        intent.putExtra("date", date)
                //        intent.putExtra("time", time)
                intent.putExtra("bookingCode", bookingCode)
                startActivity(intent)
                finish()
            }
            builder.setNegativeButton(getString(R.string.no_cancel_booking)) { _: DialogInterface, _: Int ->
                Log.wtf("NO CANCEL", "CANCEL")
                finish()
            }
            builder.show()
        })

    }

    private fun init(){
        bookButton = findViewById(R.id.btnAppointmentCartConfirmBooking)
        requestEdt = findViewById(R.id.editTextAppointmentCartRequest)
        locationTv = findViewById(R.id.tvAppointmentCartLocation)
        dateTv = findViewById(R.id.tvAppointmentCartDate)
        timeTv = findViewById(R.id.tvAppointmentCartStartTime)
        serviceNameTv = findViewById(R.id.tvAppointmentCartServiceName)
        stylistNameTv = findViewById(R.id.tvAppointmentCartStylistName)
        rangeTimeTv = findViewById(R.id.tvAppointmentCartRangeTime)
        totalTv = findViewById(R.id.tvAppointmentCartPrice)

    }


    //ADD KE DATABASE CART
    @RequiresApi(Build.VERSION_CODES.O)
    private fun addToCart(request: String){
        val cart: MutableMap<String, Any> = HashMap()
        var req = request
        if(req.isEmpty()) req = "no request"

        val todayString: String = DateFormat.getDateInstance().format(Date.from(Instant.now()))
        val today: Date = DateFormat.getDateInstance().parse(todayString)

        cart["booking_request"] = req
        cart["date"] = today
        cart["start_time"] = "12:00"
        cart["end_time"] = "12:40"
        cart["merchant_id"] = db.collection("merchants").document("barber1")
        cart["payment_type"] = "cash on merchant"
        cart["service_id"] = db.collection("services").document("AOGmbRN1fVLh0Q2S9NZl")
        cart["status"] = "ongoing"
        cart["stylist_id"] = db.collection("stylists").document("6radfHZ6dibPQMDdmZqx")
        cart["user_id"] = db.collection("users").document(mAuth.currentUser.uid.toString())
        cart["bookingCode"] = bookingCode

        db.collection("carts")
            .add(cart)
            .addOnSuccessListener {
                Log.wtf("YES BOOKING", "success add ke cart")
                Toast.makeText(this, "Booking Success", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Log.wtf("NO BOOKING", "GAGAL add ke cart")
                Toast.makeText(this, "Failed to book, please try again", Toast.LENGTH_SHORT).show()
            }
    }

    fun getRandomString(length: Int) : String {
        val charset = ('A'..'Z') + ('0'..'9')
        return (1..length)
            .map { charset.random() }
            .joinToString("")
    }
}