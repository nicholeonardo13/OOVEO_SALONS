package edu.bluejack20_2.ooveo.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.ooveo.AppointmentDetailActivity
import edu.bluejack20_2.ooveo.ChooseScheduleActivity
import edu.bluejack20_2.ooveo.R
import edu.bluejack20_2.ooveo.model.CartModel
import com.google.firebase.Timestamp
import java.sql.Timestamp.valueOf
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class OnProgressAdapter(var listCart: MutableList<CartModel>) :
    RecyclerView.Adapter<OnProgressAdapter.OnProgressHolder>() {

    private lateinit var imageUrl: String
    private lateinit var date: String
    private lateinit var time: String
    private lateinit var serviceName: String

    private lateinit var db: FirebaseFirestore

    //YG DI PASSING
    private lateinit var cartID: String
    private lateinit var locationTxt: String
    private lateinit var dateTxt: String
    private lateinit var timeTxt: String
    private lateinit var bookingCode: String
    private lateinit var stylistID: String
    private lateinit var merchantID: String
    private lateinit var serviceID: String
    private lateinit var paymentStatus: String
    private lateinit var requestBook: String


    class OnProgressHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        //Inisialisasi
        var imageView: ImageView = itemView.findViewById(R.id.ivOnprogressImage)
        var tvDate: TextView = itemView.findViewById(R.id.tvOnprogressItemDate)
        var tvLocation: TextView = itemView.findViewById(R.id.tvOnprogressLocation)
        var tvTime: TextView = itemView.findViewById(R.id.tvOnprogressTime)
        var tvServiceName: TextView = itemView.findViewById(R.id.tvOnprogressServiceName)
        var btnDetail: Button = itemView.findViewById(R.id.btnOnprogressDetail)


    }

    override fun getItemCount(): Int {
        return listCart.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: OnProgressHolder, position: Int) {

        db = FirebaseFirestore.getInstance()
        //Disini Bind data
        val user = listCart[position]

        cartID = user.id

        var format1 = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        dateTxt = format1.format(user.date.toDate())

        timeTxt = user.start_time

        user.location.get().addOnSuccessListener {
            holder.tvLocation.text = it["location"] as String
            locationTxt = it["location"] as String
            merchantID = it.id.toString()

            val requestOption = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)

            Glide.with(holder.itemView)
                .applyDefaultRequestOptions(requestOption)
                .load(it["image"] as String)
                .into(holder.imageView)

            user.serviceName.get().addOnSuccessListener { it ->
                holder.tvServiceName.text = it["name"] as String

                db.collection("carts").document(cartID).get().addOnSuccessListener { it ->

                    bookingCode = it["bookingCode"] as String
                    var stylistRef = (it["stylist_id"] as DocumentReference)
                    var serviceRef = (it["service_id"] as DocumentReference)
                    paymentStatus = it["payment_status"] as String
                    requestBook = it["booking_request"] as String

                    stylistRef.get().addOnSuccessListener { it ->
                        stylistID = it.id

                        serviceRef.get().addOnSuccessListener {it ->
                            serviceID = it.id

                            holder.btnDetail.setOnClickListener {
                                val intent = Intent(holder.itemView.context, AppointmentDetailActivity::class.java)
                                intent.putExtra("location", locationTxt)
                                Log.wtf("Date TXT", dateTxt)
                                intent.putExtra("date", dateTxt)
                                intent.putExtra("time", timeTxt)
                                intent.putExtra("bookingCode", bookingCode)
                                Log.wtf("StylistID", stylistID)
                                Log.wtf("merchantID", merchantID)
                                Log.wtf("serviceID", serviceID)
                                Log.wtf("cartID", cartID)

                                intent.putExtra("stylistID", stylistID)
                                intent.putExtra("merchantID", merchantID)
                                intent.putExtra("serviceID", serviceID)
                                intent.putExtra("payment_status", paymentStatus)
                                intent.putExtra("request", requestBook)
                                intent.putExtra("cartID", cartID)
                                holder.itemView.context.startActivity(intent)
                            }
                        }
                    }


                    holder.tvTime.text = user.start_time + " - " + user.end_time

                }
            }
        }

        var fmt = SimpleDateFormat("EEE, d MMM YYYY")
        holder.tvDate.text = fmt.format(user.date.toDate())
//        holder.tvLocation.text = user.location


//        holder.tvServiceName.text = user.serviceName


    }

    //Buat munculin Holder, Template
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnProgressHolder {
        return OnProgressHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.onprogress_item, parent, false)
        )
    }
}