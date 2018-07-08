package pt.nunoneto.codewars.repository

import android.arch.lifecycle.LiveData
import android.content.Context
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.SingleOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import pt.nunoneto.codewars.database.UserDatabase
import pt.nunoneto.codewars.entities.User
import pt.nunoneto.codewars.network.NetworkHelper
import java.util.*

object UserRepository {

    fun searchForUser(username: String, context: Context?, observer: SingleObserver<User>) {
        Single.create(SingleOnSubscribe<User> {
            emmiter ->
            val call = NetworkHelper.serviceInstance.searchForUser(username)
            val response = call.execute().body()
            if (response == null) {
                emmiter.onError(Throwable("Empty Response"))
                return@SingleOnSubscribe
            }

            var language = ""
            var languageRank = 0

            // find best language
            for ((key, value) in response.ranks.languages) {
                if (value.score > languageRank) {
                    language = key
                    languageRank = value.score
                }
            }

            val user = User.fromUserResponse(response, language, languageRank, Date())
            emmiter.onSuccess(user)

            if (context != null) {
                UserDatabase.getInstance(context)?.userDao()?.insertUser(user)
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(observer)
    }

    fun getRecentUserSearches(context: Context): LiveData<List<User>>? {
        return UserDatabase.getInstance(context)?.userDao()?.getLastFiveEntries()
    }
}