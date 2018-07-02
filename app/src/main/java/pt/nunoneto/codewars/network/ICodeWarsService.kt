package pt.nunoneto.codewars.network

import io.reactivex.Observable
import pt.nunoneto.codewars.network.entities.User
import retrofit2.http.GET
import retrofit2.http.Path

interface ICodeWarsService {

    @GET("/users/")
    fun searchUser(@Path("searchQuery") searchQuery: String) : Observable<User>
}