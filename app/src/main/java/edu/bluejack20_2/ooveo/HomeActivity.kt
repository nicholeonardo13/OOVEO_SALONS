package edu.bluejack20_2.ooveo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : AppCompatActivity() {

//    private lateinit var merchantRecyclerView: RecyclerView
    private val db = FirebaseFirestore.getInstance()
//    private val collectionReference:CollectionReference = db.collection("test")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

//        readData()
        getAllMerchantData()
    }

    fun readData(){
//        FirebaseApp.initializeApp();


        val docRef = db.collection("tests").get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        Log.d("Ada", "DocumentSnapshot data: ${document.documents}")
                    } else {
                        Log.d("Gada", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("DB Error", "get failed with ", exception)
                }

    }

    private fun getAllMerchantData(){
        db.collection("tests").get()
            .addOnSuccessListener {
                var listMerchantModel:ArrayList<MerchantModel> = ArrayList()
                listMerchantModel.clear()

                for (document in it){

                    listMerchantModel.add(
                        MerchantModel(
                        document.id as String,
                        document.data.get("akhir") as String,
                            document.data.get("awal") as String,
                    )
                    )
                }

                var merchantAdapter = MerchantAdapter(listMerchantModel)
                val recycleMerchants = findViewById<RecyclerView>(R.id.recycleMerchants)
                   recycleMerchants.layoutManager = LinearLayoutManager(this)
                    recycleMerchants.adapter = merchantAdapter

            }
            .addOnFailureListener{
                Log.d("DB Error", "get failed with ")
            }

    }



}