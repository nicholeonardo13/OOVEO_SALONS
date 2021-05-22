package edu.bluejack20_2.ooveo.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.ooveo.homes.BarberActivity
import edu.bluejack20_2.ooveo.R
import edu.bluejack20_2.ooveo.homes.BeautyActivity
import edu.bluejack20_2.ooveo.homes.ReflexologyActivity
import edu.bluejack20_2.ooveo.homes.SalonActivity
import edu.bluejack20_2.ooveo.model.UserModel

class HomeFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var userModel: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return  inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()


        val docRef =  db.collection("users").document(mAuth.currentUser.uid.toString())
        docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        userModel = UserModel(
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

                        val layoutBarber = view.findViewById<LinearLayout>(R.id.layoutBarber)
                        layoutBarber?.setOnClickListener {
                            Log.d("Click", "Barber Di Klik")
                            val intent = Intent(this.context, BarberActivity::class.java)
                            startActivity(intent)

                        }

                        val layoutBeauty = view.findViewById<LinearLayout>(R.id.layoutBeauty)
                        layoutBeauty?.setOnClickListener {
                            Log.d("Click", "Beauty Di Klik")
                            val intent = Intent(this.context, BeautyActivity::class.java)
                            startActivity(intent)

                        }

                        val layoutSalon = view.findViewById<LinearLayout>(R.id.layoutSalon)
                        layoutSalon?.setOnClickListener {
                            Log.d("Click", "layoutSalon Di Klik")
                            val intent = Intent(this.context, SalonActivity::class.java)
                            startActivity(intent)

                        }

                        val layoutRelflex = view.findViewById<LinearLayout>(R.id.layoutReflexy)
                        layoutRelflex?.setOnClickListener {
                            Log.d("Click", "layoutRelflex Di Klik")
                            val intent = Intent(this.context, ReflexologyActivity::class.java)
                            startActivity(intent)

                        }

                    } else {
                        Log.d("GAGAL", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("TAG", "get failed with ", exception)
                }

    }
}