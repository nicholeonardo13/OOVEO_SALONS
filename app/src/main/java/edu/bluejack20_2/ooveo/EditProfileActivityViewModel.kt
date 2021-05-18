package edu.bluejack20_2.ooveo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EditProfileActivityViewModel: ViewModel(){
   private var profilePicture = MutableLiveData<String>()


    fun addPP(pp: String){
            profilePicture.value = pp
    }

    fun getPP(): LiveData<String> {

        return profilePicture
    }

}