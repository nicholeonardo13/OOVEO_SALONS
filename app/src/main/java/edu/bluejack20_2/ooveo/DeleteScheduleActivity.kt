package edu.bluejack20_2.ooveo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.ooveo.model.ScheduleModel
import edu.bluejack20_2.ooveo.model.ServiceModel
import java.util.ArrayList

class DeleteScheduleActivity : AppCompatActivity() {

    private lateinit var db : FirebaseFirestore
    private lateinit var ids : String
    private lateinit var stylistID : String
    private lateinit var listScheduleModel : ArrayList<ScheduleModel>
    private lateinit var tvScheduleDate : TextView
    private lateinit var tvScheduleHour : TextView
    private lateinit var scheduleModel : ScheduleModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_schedule)

        ids = intent.extras?.getString("id").toString()
        stylistID = intent.extras?.getString("stylistID").toString()

        db = FirebaseFirestore.getInstance()

        val btnDeleteSchedule = findViewById<Button>(R.id.btnDeleteSchedule)
        tvScheduleDate = findViewById<TextView>(R.id.tvDeleteScheduleDate)
        tvScheduleHour = findViewById<TextView>(R.id.tvDeleteScheduleHour)


        getAllServiceData()

        btnDeleteSchedule.setOnClickListener {
            deleteService(scheduleModel , getNewServiceMap())

            var intent = Intent(this@DeleteScheduleActivity, ScheduleStylistAdminActivity::class.java)
            intent.putExtra("id", scheduleModel.stylistID)
            startActivity(intent)
            finish()
        }

    }

    private fun getAllServiceData(){
        db.collection("schedules").document(ids).get()
            .addOnSuccessListener {
                listScheduleModel = ArrayList()
                listScheduleModel.clear()

                scheduleModel = ScheduleModel(
                    it.id as String,
                    it.data?.get("date") as String,
                    it.data?.get("hour") as String,
                    it.data?.get("status") as String,
                    it.data?.get("stylistID") as String
                )

                listScheduleModel.add(
                    scheduleModel
                )

//                        println("TESTT")


                tvScheduleDate.setText(scheduleModel.date)
                tvScheduleHour.setText(scheduleModel.hour)


            }
            .addOnFailureListener{
                Log.d("DB Error", "get failed with ")
            }

    }

    private fun getNewServiceMap(): Map<String, Any> {
        val date = tvScheduleDate.text.toString()
        val hour = tvScheduleHour.text.toString()
        val map = mutableMapOf<String, Any>()
        if(date.isNotEmpty()) {
            map["date"] = date
        }
        if(hour.isNotEmpty()) {
            map["hour"] = hour
        }
        return map
    }

    private fun deleteService(schedule: ScheduleModel, newServiceMap: Map<String, Any>)  {
        val personQuery = db.collection("schedules")
            .whereEqualTo("date", schedule.date)
            .whereEqualTo("hour", schedule.hour)
            .whereEqualTo("status", schedule.status)
            .get().addOnSuccessListener {
                for(document in it.documents) {
                    try {
                        //personCollectionRef.document(document.id).update("age", newAge).await()
                        db.collection("schedules").document(ids).delete()

                    } catch (e: Exception) {
                        Toast.makeText(this@DeleteScheduleActivity, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }

    }
}