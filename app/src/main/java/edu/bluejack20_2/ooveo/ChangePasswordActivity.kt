package edu.bluejack20_2.ooveo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.ooveo.fragments.ProfileFragment

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var mAuth: FirebaseAuth
    private lateinit var newPassword: EditText
    private lateinit var changePasswordBtn: Button

    private lateinit var db: FirebaseFirestore



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        init()
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance()

        this.email = intent.getStringExtra("email").toString()
        this.password = intent.getStringExtra("password").toString()

        var hasEmail = false;

        var providerID = mAuth.currentUser.providerData.forEach {
            if(it.providerId == EmailAuthProvider.PROVIDER_ID) {
                hasEmail = true
            }
        }
        val userAuth = mAuth.currentUser!!

        Log.d("PROVIDER ID", providerID.toString())
        Log.d("PROVIDER_GOOGLE", GoogleAuthProvider.PROVIDER_ID)
        Log.d("PROVIDER_EMAIL", EmailAuthProvider.PROVIDER_ID)

        changePasswordBtn!!.setOnClickListener(View.OnClickListener {
            var txtNewPass = newPassword.text.toString()
            println("KEPENCTE: " + txtNewPass)
            Log.d("PROVIDER ID", providerID.toString())
            Log.d("PROVIDER_GOOGLE", GoogleAuthProvider.PROVIDER_ID)
            Log.d("PROVIDER_EMAIL", EmailAuthProvider.PROVIDER_ID)

            //SEBELUM UPDATE Password, HARUS REAUTHENTICATE USER NYA

            // Get auth credentials from the user for re-authentication. The example below shows
            // email and password credentials but there are multiple possible providers,
            // such as GoogleAuthProvider or FacebookAuthProvider.
            val credential = EmailAuthProvider.getCredential(userAuth.email, password)

//             Prompt the user to re-provide their sign-in credentials
            userAuth.reauthenticate(credential)
                .addOnCompleteListener {
                    Log.d("REAUTHENTICATE", "User re-authenticated.")

                    if(txtNewPass.isEmpty()){
                        Toast.makeText(this, "New Password must be filled!", Toast.LENGTH_SHORT ).show()
                    }else{
                        //UPDATE PASSWORD
                        val newPassword = txtNewPass
                        userAuth!!.updatePassword(newPassword)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(this, "Success change password", Toast.LENGTH_SHORT ).show()
                                    Log.d("UPDATE PASSWORD SUKSES", "User password updated.")

                                    val userRef = db.collection("users").document(mAuth.currentUser.uid.toString())
                                    userRef.update(
                                        "password", txtNewPass
                                    ).addOnSuccessListener {
                                        Log.d("SUKSES", "DocumentSnapshot successfully updated!")
                                        Toast.makeText(this, "Success save changes", Toast.LENGTH_SHORT).show()
                                        var intent = Intent(applicationContext, EditProfileActivity::class.java)
                                        startActivity(intent)
                                        finish()

                                    }
                                        .addOnFailureListener { e ->
                                            Log.w("ERROR", "Error updating document", e)
                                            Toast.makeText(this, "Failed save changes", Toast.LENGTH_SHORT)
                                                .show()
                                        }

                                }else{
                                    Toast.makeText(this, "Failed change password", Toast.LENGTH_SHORT ).show()
                                    Log.w("CHFLD", task.exception.toString())
                                }
                            }
                    }

                }

        })




    }

    private fun init(){
        newPassword = findViewById(R.id.edtChangePasswordNewPassword)
        changePasswordBtn  = findViewById(R.id.btnChangePasswordSaveChange)

    }
}