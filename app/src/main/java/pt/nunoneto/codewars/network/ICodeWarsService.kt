package pt.nunoneto.codewars.network

import io.reactivex.Observable
import pt.nunoneto.codewars.network.response.AuthoredChallengesResponse
import pt.nunoneto.codewars.network.response.ChallengeDetailsResponse
import pt.nunoneto.codewars.network.response.CompletedChallengesResponse
import pt.nunoneto.codewars.network.response.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ICodeWarsService {

    @GET("users/{username}")
    fun searchForUser(@Path("username") username: String) : Call<UserResponse>

    @GET("users/{username}/code-challenges/completed")
    fun getCompletedChallengesByUser(@Path("username") username: String,
                                     @Query("page") page: Int) : Observable<CompletedChallengesResponse>

    @GET("users/{username}/code-challenges/authored")
    fun getAuthoredChallengesByUser(@Path("username") username: String) : Observable<AuthoredChallengesResponse>

    @GET("code-challenges/{challengeId}")
    fun getChallenge(@Path("challengeId") challengeId: String) : Observable<ChallengeDetailsResponse>
}