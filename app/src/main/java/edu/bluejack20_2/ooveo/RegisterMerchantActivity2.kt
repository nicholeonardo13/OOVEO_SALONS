package edu.bluejack20_2.ooveo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import java.util.*

class RegisterMerchantActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_merchant2)


        val arrayType = arrayOf<String>(
            "Barber",
            ""
        )

        val spinner: Spinner = findViewById(R.id.spRegisterMerchantType)

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrayType)

        spinner.adapter = arrayAdapter
        spinner.onItemSelectedListener = object:

            AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    TODO("Not yet implemented")
                    //tampung apa yg di select
                    val type = arrayType[position]
                }
            }
    }
}