package br.edu.ifrn.smart.views

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.ViewGroup
import android.widget.LinearLayout

import br.edu.ifrn.smart.R

class CustomProgressDialog(context: Context) : Dialog(context, R.style.ProgressDialog) {
    companion object {

        @JvmOverloads fun show(context: Context, title: CharSequence? = null,
                               cancelable: Boolean = false, cancelListener: DialogInterface.OnCancelListener? = null): CustomProgressDialog {
            val dialog = CustomProgressDialog(context)
            dialog.setTitle(title)
            dialog.setCancelable(cancelable)
            dialog.setOnCancelListener(cancelListener)
            dialog.setContentView(R.layout.dialog_layout)
            val roundContainer = dialog.findViewById<LinearLayout>(R.id.roundContainer)

            val progressWheel = CustomProgressWheel(context)

            /* The next line will add the ProgressBar to the dialog. */
            roundContainer.addView(progressWheel, ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT))
            dialog.show()

            return dialog
        }
    }

}
