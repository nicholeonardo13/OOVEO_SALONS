package edu.bluejack20_2.ooveo.homes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import edu.bluejack20_2.ooveo.R
import edu.bluejack20_2.ooveo.adapters.viewPagerAdapter
import edu.bluejack20_2.ooveo.communicators.Communicator
import edu.bluejack20_2.ooveo.fragments.*
import edu.bluejack20_2.ooveo.viewmodels.DetailMerchantActivityViewModel

class DetailMerchantActivity : AppCompatActivity() , Communicator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_merchant)


//        toolBar.title = ""
//
//        setSupportActionBar(toolBar)

        var id = intent.extras?.getString("id")
        var address = intent.extras?.getString("address")
        var image = intent.extras?.getString("image")
        var name = intent.extras?.getString("name")
        var phoneNumber = intent.extras?.getString("phoneNumber")
        var location = intent.extras?.getString("location")
        var type = intent.extras?.getString("type")
        var about = intent.extras?.getString("about")
        if (id != null) {
//            passDataCom(id)
            var viewModel = ViewModelProvider(this).get(DetailMerchantActivityViewModel::class.java)
            if (name != null) {
                if (address != null) {
                    if (image != null) {
                        if (phoneNumber != null) {
                            if (location != null) {
                                if (type != null) {
                                    if (about != null) {
                                        viewModel.passData(id , name , address , image , phoneNumber , location , type , about)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Log.d("ROR", "Berhasil")
        }else {
            Log.d("ROR", "GAGAL")
        }
        var ivDetailMerchant = findViewById<ImageView>(R.id.ivDetailMerchant)

        val requestOption = RequestOptions()
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)

        Glide.with(this)
            .applyDefaultRequestOptions(requestOption)
            .load(image)
            .into(ivDetailMerchant)

        setTab()
    }




    fun setTab(){
        val adapter = viewPagerAdapter(supportFragmentManager)
        adapter.addFragment(ServiceFragment() , "Services")
        adapter.addFragment(ReviewFragment() , "Reviews")
        adapter.addFragment(MoreInfoFragment(), "More Info")
        val viewPager = findViewById<ViewPager>(R.id.viewPager)
        viewPager.adapter = adapter
        val tabs = findViewById<TabLayout>(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

//        tabs.getTabAt(0)!!.setIcon(R.drawable.ic_baseline_home_24)
//        tabs.getTabAt(1)!!.setIcon(R.drawable.ic_baseline_favorite_24)
//        tabs.getTabAt(2)!!.setIcon(R.drawable.ic_baseline_calendar_today_24)
//        tabs.getTabAt(3)!!.setIcon(R.drawable.ic_baseline_account_circle_24)
    }

    override fun passDataCom(id: String) {
        val bundle = Bundle()
        bundle.putString("ids" , id)

        val transaction = this.supportFragmentManager.beginTransaction()

        val fragmentService = ServiceFragment()
        fragmentService.arguments = bundle

        transaction.replace(R.id.viewPager, fragmentService)
        transaction.commit()

    }
}