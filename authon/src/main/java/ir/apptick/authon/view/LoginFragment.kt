package ir.apptick.authon.view

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import ir.apptick.authon.Authon
import ir.apptick.authon.AuthonCallback
import ir.apptick.authon.R
import ir.apptick.authon.util.Util
import java.util.*


class LoginFragment(
    private val strategy: String,
    private val language: String,
    private val logo: Int? = null,
    private val callback: AuthonCallback,
) : Fragment() {
    private var containerId: Int? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        changeLanguage(language)
        containerId = container?.id
        return when (strategy) {
            Authon.STRATEGY_PHONE_CODE -> inflater.inflate(
                R.layout.fragment_login_phone_code,
                container,
                false
            )
            Authon.STRATEGY_USERNAME_PASSWORD -> inflater.inflate(
                R.layout.fragment_login_username_password,
                container,
                false
            )

            else -> inflater.inflate(
                R.layout.fragment_login_email_password,
                container,
                false
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (logo != null)
            view.findViewById<ImageView>(R.id.iv_logo).setImageResource(logo)

        when (strategy) {
            Authon.STRATEGY_USERNAME_PASSWORD -> initUsernamePasswordStrategy()
            Authon.STRATEGY_PHONE_CODE -> initPhoneCodeStrategy()
            else -> initEmailPasswordStrategy()
        }
    }

    private fun initUsernamePasswordStrategy() {
        val btn = view!!.findViewById<Button>(R.id.login)
        btn.setOnClickListener {
            callback.success()
        }
    }

    private fun initEmailPasswordStrategy() {
        val btn = view!!.findViewById<Button>(R.id.login)
        btn.setOnClickListener {
            callback.success()
        }
        // TODO("Not yet implemented")
    }

    private fun initPhoneCodeStrategy() {
        val btn = view!!.findViewById<Button>(R.id.btn_get_code)
        btn.setOnClickListener {
            val tr = fragmentManager?.beginTransaction()
            setAnimation(tr)
            tr?.replace(containerId!!, VerificationFragment(language, logo, callback))
            tr?.addToBackStack(null)
            tr?.commit()
        }
        // TODO("Not yet implemented")
    }

    private fun setAnimation(tr: FragmentTransaction?) {
        if (language == Authon.LANGUAGE_EN)
            tr?.setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
        else
            tr?.setCustomAnimations(
                R.anim.slide_in_left,
                R.anim.slide_out_right,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )

    }

    @Suppress("DEPRECATION")
    private fun changeLanguage(mLanguage: String) {
        Util.changeLanguage(requireActivity(), mLanguage)
    }


    override fun onPause() {
        super.onPause()
        changeLanguage(Authon.defaultLocale)
    }

}