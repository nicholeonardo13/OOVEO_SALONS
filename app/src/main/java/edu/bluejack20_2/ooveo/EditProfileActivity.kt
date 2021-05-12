package edu.bluejack20_2.ooveo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText

class EditProfileActivity : AppCompatActivity() {
    private lateinit var changePhoneNumberBtn: Button
    private lateinit var changePasswordBtn: Button
    private lateinit var saveChangesBtn: Button
    private lateinit var textName: EditText
    private lateinit var textEmail: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        init()

        changePhoneNumberBtn!!.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, PhoneNumberActivity::class.java)
            startActivity(intent)
        })

        changePasswordBtn!!.setOnClickListener(View.OnClickListener {
            //Open chane password page
        })

        saveChangesBtn!!.setOnClickListener(View.OnClickListener {
            //Something will happen
        })


    }

    private fun init() {
        changePhoneNumberBtn = findViewById(R.id.btnEditProfileChangePhoneNumber)
        changePasswordBtn = findViewById(R.id.btnEditProfileChangePassword)
        saveChangesBtn = findViewById(R.id.btnEditProfileSaveChanges)
        textName = findViewById(R.id.edtEditProfileName)
        textEmail = findViewById(R.id.edtEditProfileEmail)
    }
}