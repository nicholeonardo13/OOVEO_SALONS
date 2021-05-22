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
import edu.bluejack20_2.ooveo.fragments.ProfileFragment
import java.util.concurrent.TimeUnit

class ChangePhoneNumberVerifActivity1 : AppCompatActivity() {
    private lateinit var textPhoneNumber: EditText
    private lateinit var btnSendVericationCode: Button
    private lateinit var textCodeArea: EditText
    private lateinit var backToEdtProfile: TextView


    lateinit var auth: FirebaseAuth
    lateinit var storedVerificationId:String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_phone_number_verif1)

        init()
        auth = FirebaseAuth.getInstance()

        backToEdtProfile!!.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, ProfileFragment::class.java)
            startActivity(intent)
            finish()
        })


        btnSendVericationCode.setOnClickListener{

            sendVC()
        }

        // Callback function for Phone Auth
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                //ARAHIN KE CHANGE Phone nya
                startActivity(Intent(applicationContext, ChangePhoneNumberVerifActivity2::class.java))
                finish()
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(applicationContext, "Failed", Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {

                Log.d("TAG","onCodeSent:$verificationId")
                storedVerificationId = verificationId
                resendToken = token
                var intent = Intent(applicationContext, ChangePhoneNumberVerifActivity2::class.java)
                intent.putExtra("storedVerificationId",storedVerificationId)
                intent.putExtra("phone", textPhoneNumber.text.toString())
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
            Toast.makeText(this, "Please enter country code and Phone Number", Toast.LENGTH_SHORT).show()
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
        textPhoneNumber = findViewById(R.id.edtChangePhoneNumber1PhoneNum)
        btnSendVericationCode = findViewById(R.id.btnChangePhoneNumber1SendVerificationCode)
        textCodeArea = findViewById(R.id.edtChangePhoneNumber1CodeArea)
        backToEdtProfile = findViewById(R.id.tvChangePhoneNumber1BackText)
    }
}