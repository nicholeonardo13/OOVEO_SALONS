package edu.bluejack20_2.ooveo.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.ooveo.R
import edu.bluejack20_2.ooveo.TopSpacingItemDecoration
import edu.bluejack20_2.ooveo.adapters.FavouriteAdapter
import edu.bluejack20_2.ooveo.adapters.RecyclerAdapter
import edu.bluejack20_2.ooveo.adapters.ServiceAdapter
import edu.bluejack20_2.ooveo.model.FavouriteModel
import edu.bluejack20_2.ooveo.model.MerchantModel
import edu.bluejack20_2.ooveo.model.ServiceModel
import edu.bluejack20_2.ooveo.viewmodels.DetailMerchantActivityViewModel
import java.util.*
import kotlin.collections.ArrayList

class FavouriteFragment : Fragment() {

    private lateinit var rcBarber : RecyclerView
    private val db = FirebaseFirestore.getInstance()
    private var mAuth = FirebaseAuth.getInstance();

    private lateinit var barberAdapter: FavouriteAdapter
    private lateinit var listFav :ArrayList<FavouriteModel>
    private lateinit var arr :ArrayList<String>
    private lateinit var listMerchant :ArrayList<MerchantModel>
    private lateinit var tempList: ArrayList<MerchantModel>

    private lateinit var barberList : ArrayList<MerchantModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourite, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        rcBarber  = view!!.findViewById<RecyclerView>(R.id.rcFavouritesMerchant)

        val linear = LinearLayoutManager(view!!.context)
        rcBarber.layoutManager = linear
        val topSpacingItemDecoration = TopSpacingItemDecoration(30)
        rcBarber.addItemDecoration(topSpacingItemDecoration)
        rcBarber.setHasFixedSize(true)
        barberAdapter = FavouriteAdapter()

        getAllFavData()


        rcBarber.adapter = barberAdapter
        barberAdapter.notifyDataSetChanged()

    }

    private fun getAllFavData(){
        db.collection("favourites").whereEqualTo("userID" , mAuth.uid.toString()).get()
            .addOnSuccessListener {
                listFav = ArrayList()
//                tempList = ArrayList()
                listFav.clear()
                arr = ArrayList<String>()
//                tempList.clear()
//                    Log.d("tests", "${it.documents}")
                for (document in it.documents){

                    listFav.add(
                        FavouriteModel(
                            document.id as String,
                            document.data?.get("userID") as String,
                            document.data?.get("merchantID") as String,
                        )
                    )

                    arr.add(document.data?.get("merchantID") as String)

//                        println("TESTT")
                }
//                tempList.addAll(listMerchantModel)


                getAllMerchantData(arr)

            }
            .addOnFailureListener{
                Log.d("DB Error", "get failed with ")
            }

    }

    private fun getAllMerchantData(list : ArrayList<String>){

//        var abc = ArrayList<MerchantModel>()

        listMerchant = ArrayList()

        for (bb in list){
            db.collection("merchants").document(bb).get()
                .addOnSuccessListener {
//                tempList = ArrayList()
//                    listMerchant.clear()
//                tempList.clear()
//                    Log.d("tests", "${it.documents}")
                    listMerchant.add(
                        MerchantModel(
                            it.id as String,
                            it.data?.get("name") as String,
                            it.data?.get("address") as String,
                            it.data?.get("image") as String,
                            it.data?.get("phoneNumber") as String,
                            it.data?.get("location") as String,
                            it.data?.get("type") as String,
                            it.data?.get("aboutus") as String
                        )
                    )

                    Log.e("BEBE" , listMerchant.toString())

//                        println("TESTT")

//                tempList.addAll(listMerchant)
                    barberAdapter.submitList(listMerchant)
                }
                .addOnFailureListener{
                    Log.d("DB Error", "get failed with ")
                }
        }

        Log.e("be" , listMerchant.toString())
        Log.e("be" , list.toString())
        Log.e("KOK" , "halooooooo")


    }


}