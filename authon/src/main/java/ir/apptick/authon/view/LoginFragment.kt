package ir.apptick.authon.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.gson.JsonElement
import ir.apptick.authon.Authon
import ir.apptick.authon.AuthonCallback
import ir.apptick.authon.R
import ir.apptick.authon.remote.AuthRes
import ir.apptick.authon.remote.OnServerResponse
import ir.apptick.authon.repository.AuthRepository
import ir.apptick.authon.util.Util


class LoginFragment(
    val repository: AuthRepository,
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
        val layoutId = when (strategy) {
            Authon.STRATEGY_PHONE_CODE -> R.layout.fragment_login_phone_code
            Authon.STRATEGY_USERNAME_PASSWORD -> R.layout.fragment_login_username_password
            else -> R.layout.fragment_login_email_password
        }
        return inflater.inflate(layoutId, container, false)
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
        val btnLogin = view!!.findViewById<Button>(R.id.login)
        val btnRegister = view!!.findViewById<TextView>(R.id.btn_register)
        val tvUsername = view!!.findViewById<EditText>(R.id.tv_username)
        val tvPassword = view!!.findViewById<EditText>(R.id.tv_password)
        btnRegister.setOnClickListener {
            goToRegisterFragment()
        }
        btnLogin.setOnClickListener {
            val usernameValidateResult = Authon.usernameValidator(tvUsername.text.toString())
            val passwordValidateResult = Authon.passwordValidator(tvPassword.text.toString())
            if (usernameValidateResult.result && passwordValidateResult.result) {
                repository.loginWithUsernamePassword(
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

    private fun goToRegisterFragment() {
        Util.changeFragment(
            containerId!!,
            this,
            RegisterFragment(repository, strategy, language, logo, callback),
            language,
            true
        )
    }

    private fun initEmailPasswordStrategy() {
        val btnLogin = view!!.findViewById<Button>(R.id.login)
        val btnRegister = view!!.findViewById<TextView>(R.id.btn_register)
        val tvEmail = view!!.findViewById<EditText>(R.id.tv_email)
        val tvPassword = view!!.findViewById<EditText>(R.id.tv_password)
        btnRegister.setOnClickListener {
            goToRegisterFragment()
        }
        btnLogin.setOnClickListener {
            val emailValidateResult = Authon.emailValidator(tvEmail.text.toString())
            val passwordValidateResult = Authon.passwordValidator(tvPassword.text.toString())
            if (emailValidateResult.result && passwordValidateResult.result) {
                repository.loginWithEmailPassword(
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

    private fun initPhoneCodeStrategy() {
        val btnLogin = view!!.findViewById<Button>(R.id.btn_get_code)
        val btnRegister = view!!.findViewById<TextView>(R.id.btn_register)
        val tvPhone = view!!.findViewById<EditText>(R.id.phone_number)
        btnRegister.setOnClickListener {
            if (btnLogin.text.toString() == getString(R.string.action_sign_in)) {
                btnLogin.setText(R.string.action_register)
                btnRegister.setText(R.string.action_sign_in_underline)
            } else {
                btnLogin.setText(R.string.action_sign_in)
                btnRegister.setText(R.string.action_register_underline)
            }
        }
        btnLogin.setOnClickListener {
            val forRegister = btnLogin.text.toString() == getString(R.string.action_register)
            repository.getOTP(
                tvPhone.text.toString(),
                object : OnServerResponse<JsonElement, String> {
                    override fun success(response: JsonElement) {
                        Util.changeFragment(
                            containerId!!,
                            this@LoginFragment,
                            VerificationFragment(
                                forRegister,
                                repository,
                                tvPhone.text.toString(),
                                language,
                                logo,
                                callback
                            ),
                            language,
                            true
                        )
                    }

                    override fun failed(response: String?) {
                        Util.showToast(requireContext(), response ?: "error")
                    }
                })

        }
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