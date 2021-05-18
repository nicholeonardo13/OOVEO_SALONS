package edu.bluejack20_2.ooveo.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.ooveo.*
import java.util.ArrayList

class ServiceFragment : Fragment() {

    private lateinit var rcBarber : RecyclerView
    private val db = FirebaseFirestore.getInstance()

    private lateinit var serviceAdapter: ServiceAdapter

     var ids = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val views =  inflater.inflate(R.layout.fragment_service, container, false)

        return views
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(view == null){
            return
        }

        rcBarber  = view!!.findViewById<RecyclerView>(R.id.rcService)


        var viewModel = ViewModelProvider(requireActivity()).get(DetailMerchantActivityViewModel::class.java)
        viewModel.getID().observe(this , Observer {
            Log.d("YOYO", "id : ${it}")


            val linear = LinearLayoutManager(view!!.context)
            rcBarber.layoutManager = linear
            val topSpacingItemDecoration = TopSpacingItemDecoration(30)
            rcBarber.addItemDecoration(topSpacingItemDecoration)
            rcBarber.setHasFixedSize(true)
            serviceAdapter = ServiceAdapter()

            getAllServiceData(it)

            rcBarber.adapter = serviceAdapter
            serviceAdapter.notifyDataSetChanged()
        })

    }

    private fun getAllServiceData(id : String){
        Log.d("YOYO", "idDDDD : ${id}")
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