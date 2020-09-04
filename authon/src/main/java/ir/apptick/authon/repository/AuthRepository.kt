package ir.apptick.authon.repository

import com.google.gson.JsonElement
import ir.apptick.authon.AuthonCallback
import ir.apptick.authon.datasource.AuthLocalDataSource
import ir.apptick.authon.datasource.AuthRemoteDataSource
import ir.apptick.authon.remote.AuthRes
import ir.apptick.authon.remote.OnServerResponse

class AuthRepository(
    private val local: AuthLocalDataSource,
    private val remote: AuthRemoteDataSource
) {

    fun isUserLoggedIn(): Boolean {
        return local.isUserLoggedIn()
    }

    fun logout(onLogout: () -> Unit) {
        local.logout()
        onLogout()
    }

    fun loginWithUsernamePassword(
        username: String,
        password: String,
        onServerResponse: OnServerResponse<AuthRes, String>
    ) {
        remote.loginWithUsernamePassword(username, password, onServerResponse)
    }

    fun registerWithUsernamePassword(
        username: String,
        password: String,
        onServerResponse: OnServerResponse<AuthRes, String>
    ) {
        remote.registerWithUsernamePassword(username, password, onServerResponse)
    }

    fun loginWithEmailPassword(
        email: String,
        password: String,
        onServerResponse: OnServerResponse<AuthRes, String>
    ) {
        remote.loginWithEmailPassword(email, password, onServerResponse)
    }

    fun registerWithEmailPassword(
        email: String,
        password: String,
        onServerResponse: OnServerResponse<AuthRes, String>
    ) {
        remote.registerWithEmailPassword(email, password, onServerResponse)
    }

    fun getOTP(phoneNumber: String, onServerResponse: OnServerResponse<JsonElement, String>) {
        remote.getOTP(phoneNumber, onServerResponse)
    }

    fun checkOTP(
        forRegister: Boolean,
        phoneNumber: String,
        code: String,
        onServerResponse: OnServerResponse<AuthRes, String>
    ) {
        remote.checkOTP(forRegister, phoneNumber, code, onServerResponse)
    }

    fun getAccessToken(onSuccess: (String) -> Unit, onFailed: (() -> Unit)?) {
        remote.getAccessToken(onSuccess, onFailed)
    }
}