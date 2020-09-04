package ir.apptick.authon

import AuthRemoteDataSourceImpl
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import ir.apptick.authon.local.AuthLocalDataSourceImpl
import ir.apptick.authon.local.SharedPref
import ir.apptick.authon.remote.AuthApi
import ir.apptick.authon.repository.AuthRepository
import ir.apptick.authon.view.LoginFragment


object Authon {
    private var language: String = "en"
    const val STRATEGY_EMAIL_PASSWORD = "strategy_email_password"
    const val STRATEGY_USERNAME_PASSWORD = "strategy_username_password"
    const val STRATEGY_PHONE_CODE = "strategy_phone_code"
    const val LANGUAGE_FA = "fa"
    const val LANGUAGE_EN = "en"

    var appId: String? = null
    lateinit var defaultLocale: String
    lateinit var context: Context
    private lateinit var sharedPref: SharedPref
    lateinit var repository: AuthRepository
    private var strategy: String = STRATEGY_EMAIL_PASSWORD
    fun init(applicationContext: Context, appId: String, strategy: String, language: String) {
        this.appId = appId
        this.context = applicationContext
        this.strategy = strategy
        this.language = language
        sharedPref = SharedPref(applicationContext)
        repository = AuthRepository(
            AuthLocalDataSourceImpl(sharedPref),
            AuthRemoteDataSourceImpl(AuthApi(sharedPref))
        )
    }

    @Suppress("DEPRECATION")
    fun start(
        activity: AppCompatActivity,
        container: Int,
        logoId: Int? = null,
        callback: AuthonCallback
    ) {

        with(activity) {
            this@Authon.context = this
            sharedPref.context = this
            defaultLocale = resources.configuration.locale.language
            val manager = supportFragmentManager
            val tr = manager.beginTransaction()
            tr.add(
                container, LoginFragment(repository, strategy, language, logoId, callback)
            )
            tr.commit()
        }
    }

    fun isUserLoggedIn() = repository.isUserLoggedIn()

    fun logout(onLogout: () -> Unit) = repository.logout(onLogout)

    fun getAccessToken(onSuccess: (String) -> Unit, onFailed: (() -> Unit)? = null) =
        repository.getAccessToken(onSuccess, onFailed)


    var emailValidator: (String) -> ValidateResult = {
        val regex = Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
        val result = it.matches(regex)
        ValidateResult(result, context.getString(R.string.invalid_email))
    }
    var usernameValidator: (String) -> ValidateResult = {
        if (it.length > 3)
            ValidateResult(true, null)
        else
            ValidateResult(false, context.getString(R.string.invalid_username))
    }
    var passwordValidator: (String) -> ValidateResult = {
        if (it.length >= 6)
            ValidateResult(true, null)
        else
            ValidateResult(false, context.getString(R.string.invalid_password))
    }

    fun setValidationForPassword(validator: (String) -> ValidateResult) {
        passwordValidator = validator
    }

    fun setValidationForUsername(validator: (String) -> ValidateResult) {
        usernameValidator = validator
    }

    fun setValidationForEmail(validator: (String) -> ValidateResult) {
        emailValidator = validator
    }

}

interface AuthonCallback {
    fun success()
    fun failed()
}

data class ValidateResult(
    val result: Boolean,
    val msg: String?
)