
package com.matdata.ui.snackBar

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.view.View

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
internal class SnackbarHelperChildViewJB(context: Context) : View(context) {
    init {
        isSaveEnabled = false
        setWillNotDraw(true)
        visibility = View.GONE
    }

    override fun onWindowSystemUiVisibilityChanged(visible: Int) {
        super.onWindowSystemUiVisibilityChanged(visible)

        val parent = parent
        if (parent is Snackbar) {
            parent.dispatchOnWindowSystemUiVisibilityChangedCompat(visible)
        }
    }
}
