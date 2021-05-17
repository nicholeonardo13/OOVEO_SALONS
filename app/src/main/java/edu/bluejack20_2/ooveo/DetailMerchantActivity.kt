package edu.bluejack20_2.ooveo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList

class DetailMerchantActivity : AppCompatActivity() {

    private lateinit var rcBarber : RecyclerView
    private val db = FirebaseFirestore.getInstance()

    private lateinit var serviceAdapter: ServiceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_merchant)

        rcBarber  = findViewById<RecyclerView>(R.id.rcService)

        val linear = LinearLayoutManager(this)
        rcBarber.layoutManager = linear
        val topSpacingItemDecoration = TopSpacingItemDecoration(30)
        rcBarber.addItemDecoration(topSpacingItemDecoration)
        rcBarber.setHasFixedSize(true)
        serviceAdapter = ServiceAdapter()

        var image = intent.extras?.getString("image")
        var ivDetailMerchant = findViewById<ImageView>(R.id.ivDetailMerchant)

        val requestOption = RequestOptions()
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)

        Glide.with(this)
            .applyDefaultRequestOptions(requestOption)
            .load(image)
            .into(ivDetailMerchant)

        getAllServiceData()

        rcBarber.adapter = serviceAdapter
        serviceAdapter.notifyDataSetChanged()
    }

    private fun getAllServiceData(){

        var id = intent.extras?.getString("id")


        db.collection("merchants").document(id!!).collection("services").get()
            .addOnSuccessListener {
                var listServiceModel: ArrayList<ServiceModel> = ArrayList()
                listServiceModel.clear()
//                    Log.d("tests", "${it.documents}")
                for (document in it.documents){

                    listServiceModel.add(
                        ServiceModel(
                            document.id as String,
                            document.data?.get("name") as String,
                            document.data?.get("price") as Long,
                            document.data?.get("description") as String
                        )
                    )
//                        println("TESTT")
                }

                serviceAdapter.submitList(listServiceModel)

            }
            .addOnFailureListener{
                Log.d("DB Error", "get failed with ")
            }

    }
}