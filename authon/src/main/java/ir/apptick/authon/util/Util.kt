package ir.apptick.authon.util

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.google.gson.JsonParser
import ir.apptick.authon.Authon
import ir.apptick.authon.R
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

    fun changeFragment(
        containerId: Int,
        from: Fragment,
        to: Fragment,
        language: String,
        addToBackStack: Boolean = true
    ) {
        val tr = from.requireActivity().supportFragmentManager.beginTransaction()
        setAnimation(tr, language)
        if (addToBackStack)
            tr.replace(containerId, to)
        tr.addToBackStack(null)
        tr.commit()
    }

    private fun setAnimation(tr: FragmentTransaction, language: String) {
        if (language == Authon.LANGUAGE_EN) {
            tr.setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
        } else {
            tr.setCustomAnimations(
                R.anim.slide_in_left,
                R.anim.slide_out_right,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )

        }
    }

    fun showToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun getErrorCode(errorBody: String): Int {
        val json = JsonParser.parseString(errorBody)
        return json.asJsonObject.get("code").asInt
    }

    fun getErrorData(errorBody: String): String {
        val json = JsonParser.parseString(errorBody)
        return json.asJsonObject.get("data").asString
    }
}