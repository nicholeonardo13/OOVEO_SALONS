package edu.bluejack20_2.ooveo

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.ooveo.homes.HomeActivity
import edu.bluejack20_2.ooveo.model.MerchantModel
import edu.bluejack20_2.ooveo.model.ServiceModel
import edu.bluejack20_2.ooveo.model.StylistModel
import java.sql.Timestamp
import java.text.SimpleDateFormat

class AppointmentDetailActivity : AppCompatActivity() {

    private lateinit var checkinBtn: Button
    private lateinit var cancelBookBtn: TextView

    private lateinit var barberNameTv: TextView
    private lateinit var locationTv: TextView
    private lateinit var addressTv: TextView
    private lateinit var phoneNumberTv: TextView

    private lateinit var dateTv: TextView
    private lateinit var timeTv: TextView
    private lateinit var serviceNameTv: TextView
    private lateinit var stylistNameTv: TextView

    private lateinit var rangeTimeTv: TextView
    private lateinit var bookingCodeTv: TextView
    private lateinit var checkinTimeTv: TextView
    private lateinit var paymentStatusTv: TextView
    private lateinit var totalTv: TextView
    private lateinit var requestTv: TextView

    private lateinit var db: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth
    private lateinit var merchant: MerchantModel
    private lateinit var service: ServiceModel
    private lateinit var stylist: StylistModel
    private lateinit var statusCart: String
    private lateinit var reviewStatus: String

