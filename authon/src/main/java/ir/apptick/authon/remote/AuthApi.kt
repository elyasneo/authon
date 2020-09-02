package ir.apptick.authenticationlib.remote

import com.google.gson.JsonElement
import ir.apptick.authenticationlib.local.SharedPref
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class AuthApi(val sharedPref: SharedPref) {
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

    /////////////////////////////////////////functions//////////////////////////////////////////

    fun getTest(onServerResponse: OnServerResponse<AuthRes, JsonElement>) {
        request.getTest().enqueue(object : Callback<AuthRes> {
            override fun onResponse(call: Call<AuthRes>, response: retrofit2.Response<AuthRes>) {
                onServerResponse.success(response.body()!!)
            }

            override fun onFailure(call: Call<AuthRes>, t: Throwable) {
                onServerResponse.failed()
            }
        })
    }
}