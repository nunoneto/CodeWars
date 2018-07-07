package pt.nunoneto.codewars.ui.challenges.list.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import pt.nunoneto.codewars.entities.Challenge
import pt.nunoneto.codewars.entities.ChallengePage
import pt.nunoneto.codewars.repository.ChallengesRepository

class ChallengeListViewModel (val username: String) : ViewModel(), SingleObserver<ChallengePage> {

    companion object {
        const val TAG: String= "ChallengeListViewModel"
    }

    val challenges: MutableLiveData<List<Challenge>> = MutableLiveData()
    val noMorePages: MutableLiveData<Boolean> = MutableLiveData()

    var nextPage: Int = 0
    var maxPages: Int = 0

    init {
        doLoadPage()
    }

    private fun doLoadPage() {
        ChallengesRepository.getChallengesByUser(username, nextPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this)
    }

    fun loadNextPage() {
        Log.d(TAG, "loadNextPage page=$nextPage")
        if (nextPage >= maxPages) {
            noMorePages.value = true
            return
        }

        doLoadPage()
    }

    override fun onSuccess(challengePage: ChallengePage) {
        Log.d(TAG, "onSuccess $nextPage")

        if (maxPages == 0) {
            maxPages = challengePage.maxPages
        }

        if (challenges.value != null) {
            challenges.value = ArrayList(challenges.value!!.union(challengePage.challenges))
        } else {
            challenges.value = challengePage.challenges
        }

        if (challengePage.challenges.isNotEmpty()) {
            nextPage = nextPage.inc()
        }
    }

    override fun onSubscribe(disposable: Disposable) {
        // do nothing
    }

    override fun onError(error: Throwable) {
        error.printStackTrace()
    }
}