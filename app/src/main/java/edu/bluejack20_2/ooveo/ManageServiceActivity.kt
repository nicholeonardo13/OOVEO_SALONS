package edu.bluejack20_2.ooveo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.ooveo.adapters.RecyclerAdapter
import edu.bluejack20_2.ooveo.adapters.ServiceAdapter
import edu.bluejack20_2.ooveo.adapters.ServiceAdminAdapter
import edu.bluejack20_2.ooveo.model.MerchantModel
import edu.bluejack20_2.ooveo.model.ServiceModel
import java.util.ArrayList

class ManageServiceActivity : AppCompatActivity() {

    private lateinit var rcAdminService : RecyclerView
    private val db = FirebaseFirestore.getInstance()

    private lateinit var serviceAdapter: ServiceAdminAdapter
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
        serviceAdapter = ServiceAdminAdapter()

        refreshPage()

        getAllMerchantData()

        rcAdminService.adapter = serviceAdapter
        serviceAdapter.notifyDataSetChanged()

        val fabService = findViewById<FloatingActionButton>(R.id.fabAddService)

        fabService.setOnClickListener {
            var intent = Intent(this@ManageServiceActivity, AddServiceActivity::class.java)
            intent.putExtra("id", ids)
            startActivity(intent)
        }


    }

    private fun refreshPage() {
        val swipeRefresh = findViewById<SwipeRefreshLayout>(R.id.swipeToRefreshService)

        swipeRefresh.setOnRefreshListener {

            getAllMerchantData()

            rcAdminService.adapter = serviceAdapter
            serviceAdapter.notifyDataSetChanged()

            swipeRefresh.isRefreshing = false
        }
    }


    private fun getAllMerchantData(){
        db.collection("services").whereEqualTo("merchantID" , ids).get()
            .addOnSuccessListener {
                listServiceModel = ArrayList()
                listServiceModel.clear()
                for (document in it.documents){

                    listServiceModel.add(
                        ServiceModel(
                            document.id as String,
                            document.data?.get("name") as String,
                            document.data?.get("price") as Long,
                            document.data?.get("description") as String,
                            document.data?.get("merchantID") as String
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