    private lateinit var serviceNameTxt: String
    private lateinit var stylistNameTxt: String
    private lateinit var rangeTimeTxt: String
    private lateinit var totalPriceTxt: String

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_detail)
        init()

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val cartID = intent.getStringExtra("cartID").toString()

        val location = intent.getStringExtra("location").toString()

        val d = Timestamp.valueOf(intent.getStringExtra("date"))

        val time = intent.getStringExtra("time").toString()
        val bookingCode = intent.getStringExtra("bookingCode").toString()

        val stylistID = intent.getStringExtra("stylistID").toString()
        val merchantID = intent.getStringExtra("merchantID").toString()
        val serviceID = intent.getStringExtra("serviceID").toString()

        val paymentStatus = intent.getStringExtra("payment_status").toString()
        val request = intent.getStringExtra("request").toString()

        if (paymentStatus == getString(R.string.unpaid)) {
            paymentStatusTv.setTextColor(Color.parseColor("#FF0000"))
        } else if (paymentStatus == getString(R.string.paid)) {
            paymentStatusTv.setTextColor(Color.parseColor("#20B2AA"))
        }
        paymentStatusTv.text = paymentStatus
        requestTv.text = request

        locationTv.text = location
        var fmt = SimpleDateFormat("EEE, d MMM YYYY")
        dateTv.text = fmt.format(d)
        timeTv.text = time
        bookingCodeTv.text = getString(R.string.booking_code) + " " + bookingCode
        checkinTimeTv.text = getString(R.string.check_in_time) + " " + time
        rangeTimeTv.text = time + "-" + endHour(time)
        rangeTimeTxt = time + "-" + endHour(time)

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
                    barberNameTv.text = merchant.name
                    addressTv.text = merchant.address
                    phoneNumberTv.text = merchant.phoneNumber

                    //GET service
                    db.collection("services").document(serviceID).get()
                        .addOnSuccessListener { document ->
                            if (document != null) {
                                service = ServiceModel(
                                    document.id.toString(),
                                    document["name"].toString(),
                                    document["price"] as Long,
                                    document["description"].toString(),
                                    document["merchantID"].toString()
                                )

                                serviceNameTxt = service.name
                                serviceNameTv.text =
                                    getString(R.string.service_is) + " " + service.name
                                totalTv.text =
                                    getString(R.string.idr) + " " + service.price.toString()
                                totalPriceTxt = getString(R.string.idr) + " " + service.price.toString()
                            }
                        }.addOnFailureListener {
                            Log.d("TAG", "Failed to retrieve service data")
                        }

                    //Get Stylist
                    db.collection("stylists").document(stylistID).get()
                        .addOnSuccessListener { document ->
                            if (document != null) {
                                stylist = StylistModel(
                                    document.id.toString(),
                                    document["name"].toString(),
                                    document["gender"].toString(),
                                    document["profilePicture"].toString(),
                                    document["merchantID"].toString()
                                )
                                stylistNameTxt = stylist.name
                                stylistNameTv.text =
                                    getString(R.string.stylist_is) + " " + stylist.name
                            }
                        }.addOnFailureListener {
                            Log.d("TAG", "Failed to retrieve Stylist data")
                        }

                }
            }.addOnFailureListener {
                Log.d("TAG", "Failed to retrieve merchant data")
            }

        db.collection("carts").document(cartID).get().addOnSuccessListener {
            if (it != null) {
                statusCart = it["status"].toString()


                if (statusCart == "ongoing") {
                    checkinBtn.text = getString(R.string.check_in_btn)
                    checkinBtn.setOnClickListener(View.OnClickListener {
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle(getString(R.string.check_in_question))
                        builder.setMessage(getString(R.string.checin_message))
                        builder.setPositiveButton(
                            getString(R.string.checkin_positive_message)
                        ) { _: DialogInterface, _: Int ->

                            //DISINI UPDATE DB CART NYA status jadi Completed, dan Payment jadi Paid

                            Log.wtf("cart ID in detail", cartID)

                            val homeIntent = Intent(this, HomeActivity::class.java)
                            db.collection("carts").document(cartID).update(
                                "payment_status", getString(R.string.paid),
                                "status", "completed"

                            ).addOnSuccessListener {
                                Log.d(
                                    "Cancel App success",
                                    "DocumentSnapshot successfully deleted!"
                                )
                                Toast.makeText(
                                    this,
                                    getString(R.string.checkInSuccess),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }.addOnFailureListener {
                                Log.d(
                                    "Cancel App failed",
                                    "DocumentSnapshot failed to deleted"
                                )
                                Toast.makeText(
                                    this,
                                    getString(R.string.checkOutFailed),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            startActivity(homeIntent)
                            finish()

                        }
                        builder.setNegativeButton(getString(R.string.cancel_checkin)) { _: DialogInterface, _: Int ->
                            Log.wtf("NO CANCEL", "CANCEL")
                            finish()
                        }
                        builder.show()

                    })

                    cancelBookBtn.setOnClickListener(View.OnClickListener
                    {
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle(getString(R.string.cancel_this_booking))
                        builder.setMessage(getString(R.string.cancelation_question))
                        builder.setPositiveButton(
                            getString(R.string.yes_cancel_book)
                        ) { _: DialogInterface, _: Int ->
                            Log.wtf("cart ID in detail", cartID)
                            val homeIntent = Intent(this, HomeActivity::class.java)
                            db.collection("carts").document(cartID).delete()
                                .addOnSuccessListener {
                                    Log.d(
                                        "Cancel App success",
                                        "DocumentSnapshot successfully deleted!"
                                    )
                                    Toast.makeText(
                                        this,
                                        getString(R.string.appointmentCancel),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }.addOnFailureListener {
                                    Log.d(
                                        "Cancel App failed",
                                        "DocumentSnapshot failed to deleted"
                                    )
                                    Toast.makeText(
                                        this,
                                        getString(R.string.appointmentCancelFailed),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            startActivity(homeIntent)
                            finish()

                        }
                        builder.setNegativeButton(getString(R.string.no_continue_booking)) { _: DialogInterface, _: Int ->
                            Log.wtf("NO CANCEL", "CANCEL")
                            finish()
                        }
                        builder.show()
                    })
                } else if (statusCart == "completed") {
                    val reviewStatus = it["review_status"].toString()

                        cancelBookBtn.visibility = View.INVISIBLE
                    if (reviewStatus == "not") {
                        checkinBtn.text = getString(R.string.review_btn)
                        Log.wtf("review status", reviewStatus)
                        checkinBtn.setOnClickListener(View.OnClickListener {
                            //Buka halaman review
                            val reviewIntent = Intent(this, ReviewActivity::class.java)
                            reviewIntent.putExtra("cartID", cartID)
                            reviewIntent.putExtra("serviceName", serviceNameTxt)
                            reviewIntent.putExtra("stylistName", stylistNameTxt)
                            reviewIntent.putExtra("rangeTime", rangeTimeTxt)
                            reviewIntent.putExtra("totalPrice", totalPriceTxt)
                            startActivity(reviewIntent)
                        })
                    }
                    else if (reviewStatus == "yes") {
                        checkinBtn.text = getString(R.string.haveCompleteAppointment)
                        checkinBtn.isEnabled = false
                        checkinBtn.height = 65
                        cancelBookBtn.visibility = View.INVISIBLE
                    }

                }
            }
        }


    }

    private fun init() {
        checkinBtn = findViewById(R.id.btnAppointmentDetailChecin)

        cancelBookBtn = findViewById(R.id.tvAppointmentDetailCancelBooking)
        barberNameTv = findViewById(R.id.tvAppointmentDetailBarberName)
        locationTv = findViewById(R.id.tvAppointmentDetailLocation)
        addressTv = findViewById(R.id.tvAppointmentDetailAddress)
        phoneNumberTv = findViewById(R.id.tvAppointmentDetailPhone)
        dateTv = findViewById(R.id.tvAppointmentDetailDate)
        timeTv = findViewById(R.id.tvAppointmentDetailStartTime)
        serviceNameTv = findViewById(R.id.tvAppointmentDetailServiceName)
        stylistNameTv = findViewById(R.id.tvAppointmentDetailStylistName)
        rangeTimeTv = findViewById(R.id.tvAppointmentDetailRangeTime)
        bookingCodeTv = findViewById(R.id.tvAppointmentDetailBookingCode)
        checkinTimeTv = findViewById(R.id.tvAppointmentDetailCheckinTime)
        paymentStatusTv = findViewById(R.id.tvAppointmentDetailPaymentStatusIs)
        totalTv = findViewById(R.id.tvAppointmentDetailPrice)
        requestTv = findViewById(R.id.tvAppointmentDetailRequestBook)


    }

    fun endHour(startTime: String): String {
        val timeSplit = startTime.split(":")
        val first = timeSplit[0].toInt() + 1

        return String.format("%02d:%02d", first, timeSplit[1].toInt())
    }
}