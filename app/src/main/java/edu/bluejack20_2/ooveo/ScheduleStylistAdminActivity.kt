package edu.bluejack20_2.ooveo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.ooveo.adapters.ScheduleAdminAdapter
import edu.bluejack20_2.ooveo.adapters.ServiceAdminAdapter
import edu.bluejack20_2.ooveo.model.ScheduleModel
import edu.bluejack20_2.ooveo.model.ServiceModel
import edu.bluejack20_2.ooveo.model.StylistModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.log

class ScheduleStylistAdminActivity : AppCompatActivity() {

    private lateinit var rcAdminService : RecyclerView
    private val db = FirebaseFirestore.getInstance()

    private lateinit var scheduleAdapter: ScheduleAdminAdapter
    private lateinit var listScheduleModel : ArrayList<ScheduleModel>
    private lateinit var ids : String

    private lateinit var scheduleModel: ScheduleModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_stylist_admin)

        ids = intent.extras?.getString("id").toString()

        rcAdminService  = findViewById<RecyclerView>(R.id.rcAdminSchedule)

        val linear = LinearLayoutManager(this)
        rcAdminService.layoutManager = linear
        val topSpacingItemDecoration = TopSpacingItemDecoration(30)
        rcAdminService.addItemDecoration(topSpacingItemDecoration)
        rcAdminService.setHasFixedSize(true)
        scheduleAdapter = ScheduleAdminAdapter()

        refreshPage()

        getScheduleData()

        rcAdminService.adapter = scheduleAdapter
        scheduleAdapter.notifyDataSetChanged()

        val fabService = findViewById<FloatingActionButton>(R.id.fabAddSchedule)

        fabService.setOnClickListener {
            var intent = Intent(this@ScheduleStylistAdminActivity, AddScheduleActivity::class.java)
            intent.putExtra("id", ids)
            startActivity(intent)
        }
    }

    private fun refreshPage() {
        val swipeRefresh = findViewById<SwipeRefreshLayout>(R.id.swipeToRefreshSchedule)

        swipeRefresh.setOnRefreshListener {

            getScheduleData()

            rcAdminService.adapter = scheduleAdapter
            scheduleAdapter.notifyDataSetChanged()

            swipeRefresh.isRefreshing = false
        }
    }



    private fun getScheduleData(){
//        db.collection("schedules").document(ids).get()
//            .addOnSuccessListener {
//                listScheduleModel = ArrayList()
//                listScheduleModel.clear()

//                if(it.data?.get("schedule") == null || it.data?.get("scheduleTimestamp") == null){
//                    stylisteModel = StylistModel(
//                        it.id as String,
//                        it.data?.get("name") as String,
//                        it.data?.get("gender") as String,
//                        it.data?.get("profilePicture") as String,
//                        it.data?.get("merchantID") as String,
////                        null ,
////                        null
//                    )
//                }else {
//                    stylisteModel = StylistModel(
//                        it.id as String,
//                        it.data?.get("name") as String,
//                        it.data?.get("gender") as String,
//                        it.data?.get("profilePicture") as String,
//                        it.data?.get("merchantID") as String,
////                        it.data?.get("schedule") as ArrayList<String>,
////                        it.data?.get("scheduleTimestamp") as Timestamp
//                    )

//                scheduleModel = ScheduleModel(
//                        it.id as String,
//                        it.data?.get("date") as String,
//                        it.data?.get("hour") as String,
//                        it.data?.get("status") as String,
//                        it.data?.get("stylistID") as String,
////                        it.data?.get("schedule") as ArrayList<String>,
////                        it.data?.get("scheduleTimestamp") as Timestamp
//                    )
//                }



//                listScheduleModel.add(
//                    scheduleModel
//                )

                db.collection("schedules").whereEqualTo("stylistID" , ids).get()
                    .addOnSuccessListener {
                        listScheduleModel = java.util.ArrayList()
                        listScheduleModel.clear()
                        for (document in it.documents){

                            listScheduleModel.add(
                                ScheduleModel(
                                    document.id as String,
                                    document.data?.get("date") as String,
                                    document.data?.get("hour") as String,
                                    document.data?.get("status") as String,
                                    document.data?.get("stylistID") as String
                                )
                            )

//                        println("TESTT")
                        }

                        scheduleAdapter.submitList(listScheduleModel)

                    }
                    .addOnFailureListener{
                        Log.d("DB Error", "get failed with ")
                    }


//                Log.e("waktu" , stylisteModel.startHour.toString())
//                getDateTime(stylisteModel.startHour.toString())?.let { it1 -> Log.e("tsett" , it1) }
//
////                var tt : Calendar = Calendar.set
//
//                if(stylisteModel.startHour == null || stylisteModel.endHour == null){
//                    txtStart.setText("NONE")
//                    txtEnd.setText("NONE")
//                }else{
//                    txtStart.setText(getDateTime(stylisteModel.startHour?.seconds.toString()))
//                    txtEnd.setText(getDateTime(stylisteModel.endHour?.seconds.toString()))
//                }



            }
//            .addOnFailureListener{
//                Log.d("DB Error", "get failed with ")
//            }



    private fun getDateTime(s: String): String? {
        try {
            val sdf = SimpleDateFormat("HH:mm")
            val netDate = Date(s.toLong() * 1000)
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }
}