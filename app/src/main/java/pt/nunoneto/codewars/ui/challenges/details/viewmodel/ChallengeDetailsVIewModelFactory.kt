package pt.nunoneto.codewars.ui.challenges.details.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

class ChallengeDetailsViewModelFactory(private val challengeId: String) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ChallengeDetailsViewModel(challengeId) as T
    }
}