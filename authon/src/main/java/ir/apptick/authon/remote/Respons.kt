package ir.apptick.authon.remote

data class AuthRes(
    val accessToken: String,
    val refreshToken: String,
)

data class AuthonResponse<T>(
    val code: Int,
    val data: T
)