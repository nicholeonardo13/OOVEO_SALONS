package edu.bluejack20_2.ooveo

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import at.favre.lib.crypto.bcrypt.BCrypt
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.SignInMethodQueryResult
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var myRef: DocumentReference
    private lateinit var test: String

    private var datePickerDialog: DatePickerDialog? = null
    private lateinit var dateButton: Button
    private lateinit var textViewLogin: TextView

    private lateinit var registerBtn: Button
    private lateinit var edtName: EditText
    private lateinit var edtPhone: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var edtRepassword: EditText
    private lateinit var rbFemale: RadioButton
    private lateinit var rbMale: RadioButton
    private lateinit var registerAsMerchantBtn: TextView


    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initdaePicker()
        init()
        textViewLogin.setOnClickListener{
            val intent = Intent(this@RegisterActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        dateButton = findViewById(R.id.btnRegisterDatePicker)
        dateButton.text = todaysDate
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance()

        registerAsMerchantBtn!!.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, RegisterMerchantActivity::class.java)
            startActivity(intent)
        })

        registerBtn!!.setOnClickListener( View.OnClickListener {

                val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
                var checkFlagEmail: Int

                var txtName: String = edtName.text.toString()
                var txtPhone: String = edtPhone.text.toString()
                var phoneLength = txtPhone.length
                var txtEmail: String = edtEmail.text.toString().trim()

                var txtPassword: String = edtPassword.text.toString()
                var txtRepassword: String = edtRepassword.text.toString()
                var dateText: String = dateButton.text.toString()
                var gender: String = ""
                if(rbFemale.isChecked){
                    gender = "Female"
                }else if (rbMale.isChecked){
                    gender = "Male"
                }

                if(txtName.isEmpty()){
                    Toast.makeText(this, "Name must be filled!", Toast.LENGTH_SHORT).show()
                }else if(txtName.length < 3 ){
                    Toast.makeText(this, "Name must be more than 3 character!", Toast.LENGTH_SHORT).show()
                }else if(txtPhone.isEmpty()){
                    Toast.makeText(this, "Phone must be filled!", Toast.LENGTH_SHORT).show()
                }else if(!(phoneLength == 12)){
                    Toast.makeText(this, "Phone Number must be 12 characters", Toast.LENGTH_SHORT).show()
                }
                else if(txtEmail.isEmpty()){
                    Toast.makeText(this, "Email must be filled!", Toast.LENGTH_SHORT).show()
                }else if(!(txtEmail.matches(emailPattern.toRegex()))) {
                    Toast.makeText(applicationContext, "Invalid email address format",Toast.LENGTH_SHORT).show()
                }else if(!rbFemale.isChecked && !rbMale.isChecked){
                    Toast.makeText(this, "Gender must be selected!", Toast.LENGTH_SHORT).show()
                }else if(dateText == todaysDate){
                    Toast.makeText(this, "Date birth must be choose!", Toast.LENGTH_SHORT).show()
                }else if(txtPassword.isEmpty()){
                    Toast.makeText(this, "Password must be filled!", Toast.LENGTH_SHORT).show()
                }else if(txtPassword.length < 8){
                    Toast.makeText(this, "Password must be more than 8 character!", Toast.LENGTH_SHORT).show()
                }else if(txtRepassword.isEmpty()){
                    Toast.makeText(this, "Re-password must be filled!", Toast.LENGTH_SHORT).show()
                }else if(txtRepassword != txtPassword){
                    Toast.makeText(this, "Re-password doesn't match!", Toast.LENGTH_SHORT).show()
                }else{
                    mAuth.fetchSignInMethodsForEmail(txtEmail).addOnCompleteListener(OnCompleteListener<SignInMethodQueryResult>(){
                        var check: Boolean = it.result!!.signInMethods!!.isEmpty()

                        if(!check){
                            Toast.makeText(this, "Email already used!", Toast.LENGTH_SHORT).show()
                        }else{
                            //Gausah di hash
                            //val passHash = BCrypt.withDefaults().hashToString(12,txtPassword.toCharArray())
                            createAccount(txtEmail, txtPassword, txtName, txtPhone, gender, dateText, txtPassword);
                        }
                    })
                }

            }
        )
    }

    private fun checkEmail(txtEmail: String): Boolean {
        var flag: Boolean = false
        mAuth.fetchSignInMethodsForEmail(txtEmail).addOnCompleteListener(OnCompleteListener<SignInMethodQueryResult>(){
            var check: Boolean = it.result!!.signInMethods!!.isEmpty()
            flag = check
            println("flag 1: " + flag)
        })
        println("flag 2: " + flag)
        return flag
    }

    private fun init(){
        textViewLogin = findViewById(R.id.tvRegisterLogin)
        registerBtn = findViewById(R.id.btnRegisterRegister)
        edtName = findViewById(R.id.edtRegisterName)
        edtEmail = findViewById(R.id.edtRegisterEmail)
        edtPhone = findViewById(R.id.edtMerchantRegisterPhoneNumber)
        edtPassword = findViewById(R.id.edtRegisterPassword)
        edtRepassword = findViewById(R.id.edtRegisterRePassword)
        rbFemale = findViewById(R.id.rbRegisterFemale)
        rbMale = findViewById(R.id.rbRegisterMale)
        registerAsMerchantBtn = findViewById((R.id.tvRegisterAsMerchant))

    }

    private val todaysDate: String
        private get() {
            val cal = Calendar.getInstance()
            val year = cal[Calendar.YEAR]
            var month = cal[Calendar.MONTH]
            month += 1
            val day = cal[Calendar.DAY_OF_MONTH]
            return makeDateString(day, month, year)
        }

    private fun initdaePicker() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, day ->
            var month = month
            month += 1
            val date = makeDateString(day, month, year)
            dateButton!!.text = date
        }
        val cal = Calendar.getInstance()
        val year = cal[Calendar.YEAR]
        val month = cal[Calendar.MONTH]
        val day = cal[Calendar.DAY_OF_MONTH]
        val style = AlertDialog.THEME_HOLO_LIGHT
        datePickerDialog = DatePickerDialog(this, style, dateSetListener, year, month, day)
        datePickerDialog!!.datePicker.maxDate = System.currentTimeMillis()
    }

    private fun makeDateString(day: Int, month: Int, year: Int): String {
        return getMonthFormat(month) + " " + day + " " + year
    }

    private fun getMonthFormat(month: Int): String {
        if (month == 1) return "JAN"
        if (month == 2) return "FEB"
        if (month == 3) return "MAR"
        if (month == 4) return "APR"
        if (month == 5) return "MAY"
        if (month == 6) return "JUN"
        if (month == 7) return "JUL"
        if (month == 8) return "AUG"
        if (month == 9) return "SEP"
        if (month == 10) return "OCT"
        if (month == 11) return "NOV"
        return if (month == 12) "DEC" else "JAN"

        //default should never happen
    }

    fun openDatePicker(view: View?) {
        datePickerDialog!!.show()

    }

    private fun createAccount(email: String, password: String, txtName: String, txtPhone: String, gender:String, txtDate: String, txtPassword: String) {
        var intent = Intent(this, PhoneNumberActivity::class.java)
        intent.putExtra("email", email)
        intent.putExtra("password", password)
        intent.putExtra("hashPassword", txtPassword)
        intent.putExtra("name", txtName)
        intent.putExtra("phone", txtPhone)
        intent.putExtra("gender", gender)
        intent.putExtra("dob", txtDate)
        startActivity(intent)
        finish()
    }
}