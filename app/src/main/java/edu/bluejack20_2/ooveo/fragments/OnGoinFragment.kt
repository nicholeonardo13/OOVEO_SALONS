package edu.bluejack20_2.ooveo.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.ooveo.adapters.OnProgressAdapter
import edu.bluejack20_2.ooveo.R
import edu.bluejack20_2.ooveo.model.OnprogressModel

class OnGoinFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var listOngoingModel: ArrayList<OnprogressModel>


    @SuppressLint("WrongConstant")
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
//        mAuth = FirebaseAuth.getInstance()
//        db = FirebaseFirestore.getInstance()
//
//        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_on_goin, container, false)
//
//        val rvOngoing = view.findViewById<RecyclerView>(R.id.rvOngoingAppoinment)
//
//        var linearLayout = LinearLayoutManager(view.context, LinearLayout.VERTICAL, false)
//
//        val userref = db.collection("users").document(mAuth.currentUser.uid)
//
//
//        db.collection("carts").whereEqualTo("user_id", userref).get()
//                .addOnSuccessListener {
//                    listOngoingModel = ArrayList()
//
//                    for (data in it) {
//                        listOngoingModel.add(
//                                OnprogressModel(
//                                        data["merchant_id"] as DocumentReference,
//                                        data["date"] as Timestamp,
//                                        data["merchant_id"] as DocumentReference,
//                                        data["start_time"] as String,
//                                        data["end_time"] as String,
//                                        data["service_id"] as DocumentReference
//                                )
//                        )
//
//                    }
//                    rvOngoing.adapter = OnProgressAdapter(listOngoingModel)
//                    rvOngoing.layoutManager = linearLayout
//                }

        return view
    }

    @SuppressLint("WrongConstant")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val rvOngoing = view!!.findViewById<RecyclerView>(R.id.rvOngoingAppoinment)

        var linearLayout = LinearLayoutManager(view!!.context, LinearLayout.VERTICAL, false)

        val userref = db.collection("users").document(mAuth.currentUser.uid)

        Log.wtf("User ID : ", userref.toString())
        db.collection("carts").whereEqualTo("user_id", userref).whereEqualTo("status", "ongoing")
                .get()
                .addOnSuccessListener {
                    listOngoingModel = ArrayList()

                    Log.wtf("debug 1", it.documents.size.toString())

                    for (data in it) {
                        Log.wtf("status ongoing1: ", data["status"] as String)
//                        if (data["status"] as String == ("ongoing")) {
                            listOngoingModel.add(
                                    OnprogressModel(
                                            data["merchant_id"] as DocumentReference,
                                            data["date"] as Timestamp,
                                            data["merchant_id"] as DocumentReference,
                                            data["start_time"] as String,
                                            data["end_time"] as String,
                                            data["service_id"] as DocumentReference,
                                            data["status"] as String
                                    )

                            )
//                        }else{
//                            rvOngoing.visibility = View.INVISIBLE
//                        }


                    }
                    rvOngoing.visibility = View.VISIBLE
                    rvOngoing.layoutManager = linearLayout
                    rvOngoing.adapter = OnProgressAdapter(listOngoingModel)
                }

    }


}