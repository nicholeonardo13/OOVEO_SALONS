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
import edu.bluejack20_2.ooveo.model.StylistModel
import java.util.ArrayList

class DeleteStylistActivity : AppCompatActivity() {

    private lateinit var db : FirebaseFirestore
    private lateinit var ids : String
    private lateinit var listStylistModel : ArrayList<StylistModel>
    private lateinit var tvStylistName : TextView
    private lateinit var tvStylistGender : TextView
    private lateinit var stylistModel : StylistModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_stylist)

        ids = intent.extras?.getString("id").toString()

        db = FirebaseFirestore.getInstance()

        val btnDeleteSchedule = findViewById<Button>(R.id.btnDeleteStylist)
        tvStylistName = findViewById<TextView>(R.id.tvDeleteStylistName)
        tvStylistGender = findViewById<TextView>(R.id.tvDeleteStylistGender)


        getAllServiceData()

        btnDeleteSchedule.setOnClickListener {
            deleteService(stylistModel , getNewServiceMap())

            var intent = Intent(this@DeleteStylistActivity, ManageStylistActivity::class.java)
            intent.putExtra("id", stylistModel.merchantID)
            startActivity(intent)
            finish()
        }
    }

    private fun getAllServiceData(){
        db.collection("stylists").document(ids).get()
            .addOnSuccessListener {
                listStylistModel = ArrayList()
                listStylistModel.clear()

                stylistModel = StylistModel(
                    it.id as String,
                    it.data?.get("name") as String,
                    it.data?.get("gender") as String,
                    it.data?.get("profilePicture") as String,
                    it.data?.get("merchantID") as String
                )

                listStylistModel.add(
                    stylistModel
                )

//                        println("TESTT")


                tvStylistName.setText(stylistModel.name)
                tvStylistGender.setText(stylistModel.gender)


            }
            .addOnFailureListener{
                Log.d("DB Error", "get failed with ")
            }

    }

    private fun getNewServiceMap(): Map<String, Any> {
        val name = tvStylistName.text.toString()
        val gender = tvStylistGender.text.toString()
        val map = mutableMapOf<String, Any>()
        if(name.isNotEmpty()) {
            map["name"] = name
        }
        if(gender.isNotEmpty()) {
            map["gender"] = gender
        }
        return map
    }

    private fun deleteService(stylist: StylistModel, newServiceMap: Map<String, Any>)  {
        val personQuery = db.collection("stylists")
            .whereEqualTo("name", stylist.name)
            .whereEqualTo("gender", stylist.gender)
            .whereEqualTo("profilePicture", stylist.profilePicture)
            .get().addOnSuccessListener {
                for(document in it.documents) {
                    try {
                        //personCollectionRef.document(document.id).update("age", newAge).await()
                        db.collection("stylists").document(ids).delete()

                    } catch (e: Exception) {
                        Toast.makeText(this@DeleteStylistActivity, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }

    }
}