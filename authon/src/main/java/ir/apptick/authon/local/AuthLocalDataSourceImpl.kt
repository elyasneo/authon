package ir.apptick.authon.local

import ir.apptick.authon.datasource.AuthLocalDataSource

class AuthLocalDataSourceImpl(private val sharedPref: SharedPref) : AuthLocalDataSource {
    override fun isUserLoggedIn(): Boolean {
        return sharedPref.hasToken()
    }

    override fun logout() {
        sharedPref.saveAccessToken("")
        sharedPref.saveRefreshToken("")
    }
}