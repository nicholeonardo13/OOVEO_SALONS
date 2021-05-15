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

class PhoneNumberActivity : AppCompatActivity() {
    private lateinit var sendVerificationCodeBtn: Button
    private lateinit var textCodeArea: EditText
    private lateinit var textPhoneNumber: EditText
    private lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var messageText: TextView

    lateinit var auth: FirebaseAuth
    lateinit var storedVerificationId:String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_number)
        init()
        auth = FirebaseAuth.getInstance()


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
                Toast.makeText(applicationContext, "Failed", Toast.LENGTH_LONG).show()
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
                startActivity(intent)
            }
        }

//        sendVerificationCodeBtn!!.setOnClickListener(View.OnClickListener {
//            var country_code: String = textCodeArea.toString()
//            var phone: String = textPhoneNumber.toString()
//            var phoneNumber: String = country_code + "" + phone
//            if(country_code.isEmpty() && phone.isEmpty()){
//                Toast.makeText(this, "Please enter country code and Phone Number", Toast.LENGTH_SHORT).show()
//            }else{
//                var options = PhoneAuthOptions.newBuilder(auth).setPhoneNumber(phoneNumber).setTimeout(60L, TimeUnit.SECONDS)
//                        .setActivity(this).setCallbacks(mCallbacks).build()
//
//                PhoneAuthProvider.verifyPhoneNumber(options)
//
//            }
//
//            //ADD PHONE NUMBER KE DALAM DATABASE DULU BARU BUKA VERIFICATION
//            var intent = Intent(this, PhoneNumberVerificationActivity::class.java)
//            startActivity(intent)
//        })
//        mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
//            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onVerificationFailed(p0: FirebaseException) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onCodeSent(verificationID: String, token: PhoneAuthProvider.ForceResendingToken) {
//                super.onCodeSent(verificationID, token)
//                //Somethime the code is not detected automatically
//                //so user must manually enter the code
//                messageText.setText("Verification Code has been Sent")
//                messageText.setText(View.VISIBLE)
//
//            //HARUSNYA PINDAH KE NEXT VERIFICATION --->> INI BLM BISA!!!
////                var verifIntent:
////                verifIntent = Intent(this, PhoneNumberVerificationActivity::class.java)
//
//            }
//        }
    }

    private fun sendVC() {
        var country_code: String = textCodeArea.text.toString()
        var phone: String = textPhoneNumber.text.toString()
        var phoneNumber: String = country_code + phone

        println("   ")
        println("   ")
        println("   ")
        println("   ")
        println("   ")
        println("   ")
        println(phoneNumber)
        println("   ")
        println("   ")

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

    private fun init() {
        sendVerificationCodeBtn = findViewById(R.id.btnPhoneNumberSendVerificationCode)
        textCodeArea = findViewById(R.id.edtPhoneNumberCodeArea)
        textPhoneNumber = findViewById(R.id.edtPhoneNumberPhoneNum)
        messageText = findViewById(R.id.tvPhoneNumberMessage1)
    }

//    override fun onStart() {
//        super.onStart()
//        val user = auth.currentUser
//        if(user != null){
//            println("Login successful");
//
//            val homeIntent = Intent(this, HomeActivity::class.java)
//            startActivity(homeIntent)
//            finish()
//        }else{
//            val mainActivity = Intent(this, MainActivity::class.java)
//            startActivity(mainActivity)
//            finish()
//        }
//    }
}