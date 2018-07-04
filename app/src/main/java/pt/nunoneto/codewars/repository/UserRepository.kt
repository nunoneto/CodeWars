package pt.nunoneto.codewars.repository

import io.reactivex.Observable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import pt.nunoneto.codewars.entities.User
import pt.nunoneto.codewars.network.NetworkHelper
import pt.nunoneto.codewars.network.response.UserResponse

object UserRepository {

    fun searchUser(searchQuery: String): Observable<User> {
        return NetworkHelper.createCodeWarsService()
                .searchUser(searchQuery)
                .subscribeOn(Schedulers.io())
                .map(Function<UserResponse, User> { userResponse ->
                    if (userResponse == null) {
                        return@Function null
                    }

                    var language = ""
                    var languageRank = 0

                    for ((key, value) in userResponse.ranks.languages) {
                        if (Math.abs(value.rank) > languageRank) {
                            language = key
                            languageRank = Math.abs(value.rank)
                        }
                    }

                    User.fromUserResponse(userResponse, language, languageRank)
                })
    }
}