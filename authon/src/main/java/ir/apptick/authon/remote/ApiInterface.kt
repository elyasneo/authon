package ir.apptick.authenticationlib.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiInterface {
    companion object {

        const val BASE_URL =
            "http://192.168.1.3:3000"

//        const val BASE_URL =
//            "https://api.janouni.ir"

        private const val BASE_API_URL =
            "${BASE_URL}/v1/"

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

    @GET("test")
    fun getTest(): Call<AuthRes>
}

interface OnServerResponse<responseType, failedType> {
    fun success(response: responseType)
    fun failed(response: failedType?=null)
}