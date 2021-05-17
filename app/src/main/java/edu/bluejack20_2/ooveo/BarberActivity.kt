package edu.bluejack20_2.ooveo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.ArrayList

class BarberActivity : AppCompatActivity() {

    private lateinit var rcBarber : RecyclerView
    private val db = FirebaseFirestore.getInstance()

    private lateinit var barberAdapter: RecyclerAdapter

    private lateinit var barberList : ArrayList<MerchantModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barber)

        rcBarber  = findViewById<RecyclerView>(R.id.rcBarber)

        val linear = LinearLayoutManager(this)
        rcBarber.layoutManager = linear
        val topSpacingItemDecoration = TopSpacingItemDecoration(30)
        rcBarber.addItemDecoration(topSpacingItemDecoration)
        rcBarber.setHasFixedSize(true)
        barberAdapter = RecyclerAdapter()

        getAllMerchantData()

        rcBarber.adapter = barberAdapter
        barberAdapter.notifyDataSetChanged()

    }



    private fun clearAll(){
        if(barberList != null){
            barberList.clear()

            if(barberAdapter != null){
                barberAdapter.notifyDataSetChanged()
            }
        }

         barberList = ArrayList<MerchantModel>()

    }

    private fun getAllMerchantData(){
        db.collection("merchants").whereEqualTo("type" , "Barber").get()
                .addOnSuccessListener {
                    var listMerchantModel:ArrayList<MerchantModel> = ArrayList()
                    listMerchantModel.clear()
//                    Log.d("tests", "${it.documents}")
                    for (document in it.documents){

                        listMerchantModel.add(
                                MerchantModel(
                                        document.id as String,
                                        document.data?.get("name") as String,
                                        document.data?.get("address") as String,
                                        document.data?.get("image") as String
                                )
                        )
//                        println("TESTT")
                    }

                    barberAdapter.submitList(listMerchantModel)

                }
                .addOnFailureListener{
                    Log.d("DB Error", "get failed with ")
                }

    }

}