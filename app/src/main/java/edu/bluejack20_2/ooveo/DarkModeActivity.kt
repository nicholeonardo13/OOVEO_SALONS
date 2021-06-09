package edu.bluejack20_2.ooveo

import android.content.Intent
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
import edu.bluejack20_2.ooveo.homes.HomeActivity
import edu.bluejack20_2.ooveo.model.UserModel

class DarkModeActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth
    private lateinit var userModel: UserModel

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

        //Get user datanya dulu
        userRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    userModel = UserModel(
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
                        sharedPrefsEdit.putBoolean("NightMode", false)
                        sharedPrefsEdit.apply()
                        switchModeBtn.text = getString(R.string.enable_dark_mode)
                    }else if(userModel.mode == "dark"){
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        sharedPrefsEdit.putBoolean("NightMode", true)
                        sharedPrefsEdit.apply()
                        switchModeBtn.text = getString(R.string.disable_dark_mode)
                    }

                    Log.d("TAMPILIN DATA", "DocumentSnapshot data: ${document.data}")

                } else {
                    Log.d("GAGAL", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }


        if(isNightModeOn){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            switchModeBtn.text = getString(R.string.disable_dark_mode)

        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            switchModeBtn.text = getString(R.string.enable_light_mode)
        }

        switchModeBtn.setOnClickListener(View.OnClickListener {
            if(isNightModeOn){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPrefsEdit.putBoolean("NightMode", false)
                sharedPrefsEdit.apply()
                switchModeBtn.text = getString(R.string.enable_dark_mode)
                colormode = "light"
                var intent = Intent(applicationContext, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPrefsEdit.putBoolean("NightMode", true)
                sharedPrefsEdit.apply()
                switchModeBtn.text = getString(R.string.disable_dark_mode)
                colormode = "dark"
                var intent = Intent(applicationContext, HomeActivity::class.java)
                startActivity(intent)
                finish()

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