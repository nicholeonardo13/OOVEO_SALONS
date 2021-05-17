package edu.bluejack20_2.ooveo

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException
import java.util.*

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        init()

        //init firebase
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference

        profileUser!!.setOnClickListener(View.OnClickListener {
            choosePicture()
        })

        changePhoneNumberBtn!!.setOnClickListener(View.OnClickListener {

        })

        changePasswordBtn!!.setOnClickListener(View.OnClickListener {
            //Open chane password page
        })

        saveChangesBtn!!.setOnClickListener(View.OnClickListener {
            //Something will happen
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

            }catch (e: IOException){
                e.printStackTrace()
            }
        }
    }

    private fun uploadFile(){
        if(filePath != null){
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading... ")
            progressDialog.show()

            val imageRef = storageReference!!.child("images/"+UUID.randomUUID().toString())
            imageRef.putFile(filePath!!)
                .addOnSuccessListener {
                    progressDialog.dismiss()
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
    }

    private fun init() {
        changePhoneNumberBtn = findViewById(R.id.btnEditProfileChangePhoneNumber)
        changePasswordBtn = findViewById(R.id.btnEditProfileChangePassword)
        saveChangesBtn = findViewById(R.id.btnEditProfileSaveChanges)
        textName = findViewById(R.id.edtEditProfileName)
        textEmail = findViewById(R.id.edtEditProfileEmail)
        profileUser = findViewById(R.id.tvEditProfileUserImage)
    }
}