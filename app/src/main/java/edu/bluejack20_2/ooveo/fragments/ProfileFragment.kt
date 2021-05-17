package edu.bluejack20_2.ooveo.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import edu.bluejack20_2.ooveo.EditProfileActivity
import edu.bluejack20_2.ooveo.LanguageActivity
import edu.bluejack20_2.ooveo.MainActivity
import edu.bluejack20_2.ooveo.R

class ProfileFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var logoutBtn: Button
    private lateinit var languageBtn: Button
    private lateinit var edtProfileBtn: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var userProfile: ImageView

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

        mAuth = FirebaseAuth.getInstance()

        logoutBtn = view.findViewById<Button>(R.id.btnProfileLogout)
        languageBtn = view.findViewById<Button>(R.id.btnProfileLanguage)
        edtProfileBtn = view.findViewById<Button>(R.id.btnProfilEditProfile)
        userProfile = view.findViewById<ImageView>(R.id.ivProfileUserImage)

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
            var intent = Intent(this.context, EditProfileActivity::class.java)
            startActivity(intent)
        })
    }


}