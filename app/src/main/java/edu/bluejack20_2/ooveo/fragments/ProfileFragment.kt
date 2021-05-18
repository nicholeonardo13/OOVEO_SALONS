package edu.bluejack20_2.ooveo.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.ooveo.*
import edu.bluejack20_2.ooveo.model.User

class ProfileFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var logoutBtn: Button
    private lateinit var languageBtn: Button
    private lateinit var edtProfileBtn: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var userProfile: ImageView

    private lateinit var db: FirebaseFirestore
    private lateinit var edtName: TextView
    private lateinit var user: User
    private lateinit var edtEmail: TextView
    private lateinit var ivProfilePicture: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        logoutBtn = view!!.findViewById<Button>(R.id.btnProfileLogout)
        languageBtn = view!!.findViewById<Button>(R.id.btnProfileLanguage)
        edtProfileBtn = view!!.findViewById<Button>(R.id.btnProfilEditProfile)
        userProfile = view!!.findViewById<ImageView>(R.id.ivProfileUserImage)
        edtName = view!!.findViewById<TextView>(R.id.tvProfileUserName)
        edtEmail = view!!.findViewById<TextView>(R.id.tvProfileEmail)
        ivProfilePicture = view!!.findViewById<ImageView>(R.id.ivProfileUserImage)

        println(" ")
        println(" ")
        println(" ")
        println("ID CURRENT USER")
        println(mAuth.currentUser.uid.toString())

        val docRef =  db.collection("users").document(mAuth.currentUser.uid.toString())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    user = User(
                        document.id.toString(),
                        document["role"].toString(),
                        document["name"].toString(),
                        document["phoneNumber"].toString(),
                        document["email"].toString(),
                        document["gender"].toString(),
                        document["dob"].toString(),
                        document["password"].toString(),
                        document["profilePicture"].toString()
                    )
                    edtName.setText(user.name)
                    edtEmail.setText(user.email)

                    val requestOption = RequestOptions()
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)

                    Glide.with(this)
                        .applyDefaultRequestOptions(requestOption)
                        .load(user.profilePicture)
                        .into(ivProfilePicture)
                    Log.d("TAMPILIN DATA", "DocumentSnapshot data: ${document.data}")

                } else {
                    Log.d("GAGAL", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }


        //Change user image profile
        userProfile!!.setOnClickListener(View.OnClickListener {

        })

        //LOGOUT
        logoutBtn!!.setOnClickListener(View.OnClickListener {
            //FirebaseAuth.getInstance().signOut();
            mAuth.signOut()
            val intent = Intent(this.context, MainActivity::class.java)
            startActivity(intent);
            activity?.finish()

        })

        //CHANGE LANGUAGE
        languageBtn!!.setOnClickListener(View.OnClickListener {
            var intent = Intent(this.context, LanguageActivity::class.java)
            startActivity(intent)
        })

        //MOVE TO EDIT PROFILE PAGE
        edtProfileBtn!!.setOnClickListener(View.OnClickListener {

            val viewModel = ViewModelProvider(requireActivity()).get(EditProfileActivityViewModel::class.java)
            println("Print PP: "+ user.profilePicture)
            viewModel.addPP(user.profilePicture)

            var intent = Intent(this.context, EditProfileActivity::class.java)
            startActivity(intent)
        })
    }


}