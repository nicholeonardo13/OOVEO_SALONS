package edu.bluejack20_2.ooveo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.ooveo.adapters.RecyclerAdapter
import edu.bluejack20_2.ooveo.adapters.ServiceAdapter
import edu.bluejack20_2.ooveo.model.MerchantModel
import edu.bluejack20_2.ooveo.model.ServiceModel
import java.util.ArrayList

class ManageServiceActivity : AppCompatActivity() {

    private lateinit var rcAdminService : RecyclerView
    private val db = FirebaseFirestore.getInstance()

    private lateinit var serviceAdapter: ServiceAdapter
    private lateinit var listServiceModel : ArrayList<ServiceModel>
    private lateinit var ids : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_service)

        ids = intent.extras?.getString("id").toString()

        rcAdminService  = findViewById<RecyclerView>(R.id.rcAdminService)

        val linear = LinearLayoutManager(this)
        rcAdminService.layoutManager = linear
        val topSpacingItemDecoration = TopSpacingItemDecoration(30)
        rcAdminService.addItemDecoration(topSpacingItemDecoration)
        rcAdminService.setHasFixedSize(true)
        serviceAdapter = ServiceAdapter()

        getAllMerchantData()

        rcAdminService.adapter = serviceAdapter
        serviceAdapter.notifyDataSetChanged()
    }


    private fun getAllMerchantData(){
        db.collection("merchants").document(ids!!).collection("services").get()
            .addOnSuccessListener {
                listServiceModel = ArrayList()
                listServiceModel.clear()
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