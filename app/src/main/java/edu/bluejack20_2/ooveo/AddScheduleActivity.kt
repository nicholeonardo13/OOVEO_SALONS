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
import edu.bluejack20_2.ooveo.model.ServiceModel
import edu.bluejack20_2.ooveo.model.StylistModel
import java.text.SimpleDateFormat
import java.util.*

class AddScheduleActivity : AppCompatActivity() {

    private lateinit var db : FirebaseFirestore
    private lateinit var ids : String
    private lateinit var listStylistModel : ArrayList<StylistModel>

    private lateinit var numberPicker : NumberPicker
    private lateinit var btnAddJadwal : Button

    private lateinit var txtStart : TextView

    private lateinit var stylisteModel : StylistModel

    private var formatStart = "";
    private var formatEnd = "";



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_schedule)

        ids = intent.extras?.getString("id").toString()
        Log.e("yo" , ids)

        db = FirebaseFirestore.getInstance()

        btnAddJadwal = findViewById<Button>(R.id.btnAddJadwal)
        numberPicker = findViewById<NumberPicker>(R.id.numberPicker)

        numberPicker.minValue = 0
        numberPicker.maxValue = 23

        numberPicker.setOnValueChangedListener { picker, oldVal, newVal ->

            formatStart = newVal.toString()

        }

        txtStart = findViewById<TextView>(R.id.txtTitleStart)



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



        getStylistData()

        btnAddJadwal.setOnClickListener {
            if(formatStart.equals("")){
                Toast.makeText(this, "Start Hour must be filled", Toast.LENGTH_SHORT).show()
            }else {
                addSchedule(stylisteModel , getNewStylistMap())
            }
        }
    }



    private fun getStylistData(){
        db.collection("stylists").document(ids).get()
            .addOnSuccessListener {
                listStylistModel = ArrayList()
                listStylistModel.clear()

                stylisteModel = StylistModel(
                    it.id as String,
                    it.data?.get("name") as String,
                    it.data?.get("gender") as String,
                    it.data?.get("profilePicture") as String,
                    it.data?.get("merchantID") as String,
//                    it.data?.get("schedule") as ArrayList<String>,
//                    it.data?.get("scheduleTimestamp") as Timestamp
                )

                listStylistModel.add(
                    stylisteModel
                )

            }
            .addOnFailureListener{
                Log.d("DB Error", "get failed with ")
            }

    }


    private fun getNewStylistMap(): Map<String, Any> {
        val start = formatStart.toString()
        var ss : ArrayList<String>
        ss = ArrayList()
        ss.add(start)
        val map = mutableMapOf<String, Any>()
        if(start != null) {
            map["schedule"] = ss
        }
//        if(end != null) {
//            map["schedule"] = end
//        }

        return map
    }


    private fun addSchedule(stylist: StylistModel, newStylistMap: Map<String, Any>)  {
//        val stylistQuery = db.collection("stylists")
//            .whereEqualTo("name", stylist.name)
//            .whereEqualTo("gender" , stylist.gender)
//            .get().addOnSuccessListener {
//                for(document in it.documents) {
//                    try {
//                        //personCollectionRef.document(document.id).update("age", newAge).await()
//                        db.collection("stylists").document(ids).set(
//                            newStylistMap,
//                            SetOptions.merge()
//                        )
//
//                    } catch (e: Exception) {
//                        Toast.makeText(this@AddScheduleActivity, e.message, Toast.LENGTH_LONG).show()
//                    }
//                }
//            }

        val schedule : MutableMap<String, Any> = HashMap()

        val calendar = Calendar.getInstance()
        var date = SimpleDateFormat("MM/dd/yyyy").format(calendar.time)

        schedule["date"] = date.toString()
        schedule["hour"] = "0" + formatStart + ":00"
        schedule["status"] = "bisa"
        schedule["stylistID"] = ids

        db.collection("schedules").add(schedule)
            .addOnSuccessListener {
                Toast.makeText(this@AddScheduleActivity , "Berhasil" , Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this@AddScheduleActivity , "GAGAL" , Toast.LENGTH_SHORT).show()
            }
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

