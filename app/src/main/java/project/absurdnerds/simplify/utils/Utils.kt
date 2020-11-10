package project.absurdnerds.simplify.utils

import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment

/**
 * This function enable the visibility of View
 */
fun View.visible(): View {
    this.visibility = View.VISIBLE
    if (this is Group) {
        this.requestLayout()
    }
    return this
}

/**
 * This function hide the visibility of View
 */
fun View.inVisible(): View {
    this.visibility = View.INVISIBLE
    if (this is Group) {
        this.requestLayout()
    }
    return this
}

/**
 * This function remove the visibility of View
 */
fun View.gone(): View {
    this.visibility = View.GONE
    if (this is Group) {
        this.requestLayout()
    }
    return this
}

/**
 * This showToast fun can be called from Activity
 */
fun AppCompatActivity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

/**
 * This showToast fun can be called from fragment
 */
fun Fragment.showToast(message: String?) {
    Toast.makeText(this.activity, message, Toast.LENGTH_SHORT).show()
}