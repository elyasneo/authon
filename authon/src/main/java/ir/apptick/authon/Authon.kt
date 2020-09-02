package ir.apptick.authon

import androidx.appcompat.app.AppCompatActivity
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
    private var strategy: String = STRATEGY_EMAIL_PASSWORD
    fun init(appId: String, strategy: String, language: String) {
        this.appId = appId
        this.strategy = strategy
        this.language = language
    }

    @Suppress("DEPRECATION")
    fun start(
        activity: AppCompatActivity,
        container: Int,
        logoId: Int? = null,
        callback: AuthonCallback
    ) {
        with(activity) {
            defaultLocale = resources.configuration.locale.language
            val manager = supportFragmentManager
            val tr = manager.beginTransaction()
            tr.add(container, LoginFragment(strategy, language, logoId, callback))
            tr.commit()
        }
    }
}

interface AuthonCallback {
    fun success()
    fun failed()
}