package edu.bluejack20_2.ooveo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import edu.bluejack20_2.ooveo.model.ServiceModel
import java.util.ArrayList

class UpdateServiceActivity : AppCompatActivity() {

    private lateinit var db : FirebaseFirestore
    private lateinit var ids : String
    private lateinit var listServiceModel : ArrayList<ServiceModel>
    private lateinit var edtServiceName : EditText
    private lateinit var edtServicePrice : EditText
    private lateinit var edtServiceDesc : EditText
    private lateinit var serviceModel : ServiceModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_service)

        ids = intent.extras?.getString("id").toString()

        db = FirebaseFirestore.getInstance()

        val btnUpdateService = findViewById<Button>(R.id.btnUpdateService)
         edtServiceName = findViewById<EditText>(R.id.edtUpdateServiceName)
         edtServicePrice = findViewById<EditText>(R.id.edtUpdateServicePrice)
         edtServiceDesc = findViewById<EditText>(R.id.edtUpdateServiceDescription)


        getAllServiceData()

        btnUpdateService.setOnClickListener {

            var name = edtServiceName.toString()
            var price = edtServicePrice.toString()
            var desc = edtServiceDesc.toString()

            if(name.equals("")){
                Toast.makeText(this, "Name must be filled", Toast.LENGTH_SHORT).show()
            }else if(price.equals("") || Integer.parseInt(price) > 0){
                Toast.makeText(this, "Price must be more filled", Toast.LENGTH_SHORT).show()
            }else if(desc.equals("")){
                Toast.makeText(this, "Description must be more filled", Toast.LENGTH_SHORT).show()
            }else {
                updatePerson(serviceModel , getNewServiceMap())

                var intent = Intent(this@UpdateServiceActivity, ManageServiceActivity::class.java)
                intent.putExtra("id", serviceModel.merchantID)
                startActivity(intent)
                finish()
            }

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


                edtServiceName.setText(serviceModel.name)
                edtServicePrice.setText(serviceModel.price.toString())
                edtServiceDesc.setText(serviceModel.description)


            }
            .addOnFailureListener{
                Log.d("DB Error", "get failed with ")
            }

    }

    private fun getNewServiceMap(): Map<String, Any> {
        val name = edtServiceName.text.toString()
        val price = edtServicePrice.text.toString()
        val desc = edtServiceDesc.text.toString()
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

    private fun updatePerson(service: ServiceModel, newServiceMap: Map<String, Any>)  {
        val personQuery = db.collection("services")
            .whereEqualTo("name", service.name)
            .whereEqualTo("price", service.price)
            .whereEqualTo("description", service.description)
            .get().addOnSuccessListener {
                for(document in it.documents) {
                    try {
                        //personCollectionRef.document(document.id).update("age", newAge).await()
                        db.collection("services").document(ids).set(
                            newServiceMap,
                            SetOptions.merge()
                        )

                    } catch (e: Exception) {
                        Toast.makeText(this@UpdateServiceActivity, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }

            }
    }
