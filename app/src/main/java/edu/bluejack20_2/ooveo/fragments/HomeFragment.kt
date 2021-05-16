package edu.bluejack20_2.ooveo.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.bluejack20_2.ooveo.BarberActivity
import edu.bluejack20_2.ooveo.R
import edu.bluejack20_2.ooveo.RegisterActivity

class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return  inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutBarber = view.findViewById<LinearLayout>(R.id.layoutBarber)
        layoutBarber?.setOnClickListener {
            Log.d("Click", "Barber Di Klik")
            val intent = Intent(this.context, BarberActivity::class.java)
            startActivity(intent)

        }
    }
}