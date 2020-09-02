package ir.apptick.authenticationlib.repository

import ir.apptick.authenticationlib.datasource.AuthLocalDataSource
import ir.apptick.authenticationlib.datasource.AuthRemoteDataSource

class AuthRepository(val local: AuthLocalDataSource, val remote: AuthRemoteDataSource) {

}