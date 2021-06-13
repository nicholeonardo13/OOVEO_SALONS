package edu.bluejack20_2.ooveo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.ooveo.adapters.ScheduleAdapter
import edu.bluejack20_2.ooveo.adapters.ScheduleAdminAdapter
import edu.bluejack20_2.ooveo.model.ScheduleModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChooseScheduleActivity : AppCompatActivity() {

    private lateinit var rcAdminService : RecyclerView
    private val db = FirebaseFirestore.getInstance()

    private lateinit var scheduleAdapter: ScheduleAdapter
    private lateinit var listScheduleModel : ArrayList<ScheduleModel>
    private lateinit var ids : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_schedule)


        ids = intent.extras?.getString("id").toString()

        rcAdminService  = findViewById<RecyclerView>(R.id.rcSchedule)

        val linear = LinearLayoutManager(this)
        rcAdminService.layoutManager = linear
        val topSpacingItemDecoration = TopSpacingItemDecoration(30)
        rcAdminService.addItemDecoration(topSpacingItemDecoration)
        rcAdminService.setHasFixedSize(true)
        scheduleAdapter = ScheduleAdapter()

        refreshPage()

        getScheduleData()

        rcAdminService.adapter = scheduleAdapter
        scheduleAdapter.notifyDataSetChanged()
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

        var cal = Calendar.getInstance()
        var date = SimpleDateFormat("MM/dd/yyyy").format(cal.time)

        db.collection("schedules").whereEqualTo("stylistID" , ids).whereEqualTo("status" , "bisa").whereEqualTo("date" , date).get()
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
    }
}