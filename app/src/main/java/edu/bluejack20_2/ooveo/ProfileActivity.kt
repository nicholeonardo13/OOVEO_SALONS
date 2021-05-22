package edu.bluejack20_2.ooveo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.ooveo.model.User

class ProfileActivity : AppCompatActivity() {
    private lateinit var logoutBtn: Button
    private lateinit var languageBtn: Button
    private lateinit var edtProfileBtn: Button
    private lateinit var db: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth;
    private lateinit var edtName: EditText
    private lateinit var user: User
    private lateinit var edtEmail: EditText
    private lateinit var tvPhone: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        init()
        mAuth = FirebaseAuth.getInstance()

        logoutBtn!!.setOnClickListener(View.OnClickListener {
            //FirebaseAuth.getInstance().signOut();
            mAuth.signOut()
            var intent =  Intent(this, MainActivity::class.java);
            startActivity(intent);
            finish()

        })

        languageBtn!!.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, LanguageActivity::class.java)
            startActivity(intent)
            finish()
        })

        edtProfileBtn!!.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
            finish()
        })


    }

    override fun onStart() {
        super.onStart()
        //Check dulu, kalo blm login gabisa kesini, bakal di arahin ke login
        var currentUser: FirebaseUser = mAuth.getCurrentUser();
        if(currentUser == null){
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun init(){
        logoutBtn = findViewById(R.id.btnProfileLogout)
        languageBtn = findViewById(R.id.btnProfileLanguage)
        edtProfileBtn = findViewById(R.id.btnProfilEditProfile)
        edtName = findViewById(R.id.edtEditProfileName)
        edtEmail = findViewById(R.id.edtEditProfileEmail)
        tvPhone = findViewById(R.id.tvProfilePhoneNumber)

    }


}