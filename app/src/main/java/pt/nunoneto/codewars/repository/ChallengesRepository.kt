package pt.nunoneto.codewars.repository

import io.reactivex.Observable
import io.reactivex.Single
import pt.nunoneto.codewars.utils.LanguageColorHelper
import pt.nunoneto.codewars.entities.Challenge
import pt.nunoneto.codewars.entities.ChallengePage
import pt.nunoneto.codewars.entities.Language
import pt.nunoneto.codewars.network.NetworkHelper

object ChallengesRepository {

    fun getChallengesByUser(username: String, page: Int) : Single<ChallengePage> {
        return NetworkHelper.serviceInstance.getCompletedChallengesByUser(username, page)
                .map { response ->

                    // transform challenges
                    var challenges = Observable.just(response.data).flatMapIterable { item -> item }
                            .map{ responseItem ->
                                var languages = getLanguages(responseItem.completedLanguages)
                                Challenge.fromResponse(responseItem, languages)}
                            .toList().blockingGet()

                    ChallengePage(challenges, response.totalPages)
                }
                .singleOrError()
    }

    private fun getLanguages(languageList: List<String>) : List<Language> {
        return languageList.asIterable()
                .map { languageString ->
                    Language(languageString, LanguageColorHelper.getColorForLanguage(languageString))}
    }
}