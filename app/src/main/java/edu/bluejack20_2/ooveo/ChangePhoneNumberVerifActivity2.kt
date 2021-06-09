package edu.bluejack20_2.ooveo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.ooveo.fragments.ProfileFragment


class ChangePhoneNumberVerifActivity2 : AppCompatActivity() {
    private lateinit var otpCode: EditText
    private lateinit var verifyBtn: Button

    lateinit var auth: FirebaseAuth
    private lateinit var phone: String
    private lateinit var db: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_phone_number_verif2)

        init()

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val storedVerificationId=intent.getStringExtra("storedVerificationId")

        verifyBtn.setOnClickListener{
            val userRef = db.collection("users").document(auth.currentUser.uid.toString())

            var otp=otpCode.text.toString().trim()
            if(otp.isNotEmpty()){
                val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(storedVerificationId.toString(), otp)
                //Kalo udh berhasil baru bisa change phone number


//                val phoneAuthCredential =
//                    PhoneAuthProvider.getCredential("+91-98298XXXX2", "OTP_CODE")
                // Update Mobile Number...
                // Update Mobile Number...
                auth.getCurrentUser().updatePhoneNumber(credential)
                    .addOnCompleteListener(OnCompleteListener<Void?> { task ->
                        if (task.isSuccessful) {
                            // Update Successfully

                            phone = intent.getStringExtra("phone").toString()
                            val phoneNumber = "0$phone"

                            userRef.update(
                                "phone", phoneNumber
                            ).addOnSuccessListener {
                                Log.d("SUKSES", "DocumentSnapshot successfully updated!")
                                Toast.makeText(this, "Success change phone number", Toast.LENGTH_SHORT).show()
                                var intent = Intent(applicationContext, EditProfileActivity::class.java)
                                startActivity(intent)
                                finish()

                            }.addOnFailureListener { e ->
                                    Log.w("ERROR", "Error updating document", e)
                                    Toast.makeText(this, "Failed save changes", Toast.LENGTH_SHORT ).show()
                                }
                        } else {
                            Toast.makeText(this, "Success change phone number", Toast.LENGTH_SHORT).show()
                        }
                    })
            }else{
                Toast.makeText(this, "Enter OTP", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun init(){
        otpCode = findViewById(R.id.edtChangePhoneNumber2OTPCode)
        verifyBtn = findViewById(R.id.btnChangePhoneNumber2VerificationCode)
    }
}