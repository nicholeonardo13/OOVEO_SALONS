package edu.bluejack20_2.ooveo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import edu.bluejack20_2.ooveo.homes.HomeActivity
import java.util.concurrent.TimeUnit

class  PhoneNumberActivity : AppCompatActivity() {
    private lateinit var sendVerificationCodeBtn: Button
    private lateinit var textCodeArea: EditText
    private lateinit var textPhoneNumber: EditText
    private lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    lateinit var auth: FirebaseAuth
    lateinit var storedVerificationId:String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    private lateinit var email: String;
    private lateinit var password: String;
    private lateinit var name: String
    private lateinit var phone: String
    private lateinit var hashPassword: String
    private lateinit var dob: String
    private lateinit var gender: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_number)
        init()
        auth = FirebaseAuth.getInstance()

        this.email = intent.getStringExtra("email").toString()
        this.password = intent.getStringExtra("password").toString()
        this.name = intent.getStringExtra("name").toString()
        this.phone = intent.getStringExtra("phone").toString()
        this.hashPassword = intent.getStringExtra("hashPassword").toString()
        this.gender = intent.getStringExtra("gender").toString()
        this.dob = intent.getStringExtra("dob").toString()

//        var phoneV = phone.substring(1, 12)
//        textPhoneNumber.setText(phoneV)
        var phoneV = phone.replaceRange(0, 1, "")
        textPhoneNumber.setText(phoneV)
//        var currentUser = auth.currentUser
//        if(currentUser != null) {
//            startActivity(Intent(this, HomeActivity::class.java))
//            finish()
//        }

        sendVerificationCodeBtn.setOnClickListener{
            sendVC()
        }

        // Callback function for Phone Auth
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                startActivity(Intent(applicationContext, HomeActivity::class.java))
                finish()
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(applicationContext, getString(R.string.duhGagal), Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {

                Log.d("TAG","onCodeSent:$verificationId")
                storedVerificationId = verificationId
                resendToken = token
                var intent = Intent(applicationContext, PhoneNumberVerificationActivity::class.java)
                intent.putExtra("storedVerificationId",storedVerificationId)
                intent.putExtra("email", email)
                intent.putExtra("password", password)
                //PASS VALUE KE AUTH DISINI
                intent.putExtra("hashPassword", hashPassword)
                intent.putExtra("name", name)
                intent.putExtra("phone", phone)
                intent.putExtra("gender", gender)
                intent.putExtra("dob", dob)

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
            Toast.makeText(this, getString(R.string.countryCodeandPassword), Toast.LENGTH_SHORT).show()
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

    private fun init() {
        sendVerificationCodeBtn = findViewById(R.id.btnPhoneNumberSendVerificationCode)
        textCodeArea = findViewById(R.id.edtPhoneNumberCodeArea)
        textPhoneNumber = findViewById(R.id.edtPhoneNumberPhoneNum)
    }

}