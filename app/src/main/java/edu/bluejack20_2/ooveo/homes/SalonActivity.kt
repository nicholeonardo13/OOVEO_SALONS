package edu.bluejack20_2.ooveo.homes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.ooveo.R
import edu.bluejack20_2.ooveo.TopSpacingItemDecoration
import edu.bluejack20_2.ooveo.adapters.RecyclerAdapter
import edu.bluejack20_2.ooveo.model.MerchantModel
import java.util.*

class SalonActivity : AppCompatActivity() {
    private lateinit var rcSalon : RecyclerView
    private val db = FirebaseFirestore.getInstance()

    private lateinit var merchantAdapter: RecyclerAdapter
    private lateinit var listMerchantModel :ArrayList<MerchantModel>
    private lateinit var tempList: ArrayList<MerchantModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_salon)

        var toolBar = findViewById<Toolbar>(R.id.toolbar)
        toolBar.title = ""

        setSupportActionBar(toolBar)

        rcSalon  = findViewById<RecyclerView>(R.id.rcSalon)

        val linear = LinearLayoutManager(this)
        rcSalon.layoutManager = linear
        val topSpacingItemDecoration = TopSpacingItemDecoration(30)
        rcSalon.addItemDecoration(topSpacingItemDecoration)
        rcSalon.setHasFixedSize(true)
        merchantAdapter = RecyclerAdapter()

        getAllMerchantData()

        rcSalon.adapter = merchantAdapter
        merchantAdapter.notifyDataSetChanged()

    }


    private fun getAllMerchantData(){
        db.collection("merchants").whereEqualTo("type" , "Salon").get()
            .addOnSuccessListener {
                listMerchantModel = ArrayList()
                tempList = ArrayList()
                listMerchantModel.clear()
                tempList.clear()
                for (document in it.documents){

                    listMerchantModel.add(
                        MerchantModel(
                            document.id as String,
                            document.data?.get("name") as String,
                            document.data?.get("address") as String,
                            document.data?.get("image") as String,
                            document.data?.get("phoneNumber") as String,
                            document.data?.get("location") as String,
                            document.data?.get("type") as String,
                            document.data?.get("aboutus") as String
                        )
                    )

//                        println("TESTT")
                }
                tempList.addAll(listMerchantModel)
                merchantAdapter.submitList(tempList)

            }
            .addOnFailureListener{
                Log.d("DB Error", "get failed with ")
            }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.search_menu, menu)
        var item = menu?.findItem(R.id.search_action)
        val searchView = item?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                tempList.clear()
                val searchText = newText!!.toLowerCase(Locale.getDefault())

                if(searchText.isNotEmpty()){

                    listMerchantModel.forEach {

                        if(it.name.toLowerCase(Locale.getDefault()).contains(searchText)){
                            tempList.add(it)
                        }

                    }

//                    rcBarber.adapter!!.notifyDataSetChanged()
                    merchantAdapter.notifyDataSetChanged()

                }else {
                    tempList.clear()
                    tempList.addAll(listMerchantModel)
                    merchantAdapter.notifyDataSetChanged()
                }


                return false
            }

        })


        return super.onCreateOptionsMenu(menu)
    }
}