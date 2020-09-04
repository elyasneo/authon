package ir.apptick.authon.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import ir.apptick.authon.Authon
import ir.apptick.authon.AuthonCallback
import ir.apptick.authon.R
import ir.apptick.authon.remote.AuthRes
import ir.apptick.authon.remote.OnServerResponse
import ir.apptick.authon.repository.AuthRepository
import ir.apptick.authon.util.Util
import okio.Utf8

class RegisterFragment(
    private val repository: AuthRepository,
    private val strategy: String,
    private val language: String,
    private val logo: Int? = null,
    private val callback: AuthonCallback,
) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Util.changeLanguage(requireActivity(), language)
        val layoutId = when (strategy) {
            Authon.STRATEGY_USERNAME_PASSWORD -> R.layout.fragment_register_username_password
            Authon.STRATEGY_EMAIL_PASSWORD -> R.layout.fragment_register_email_password
            else -> throw Exception("Illegal strategy")
        }
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (logo != null)
            view.findViewById<ImageView>(R.id.iv_logo).setImageResource(logo)
        view.findViewById<ImageView>(R.id.btn_back).setOnClickListener {
            requireActivity().onBackPressed()
        }
        when (strategy) {
            Authon.STRATEGY_USERNAME_PASSWORD -> initUsernamePasswordStrategy()
            Authon.STRATEGY_EMAIL_PASSWORD -> initEmailPasswordStrategy()
            else -> throw Exception("Illegal strategy")
        }
    }

    private fun initEmailPasswordStrategy() {
        val btnLogin = view!!.findViewById<Button>(R.id.register)
        val tvEmail = view!!.findViewById<EditText>(R.id.tv_email)
        val tvPassword = view!!.findViewById<EditText>(R.id.tv_password)
        btnLogin.setOnClickListener {
            val emailValidateResult = Authon.emailValidator(tvEmail.text.toString())
            val passwordValidateResult = Authon.passwordValidator(tvPassword.text.toString())
            if (emailValidateResult.result && passwordValidateResult.result) {
                repository.registerWithEmailPassword(
                    tvEmail.text.toString(),
                    tvPassword.text.toString(),
                    object : OnServerResponse<AuthRes, String> {
                        override fun success(response: AuthRes) {
                            callback.success()
                        }

                        override fun failed(response: String?) {
                            Util.showToast(requireContext(), response ?: "")
                            callback.failed()
                        }
                    }
                )
            } else {
                tvEmail.error = if (!emailValidateResult.result) emailValidateResult.msg else null
                tvPassword.error =
                    if (!passwordValidateResult.result) passwordValidateResult.msg else null
            }

        }

    }

    private fun initUsernamePasswordStrategy() {
        val btnRegister = view!!.findViewById<TextView>(R.id.register)
        val tvUsername = view!!.findViewById<EditText>(R.id.tv_username)
        val tvPassword = view!!.findViewById<EditText>(R.id.tv_password)
        btnRegister.setOnClickListener {
            val usernameValidateResult = Authon.usernameValidator(tvUsername.text.toString())
            val passwordValidateResult = Authon.passwordValidator(tvPassword.text.toString())
            if (usernameValidateResult.result && passwordValidateResult.result) {
                repository.registerWithUsernamePassword(
                    tvUsername.text.toString(),
                    tvPassword.text.toString(),
                    object : OnServerResponse<AuthRes, String> {
                        override fun success(response: AuthRes) {
                            callback.success()
                        }

                        override fun failed(response: String?) {
                            Util.showToast(requireContext(), response ?: "")
                            callback.failed()
                        }
                    }
                )
            } else {
                tvUsername.error =
                    if (!usernameValidateResult.result) usernameValidateResult.msg else null
                tvPassword.error =
                    if (!passwordValidateResult.result) passwordValidateResult.msg else null
            }

        }

    }

    override fun onPause() {
        super.onPause()
        Util.changeLanguage(requireActivity(), Authon.defaultLocale)
    }


}