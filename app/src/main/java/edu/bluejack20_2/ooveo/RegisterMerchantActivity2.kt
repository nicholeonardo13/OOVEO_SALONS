package edu.bluejack20_2.ooveo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.SignInMethodQueryResult
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.ooveo.homes.HomeActivity
import java.util.*
import kotlin.collections.HashMap

class RegisterMerchantActivity2 : AppCompatActivity() {

    private lateinit var aboutUs: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var repassword: EditText
    private lateinit var registerBtn: Button
    private lateinit var loginTextView: TextView

    private lateinit var txtAboutUs: String
    private lateinit var txtEmail: String
    private lateinit var txtPassword: String
    private lateinit var txtRepassword: String

    private lateinit var autoCompeleteTv: AutoCompleteTextView
    private lateinit var mAuth: FirebaseAuth

    private lateinit var typeSpinner: TextInputLayout
    private lateinit var db: FirebaseFirestore

    override fun onResume() {
        super.onResume()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_merchant2)
        init()
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val arrayTypes = arrayOf(resources.getStringArray(R.array.types))
        val types = resources.getStringArray(R.array.types)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item_type, types)
        autoCompeleteTv.setAdapter(arrayAdapter)



        loginTextView!!.setOnClickListener(View.OnClickListener {
            var loginIntent = Intent(this, MainActivity::class.java)
            startActivity(loginIntent)
            finish()
        })

//        intent.putExtra("name", txtName)
//        intent.putExtra("address", txtAddress)
//        intent.putExtra("location", txtLocation)
//        intent.putExtra("phone", txtPhone)
//        //INI HARUS DIGANTI JADI URL YG UDH DI DOWNLOAD
//        intent.putExtra("banner", bannerURL)

        val name = intent.getStringExtra("name").toString()
        val address = intent.getStringExtra("address").toString()
        val location = intent.getStringExtra("location").toString()
        val phoneNumber = intent.getStringExtra("phone").toString()
        val bannerURL = intent.getStringExtra("banner").toString()

        registerBtn!!.setOnClickListener(View.OnClickListener {
            //Get type from spinner
            val typeInput = autoCompeleteTv.text.toString()
            val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
            txtAboutUs = aboutUs.text.toString()
            txtEmail = email.text.toString()
            txtPassword = password.text.toString()
            txtRepassword = repassword.text.toString()

            println("Types $typeInput")
            if(txtAboutUs.isEmpty()){
                Toast.makeText(this, "About Us must be filled!", Toast.LENGTH_SHORT).show()
            }else if(typeInput.isEmpty()){
                Toast.makeText(applicationContext, "Please choose types!",Toast.LENGTH_SHORT).show()
            }else if(txtEmail.isEmpty()){
                Toast.makeText(this, "Email Us must be filled!", Toast.LENGTH_SHORT).show()
            }else if(!(txtEmail.matches(emailPattern.toRegex()))) {
                Toast.makeText(applicationContext, "Invalid email address format",Toast.LENGTH_SHORT).show()
            }else if(txtPassword.isEmpty()){
                Toast.makeText(applicationContext, "Password must be filled!",Toast.LENGTH_SHORT).show()
            }else if(txtPassword.length < 8){
                Toast.makeText(applicationContext, "Password must be more than 8 characters",Toast.LENGTH_SHORT).show()
            }else if(txtRepassword.isEmpty()){
                Toast.makeText(applicationContext, "Repassword must be filled",Toast.LENGTH_SHORT).show()
            }else if(txtPassword != txtRepassword){
                Toast.makeText(applicationContext, "Password and Re-password not match",Toast.LENGTH_SHORT).show()
            }else {
                mAuth.fetchSignInMethodsForEmail(txtEmail).addOnCompleteListener(OnCompleteListener<SignInMethodQueryResult>(){
                    var check: Boolean = it.result!!.signInMethods!!.isEmpty()

                    if(!check){
                        Toast.makeText(this, "Email already used!", Toast.LENGTH_SHORT).show()
                    }else{
                        //Gausah di hash
                        //val passHash = BCrypt.withDefaults().hashToString(12,txtPassword.toCharArray())
                        mAuth.createUserWithEmailAndPassword(txtEmail, txtPassword)
                                .addOnCompleteListener(this) { task ->
                                    if (task.isSuccessful) {

                                        createAccount(name, address, location, phoneNumber, bannerURL, txtAboutUs,
                                                typeInput, txtEmail, txtPassword);
                                        var intent = Intent(this, HomeActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("TAG", "createUserWithEmail:success")
                                        val user = mAuth.currentUser
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("FAILED CREATE REGISTER", "createUserWithEmail:failure", task.exception)
                                        Toast.makeText(baseContext, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show()
                                    }
                                }
                    }
                })
            }

        })
    }

    fun init(){
        aboutUs = findViewById(R.id.edtMerchantRegisterAboutUs)
        email = findViewById(R.id.edtMerchantRegisterEmail)
        password = findViewById(R.id.edtMerchantRegisterPassword)
        repassword = findViewById(R.id.edtMerchantRegisterRePassword)
        registerBtn = findViewById(R.id.btnMerchantRegisterRegister)
        loginTextView = findViewById(R.id.tvMerchantRegisterLogin)
        autoCompeleteTv = findViewById(R.id.autoCompleteTextView)
        typeSpinner = findViewById(R.id.spnRegisterMerchantTypes)
    }

    private fun createAccount(name: String, address: String, location: String, phoneNumber: String,
                              bannerUrl:String, aboutUs: String, types: String, email:String,
                              password: String) {
        saveUserFireStore(name, address, location, phoneNumber, bannerUrl, aboutUs, types, email, password)

    }

    private fun saveUserFireStore(name: String, address: String, location: String, phoneNumber: String,
                                  bannerUrl:String, aboutUs: String, types: String, email:String,
                                  password: String){
        val user: MutableMap<String, Any> = HashMap()
        val merchant: MutableMap<String, Any> = HashMap()


        user["name"] = name
        user["address"] = address
        user["location"] = location
        user["phone"] = phoneNumber
        user["profilePicture"] = bannerUrl
        user["aboutUs"] = aboutUs
        user["types"] = types
        user["email"] = email
        user["password"] = password
        user["role"] = "Merchant"
        user["mode"] = "light"

        merchant["aboutus"] = aboutUs
        merchant["image"] = bannerUrl
        merchant["location"] = location
        merchant["address"] = address
        merchant["name"] = name
        merchant["phoneNumber"] = phoneNumber
        merchant["type"] = types
        merchant["ownerID"] = mAuth.currentUser.uid.toString()

        db.collection("merchants")
                .add(merchant)
                .addOnSuccessListener {
                    Toast.makeText(this, "add to merchant success ", Toast.LENGTH_SHORT ).show()
                }
                .addOnFailureListener{
                    Toast.makeText(this, "Failed add to merchants ", Toast.LENGTH_SHORT ).show()
                }


        db.collection("users")
                .document(mAuth.currentUser.uid.toString())
                .set(user)
                .addOnSuccessListener {
                    Toast.makeText(this, "Register success ", Toast.LENGTH_SHORT ).show()
                }
                .addOnFailureListener{
                    Toast.makeText(this, "Failed to Register ", Toast.LENGTH_SHORT ).show()
                }



    }}