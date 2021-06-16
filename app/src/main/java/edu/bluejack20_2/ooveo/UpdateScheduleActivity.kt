package edu.bluejack20_2.ooveo

import android.app.AlertDialog
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import edu.bluejack20_2.ooveo.model.ScheduleModel
import edu.bluejack20_2.ooveo.model.ServiceModel
import java.text.SimpleDateFormat
import java.util.*

class UpdateScheduleActivity : AppCompatActivity() {

    private lateinit var db : FirebaseFirestore
    private lateinit var ids : String
    private lateinit var stylistID : String
    private lateinit var listScheduleModel : ArrayList<ScheduleModel>
    private lateinit var listScheduleModel2 : ArrayList<ScheduleModel>
    private lateinit var scheduleModel : ScheduleModel

    private var datePickerDialog: DatePickerDialog? = null

    private lateinit var numberPicker : NumberPicker
    private lateinit var btnUpdateJadwal : Button

    private lateinit var dateButton: Button

    private lateinit var txtStart : TextView
    private var formatStart = "";

    private lateinit var datt : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_schedule)
        initdaePicker()

        ids = intent.extras?.getString("id").toString()
        stylistID = intent.extras?.getString("stylistID").toString()
        Log.e("yo" , ids)

        db = FirebaseFirestore.getInstance()

        btnUpdateJadwal = findViewById<Button>(R.id.btnUpdateJadwal)
        numberPicker = findViewById<NumberPicker>(R.id.numberPicker)
        txtStart = findViewById(R.id.txtTitleStart)

        dateButton = findViewById(R.id.btnScheduleDatePicker)
        dateButton.text = todaysDate

        numberPicker.minValue = 0
        numberPicker.maxValue = 23

//        listScheduleModel = ArrayList()

        datt = "";

        numberPicker.setOnValueChangedListener { picker, oldVal, newVal ->

            formatStart = newVal.toString()
            txtStart.setText(formatStart+":00")

        }

        getScheduleData()

        btnUpdateJadwal.setOnClickListener {
            if(formatStart.equals("")){
                Toast.makeText(this, getString(R.string.jamHarusDiisi), Toast.LENGTH_SHORT).show()
            }else {
                updateSchedule()
            }
        }
    }

    private val todaysDate: String
        private get() {
            val cal = Calendar.getInstance()
            val year = cal[Calendar.YEAR]
            var month = cal[Calendar.MONTH]
            month += 1
            val day = cal[Calendar.DAY_OF_MONTH]
            return makeDateString(day, month, year)
        }

    private fun initdaePicker() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, day ->
            var month = month
            month += 1
            val date = makeDateString(day, month, year)
            dateButton!!.text = date
            datt = ""+getMonthFormat(month)+"/"+day+"/"+year

        }
        val cal = Calendar.getInstance()
        val year = cal[Calendar.YEAR]
        val month = cal[Calendar.MONTH]
        val day = cal[Calendar.DAY_OF_MONTH]
        val style = AlertDialog.THEME_HOLO_LIGHT
        datePickerDialog = DatePickerDialog(this, style, dateSetListener, year, month, day)
        datePickerDialog!!.datePicker.maxDate = System.currentTimeMillis()+24*3600*1000*2
        var calen = Calendar.getInstance().apply {
            this.set(Calendar.SECOND , 0)
            this.set(Calendar.MINUTE , 0)
            this.set(Calendar.HOUR_OF_DAY , 0)
            this.set(Calendar.HOUR , 0)
        }
//        datePickerDialog!!.datePicker.minDate = System.currentTimeMillis()-1000*3600*24
        datePickerDialog!!.datePicker.minDate = calen.timeInMillis

    }

    private fun makeDateString(day: Int, month: Int, year: Int): String {
        return getMonthFormat(month) + " " + day + " " + year
    }

    private fun getMonthFormat(month: Int): String {
        if (month == 1) return "01"
        if (month == 2) return "02"
        if (month == 3) return "03"
        if (month == 4) return "04"
        if (month == 5) return "05"
        if (month == 6) return "06"
        if (month == 7) return "07"
        if (month == 8) return "08"
        if (month == 9) return "09"
        if (month == 10) return "10"
        if (month == 11) return "11"
        return if (month == 12) "12" else "01"

        //default should never happen
    }

    fun openDatePickerSchedule(view: View?) {
        datePickerDialog!!.show()

    }

    private fun getNewScheduleMap(): Map<String, Any> {
        val start = formatStart+":00"
        val dates = datt
        val map = mutableMapOf<String, Any>()
        if(!start.equals("")) {
            map["hour"] = start
        }
        if(!dates.equals("")) {
            map["date"] = dates
        }

        return map
    }

    private fun getScheduleData(){
        db.collection("schedules").document(ids).get()
            .addOnSuccessListener {
                listScheduleModel2 = ArrayList()
                listScheduleModel2.clear()

                scheduleModel = ScheduleModel(
                    it.id as String,
                    it.data?.get("date") as String,
                    it.data?.get("hour") as String,
                    it.data?.get("status") as String,
                    it.data?.get("stylistID") as String
                )

                listScheduleModel2.add(
                    scheduleModel
                )

//                        println("TESTT")


                txtStart.setText(getString(R.string.jamAwal) + scheduleModel.hour)


            }
            .addOnFailureListener{
                Log.d("DB Error", "get failed with ")
            }

    }


    private fun updateSchedule()  {

        var hour = formatStart + ":00"

        getAllSchedule(hour)


    }


    private fun getAllSchedule(hour : String){

        Log.e("BAS", stylistID + "|" + hour)

        db.collection("schedules").whereEqualTo("stylistID" , stylistID).whereEqualTo("hour" , hour).get()
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

                Log.e("BOM" , listScheduleModel.toString())

                if(!listScheduleModel.isEmpty()){
                    Toast.makeText(this@UpdateScheduleActivity , getString(R.string.updateYangbener) , Toast.LENGTH_SHORT).show()
                    Log.e("masuk" , "MASUK KE SINI")
                }else{
                    updatePerson(scheduleModel , getNewScheduleMap())
                }

            }

    }

    private fun updatePerson(schedule: ScheduleModel, newScheduleMap: Map<String, Any>)  {
        val personQuery = db.collection("schedules")
            .whereEqualTo("date", schedule.date)
            .whereEqualTo("hour", schedule.hour)
            .whereEqualTo("status", schedule.status)
            .get().addOnSuccessListener {
                for(document in it.documents) {
                    try {
                        //personCollectionRef.document(document.id).update("age", newAge).await()
                        db.collection("schedules").document(ids).set(
                            newScheduleMap,
                            SetOptions.merge()
                        )

                    } catch (e: Exception) {
                        Toast.makeText(this@UpdateScheduleActivity, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }

    }
}