package ir.apptick.authon.local


import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.yakivmospan.scytale.Crypto
import com.yakivmospan.scytale.Options
import com.yakivmospan.scytale.Store
import javax.crypto.SecretKey

class SharedPref(var context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        const val TAG = "SharedPref"
        const val SHARED_PREF_NAME = "authon_perf"
        private const val ACCESS_TOKEN_KEY = "access_token_key"
        private const val REFRESH_TOKEN_KEY = "refresh_token_key"
    }

    @SuppressLint("ApplySharedPref")
    private fun saveToken(key: String, token: String) {
        val mKey: SecretKey
        val crypto = Crypto(Options.TRANSFORMATION_SYMMETRIC)
        val store = Store(context)
        mKey = store.generateSymmetricKey(key, null)
        val encryptedData = crypto.encrypt(token, mKey)
        Log.i(
            TAG,
            "loadEncryptionKey:Random Password Generated and Encrypted using Generated KeyStore Key"
        )

        sharedPreferences.edit().putString(key, encryptedData).commit()
    }

    private fun getToken(key: String): String {
        return try {
            val store = Store(context)
            val crypto = Crypto(Options.TRANSFORMATION_SYMMETRIC)
            val encryptedData = sharedPreferences.getString(key, "")
            val mKey = store.getSymmetricKey(key, null)
            val decryptedData = crypto.decrypt(encryptedData!!, mKey)
            Log.i(TAG, "loadEncryptionKey: Token Decrypted")
            if (decryptedData == "") "" else "Bearer $decryptedData"

        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }

    }

    fun saveAccessToken(token: String) {
        saveToken(ACCESS_TOKEN_KEY, token)
    }

    fun saveRefreshToken(token: String) {
        saveToken(REFRESH_TOKEN_KEY, token)
    }

    fun getAccessToken(): String {
        return getToken(ACCESS_TOKEN_KEY)
    }

    fun getRefreshToken(): String {
        return getToken(REFRESH_TOKEN_KEY)
    }


    fun hasToken(): Boolean {
        return getToken(ACCESS_TOKEN_KEY).isNotEmpty() && getToken(REFRESH_TOKEN_KEY).isNotEmpty()
    }
}

