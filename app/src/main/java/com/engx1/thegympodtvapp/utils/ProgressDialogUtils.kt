package com.engx1.thegympodtvapp.utils

import android.app.ProgressDialog
import android.content.Context
import android.os.Build

class ProgressDialogUtils {
    init {
        throw Error("U will not able to instantiate it")
    }
    companion object {
        private var progressDialog: ProgressDialog? = null
        fun show(context: Context) {
            try {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
                val style: Int =
                    android.R.style.Theme_Material_Light_Dialog

                progressDialog = ProgressDialog(context, style)
                progressDialog!!.setMessage("Please Wait...")
                progressDialog!!.setCancelable(false)
                progressDialog!!.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        fun show(context: Context, message: String) {
            try {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
                val style: Int =
                    android.R.style.Theme_Material_Light_Dialog

                progressDialog = ProgressDialog(context, style)
                progressDialog!!.setMessage(message)
                progressDialog!!.setCancelable(false)
                progressDialog!!.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        fun dismiss() {
            if (progressDialog != null && progressDialog!!.isShowing) {
                try {
                    progressDialog!!.dismiss()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                progressDialog = null
            }
        }
    }
}