package pt.nunoneto.codewars.ui.challenges.list.viewmodel

import android.support.annotation.IntDef

class Challenge {

    @IntDef(CHALLENGE_TYPE_AUTHORED, CHALLENGE_TYPE_COMPLETED)
    annotation class ChallengeType

    companion object {
        const val CHALLENGE_TYPE_COMPLETED = 0
        const val CHALLENGE_TYPE_AUTHORED = 1
    }
}