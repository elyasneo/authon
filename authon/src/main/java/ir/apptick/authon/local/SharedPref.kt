package ir.apptick.authenticationlib.local


import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.yakivmospan.scytale.Crypto
import com.yakivmospan.scytale.Options
import com.yakivmospan.scytale.Store
import javax.crypto.SecretKey

class SharedPref(val context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        const val TAG = "SharedPref"
        const val SHARED_PREF_NAME = "auth_lib_perf"
        private const val TOKEN_KEY = "token_key"
    }

    @SuppressLint("ApplySharedPref")
    fun saveToken(token: String) {
        val key: SecretKey
        val crypto = Crypto(Options.TRANSFORMATION_SYMMETRIC)
        val store = Store(context)
        key = store.generateSymmetricKey(TOKEN_KEY, null)
        val encryptedData = crypto.encrypt(token, key)
        Log.i(
            TAG,
            "loadEncryptionKey:Random Password Generated and Encrypted using Generated KeyStore Key"
        )

        sharedPreferences.edit().putString("token", encryptedData).commit()
    }

    fun getToken(): String {
        return try {
            val store = Store(context)
            val crypto = Crypto(Options.TRANSFORMATION_SYMMETRIC)
            val encryptedData = sharedPreferences.getString("token", "")
            val key = store.getSymmetricKey(TOKEN_KEY, null)
            val decryptedData = crypto.decrypt(encryptedData!!, key)
            Log.i(TAG, "loadEncryptionKey: Token Decrypted")
            if (decryptedData == "") "" else "Bearer $decryptedData"

        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }

    }


    fun hasToken(): Boolean {
        return getToken().isNotEmpty()
    }
}

