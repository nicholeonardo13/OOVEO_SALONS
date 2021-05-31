package edu.bluejack20_2.ooveo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.ooveo.adapters.ServiceAdminAdapter
import edu.bluejack20_2.ooveo.adapters.StylistAdminAdapter
import edu.bluejack20_2.ooveo.model.ServiceModel
import edu.bluejack20_2.ooveo.model.StylistModel
import java.util.ArrayList

class ManageStylistActivity : AppCompatActivity() {
    private lateinit var rcAdminStylist : RecyclerView
    private val db = FirebaseFirestore.getInstance()

    private lateinit var sylistAdapter: StylistAdminAdapter
    private lateinit var listStylistModel : ArrayList<StylistModel>
    private lateinit var ids : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_stylist)

        ids = intent.extras?.getString("id").toString()

        rcAdminStylist  = findViewById<RecyclerView>(R.id.rcAdminStylist)

        val linear = LinearLayoutManager(this)
        rcAdminStylist.layoutManager = linear
        val topSpacingItemDecoration = TopSpacingItemDecoration(30)
        rcAdminStylist.addItemDecoration(topSpacingItemDecoration)
        rcAdminStylist.setHasFixedSize(true)
        sylistAdapter = StylistAdminAdapter()

        refreshPage()

        getAllMerchantData()

        rcAdminStylist.adapter = sylistAdapter
        sylistAdapter.notifyDataSetChanged()

        val fabStylist = findViewById<FloatingActionButton>(R.id.fabAddStylist)

        fabStylist.setOnClickListener {
//            var intent = Intent(this@ManageStylistActivity, AddServiceActivity::class.java)
//            intent.putExtra("id", ids)
//            startActivity(intent)
        }


    }

    private fun refreshPage() {
        val swipeRefresh = findViewById<SwipeRefreshLayout>(R.id.swipeToRefreshService)

        swipeRefresh.setOnRefreshListener {

            getAllMerchantData()

            rcAdminStylist.adapter = sylistAdapter
            sylistAdapter.notifyDataSetChanged()

            swipeRefresh.isRefreshing = false
        }
    }


    private fun getAllMerchantData(){
        db.collection("stylists").whereEqualTo("merchantID" , ids).get()
            .addOnSuccessListener {
                listStylistModel = ArrayList()
                listStylistModel.clear()
                for (document in it.documents){

                    listStylistModel.add(
                        StylistModel(
                            document.id as String,
                            document.data?.get("name") as String,
                            document.data?.get("gender") as String,
                            document.data?.get("profilePicture") as String,
                            document.data?.get("merchantID") as String
                        )
                    )

//                        println("TESTT")
                }

                sylistAdapter.submitList(listStylistModel)

            }
            .addOnFailureListener{
                Log.d("DB Error", "get failed with ")
            }

    }
}