package pt.nunoneto.codewars.ui.users

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.util.Log
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import pt.nunoneto.codewars.entities.User
import pt.nunoneto.codewars.repository.UserRepository

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
                Log.e(TAG, "searchUser.onError e=" + e.localizedMessage)
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
}