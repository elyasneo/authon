package ir.apptick.authon.datasource

interface AuthLocalDataSource {
    fun isUserLoggedIn(): Boolean
    fun logout()
}