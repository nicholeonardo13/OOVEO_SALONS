package edu.bluejack20_2.ooveo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DetailMerchantActivityViewModel: ViewModel() {

    var ids = MutableLiveData<String>()
    var names = MutableLiveData<String>()
    var addresss = MutableLiveData<String>()
    var images = MutableLiveData<String>()
    var phoneNumbers = MutableLiveData<String>()
    var locations = MutableLiveData<String>()
    var types = MutableLiveData<String>()
    var abouts = MutableLiveData<String>()


    fun getID(): MutableLiveData<String> {
        return ids
    }

    fun getName(): MutableLiveData<String> {
        return names
    }

    fun getAddress(): MutableLiveData<String> {
        return addresss
    }

    fun getImage(): MutableLiveData<String> {
        return images
    }

    fun getPhoneNumber(): MutableLiveData<String> {
        return phoneNumbers
    }

    fun getLocation(): MutableLiveData<String> {
        return locations
    }

    fun getType(): MutableLiveData<String> {
        return types
    }

    fun getAbout(): MutableLiveData<String> {
        return abouts
    }

    fun passData(id : String , name : String , address : String , image : String , phoneNumber : String , location : String , type : String , about : String){
        ids.value = id
        names.value = name
        addresss.value = address
        images.value = image
        phoneNumbers.value = phoneNumber
        locations.value = location
        types.value = type
        abouts.value = about

    }

}