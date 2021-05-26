package edu.bluejack20_2.ooveo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.ooveo.model.User

class LandingActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var userModel: User
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        val user = mAuth.currentUser

        /*If user is not authenticated, send him to MainActivity(Login) to authenticate first.
        * Else send him/her to HomeActivity*/


        Handler().postDelayed({
            if(user != null){
                getUserData()
                val homeIntent = Intent(this, HomeActivity::class.java)
                startActivity(homeIntent)
                finish()
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                val mainActivity = Intent(this, MainActivity::class.java)
                startActivity(mainActivity)
                finish()
            }
        }, 2000)
    }

    fun  getUserData(){
        val docRef = db.collection("users").document(mAuth.currentUser.uid.toString())
        docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        userModel = User(
                                document.id.toString(),
                                document["role"].toString(),
                                document["name"].toString(),
                                document["phone"].toString(),
                                document["email"].toString(),
                                document["gender"].toString(),
                                document["dob"].toString(),
                                document["password"].toString(),
                                document["profilePicture"].toString(),
                                document["mode"].toString()
                        )

                        if(userModel.mode == "light"){
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        }else if(userModel.mode == "dark"){
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        }

                        Log.d("TAMPILIN DATA", "DocumentSnapshot data: ${document.data}")

                    } else {
                        Log.d("GAGAL", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("TAG", "get failed with ", exception)
                }

    }

}