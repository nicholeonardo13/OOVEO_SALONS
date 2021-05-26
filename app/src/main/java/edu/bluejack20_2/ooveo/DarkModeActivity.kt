package edu.bluejack20_2.ooveo

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class DarkModeActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth

    private lateinit var switchModeBtn: Button
    private lateinit var colormode: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dark_mode)
        switchModeBtn = findViewById(R.id.btnDarkModeSwitch)

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance()
        val userRef = db.collection("users").document(mAuth.currentUser.uid.toString())

        val appSettingPreference: SharedPreferences = getSharedPreferences("AppSettingPrefs", 0)
        val sharedPrefsEdit: SharedPreferences.Editor = appSettingPreference.edit()
        val isNightModeOn: Boolean = appSettingPreference.getBoolean("NightMode", false)

        if(isNightModeOn){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            switchModeBtn.text = "Disable Dark Mode"

        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            switchModeBtn.text = "Enable Dark Mode"
        }


        switchModeBtn.setOnClickListener(View.OnClickListener {
            if(isNightModeOn){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPrefsEdit.putBoolean("NightMode", false)
                sharedPrefsEdit.apply()
                switchModeBtn.text = "Enable Dark Mode"
                colormode = "light"
                Toast.makeText(this, "You must restart Application", Toast.LENGTH_SHORT ).show()
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPrefsEdit.putBoolean("NightMode", true)
                sharedPrefsEdit.apply()
                switchModeBtn.text = "Disable Dark Mode"
                colormode = "dark"
                Toast.makeText(this, "You must restart Application", Toast.LENGTH_SHORT ).show()
            }


            //UPDATE DATA DI FIRESTORE
            userRef.update(
                    "mode", colormode
            ).addOnSuccessListener {
                Log.d("SUKSES", "DocumentSnapshot successfully updated!")

            }.addOnFailureListener { e ->
                Log.w("ERROR", "Error updating document", e)
            }

        })


    }
}