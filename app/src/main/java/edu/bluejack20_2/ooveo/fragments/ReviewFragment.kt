package edu.bluejack20_2.ooveo.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.ooveo.R
import edu.bluejack20_2.ooveo.TopSpacingItemDecoration
import edu.bluejack20_2.ooveo.adapters.ReviewAdapter
import edu.bluejack20_2.ooveo.adapters.ServiceAdapter
import edu.bluejack20_2.ooveo.viewmodels.DetailMerchantActivityViewModel
import java.util.*
import kotlin.collections.ArrayList
import androidx.lifecycle.Observer
import com.google.firebase.firestore.DocumentReference
import edu.bluejack20_2.ooveo.model.*


class ReviewFragment : Fragment() {

    private lateinit var rcBarber : RecyclerView
    private lateinit var tvRating : TextView
    private val db = FirebaseFirestore.getInstance()
    private var mAuth = FirebaseAuth.getInstance();
    private lateinit var listRating : ArrayList<Long>
    private  var rating : Long = 0

    private lateinit var barberAdapter: ReviewAdapter
    private lateinit var listReview :ArrayList<ReviewModel>
    private lateinit var listCart :ArrayList<CartLeo>
    private lateinit var arr :ArrayList<String>
    private lateinit var listUser :ArrayList<UserModel>
    private lateinit var tempList: ArrayList<MerchantModel>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


//        rcBarber  = view!!.findViewById<RecyclerView>(R.id.rcReview)
//
//        val linear = LinearLayoutManager(view!!.context)
//        rcBarber.layoutManager = linear
//        val topSpacingItemDecoration = TopSpacingItemDecoration(30)
//        rcBarber.addItemDecoration(topSpacingItemDecoration)
//        rcBarber.setHasFixedSize(true)
//        barberAdapter = ReviewAdapter()
//
//        getAllCartData(id)
//
//
//        rcBarber.adapter = barberAdapter
//        barberAdapter.notifyDataSetChanged()

        return inflater.inflate(R.layout.fragment_review, container, false)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



        if(view == null){
            return
        }

        tvRating = view!!.findViewById(R.id.tvMerchantRating)
        rcBarber  = view!!.findViewById<RecyclerView>(R.id.rcReview)





        var viewModel = ViewModelProvider(requireActivity()).get(DetailMerchantActivityViewModel::class.java)
        viewModel.getID().observe(this , Observer {
            Log.d("YOYO", "id : ${it}")


            val linear = LinearLayoutManager(view!!.context)
            rcBarber.layoutManager = linear
            val topSpacingItemDecoration = TopSpacingItemDecoration(30)
            rcBarber.addItemDecoration(topSpacingItemDecoration)
            rcBarber.setHasFixedSize(true)
            barberAdapter = ReviewAdapter()

            getAllCartData(it)

            rcBarber.adapter = barberAdapter
            barberAdapter.notifyDataSetChanged()
        })

    }


    private fun getAllReviewData(cartID : ArrayList<DocumentReference>){
        listReview = ArrayList()
        listRating = ArrayList()

        for (bb in cartID){
            db.collection("reviews").whereEqualTo("cartID" , bb).get()
                .addOnSuccessListener {
//                tempList = ArrayList()
//                    listMerchant.clear()
//                tempList.clear()
//                    Log.d("tests", "${it.documents}")

                    for(cc in it.documents){
                        rating = 0
                        listReview.add(
                            ReviewModel(
                                cc.id as String,
                                cc.data?.get("rating") as Long,
                                cc.data?.get("feedback") as String,
                                cc.data?.get("cartID") as DocumentReference,
                            )
                        )
                        Log.e("BABA" , listReview.toString())
                        listRating.add(cc.data?.get("rating") as Long)

                        for (li in listRating){

                            Log.e("li" , li.toString())
                            rating += li
                            var hasil = rating/listRating.size
                            tvRating.setText("Merchant Rating : " + hasil)
                        }
                    }

                    Log.e("BEBE" , listReview.toString())


                    Log.e("rating ::" , rating.toString())
                    Log.e("size ::" , listRating.size.toString())





//                        println("TESTT")

//                tempList.addAll(listMerchant)
                    barberAdapter.submitList(listReview)
                }
                .addOnFailureListener{
                    Log.d("DB Error", "get failed with ")
                }
        }




        Log.e("bea" , listReview.toString())
        Log.e("beo" , cartID.toString())
        Log.e("KOK" , "pipipipipipapapapappa")


    }

    private fun getAllCartData(id : String){

        var arr = ArrayList<DocumentReference>()

        val merchantref = db.collection("merchants").document(id)
        Log.e("ref", merchantref.toString())

        Log.d("YOYO", "idDDDD : ${id}")
        db.collection("carts").whereEqualTo("merchant_id" , merchantref).get()
            .addOnSuccessListener {
                listCart = java.util.ArrayList()
                tempList = java.util.ArrayList()
                listCart.clear()
                tempList.clear()
//                    Log.d("tests", "${it.documents}")
                for (document in it.documents){
                    listCart.add(
                        CartLeo(
                            document.id as String,
                            document.data?.get("user_id") as DocumentReference,
                            document.data?.get("merchant_id") as DocumentReference,
                        )
                    )
                    var cartref = db.collection("carts").document(document.id)
                    arr.add((cartref))
//                        println("TESTT")
                    Log.e("arrrr" , listCart.toString())
                }
//                tempList.addAll(listServiceModel)
//                serviceAdapter.submitList(tempList)


                Log.e("Book sha" , arr.toString())
                getAllReviewData(arr)

            }
            .addOnFailureListener{
                Log.d("DB Error", "get failed with ")
            }

    }


}