package ir.apptick.authsdk

import android.app.Application
import ir.apptick.authon.Authon

class Base : Application() {
    override fun onCreate() {
        super.onCreate()
        Authon.init(
            applicationContext,
            "5f526e8eda97121ee0453362",
            Authon.STRATEGY_USERNAME_PASSWORD,
            Authon.LANGUAGE_FA
        )
    }
}