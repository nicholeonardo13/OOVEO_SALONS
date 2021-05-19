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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import edu.bluejack20_2.ooveo.viewmodels.EditProfileActivityViewModel
import java.io.IOException

class EditProfileActivity : AppCompatActivity() {
    private lateinit var changePhoneNumberBtn: Button
    private lateinit var changePasswordBtn: Button
    private lateinit var saveChangesBtn: Button
    private lateinit var textName: EditText
    private lateinit var textEmail: EditText
    private lateinit var  profileUser: ImageView

    private var filePath: Uri? = null
    private var storage: FirebaseStorage?=null
    private var storageReference: StorageReference? = null
    private val PICK_IMAGE_REQUEST = 1234

    private lateinit var db: FirebaseFirestore
    private lateinit var myRef: DocumentReference
    private lateinit var mAuth: FirebaseAuth
    private lateinit var edtProfilePicture: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        init()

        //init firebase
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance()

        val viewModel = ViewModelProvider(this).get(EditProfileActivityViewModel::class.java)
        viewModel.getPP().observe(this,{
            val requestOption = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)

            Glide.with(this)
                .applyDefaultRequestOptions(requestOption)
                .load(it)
                .into(profileUser)

            println("THIS IS IT")
            println(it)
        })


        profileUser!!.setOnClickListener(View.OnClickListener {
            choosePicture()
        })

        changePhoneNumberBtn!!.setOnClickListener(View.OnClickListener {

        })

        changePasswordBtn!!.setOnClickListener(View.OnClickListener {
            //Open chane password page
        })

        saveChangesBtn!!.setOnClickListener(View.OnClickListener { taskSnapshot ->
            var edtName: String = textName.text.toString()
            var edtEmail: String = textEmail.text.toString()


            updateDataUser(edtName, edtEmail)
        })

    }

    private fun choosePicture() {
        val intent = Intent()
        intent.type="image/*"
        intent.action= Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "SELECT PICTURE"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
            && data != null && data.data != null){
            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                profileUser!!.setImageBitmap(bitmap)
                //LGSG UPLOAD AJA GPP, KARENA NAMANYA GA RANDOM JADI BAKAL KE OVERRIDE HARUSNYA
                edtProfilePicture = uploadFile()

            }catch (e: IOException){
                e.printStackTrace()
            }
        }
    }

    private fun uploadFile(): String{
        var imageURL: String = ""
        if(filePath != null){
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading... ")
            progressDialog.show()

            val imageRef = storageReference!!.child("user/" + mAuth.currentUser.uid.toString())
            imageRef.putFile(filePath!!)
                .addOnSuccessListener {

                    progressDialog.dismiss()
                    imageRef.downloadUrl.addOnSuccessListener {

                        val userRef = db.collection("users").document(mAuth.currentUser.uid.toString())


                        userRef.update(
                                "profilePicture", it.toString()
                        ).addOnSuccessListener {
                            Log.d("SUKSES", "DocumentSnapshot successfully updated!")
                            Toast.makeText(this, "Update profile picture", Toast.LENGTH_SHORT ).show()

                        }.addOnFailureListener { e ->
                            Log.w("ERROR", "Error updating document", e)
                            Toast.makeText(this, "Failed update profile picture", Toast.LENGTH_SHORT ).show()}
                    }


                    Toast.makeText(applicationContext, "File Uploaded", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener{
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()
                }
                .addOnProgressListener { taskSnapShot->
                    val progress = 100.00 * taskSnapShot.bytesTransferred/taskSnapShot.totalByteCount
                    progressDialog.setMessage("Uploaded " + progress.toInt() + "%... ")

                }
        }
        return imageURL
    }

    private fun init() {
        changePhoneNumberBtn = findViewById(R.id.btnEditProfileChangePhoneNumber)
        changePasswordBtn = findViewById(R.id.btnEditProfileChangePassword)
        saveChangesBtn = findViewById(R.id.btnEditProfileSaveChanges)
        textName = findViewById(R.id.edtEditProfileName)
        textEmail = findViewById(R.id.edtEditProfileEmail)
        profileUser = findViewById(R.id.ivEditProfileUserImage)
    }

    private fun updateDataUser(edtName: String, edtEmail:String){
        val userRef = db.collection("users").document(mAuth.currentUser.uid.toString())

        userRef.update(
            "name", edtName,
            "email", edtEmail
        ).addOnSuccessListener {
            Log.d("SUKSES", "DocumentSnapshot successfully updated!")
            Toast.makeText(this, "Success save changes", Toast.LENGTH_SHORT ).show()

        }
                .addOnFailureListener { e ->
                    Log.w("ERROR", "Error updating document", e)
                    Toast.makeText(this, "Failed save changes", Toast.LENGTH_SHORT ).show()}
    }

}