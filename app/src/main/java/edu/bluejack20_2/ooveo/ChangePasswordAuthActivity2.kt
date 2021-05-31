package edu.bluejack20_2.ooveo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class ChangePasswordAuthActivity2 : AppCompatActivity() {
    private lateinit var email: String
    private lateinit var password: String

    private lateinit var otpCode: EditText
    private lateinit var verifyBtn: Button

    lateinit var auth: FirebaseAuth
    private lateinit var phone: String
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password_auth2)
        init()

        auth = FirebaseAuth.getInstance()

        val storedVerificationId=intent.getStringExtra("storedVerificationId")

        this.email = intent.getStringExtra("email").toString()
        this.password = intent.getStringExtra("password").toString()

        verifyBtn.setOnClickListener{
            var otp=otpCode.text.toString().trim()
            if(!otp.isEmpty()){
                val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(
                    storedVerificationId.toString(), otp)
                //Kalo udh berhasil baru bisa change password
                var intent = Intent(applicationContext, ChangePasswordActivity::class.java)
                intent.putExtra("email", email)
                intent.putExtra("password", password)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this,"Enter OTP", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun init(){
        otpCode = findViewById(R.id.edtChangePasswordAuth2OTPCode)
        verifyBtn = findViewById(R.id.btnChangePasswordAuth2VerificationCode)
    }
}