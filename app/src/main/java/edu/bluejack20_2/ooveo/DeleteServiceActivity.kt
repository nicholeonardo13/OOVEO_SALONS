package edu.bluejack20_2.ooveo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import edu.bluejack20_2.ooveo.model.ServiceModel
import java.util.ArrayList

class DeleteServiceActivity : AppCompatActivity() {
    private lateinit var db : FirebaseFirestore
    private lateinit var ids : String
    private lateinit var listServiceModel : ArrayList<ServiceModel>
    private lateinit var tvServiceName : TextView
    private lateinit var tvServicePrice : TextView
    private lateinit var tvServiceDesc : TextView
    private lateinit var serviceModel : ServiceModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_service)

        ids = intent.extras?.getString("id").toString()

        db = FirebaseFirestore.getInstance()

        val btnDeleteService = findViewById<Button>(R.id.btnDeleteService)
        tvServiceName = findViewById<TextView>(R.id.tvDeleteServiceName)
        tvServicePrice = findViewById<TextView>(R.id.tvDeleteServicePrice)
        tvServiceDesc = findViewById<TextView>(R.id.tvDeleteServiceDescription)


        getAllServiceData()

        btnDeleteService.setOnClickListener {
            deleteService(serviceModel , getNewServiceMap())

            var intent = Intent(this@DeleteServiceActivity, ManageServiceActivity::class.java)
            intent.putExtra("id", serviceModel.merchantID)
            startActivity(intent)
            finish()
        }

    }

    private fun getAllServiceData(){
        db.collection("services").document(ids).get()
            .addOnSuccessListener {
                listServiceModel = ArrayList()
                listServiceModel.clear()

                serviceModel = ServiceModel(
                    it.id as String,
                    it.data?.get("name") as String,
                    it.data?.get("price") as Long,
                    it.data?.get("description") as String,
                    it.data?.get("merchantID") as String
                )

                listServiceModel.add(
                    serviceModel
                )

//                        println("TESTT")


                tvServiceName.setText(serviceModel.name)
                tvServicePrice.setText(serviceModel.price.toString())
                tvServiceDesc.setText(serviceModel.description)


            }
            .addOnFailureListener{
                Log.d("DB Error", "get failed with ")
            }

    }

    private fun getNewServiceMap(): Map<String, Any> {
        val name = tvServiceName.text.toString()
        val price = tvServicePrice.text.toString()
        val desc = tvServiceDesc.text.toString()
        val map = mutableMapOf<String, Any>()
        if(name.isNotEmpty()) {
            map["name"] = name
        }
        if(price.isNotEmpty()) {
            map["price"] = price.toLong()
        }
        if(desc.isNotEmpty()) {
            map["description"] = desc
        }
        return map
    }

    private fun deleteService(service: ServiceModel, newServiceMap: Map<String, Any>)  {
        val personQuery = db.collection("services")
            .whereEqualTo("name", service.name)
            .whereEqualTo("price", service.price)
            .whereEqualTo("description", service.description)
            .get().addOnSuccessListener {
                for(document in it.documents) {
                    try {
                        //personCollectionRef.document(document.id).update("age", newAge).await()
                        db.collection("services").document(ids).delete()

                    } catch (e: Exception) {
                        Toast.makeText(this@DeleteServiceActivity, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }

    }
}