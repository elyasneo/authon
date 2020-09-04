package ir.apptick.authon.remote

import androidx.annotation.StringRes
import com.google.gson.JsonElement
import ir.apptick.authon.R
import ir.apptick.authon.local.SharedPref
import ir.apptick.authon.util.Util
import okhttp3.internal.wait
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthApi(private val sharedPref: SharedPref) {
    companion object {
        const val TAG = "ApiService"
        private const val TOKEN_KEY = "token_key"
        private var apiService: AuthApi? = null
        fun getInstance(sharedPref: SharedPref): AuthApi {
            return if (apiService != null)
                apiService!!
            else {
                apiService = AuthApi(sharedPref)
                apiService!!
            }
        }
    }

    private val request: ApiInterface = ApiInterface()

    fun getString(@StringRes id: Int): String {
        return sharedPref.context.getString(id)
    }

    fun saveTokens(authRes: AuthRes) {
        sharedPref.saveRefreshToken(authRes.refreshToken)
        sharedPref.saveAccessToken(authRes.accessToken)
    }

    /////////////////////////////////////////functions//////////////////////////////////////////

    fun registerWithUsernamePassword(
        username: String,
        password: String,
        onServerResponse: OnServerResponse<AuthRes, String>
    ) {
        request.registerWithUsernamePassword(username, password)
            .enqueue(object : Callback<AuthonResponse<AuthRes>> {
                override fun onResponse(
                    call: Call<AuthonResponse<AuthRes>>,
                    response: Response<AuthonResponse<AuthRes>>
                ) {
                    if (response.isSuccessful) {
                        saveTokens(response.body()!!.data)
                        onServerResponse.success(response.body()!!.data)
                    } else {
                        if (response.code() == 401)
                            onServerResponse.failed(getString(R.string.login_first))
                        else
                            onServerResponse.failed(getString(R.string.error))
                    }
                }

                override fun onFailure(call: Call<AuthonResponse<AuthRes>>, t: Throwable) {
                    onServerResponse.failed(getString(R.string.error))
                }
            })
    }

    fun loginWithUsernamePassword(
        username: String,
        password: String,
        onServerResponse: OnServerResponse<AuthRes, String>
    ) {
        request.loginWithUsernamePassword(username, password)
            .enqueue(object : Callback<AuthonResponse<AuthRes>> {
                override fun onResponse(
                    call: Call<AuthonResponse<AuthRes>>,
                    response: Response<AuthonResponse<AuthRes>>
                ) {
                    if (response.isSuccessful) {
                        saveTokens(response.body()!!.data)
                        onServerResponse.success(response.body()!!.data)
                    } else {
                        if (response.code() == 400)
                            onServerResponse.failed(getString(R.string.username_password_wrong))
                        else
                            onServerResponse.failed(getString(R.string.error))
                    }
                }

                override fun onFailure(call: Call<AuthonResponse<AuthRes>>, t: Throwable) {
                    onServerResponse.failed(getString(R.string.error))
                }
            })
    }

    fun registerWithEmailPassword(
        email: String,
        password: String,
        onServerResponse: OnServerResponse<AuthRes, String>
    ) {
        request.registerWithEmailPassword(email, password)
            .enqueue(object : Callback<AuthonResponse<AuthRes>> {
                override fun onResponse(
                    call: Call<AuthonResponse<AuthRes>>,
                    response: Response<AuthonResponse<AuthRes>>
                ) {
                    if (response.isSuccessful) {
                        saveTokens(response.body()!!.data)
                        onServerResponse.success(response.body()!!.data)
                    } else {
                        if (response.code() == 401)
                            onServerResponse.failed(getString(R.string.login_first))
                        else
                            onServerResponse.failed(getString(R.string.error))
                    }
                }

                override fun onFailure(call: Call<AuthonResponse<AuthRes>>, t: Throwable) {
                    onServerResponse.failed(getString(R.string.error))
                }
            })
    }

    fun loginWithEmailPassword(
        email: String,
        password: String,
        onServerResponse: OnServerResponse<AuthRes, String>
    ) {
        request.loginWithEmailPassword(email, password)
            .enqueue(object : Callback<AuthonResponse<AuthRes>> {
                override fun onResponse(
                    call: Call<AuthonResponse<AuthRes>>,
                    response: Response<AuthonResponse<AuthRes>>
                ) {
                    if (response.isSuccessful) {
                        saveTokens(response.body()!!.data)
                        onServerResponse.success(response.body()!!.data)
                    } else {
                        if (response.code() == 400)
                            onServerResponse.failed(getString(R.string.email_password_wrong))
                        else
                            onServerResponse.failed(getString(R.string.error))
                    }
                }

                override fun onFailure(call: Call<AuthonResponse<AuthRes>>, t: Throwable) {
                    onServerResponse.failed(getString(R.string.error))
                }
            })
    }

    fun getOTP(phoneNumber: String, onServerResponse: OnServerResponse<JsonElement, String>) {
        request.getOTP(phoneNumber).enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful)
                    onServerResponse.success(response.body()!!)
                else {
                    onServerResponse.failed(getString(R.string.error))
                }

            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                onServerResponse.failed(getString(R.string.error))
            }
        })
    }

    fun checkOTP(
        forRegister: Boolean,
        phoneNumber: String,
        code: String,
        onServerResponse: OnServerResponse<AuthRes, String>
    ) {
        val callback = object : Callback<AuthonResponse<AuthRes>> {
            override fun onResponse(
                call: Call<AuthonResponse<AuthRes>>,
                response: Response<AuthonResponse<AuthRes>>
            ) {
                if (response.isSuccessful) {
                    saveTokens(response.body()!!.data)
                    onServerResponse.success(response.body()!!.data)
                } else {
                    val code = Util.getErrorCode(response.errorBody()!!.string())
                    val msg = when (code) {
                        41 -> getString(R.string.register_first)
                        42 -> getString(R.string.login_first)
                        43 -> getString(R.string.code_wrong)
                        else -> getString(R.string.error)
                    }
                    onServerResponse.failed(msg)
                }

            }

            override fun onFailure(call: Call<AuthonResponse<AuthRes>>, t: Throwable) {
                onServerResponse.failed(getString(R.string.error))
            }
        }
        if (forRegister)
            request.checkOTPRegister(phoneNumber, code).enqueue(callback)
        else
            request.checkOTPLogin(phoneNumber, code).enqueue(callback)

    }

    fun getAccessToken(onServerResponse: OnServerResponse<String, String>) {
        request.getAccessToken(sharedPref.getRefreshToken())
            .enqueue(object : Callback<AuthonResponse<JsonElement>> {
                override fun onResponse(
                    call: Call<AuthonResponse<JsonElement>>,
                    response: Response<AuthonResponse<JsonElement>>
                ) {
                    if (response.isSuccessful) {
                        val token = response.body()!!.data.asJsonObject.get("accessToken").asString
                        sharedPref.saveAccessToken(token)
                        onServerResponse.success(token)
                    } else {
                        onServerResponse.failed()
                    }
                }

                override fun onFailure(call: Call<AuthonResponse<JsonElement>>, t: Throwable) {
                    onServerResponse.failed()
                }
            })

    }
}