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
import edu.bluejack20_2.ooveo.R
import edu.bluejack20_2.ooveo.adapters.OnProgressAdapter
import edu.bluejack20_2.ooveo.model.OnprogressModel

class HistoryFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var listOngoingModel: ArrayList<OnprogressModel>

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    @SuppressLint("WrongConstant")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val rvCompleted = view!!.findViewById<RecyclerView>(R.id.rvHistoryCompletedAppointment)

        var linearLayout = LinearLayoutManager(view!!.context, LinearLayout.VERTICAL, false)

        val userref = db.collection("users").document(mAuth.currentUser.uid)

        Log.wtf("User ID : ", userref.toString())
        db.collection("carts").whereEqualTo("user_id", userref)
                .whereEqualTo("status", "completed").get()
                .addOnSuccessListener {
                    listOngoingModel = ArrayList()

                    Log.wtf("debug 1", it.documents.size.toString())

                    for (data in it) {
                        Log.wtf("status history2: ", data["status"] as String)
//                        if (data["status"] as String == "completed") {
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
//                        } else {
//                            rvCompleted.visibility = View.INVISIBLE
//                        }
                    }
                    rvCompleted.visibility = View.VISIBLE
                    rvCompleted.layoutManager = linearLayout
                    rvCompleted.adapter = OnProgressAdapter(listOngoingModel)
                }
    }
}