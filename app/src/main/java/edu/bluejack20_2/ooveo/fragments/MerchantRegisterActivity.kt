package edu.bluejack20_2.ooveo.fragments

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import edu.bluejack20_2.ooveo.MainActivity
import edu.bluejack20_2.ooveo.R

class MerchantRegisterActivity : AppCompatActivity() {

    private lateinit var textName: EditText
    private lateinit var textPhoneNumber: EditText
    private lateinit var textEmail: EditText
    private lateinit var textAddress: EditText
    private lateinit var textPassword: EditText
    private lateinit var textRepassword: EditText
    private lateinit var textSelectedFile: EditText
    private lateinit var btnRegister: Button
    private lateinit var tvLogin: TextView
    private lateinit var btnUploadBanner: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_merchant_register)
        init()

        tvLogin!!.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        })
    }

    private fun init() {
        textName = findViewById(R.id.edtRegisterName)
        textPhoneNumber = findViewById(R.id.edtMerchantRegisterPhoneNumber)
        textEmail = findViewById(R.id.edtRegisterEmail)
        textAddress = findViewById(R.id.edtMerchantRegisterAddress)
        textPassword = findViewById(R.id.edtRegisterPassword)
        textRepassword = findViewById(R.id.edtRegisterRePassword)
        textSelectedFile = findViewById(R.id.tvMerchantRegisterBanner)
        tvLogin = findViewById(R.id.tvRegisterLogin)
        btnUploadBanner = findViewById(R.id.btnMerchantRegisterUploadbanner)
        btnRegister = findViewById(R.id.btnRegisterRegister)
    }


}