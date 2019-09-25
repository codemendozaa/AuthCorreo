package com.codemen.authcorreo

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import java.lang.Exception

open class Utils : AppCompatActivity(){

    @VisibleForTesting
    val progressDialog by lazy {
        ProgressDialog(this)
    }

    fun showProgressDialog() {
        progressDialog.setMessage(getString(R.string.loading))
        progressDialog.isIndeterminate = true
        progressDialog.show()
    }

    fun hideProgressDialog() {
        if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }

    fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    public override fun onStop() {
        super.onStop()
        hideProgressDialog()
    }


    fun Activity.ToastShort(mensaje :String){
        Toast.makeText(this,mensaje, Toast.LENGTH_SHORT).show()

    }

    fun Activity.ToastLong(mensaje :String){
        Toast.makeText(this,mensaje, Toast.LENGTH_LONG).show()

    }

    fun writeToLog(content: String, exception: Exception?) {
        Log.d("TestLog:", content)
    }

    fun writeLog(content: String) {
        Log.d("TestLog:", content)
    }


}