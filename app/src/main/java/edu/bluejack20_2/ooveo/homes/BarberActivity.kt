package edu.bluejack20_2.ooveo.homes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.malinskiy.superrecyclerview.SuperRecyclerView
import edu.bluejack20_2.ooveo.model.MerchantModel
import edu.bluejack20_2.ooveo.R
import edu.bluejack20_2.ooveo.TopSpacingItemDecoration
import edu.bluejack20_2.ooveo.adapters.RecyclerAdapter
import java.util.*

class BarberActivity : AppCompatActivity() {

    private lateinit var rcBarber : SuperRecyclerView
    private val db = FirebaseFirestore.getInstance()

    private lateinit var barberAdapter: RecyclerAdapter
    private lateinit var listMerchantModel :ArrayList<MerchantModel>
    private lateinit var tempList: ArrayList<MerchantModel>
    private lateinit var mAuth: FirebaseAuth
    private var lagiSearch = false

    private lateinit var barberList : ArrayList<MerchantModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barber)
        mAuth = FirebaseAuth.getInstance()
        var toolBar = findViewById<Toolbar>(R.id.toolbar)
        toolBar.title = ""

        setSupportActionBar(toolBar)
        rcBarber  = findViewById(R.id.rcBarber)

        listMerchantModel = ArrayList()

        val linear = LinearLayoutManager(this)
        rcBarber.setLayoutManager( linear)
        val topSpacingItemDecoration = TopSpacingItemDecoration(30)
        rcBarber.addItemDecoration(topSpacingItemDecoration)
//        rcBarber.setF (true)
        barberAdapter = RecyclerAdapter()

        getAllMerchantData()

        rcBarber.adapter = barberAdapter
        barberAdapter.notifyDataSetChanged()

        rcBarber.setupMoreListener({_ , _ , curr->

            if(lagiSearch){
                return@setupMoreListener
            }

            if(listMerchantModel.size == 0){
//               Log.e("IT" , it.toString())
                    db.collection("merchants").whereEqualTo("type" , "Barber").whereNotEqualTo("ownerID" , mAuth.currentUser.uid.toString()).orderBy("name").limit(3).get()
                        .addOnSuccessListener {
//                            listMerchantModel = ArrayList()
                            tempList = ArrayList()
//                            listMerchantModel.clear()
                            tempList.clear()
//                    Log.e("tests", "${it.documents}")
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

                            barberAdapter.submitList(tempList)

                        }
                        .addOnFailureListener{
                            Log.d("DB Error", "get failed with ")
                        }

            }else{
                var  curs = listMerchantModel[listMerchantModel.size-1]
                Log.e("CURS" , curs.toString())
                db.collection("merchants").document(curs.id).get().addOnSuccessListener {
//               Log.e("IT" , it.toString())
                    db.collection("merchants").whereEqualTo("type" , "Barber").orderBy("name").startAfter(it.data?.get("ownerID")).limit(3).get()
                        .addOnSuccessListener {
//                            listMerchantModel = ArrayList()
                            tempList = ArrayList()
//                            listMerchantModel.clear()
                            tempList.clear()
//                    Log.e("tests", "${it.documents}")
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

                            barberAdapter.submitList(tempList)

                        }
                        .addOnFailureListener{
                            Log.d("DB Error", "get failed with ")
                        }
                }
            }

        },4)
    }

    private fun getAllMerchantData(){
        db.collection("merchants").whereEqualTo("type" , "Barber").whereNotEqualTo("ownerID" , mAuth.currentUser.uid.toString()).orderBy("ownerID").limit(3).get()
                .addOnSuccessListener {
                    listMerchantModel = ArrayList()
                    tempList = ArrayList()
//                    listMerchantModel.clear()
                    tempList.clear()
//                    Log.d("tests", "${it.documents}")
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

                    barberAdapter.submitList(tempList)

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
                    lagiSearch = true
                    listMerchantModel.forEach {

                        if(it.name.toLowerCase(Locale.getDefault()).contains(searchText)){
                            tempList.add(it)
                        }

                    }

//                    rcBarber.adapter!!.notifyDataSetChanged()
                    barberAdapter.notifyDataSetChanged()

                }else {
                    lagiSearch = false
                    tempList.clear()
                    tempList.addAll(listMerchantModel)
                    barberAdapter.notifyDataSetChanged()
                }


                return false
            }

        })


        return super.onCreateOptionsMenu(menu)
    }

}