package pt.nunoneto.codewars.ui.challenges.list.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

class ChallengeViewModelFactory(val username:String) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ChallengeListViewModel(username) as T
    }

}