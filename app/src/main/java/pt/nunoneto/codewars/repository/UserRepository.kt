package pt.nunoneto.codewars.repository

import android.arch.lifecycle.LiveData
import android.content.Context
import io.reactivex.Observer
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import pt.nunoneto.codewars.database.UserDatabase
import pt.nunoneto.codewars.entities.User
import pt.nunoneto.codewars.network.NetworkHelper
import java.util.*

object UserRepository {

    fun searchUser(searchQuery: String, context: Context?, uiObserver: SingleObserver<User>) {
        val observable = NetworkHelper.serviceInstance
                .searchUser(searchQuery)
                .subscribeOn(Schedulers.io())
                .map { userResponse ->
                    var language = ""
                    var languageRank = 0

                    // find best language
                    for ((key, value) in userResponse.ranks.languages) {
                        if (value.score > languageRank) {
                            language = key
                            languageRank = value.score
                        }
                    }

                    User.fromUserResponse(userResponse, language, languageRank, Date())
                }

        // Update UI
        observable
                .singleOrError()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(uiObserver)

        // Update recent users db
        if (context != null) {
            observable
                    .observeOn(Schedulers.io())
                    .subscribe(getUpdateRecentUsersObserver(context))
        }
    }

    fun getRecentUserSearches(context: Context): LiveData<List<User>>? {
        return UserDatabase.getInstance(context)?.userDao()?.getLastFiveEntries()
    }

    private fun getUpdateRecentUsersObserver(context: Context) : Observer<User> {
        return object: Observer<User> {
            override fun onComplete() {
                // do nothing
            }

            override fun onSubscribe(d: Disposable) {
                // do nothing
            }

            override fun onNext(user: User) {
                UserDatabase.getInstance(context)?.userDao()?.insertUser(user)
            }

            override fun onError(e: Throwable) {
                // do nothing
            }
        }
    }
}