package edu.bluejack20_2.ooveo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class ChangePasswordAuthActivity1 : AppCompatActivity() {

    private  lateinit var phone: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var textPhoneNumber: EditText
    private lateinit var btnSendVericationCode: Button
    private lateinit var textCodeArea: EditText


    lateinit var auth: FirebaseAuth
    lateinit var storedVerificationId:String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password_auth1)
        init()
        auth = FirebaseAuth.getInstance()

        this.phone = intent.getStringExtra("phone").toString()
        this.email = intent.getStringExtra("email").toString()
        this.password = intent.getStringExtra("password").toString()

        if(textPhoneNumber.text.equals("Please add phone number")){

            Toast.makeText(applicationContext, getString(R.string.errorInsertPhoneNumber), Toast.LENGTH_LONG).show()
        }else{
            var phoneV = phone.replaceRange(0, 1, "")
            textPhoneNumber.setText(phoneV)
        }

        btnSendVericationCode.setOnClickListener{
            sendVC()
        }

        // Callback function for Phone Auth
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                //ARAHIN KE CHANGE PASSWORDNYA
                startActivity(Intent(applicationContext, ChangePasswordActivity::class.java))
                intent.putExtra("email", email)
                intent.putExtra("password", password)
                finish()
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(applicationContext, getString(R.string.failedText), Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {

                Log.d("TAG","onCodeSent:$verificationId")
                storedVerificationId = verificationId
                resendToken = token
                var intent = Intent(applicationContext, ChangePasswordAuthActivity2::class.java)
                intent.putExtra("storedVerificationId",storedVerificationId)
                intent.putExtra("email", email)
                intent.putExtra("password", password)
                startActivity(intent)
                finish()
            }
        }

    }

    private fun sendVC() {
        var country_code: String = textCodeArea.text.toString()
        var phone: String = textPhoneNumber.text.toString()
        var phoneNumber: String = country_code + phone

        if(country_code.isEmpty() && phone.isEmpty()){
            Toast.makeText(this, getString(R.string.errorCountryCode), Toast.LENGTH_SHORT).show()
        }else{
            sendVerificationcode(phoneNumber)

        }
    }

    private fun sendVerificationcode(number: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun init(){
        textPhoneNumber = findViewById(R.id.edtChangePasswordPhoneNum)
        btnSendVericationCode = findViewById(R.id.btnChangePasswordSendVerificationCode)
        textCodeArea = findViewById(R.id.edtChangePasswordCodeArea)
    }
}