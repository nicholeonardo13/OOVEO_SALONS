package edu.bluejack20_2.ooveo

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode

class DarkModeActivity : AppCompatActivity() {

    private lateinit var switchModeBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dark_mode)
        switchModeBtn = findViewById(R.id.btnDarkModeSwitch)

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
                Toast.makeText(this, "You must restart Application", Toast.LENGTH_SHORT ).show()
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPrefsEdit.putBoolean("NightMode", true)
                sharedPrefsEdit.apply()
                switchModeBtn.text = "Disable Dark Mode"
                Toast.makeText(this, "You must restart Application", Toast.LENGTH_SHORT ).show()
            }


        })
    }
}