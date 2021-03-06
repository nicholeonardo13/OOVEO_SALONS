package edu.bluejack20_2.ooveo

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.ooveo.homes.HomeActivity
import edu.bluejack20_2.ooveo.model.UserModel
import java.util.HashMap

class MainActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    companion object{
        private const val RC_SIGN_IN = 120
    }

    private var registerNowBtn: TextView? = null
    private lateinit var mAuth: FirebaseAuth
    private lateinit var google_sign_in_btn: ImageView
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var signinBtn: Button
    private lateinit var txtEmail: EditText
    private lateinit var txtPassword: EditText

    private lateinit var userModel: UserModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        db = FirebaseFirestore.getInstance()
        registerNowBtn!!.setOnClickListener {
            val intent = Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        //Firebase Auth instance
        mAuth = FirebaseAuth.getInstance()

        signinBtn!!.setOnClickListener(View.OnClickListener {
            var email: String =  txtEmail.text.toString()
            var password: String = txtPassword.text.toString()

//            var email: String =  "sei@gmail.com"
//            var password: String = "sipsipdripdrip"
            if(email.isEmpty()){
                Toast.makeText(this, getString(R.string.emailHarusDiisiBos), Toast.LENGTH_SHORT).show()
            }else if(password.isEmpty()){
                Toast.makeText(this, getString(R.string.passwordKosong), Toast.LENGTH_SHORT).show()
            }else{
                signIn(email, password)
            }
         }
        )

        google_sign_in_btn!!.setOnClickListener{
            signInWithGoogle()
            println("Button Clicked!")
        }

    }

    private fun init() {
        registerNowBtn = findViewById(R.id.tvRegisterLogin)
        google_sign_in_btn = findViewById(R.id.ivMainGoogle)
        signinBtn = findViewById(R.id.btnMainLogin)
        txtEmail = findViewById(R.id.edtMainEmail)
        txtPassword = findViewById(R.id.edtMainPassword)
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun signIn(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if(task.isSuccessful) {
                    getUserDataChangeMode()
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent);
                    finish()
                }
                else {
                    Toast.makeText(this, getString(R.string.loginGagalMen), Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if(task.isSuccessful){
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("MainActivity", "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("MainActivity", "Google sign in failed", e)
                }
            }else{
                Log.w("MainActivity", exception.toString())
            }

        }
    }

    //SUCCESS LOGIN WITH GOOGLE
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("MainActivity", "signInWithCredential:success")

                    var name: String
                    var email: String
                    var photoUrl: Uri
                    var phoneNum: String
                    var uid: String

                    val user = mAuth.currentUser

                    println("phone num: " + user.phoneNumber + " name: " + user.displayName + " email: " + user.email.toString() )
                    saveUserFireStore(user.displayName, "Please add phone number", user.email, user.photoUrl.toString(), "xxx", "xxx" )



                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
//                    val user = mAuth.currentUser
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("MainActivity", "signInWithCredential:failure", task.exception)
                }
            }
    }

    private fun saveUserFireStore(txtName: String, txtPhone: String, txtEmail:String, photoUrl:String, txtDate: String, txtPassword: String){
        val user: MutableMap<String, Any> = HashMap()
        user["role"] = "User"
        user["name"] = txtName
        user["phone"] = txtPhone
        user["email"] = txtEmail
        user["dob"] = txtDate
        user["password"] = txtPassword
        user["profilePicture"] = photoUrl
        user["mode"] = "light"

        db.collection("users")
            .document(mAuth.currentUser.uid.toString())
            .set(user)
            .addOnSuccessListener {
                Toast.makeText(this, getString(R.string.berhasilRegister), Toast.LENGTH_SHORT ).show()
            }
            .addOnFailureListener{
                Toast.makeText(this, getString(R.string.gagalRegister), Toast.LENGTH_SHORT ).show()
            }

    }

    fun  getUserDataChangeMode(){
        val docRef = db.collection("users").document(mAuth.currentUser.uid.toString())
        docRef.get()
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
                        }else if(userModel.mode == "dark"){
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        }

                        Log.d("TAMPILIN DATA", "DocumentSnapshot data: ${document.data}")

                    } else {
                        Log.d("GAGAL", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("TAG", "get failed with ", exception)
                }

    }
}