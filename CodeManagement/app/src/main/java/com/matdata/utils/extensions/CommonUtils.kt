
package com.matdata.utils.extensions

import android.graphics.Typeface
import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.matdata.R


fun View.visibleView(visible: Boolean) {
    this.visibility = if (visible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}


//change status bar color
@RequiresApi(Build.VERSION_CODES.O)
fun AppCompatActivity.setStatusBarColor(color: Int, showLight: Boolean = true) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val window = this.window

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = color
        window.navigationBarColor = color


    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val view = this.window.decorView
        view.let {
            if (showLight) {

                it.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR

            } else {
                it.systemUiVisibility = it.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv() //set status text  light

            }
        }
    }
}


fun TextView.showTextSelected(isSelected: Boolean) {
    if (isSelected) {
        setTextColor(ContextCompat.getColor(this.context, R.color.Black))
        setTypeface(null, Typeface.BOLD)
    } else {
        setTextColor(ContextCompat.getColor(this.context, R.color.Gray))
        setTypeface(null, Typeface.NORMAL)


    }
}


fun TextView.setColor(lightGrey: Int) {
    this.setTextColor(ContextCompat.getColor(this.context !!, lightGrey))
}

fun TextView.setSpanString(spanText: String, start: Int, end: Int = spanText.length, showBold: Boolean = false, color: Int = R.color.colorPrimary, onSpanClick: () -> Unit = {}) {
    val ss = SpannableString(spanText)
    val clickableSpan = object : ClickableSpan() {
        override fun onClick(textView: View) {
            onSpanClick()
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
            ds.color = ContextCompat.getColor(this@setSpanString.context, color)
        }
    }
    ss.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    if (showBold) {
        ss.setSpan(StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    text = ss
    movementMethod = LinkMovementMethod.getInstance()
    highlightColor = ContextCompat.getColor(this@setSpanString.context, R.color.transparent)

}


fun <T> LiveData<T>.reObserve(owner: LifecycleOwner, observer: Observer<T>) {
    removeObserver(observer)
    observe(owner, observer)
}

fun RecyclerView.onScroll(onScrollState: (state: Int) -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            onScrollState(newState)
        }
    })
}

fun TextView.setHtmlData(source: String?) {
    text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(source)
    }
}






