package pt.nunoneto.codewars.network

import io.reactivex.Observable
import pt.nunoneto.codewars.network.response.CompletedChallengesResponse
import pt.nunoneto.codewars.network.response.UserResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ICodeWarsService {

    @GET("users/{username}")
    fun searchUser(@Path("username") username: String) : Observable<UserResponse>

    @GET("users/{username}/code-challenges/completed")
    fun getCompletedChallengesByUser(@Path("username") username: String,
                                     @Query("page") page: Int) : Observable<CompletedChallengesResponse>
}