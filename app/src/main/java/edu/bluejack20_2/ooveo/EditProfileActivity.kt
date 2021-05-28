package edu.bluejack20_2.ooveo

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import at.favre.lib.crypto.bcrypt.BCrypt
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.SignInMethodQueryResult
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import edu.bluejack20_2.ooveo.model.UserModel
import java.io.IOException
import java.util.*

class EditProfileActivity : AppCompatActivity() {
    private lateinit var changePasswordBtn: Button
    private lateinit var changePhoneNumberBtn: Button
    private lateinit var saveChangesBtn: Button
    private lateinit var textName: EditText
    private lateinit var textEmail: EditText
    private lateinit var profileUser: ImageView

    private lateinit var currentEmail: String
    private lateinit var currentPhone: String
    private lateinit var currentPhotoProfile: String

    private var filePath: Uri? = null
    private var storage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private val PICK_IMAGE_REQUEST = 1234

    private lateinit var user: UserModel
    private lateinit var db: FirebaseFirestore
    private lateinit var myRef: DocumentReference
    private lateinit var mAuth: FirebaseAuth
    private lateinit var edtProfilePicture: String
    private lateinit var currentPassword: String
    private lateinit var profileUrl: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        init()

        //init firebase
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance()

//        val viewModel = ViewModelProvider(this).get(EditProfileActivityViewModel::class.java)
//        viewModel.getPP().observe(this, {
//            val requestOption = RequestOptions()
//                .placeholder(R.drawable.ic_launcher_background)
//                .error(R.drawable.ic_launcher_background)
//
//            Glide.with(this)
//                .applyDefaultRequestOptions(requestOption)
//                .load(it)
//                .into(profileUser)
//
//            println("THIS IS IT")
//            println(it)
//        })

        val docRef = db.collection("users").document(mAuth.currentUser.uid.toString())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    user = UserModel(
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
                    textName.setText(user.name)
                    textEmail.setText(user.email)
                    currentEmail = user.email
                    currentPassword = user.password
                    currentPhone = user.phone
                    currentPhotoProfile = user.profilePicture

                    val requestOption = RequestOptions()
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)

                    Glide.with(this)
                        .applyDefaultRequestOptions(requestOption)
                        .load(currentPhotoProfile)
                        .into(profileUser)
                    Log.d("TAMPILIN DATA", "DocumentSnapshot data: ${document.data}")

                } else {
                    Log.d("GAGAL", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }

        profileUser!!.setOnClickListener(View.OnClickListener {
            choosePicture()
        })

        changePhoneNumberBtn!!.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, ChangePhoneNumberVerifActivity1::class.java)
            startActivity(intent)
        })

        changePasswordBtn!!.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, ChangePasswordAuthActivity1::class.java)
            intent.putExtra("phone", currentPhone)
            intent.putExtra("email", currentEmail)
            intent.putExtra("password", currentPassword)
            startActivity(intent)

        })

        saveChangesBtn!!.setOnClickListener(View.OnClickListener { taskSnapshot ->
            var txtName: String = textName.text.toString()
            var txtEmail: String = textEmail.text.toString()

            val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

            if (txtName.isEmpty()) {
                Toast.makeText(this, "Name must be filled!", Toast.LENGTH_SHORT).show()
            } else if (txtName.length < 3) {
                Toast.makeText(this, "Name must be more than 3 character!", Toast.LENGTH_SHORT)
                    .show()
            } else if (txtEmail.isEmpty()) {
                Toast.makeText(this, "Email must be filled!", Toast.LENGTH_SHORT).show()
            } else if (!(txtEmail.matches(emailPattern.toRegex()))) {
                Toast.makeText(
                    applicationContext,
                    "Invalid email address format",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                updateDataUser(txtName, txtEmail)
                finish()
            }
        })

    }

    private fun choosePicture() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "SELECT PICTURE"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
            && data != null && data.data != null
        ) {
            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                profileUser!!.setImageBitmap(bitmap)
                //LGSG UPLOAD AJA GPP, KARENA NAMANYA GA RANDOM JADI BAKAL KE OVERRIDE HARUSNYA
                edtProfilePicture = uploadFile()

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun uploadFile(): String {
        var imageURL: String = ""
        if (filePath != null) {
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading... ")
            progressDialog.show()

            val imageRef = storageReference!!.child("user/" + mAuth.currentUser.uid.toString())
            imageRef.putFile(filePath!!)
                .addOnSuccessListener {

                    progressDialog.dismiss()
                    imageRef.downloadUrl.addOnSuccessListener {

                        currentPhotoProfile = it.toString()
                    }


                    Toast.makeText(applicationContext, "File Uploaded", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()
                }
                .addOnProgressListener { taskSnapShot ->
                    val progress =
                        100.00 * taskSnapShot.bytesTransferred / taskSnapShot.totalByteCount
                    progressDialog.setMessage("Uploaded " + progress.toInt() + "%... ")

                }
        }
        return imageURL
    }

    private fun init() {
        changePasswordBtn = findViewById(R.id.btnEditProfileChangePassword)
        saveChangesBtn = findViewById(R.id.btnEditProfileSaveChanges)
        textName = findViewById(R.id.edtEditProfileName)
        textEmail = findViewById(R.id.edtEditProfileEmail)
        profileUser = findViewById(R.id.ivEditProfileUserImage)
        changePhoneNumberBtn = findViewById(R.id.btnEditProfileChangePhoneNumber)
    }

    private fun updateDataUser(edtName: String, edtEmail: String) {
        val userRef = db.collection("users").document(mAuth.currentUser.uid.toString())
        //KALO EMAIL GA DI GANTI, GAUSAH GANTI phone numbernya
        if (edtEmail == currentEmail) {
            userRef.update(
                "name", edtName, "profilePicture", currentPhotoProfile

            ).addOnSuccessListener {
                Log.d("SUKSES", "DocumentSnapshot successfully updated!")
                Toast.makeText(this, "Success save changes", Toast.LENGTH_SHORT).show()

            }
                .addOnFailureListener { e ->
                    Log.w("ERROR", "Error updating document", e)
                    Toast.makeText(this, "Failed save changes", Toast.LENGTH_SHORT).show()
                }
        } else {
            //CHECK EMAIL UDAH PERNAH DI PAKE ATAU BELOM
            mAuth.fetchSignInMethodsForEmail(edtEmail)
                .addOnCompleteListener(OnCompleteListener<SignInMethodQueryResult>() {
                    var check: Boolean = it.result!!.signInMethods!!.isEmpty()

                    if (!check) {
                        Toast.makeText(this, "Email already used!", Toast.LENGTH_SHORT).show()
                    } else {
                        val userAuth = mAuth.currentUser!!


                        // UPDATE EMAIL
                        userAuth!!.updateEmail(edtEmail)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d("UPDATE USER EMAIL", "User email address updated.")
                                }
                            }
                        //UPDATE DATA DI FIRESTORE
                        userRef.update(
                            "name", edtName,
                            "email", edtEmail,
                            "profilePicture", currentPhotoProfile
                        ).addOnSuccessListener {
                            Log.d("SUKSES", "DocumentSnapshot successfully updated!")
                            Toast.makeText(this, "Success save changes", Toast.LENGTH_SHORT).show()

                        }.addOnFailureListener { e ->
                                Log.w("ERROR", "Error updating document", e)
                                Toast.makeText(this, "Failed save changes", Toast.LENGTH_SHORT)
                                    .show()
                            }
                    }
                })
        }
    }

}