package ir.apptick.authon.datasource

import com.google.gson.JsonElement
import ir.apptick.authon.remote.AuthRes
import ir.apptick.authon.remote.OnServerResponse

interface AuthRemoteDataSource {
    fun loginWithUsernamePassword(
        username: String,
        password: String,
        onServerResponse: OnServerResponse<AuthRes, String>
    )

    fun registerWithUsernamePassword(
        username: String,
        password: String,
        onServerResponse: OnServerResponse<AuthRes, String>
    )

    fun loginWithEmailPassword(
        email: String,
        password: String,
        onServerResponse: OnServerResponse<AuthRes, String>
    )

    fun registerWithEmailPassword(
        email: String,
        password: String,
        onServerResponse: OnServerResponse<AuthRes, String>
    )

    fun getOTP(phoneNumber: String, onServerResponse: OnServerResponse<JsonElement, String>)

    fun checkOTP(
        forRegister: Boolean,
        phoneNumber: String,
        code: String,
        onServerResponse: OnServerResponse<AuthRes, String>
    )

    fun getAccessToken(onSuccess: (String) -> Unit, onFailed: (() -> Unit)? = null)
}