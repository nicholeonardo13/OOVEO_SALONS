package edu.bluejack20_2.ooveo

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.*

class RegisterActivity : AppCompatActivity() {
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

        registerBtn!!.setOnClickListener( View.OnClickListener {
                var txtName: String = edtName.text.toString()
                var txtPhone: String = edtPhone.text.toString()
                var txtEmail: String = edtEmail.text.toString()
                var txtPassword: String = edtPassword.text.toString()
                var txtRepassword: String = edtRepassword.text.toString()
                var dateText: String = dateButton.text.toString()
                var gender: String = ""

                if(txtName.isEmpty()){
                    Toast.makeText(this, "Name must be filled!", Toast.LENGTH_SHORT).show()
                }else if(txtPhone.isEmpty()){
                    Toast.makeText(this, "Phone must be filled!", Toast.LENGTH_SHORT).show()
                }else if(txtEmail.isEmpty()){
                    Toast.makeText(this, "Email must be filled!", Toast.LENGTH_SHORT).show()
                }else if(!rbFemale.isChecked && !rbMale.isChecked){
                    Toast.makeText(this, "Gender must be selected!", Toast.LENGTH_SHORT).show()
                    if(rbFemale.isChecked){
                        gender = "Female"
                    }else{
                        gender = "Male"
                    }
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
                    createAccount(txtEmail, txtPassword);
//                    saveUserFireStore(txtName, txtPhone, txtEmail, gender, dateText, txtPassword)
                    var intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)

                }

            }
        )
    }

    private fun saveUserFireStore(txtName: String, txtPhone: String, txtEmail:String, gender:String, txtDate: String, txtPassword: String ){
        val db = FirebaseFirestore.getInstance()
        val user: MutableMap<String, Any> = HashMap()
        user["name"] = txtName
        user["phone"] = txtPhone
        user["email"] = txtEmail
        user["gender"] = gender
        user["dob"] = txtDate
        user["password"] = txtPassword

        db.collection("users")
            .add(user)
            .addOnSuccessListener {
                Toast.makeText(this@RegisterActivity, "user added successfully ", Toast.LENGTH_SHORT ).show()
            }
            .addOnFailureListener{
                Toast.makeText(this@RegisterActivity, "user Failed to add ", Toast.LENGTH_SHORT ).show()
            }

    }

    private fun init(){
        textViewLogin = findViewById(R.id.tvRegisterLogin)
        registerBtn = findViewById(R.id.btnRegisterRegister)
        edtName = findViewById(R.id.edtRegisterName)
        edtEmail = findViewById(R.id.edtRegisterEmail)
        edtPhone = findViewById(R.id.edtRegisterPhoneNumber)
        edtPassword = findViewById(R.id.edtRegisterPassword)
        edtRepassword = findViewById(R.id.edtRegisterRePassword)
        rbFemale = findViewById(R.id.rbRegisterFemale)
        rbMale = findViewById(R.id.rbRegisterMale)
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

    private fun createAccount(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if(task.isSuccessful) {
                  val registerIntent = Intent(this, MainActivity::class.java)
                  startActivity(registerIntent)
                }
                else {
                    Toast.makeText(this, "Register failed!", Toast.LENGTH_SHORT).show()
                }
            }
    }
}