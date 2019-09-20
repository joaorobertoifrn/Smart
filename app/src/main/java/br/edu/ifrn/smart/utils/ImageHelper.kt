package br.edu.ifrn.smart.utils

import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue

import br.edu.ifrn.smart.BuildConfig

object ImageHelper {

    fun dipTOpx(dip: Int, context: Context): Int {
        val displayMetrics = context.resources.displayMetrics
        val px = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip.toFloat(), displayMetrics))
        if (BuildConfig.DEBUG)
            Log.d("dipTOpx", "${dip} dp is ${px}px on this screen with density ${displayMetrics.densityDpi}")
        return px
    }

}
