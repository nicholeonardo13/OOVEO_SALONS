package edu.bluejack20_2.ooveo

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.ooveo.model.MerchantModel
import edu.bluejack20_2.ooveo.model.ServiceModel
import edu.bluejack20_2.ooveo.model.StylistModel
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.collections.HashMap

class AppointmentCartActivity : AppCompatActivity() {
    private lateinit var bookButton: Button
    private lateinit var requestEdt: EditText


    private lateinit var locationTv: TextView
    private lateinit var timeTv: TextView
    private lateinit var dateTv: TextView
    private lateinit var serviceNameTv: TextView
    private lateinit var stylistNameTv: TextView
    private lateinit var rangeTimeTv: TextView
    private lateinit var totalTv: TextView


    private lateinit var merchantImage: ImageView
    private lateinit var bookingCode: String

    private lateinit var db: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth
    private lateinit var merchant: MerchantModel
    private lateinit var service: ServiceModel
    private lateinit var stylist: StylistModel

    private lateinit var merchantID: String
    private lateinit var date: Timestamp
    private lateinit var startTime: String
    private lateinit var stylistID: String
    private lateinit var serviceID: String
    private lateinit var scheduleID: String
    private lateinit var location: String

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_cart)
        init();

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()



//        DAPETIN DULU DATA NYA DARI INTENT

        val d = intent.getStringExtra("date").toString()
        date = Timestamp.valueOf(LocalDate.parse(d, DateTimeFormatter.ofPattern("MM/dd/yyyy")).toString() + " 00:00:00")
        startTime = intent.getStringExtra("hour").toString()
        stylistID = intent.getStringExtra("stylistID").toString()
        merchantID = intent.getStringExtra("merchantID").toString()
        serviceID = intent.getStringExtra("serviceID").toString()
        scheduleID = intent.getStringExtra("id").toString()

        timeTv.text = startTime
        rangeTimeTv.text = startTime +  "-" + endHour(startTime)

        var fmt = SimpleDateFormat("EEE, d MMM YYYY")
        dateTv.text = fmt.format(date)



        db.collection("merchants").document(merchantID)
            .get().addOnSuccessListener { document ->
                if (document != null) {
                    merchant = MerchantModel(
                        document.id.toString(),
                        document["name"].toString(),
                        document["address"].toString(),
                        document["image"].toString(),
                        document["phoneNumber"].toString(),
                        document["location"].toString(),
                        document["type"].toString(),
                        document["about"].toString()
                    )
                    location = merchant.location
                    locationTv.text = location
                    val requestOption = RequestOptions()
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)

                    if (applicationContext != null) {
                        Glide.with(applicationContext)
                            .applyDefaultRequestOptions(requestOption)
                            .load(merchant.image)
                            .into(merchantImage)
                    }
                    //GET service
                    db.collection("services").document(serviceID).get()
                        .addOnSuccessListener { document ->
                            if(document != null ){
                                service = ServiceModel(
                                    document.id.toString(),
                                    document["name"].toString(),
                                    document["price"] as Long,
                                    document["description"].toString(),
                                    document["merchantID"].toString()
                                )
                                serviceNameTv.text = getString(R.string.service_is) + service.name
                                totalTv.text = getString(R.string.idr) + service.price.toString()
                            }
                        }.addOnFailureListener{
                            Log.d("TAG", "Failed to retrieve service data")
                        }

                    //Get Stylist
                    db.collection("stylists").document(stylistID).get()
                        .addOnSuccessListener { document ->
                            if(document != null ){
                                stylist = StylistModel(
                                    document.id.toString(),
                                    document["name"].toString(),
                                    document["gender"].toString(),
                                    document["profilePicture"].toString(),
                                    document["merchantID"].toString()
                                )
                                stylistNameTv.text = getString(R.string.stylist_is) + stylist.name
                            }
                        }.addOnFailureListener{
                            Log.d("TAG", "Failed to retrieve Stylist data")
                        }
                }
            }.addOnFailureListener{
                Log.d("TAG", "Failed to retrieve merchant data")
            }




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
                val requestTxt = requestEdt.text.toString()
                addToCart(requestTxt)
                Log.wtf("YES BOOKING", "Add ke cart")
                val newIntent = Intent(this, AppointmentCreatedActivity::class.java)
                //KIRIM DATA PAKE INTENT
                newIntent.putExtra("location", location)
                newIntent.putExtra("date", date.toString())
                newIntent.putExtra("time", startTime)
                newIntent.putExtra("stylistID", stylistID)
                newIntent.putExtra("merchantID", merchantID)
                newIntent.putExtra("serviceID", serviceID)
                newIntent.putExtra("userID", mAuth.currentUser.uid)
                newIntent.putExtra("bookingCode", bookingCode)
                newIntent.putExtra("payment_status", getString(R.string.unpaid))
                newIntent.putExtra("request", requestTxt)

                startActivity(newIntent)
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
        merchantImage = findViewById(R.id.ivAppointmentCartImage)

    }


    //ADD KE DATABASE CART
    @RequiresApi(Build.VERSION_CODES.O)
    private fun addToCart(request: String){
        val cart: MutableMap<String, Any> = HashMap()
        var req = request
        if(req.isEmpty()) req = "no request"


        cart["booking_request"] = req
        cart["date"] = date
        cart["start_time"] = startTime
        cart["end_time"] = endHour(startTime)
        cart["merchant_id"] = db.collection("merchants").document(merchantID)
        cart["payment_type"] = "cash on merchant"
        cart["service_id"] = db.collection("services").document(serviceID)
        cart["status"] = "ongoing"
        cart["stylist_id"] = db.collection("stylists").document(stylistID)
        cart["user_id"] = db.collection("users").document(mAuth.currentUser.uid.toString())
        cart["bookingCode"] = bookingCode
        cart["payment_status"] = "unpaid"

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

    fun endHour(startTime: String): String{
        val timeSplit = startTime.split(":")
        val first = timeSplit[0].toInt() + 1

        return String.format("%02d:%02d", first, timeSplit[1].toInt())
    }
}