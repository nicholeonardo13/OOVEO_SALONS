package edu.bluejack20_2.ooveo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import java.util.HashMap

class PhoneNumberVerificationActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var otpCode: EditText
    private lateinit var verifyBtn: Button

    lateinit var auth: FirebaseAuth

    private lateinit var email: String;
    private lateinit var password: String;
    private lateinit var name: String
    private lateinit var phone: String
    private lateinit var hashPassword: String
    private lateinit var dob: String
    private lateinit var gender: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_number_verification)
        init()
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        val storedVerificationId=intent.getStringExtra("storedVerificationId")

        verifyBtn.setOnClickListener{
            var otp=otpCode.text.toString().trim()
            if(!otp.isEmpty()){
                val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(
                        storedVerificationId.toString(), otp)
                signInWithPhoneAuthCredential(credential)
            }else{
                Toast.makeText(this,"Enter OTP",Toast.LENGTH_SHORT).show()
            }
        }

    }

    //Signin dengan email dan password
    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if(task.isSuccessful) {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent);
                    finish()
                }
                else {
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        println("CREDENTIAL BERHASIL "+ credential)
                        println("CREDENTIAL BERHASIL "+ credential)
                        println("CREDENTIAL BERHASIL "+ credential)
                        println("CREDENTIAL BERHASIL "+ credential)

//                        Toast.makeText(applicationContext, "Trying to link", Toast.LENGTH_LONG)

                        email = intent.getStringExtra("email").toString()
                        password = intent.getStringExtra("password").toString()
                        this.name = intent.getStringExtra("name").toString()
                        this.phone = intent.getStringExtra("phone").toString()
                        this.hashPassword = intent.getStringExtra("hashPassword").toString()
                        this.gender = intent.getStringExtra("gender").toString()
                        this.dob = intent.getStringExtra("dob").toString()

                        linkPhoneWithEmail(email, password)
                        saveUserFireStore(name, phone, email, gender, dob, hashPassword)

                        startActivity(Intent(applicationContext, HomeActivity::class.java))
                        finish()
// ...
                    } else {
// Sign in failed, display a message and update the UI
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
// The verification code entered was invalid
                            println("CREDENTIAL GAGAL "+ credential)
                            println("CREDENTIAL GAGAL "+ credential)

                            Toast.makeText(this,"Invalid OTP",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
    }

    private fun init() {
        otpCode = findViewById(R.id.edtPhoneNumberOTPCode)
        verifyBtn = findViewById(R.id.btnPhoneNumVerificationCode)
    }

    private fun linkPhoneWithEmail(email: String, password: String) {

        var emailCredential : AuthCredential = EmailAuthProvider.getCredential(email, password)

        auth.currentUser!!.linkWithCredential(emailCredential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Log.d("TAG", "linkWithCredential:success")
                val user = task.result?.user

                println("")
                println("")
                println("")
                println("")
                println("")
                println("link successful")
                println("")
                println("")
                println("")
                println("")
                println("")

                Toast.makeText(applicationContext, "Link successful", Toast.LENGTH_LONG)
            } else {
                Log.w("TAG", "linkWithCredential:failure", task.exception)
                Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUserFireStore(txtName: String, txtPhone: String, txtEmail:String, gender:String, txtDate: String, txtPassword: String ){
        val user: MutableMap<String, Any> = HashMap()

        var photopict: String = ""
        if(gender == "Female"){
            photopict = "https://firebasestorage.googleapis.com/v0/b/ooveo-966be.appspot.com/o/user%2Ffemale.png?alt=media&token=4bf1aa52-5e88-4048-ba9d-ee01c7e5c096"
        }else{
            photopict = "https://firebasestorage.googleapis.com/v0/b/ooveo-966be.appspot.com/o/user%2Fmale.png?alt=media&token=88d1c191-a741-4ae6-a625-418397b03dc0"
        }
        user["role"] = "User"
        user["name"] = txtName
        user["phone"] = txtPhone
        user["email"] = txtEmail
        user["gender"] = gender
        user["dob"] = txtDate
        user["password"] = txtPassword
        user["profilePicture"] = photopict

        db.collection("users")
                .document(auth.currentUser.uid.toString())
                .set(user)
                .addOnSuccessListener {
                    Toast.makeText(this, "Register success ", Toast.LENGTH_SHORT ).show()
                }
                .addOnFailureListener{
                    Toast.makeText(this, "Failed to Register ", Toast.LENGTH_SHORT ).show()
                }

    }

}