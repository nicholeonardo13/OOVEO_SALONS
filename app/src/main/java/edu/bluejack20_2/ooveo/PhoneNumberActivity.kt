package edu.bluejack20_2.ooveo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText

class PhoneNumberActivity : AppCompatActivity() {
    private lateinit var nextBtn: Button
    private lateinit var textCodeArea: EditText
    private lateinit var textPhoneNumber: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_number)
        init()



        nextBtn!!.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, PhoneNumberVerificationActivity::class.java)
            startActivity(intent)
        })
    }

    private fun init() {
        nextBtn = findViewById(R.id.btnPhoneNumberNext)
        textCodeArea = findViewById(R.id.edtPhoneNumberCodeArea)
        textPhoneNumber = findViewById(R.id.edtPhoneNumberPhoneNum)
    }
}