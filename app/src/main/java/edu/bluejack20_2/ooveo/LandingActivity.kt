package edu.bluejack20_2.ooveo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

import com.google.firebase.auth.FirebaseAuth

class LandingActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser

        /*If user is not authenticated, send him to MainActivity(Login) to authenticate first.
        * Else send him/her to HomeActivity*/

        Handler().postDelayed({
            if(user != null){
                val homeIntent = Intent(this, HomeActivity::class.java)
                startActivity(homeIntent)
            }else{
                val mainActivity = Intent(this, MainActivity::class.java)
                startActivity(mainActivity)
            }
        }, 2000)
    }


}