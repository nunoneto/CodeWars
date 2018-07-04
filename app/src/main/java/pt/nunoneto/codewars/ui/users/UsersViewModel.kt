package pt.nunoneto.codewars.ui.users

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import pt.nunoneto.codewars.entities.User
import pt.nunoneto.codewars.repository.UserRepository

class UsersViewModel : ViewModel() {

    var mutableSearchedUser: MutableLiveData<User> = MutableLiveData()

    var searching: MutableLiveData<Boolean> = MutableLiveData()
    var error: MutableLiveData<Boolean> = MutableLiveData()

    fun searchUser(query:String) {
        mutableSearchedUser.value = null
        searching.value = true

        UserRepository.searchUser(query)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object: Observer<User> {
                    override fun onComplete() {
                        searching.value = false
                    }

                    override fun onSubscribe(d: Disposable?) {
                        // do nothing
                    }

                    override fun onNext(user: User?) {
                        mutableSearchedUser.value = user
                    }

                    override fun onError(e: Throwable?) {
                        error.value = true
                        searching.value = false
                    }
                })
    }
}