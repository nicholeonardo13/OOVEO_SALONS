package edu.bluejack20_2.ooveo.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import edu.bluejack20_2.ooveo.R
import edu.bluejack20_2.ooveo.adapters.viewPagerAdapter


class AppointmentFragment : Fragment() {

    private var myContext: FragmentActivity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_appointment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTab()
    }


    fun setTab(){
        val adapter = viewPagerAdapter(childFragmentManager)
        adapter.addFragment(OnGoinFragment(), getString(R.string.onGoingTab))
        adapter.addFragment(HistoryFragment(), getString(R.string.historyTab))
        val viewPager = view!!.findViewById<ViewPager>(R.id.viewPager)
        viewPager.adapter = adapter
        val tabs = view!!.findViewById<TabLayout>(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
    }

    override fun onAttach(activity: Activity) {
        myContext = activity as FragmentActivity
        super.onAttach(activity)
    }


}