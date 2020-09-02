package ir.apptick.authsdk

import android.app.Application
import ir.apptick.authon.Authon

class Base : Application() {
    override fun onCreate() {
        super.onCreate()
        Authon.init("213123", Authon.STRATEGY_PHONE_CODE, Authon.LANGUAGE_FA)
    }
}