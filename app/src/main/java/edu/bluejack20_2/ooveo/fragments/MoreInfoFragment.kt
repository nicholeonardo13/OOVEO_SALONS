package edu.bluejack20_2.ooveo.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import edu.bluejack20_2.ooveo.viewmodels.DetailMerchantActivityViewModel
import edu.bluejack20_2.ooveo.R
import edu.bluejack20_2.ooveo.homes.DetailMerchantActivity
import edu.bluejack20_2.ooveo.homes.MapActivity
import edu.bluejack20_2.ooveo.homes.ReflexologyActivity

class MoreInfoFragment : Fragment() {

    private  lateinit var tvMoreInfo : TextView
    private lateinit var phoneNumberMerchant: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more_info, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tvMoreInfo = view!!.findViewById<TextView>(R.id.tvAboutUs)

        var viewModel = ViewModelProvider(requireActivity()).get(DetailMerchantActivityViewModel::class.java)


        viewModel.getAbout().observe(this, {
            tvMoreInfo.text = it
        })

        viewModel.getPhoneNumber().observe(this, {
            phoneNumberMerchant= it

            val btnCall = view!!.findViewById<Button>(R.id.btnCall)

            btnCall.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(phoneNumberMerchant)))
                activity?.startActivity(intent)

            }
        })

        val btnMap = view!!.findViewById<Button>(R.id.btnLocation)
        btnMap.setOnClickListener {
            val intent = Intent(this.context, MapActivity::class.java)
            startActivity(intent)

        }


    }


}