package br.edu.ifrn.smart.activities

import android.support.v7.app.AppCompatActivity
import br.edu.ifrn.smart.views.CustomProgressDialog

open class BaseActivity : AppCompatActivity() {

    private var progressDialog: CustomProgressDialog? = null

    fun showProgressDialog() {
        progressDialog = CustomProgressDialog.show(this)
    }

    fun dismissProgressDialog() {
        progressDialog?.dismiss()
    }

}