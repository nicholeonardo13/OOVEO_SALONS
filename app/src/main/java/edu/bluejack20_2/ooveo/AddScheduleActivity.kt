package edu.bluejack20_2.ooveo

import android.app.AlertDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import edu.bluejack20_2.ooveo.model.ScheduleModel
import edu.bluejack20_2.ooveo.model.ServiceModel
import edu.bluejack20_2.ooveo.model.StylistModel
import java.text.SimpleDateFormat
import java.util.*

class AddScheduleActivity : AppCompatActivity() {

    private lateinit var db : FirebaseFirestore
    private lateinit var ids : String
    private lateinit var listScheduleModel : ArrayList<ScheduleModel>

    private lateinit var numberPicker : NumberPicker
    private lateinit var btnAddJadwal : Button

    private lateinit var txtStart : TextView


    private var formatStart = "";



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_schedule)

        ids = intent.extras?.getString("id").toString()
        Log.e("yo" , ids)

        db = FirebaseFirestore.getInstance()

        btnAddJadwal = findViewById<Button>(R.id.btnAddJadwal)
        numberPicker = findViewById<NumberPicker>(R.id.numberPicker)
        txtStart = findViewById<TextView>(R.id.txtTitleStart)


        numberPicker.minValue = 0
        numberPicker.maxValue = 23

        listScheduleModel = ArrayList()

        numberPicker.setOnValueChangedListener { picker, oldVal, newVal ->

            formatStart = newVal.toString()
            txtStart.setText(formatStart + ":00")

        }





//        btnChooseStart.setOnClickListener {
//            val calendar = Calendar.getInstance()
//            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hourOfDay, minute ->
//
//
//
//                calendar.set(Calendar.HOUR_OF_DAY , hourOfDay)
//                calendar.set(Calendar.MINUTE, minute)
//
//                formatStart = SimpleDateFormat("HH:mm").format(calendar.time)
//                start2 = calendar.time
//                txtStart.text = "Start Hour : " + formatStart
//
//            }
//            var style = AlertDialog.THEME_HOLO_DARK;
//            TimePickerDialog(this,style ,  timeSetListener , Calendar.HOUR_OF_DAY , 0 , true).show()



//        getStylistData()

        btnAddJadwal.setOnClickListener {
            if(formatStart.equals("")){
                Toast.makeText(this, getString(R.string.errorStartHour), Toast.LENGTH_SHORT).show()
            }else {
                addSchedule()
            }
        }
    }



//    private fun getStylistData(){
//        db.collection("stylists").document(ids).get()
//            .addOnSuccessListener {
//                listStylistModel = ArrayList()
//                listStylistModel.clear()
//
//                stylisteModel = StylistModel(
//                    it.id as String,
//                    it.data?.get("name") as String,
//                    it.data?.get("gender") as String,
//                    it.data?.get("profilePicture") as String,
//                    it.data?.get("merchantID") as String,
////                    it.data?.get("schedule") as ArrayList<String>,
////                    it.data?.get("scheduleTimestamp") as Timestamp
//                )
//
//                listStylistModel.add(
//                    stylisteModel
//                )
//
//            }
//            .addOnFailureListener{
//                Log.d("DB Error", "get failed with ")
//            }
//
//    }

    private fun getAllSchedule(date : String , hour : String){
        db.collection("schedules").whereEqualTo("stylistID" , ids).whereEqualTo("hour" , hour).get()
            .addOnSuccessListener {
                listScheduleModel = ArrayList()
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


                if(!listScheduleModel.isEmpty()){
                    Toast.makeText(this@AddScheduleActivity , getString(R.string.errorJamDipilih) , Toast.LENGTH_SHORT).show()
                }else{
                    val schedule : MutableMap<String, Any> = HashMap()
                    schedule["date"] = date.toString()
                    schedule["hour"] = hour
                    schedule["status"] = "bisa"
                    schedule["stylistID"] = ids

                    db.collection("schedules").add(schedule)
                        .addOnSuccessListener {
                            Toast.makeText(this@AddScheduleActivity , getString(R.string.berhasilMessage) , Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this@AddScheduleActivity , getString(R.string.gagalMessage) , Toast.LENGTH_SHORT).show()
                        }
                }

            }

    }


    private fun addSchedule()  {

        val calendar = Calendar.getInstance()
        var date = SimpleDateFormat("MM/dd/yyyy").format(calendar.time)

        var hour = formatStart + ":00"

        getAllSchedule(date , hour)


    }

    }
//
//
//        val stylist : MutableMap<String, Any> = HashMap()
//
//        stylist["startHour"] = start2
//        stylist["endHour"] = end2
//
//        db.collection("services").document(ids).
//            .addOnSuccessListener {
//                Toast.makeText(this@AddServiceActivity , "Berhasil" , Toast.LENGTH_SHORT).show()
//            }
//            .addOnFailureListener {
//                Toast.makeText(this@AddServiceActivity , "GAGAL" , Toast.LENGTH_SHORT).show()
//            }
//    }

