package pt.nunoneto.codewars.ui.users

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.content.Intent
import android.util.Log
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import pt.nunoneto.codewars.entities.User
import pt.nunoneto.codewars.repository.UserRepository
import pt.nunoneto.codewars.ui.challenges.ChallengesActivity
import pt.nunoneto.codewars.utils.IntentValues

class UsersViewModel : ViewModel() {

    companion object {
        const val TAG = "UsersViewModel"
    }

    var mutableSearchedUser: MutableLiveData<User> = MutableLiveData()

    var searching: MutableLiveData<Boolean> = MutableLiveData()
    var error: MutableLiveData<Boolean> = MutableLiveData()

    fun searchUser(query:String, context: Context?) {
        mutableSearchedUser.value = null
        searching.value = true

        val observer = object: Observer<User> {
            override fun onSubscribe(d: Disposable) {
                // do nothing
            }

            override fun onNext(user: User) {
                mutableSearchedUser.value = user
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                error.value = true
                searching.value = false
            }

            override fun onComplete() {
                searching.value = false
            }
        }

        UserRepository.searchUser(query, context, observer)
    }

    fun getRecentUserSearches(context: Context) : LiveData<List<User>>? {
        return UserRepository.getRecentUserSearches(context)
    }

    fun onRecentListPositionSelected(username: String, name:String, context: Context?) {
        if (context == null) {
            return
        }

        var intent = Intent(context, ChallengesActivity::class.java)
        intent.putExtra(IntentValues.EXTRA_USER_USERNAME, username)
        intent.putExtra(IntentValues.EXTRA_USER_NAME, name)
        context.startActivity(intent)
    }
}