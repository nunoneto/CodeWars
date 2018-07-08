package pt.nunoneto.codewars.ui.users

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.content.Intent
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import pt.nunoneto.codewars.entities.User
import pt.nunoneto.codewars.repository.UserRepository
import pt.nunoneto.codewars.ui.challenges.ChallengesActivity
import pt.nunoneto.codewars.utils.IntentValues
import pt.nunoneto.codewars.utils.SingleLiveEvent

class UsersViewModel : ViewModel() {

    var mutableSearchedUser: MutableLiveData<User> = MutableLiveData()

    var searching: SingleLiveEvent<Boolean> = SingleLiveEvent()
    var error: SingleLiveEvent<Boolean> = SingleLiveEvent()

    fun searchUser(query:String, context: Context?) {
        mutableSearchedUser.value = null
        searching.value = true

        val observer = object: SingleObserver<User> {
            override fun onSuccess(user: User) {
                mutableSearchedUser.value = user
                searching.value = false
            }

            override fun onSubscribe(d: Disposable) {
                // do nothing
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                error.value = true
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

        val intent = Intent(context, ChallengesActivity::class.java)
        intent.putExtra(IntentValues.EXTRA_USER_USERNAME, username)
        intent.putExtra(IntentValues.EXTRA_USER_NAME, name)
        context.startActivity(intent)
    }
}