package edu.bluejack20_2.ooveo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import edu.bluejack20_2.ooveo.DetailMerchantActivityViewModel
import edu.bluejack20_2.ooveo.R

class MoreInfoFragment : Fragment() {

    private  lateinit var tvMoreInfo : TextView

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
    }


}