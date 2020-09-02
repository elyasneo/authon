package ir.apptick.authon.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.apptick.authon.Authon
import ir.apptick.authon.AuthonCallback
import ir.apptick.authon.R
import ir.apptick.authon.util.Util
import kotlinx.android.synthetic.main.fragment_login_phone_code_verification.*


class VerificationFragment(
    private val language: String,
    private val logo: Int? = null,
    private val callback: AuthonCallback,
) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        changeLanguage(language)
        return inflater.inflate(R.layout.fragment_login_phone_code_verification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (logo != null)
            iv_logo.setImageResource(logo)


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