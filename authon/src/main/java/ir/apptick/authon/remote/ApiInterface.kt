package ir.apptick.authon.remote

import android.telephony.PhoneNumberFormattingTextWatcher
import com.google.gson.JsonElement
import ir.apptick.authon.Authon
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiInterface {
    companion object {

        const val BASE_URL =
            "http://192.168.1.103:5000"

//        const val BASE_URL =
//            "https://api.janouni.ir"

        private const val BASE_API_URL =
            "$BASE_URL/api/"

        private var requestService: ApiInterface? = null
        operator fun invoke(): ApiInterface {
            return if (requestService != null)
                requestService!!
            else {
                val interceptor = HttpLoggingInterceptor()
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
                val client =
                    OkHttpClient.Builder()
                        .addInterceptor(interceptor)
                        .retryOnConnectionFailure(true).build()
                requestService = Retrofit.Builder()
                    .baseUrl(BASE_API_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ApiInterface::class.java)

                requestService!!
            }
        }
    }

    @POST("auth/login/{id}")
    @FormUrlEncoded
    fun loginWithUsernamePassword(
        @Field("username") username: String,
        @Field("password") password: String,
        @Path("id") id: String = Authon.appId ?: ""
    ): Call<AuthonResponse<AuthRes>>

    @POST("auth/register/{id}")
    @FormUrlEncoded
    fun registerWithUsernamePassword(
        @Field("username") username: String,
        @Field("password") password: String,
        @Path("id") id: String = Authon.appId ?: ""
    ): Call<AuthonResponse<AuthRes>>

    @POST("auth/login/{id}")
    @FormUrlEncoded
    fun loginWithEmailPassword(
        @Field("email") email: String,
        @Field("password") password: String,
        @Path("id") id: String = Authon.appId ?: ""
    ): Call<AuthonResponse<AuthRes>>

    @POST("auth/register/{id}")
    @FormUrlEncoded
    fun registerWithEmailPassword(
        @Field("email") email: String,
        @Field("password") password: String,
        @Path("id") id: String = Authon.appId ?: ""
    ): Call<AuthonResponse<AuthRes>>

    @POST("auth/get_otp")
    @FormUrlEncoded
    fun getOTP(
        @Field("phoneNumber") phoneNumber: String
    ): Call<JsonElement>

    @POST("auth/register/{id}")
    @FormUrlEncoded
    fun checkOTPRegister(
        @Field("phoneNumber") phoneNumber: String,
        @Field("code") code: String,
        @Path("id") id: String = Authon.appId ?: ""
    ): Call<AuthonResponse<AuthRes>>

    @POST("auth/login/{id}")
    @FormUrlEncoded
    fun checkOTPLogin(
        @Field("phoneNumber") phoneNumber: String,
        @Field("code") code: String,
        @Path("id") id: String = Authon.appId ?: ""
    ): Call<AuthonResponse<AuthRes>>

    @GET("auth/accesstoken")
    fun getAccessToken(@Header("Authorization") refreshToken: String): Call<AuthonResponse<JsonElement>>
}

interface OnServerResponse<responseType, failedType> {
    fun success(response: responseType)
    fun failed(response: failedType? = null)
}