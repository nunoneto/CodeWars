package pt.nunoneto.codewars.ui.challenges.details.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import pt.nunoneto.codewars.entities.ChallengeDetails
import pt.nunoneto.codewars.repository.ChallengesRepository

class ChallengeDetailsViewModel(private val challengeId: String) : ViewModel(), SingleObserver<ChallengeDetails> {

    val details: MutableLiveData<ChallengeDetails> = MutableLiveData()
    val error: MutableLiveData<Boolean> = MutableLiveData()

    init {
        loadChallengeDetails()
    }

    private fun loadChallengeDetails() {
        ChallengesRepository.getChallengeDetails(challengeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this)
    }

    override fun onSuccess(details: ChallengeDetails) {
        this.details.value = details
    }

    override fun onSubscribe(d: Disposable) {
        // do nothing
    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        error.value = true
    }

}