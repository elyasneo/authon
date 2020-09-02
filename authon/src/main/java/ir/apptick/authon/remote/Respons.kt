package ir.apptick.authenticationlib.remote

data class AuthRes(
    val accessToke: String,
    val refreshToken: String,
)