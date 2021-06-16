package edu.bluejack20_2.ooveo

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater

class LoadingDialog {
    private  var activity: Activity;
    private lateinit var dialog: AlertDialog;

    constructor(myActivity: Activity){
        activity = myActivity
    }

    fun startLoadingDialog(){
        var builder =  AlertDialog.Builder(activity)
        var inflater: LayoutInflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custome_progressbar, null))
        builder.setCancelable(true)
        dialog = builder.create()
        dialog.show()
    }

    fun dismissDialog(){
        dialog.dismiss();
    }
}