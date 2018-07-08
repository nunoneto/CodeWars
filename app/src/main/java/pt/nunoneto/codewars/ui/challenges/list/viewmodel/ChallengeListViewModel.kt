package pt.nunoneto.codewars.ui.challenges.list.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import pt.nunoneto.codewars.entities.AuthoredChallenge
import pt.nunoneto.codewars.entities.Challenge
import pt.nunoneto.codewars.entities.CompletedChallengePage
import pt.nunoneto.codewars.repository.ChallengesRepository
import pt.nunoneto.codewars.ui.challenges.list.ChallengesListFragment

class ChallengeListViewModel (val username: String, val challengeType: Int) : ViewModel() {

    companion object {
        const val TAG: String= "ChallengeListViewModel"
    }

    val challenges: MutableLiveData<List<Challenge>> = MutableLiveData()
    val noMorePages: MutableLiveData<Boolean> = MutableLiveData()
    val error: MutableLiveData<Boolean> = MutableLiveData()

    var nextPage: Int = 0
    var maxPages: Int = 0

    init {
        doLoadPage()
    }

    private fun doLoadPage() {
        error.value = false
        if (challengeType == ChallengesListFragment.CHALLENGE_TYPE_COMPLETED) {
            ChallengesRepository.getCompletedChallengesByUser(username, nextPage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object: SingleObserver<CompletedChallengePage> {
                        override fun onSubscribe(d: Disposable) {
                            // do nothing
                        }

                        override fun onError(e: Throwable) {
                            e.printStackTrace()
                        }

                        override fun onSuccess(challengePage: CompletedChallengePage) {
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
                    })
        } else {
            ChallengesRepository.getAuthoredChallengesByUser(username)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object: SingleObserver<List<AuthoredChallenge>> {
                        override fun onSuccess(challengesList: List<AuthoredChallenge>) {
                            challenges.value = challengesList
                        }

                        override fun onSubscribe(d: Disposable) {
                            // do nothing
                        }

                        override fun onError(e: Throwable) {
                            e.printStackTrace()
                            error.value = true
                        }
                    })
        }
    }

    fun loadNextPage() {
        Log.d(TAG, "loadNextPage page=$nextPage")
        if (challengeType == ChallengesListFragment.CHALLENGE_TYPE_AUTHORED) {
            return
        }

        if (nextPage >= maxPages) {
            noMorePages.value = true
            return
        }

        doLoadPage()
    }
}