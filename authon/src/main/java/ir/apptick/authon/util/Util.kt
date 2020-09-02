package ir.apptick.authon.util

import android.app.Activity
import android.content.res.Configuration
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import java.util.*

object Util {
    fun hideKeyboardFrom(fragment: Fragment) {
        val imm = fragment.requireContext()
            .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(fragment.requireView().rootView.windowToken, 0)
        fragment.requireView().rootView.clearFocus()
    }

    @Suppress("DEPRECATION")
    fun changeLanguage(activity: FragmentActivity, mLanguage: String) {
        val locale = Locale(mLanguage)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        activity.baseContext.resources.updateConfiguration(
            config,
            activity.baseContext.resources.displayMetrics
        )
    }
}