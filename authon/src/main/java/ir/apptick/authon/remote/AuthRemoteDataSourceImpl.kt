import com.google.gson.JsonElement
import ir.apptick.authon.datasource.AuthRemoteDataSource
import ir.apptick.authon.remote.AuthApi
import ir.apptick.authon.remote.AuthRes
import ir.apptick.authon.remote.OnServerResponse

class AuthRemoteDataSourceImpl(private val api: AuthApi) : AuthRemoteDataSource {

    override fun loginWithUsernamePassword(
        username: String,
        password: String,
        onServerResponse: OnServerResponse<AuthRes, String>
    ) {
        api.loginWithUsernamePassword(username, password, onServerResponse)
    }

    override fun registerWithUsernamePassword(
        username: String,
        password: String,
        onServerResponse: OnServerResponse<AuthRes, String>
    ) {
        api.registerWithUsernamePassword(username, password, onServerResponse)
    }

    override fun loginWithEmailPassword(
        email: String,
        password: String,
        onServerResponse: OnServerResponse<AuthRes, String>
    ) {
        api.loginWithEmailPassword(email, password, onServerResponse)
    }

    override fun registerWithEmailPassword(
        email: String,
        password: String,
        onServerResponse: OnServerResponse<AuthRes, String>
    ) {
        api.registerWithEmailPassword(email, password, onServerResponse)
    }

    override fun getOTP(
        phoneNumber: String,
        onServerResponse: OnServerResponse<JsonElement, String>
    ) {
        api.getOTP(phoneNumber, onServerResponse)
    }

    override fun checkOTP(
        forRegister: Boolean,
        phoneNumber: String,
        code: String,
        onServerResponse: OnServerResponse<AuthRes, String>
    ) {
        api.checkOTP(forRegister, phoneNumber, code, onServerResponse)
    }

    override fun getAccessToken(onSuccess: (String) -> Unit, onFailed: (() -> Unit)?) {
        api.getAccessToken(object : OnServerResponse<String, String> {
            override fun success(response: String) {
                onSuccess(response)
            }

            override fun failed(response: String?) {
                onFailed?.invoke()
            }
        })
    }
}