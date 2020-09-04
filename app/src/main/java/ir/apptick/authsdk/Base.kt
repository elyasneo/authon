package ir.apptick.authsdk

import android.app.Application
import ir.apptick.authon.Authon

class Base : Application() {
    override fun onCreate() {
        super.onCreate()
        Authon.init(
            applicationContext,
            "5f5253813f9d684c2093f9a0",
            Authon.STRATEGY_PHONE_CODE,
            Authon.LANGUAGE_FA
        )
    }
}