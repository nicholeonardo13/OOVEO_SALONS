package edu.bluejack20_2.ooveo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {
    private lateinit var logoutBtn: Button
    private lateinit var languageBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        init()
        logoutBtn!!.setOnClickListener(View.OnClickListener {
            FirebaseAuth.getInstance().signOut();
            var intent =  Intent(this, MainActivity::class.java);
            startActivity(intent);
            finish()

        })

        languageBtn!!.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, LanguageActivity::class.java)
            startActivity(intent)
        })

    }

    private fun init(){
        logoutBtn = findViewById(R.id.btnProfileLogout)
        languageBtn = findViewById(R.id.btnProfileLanguage)
    }
}