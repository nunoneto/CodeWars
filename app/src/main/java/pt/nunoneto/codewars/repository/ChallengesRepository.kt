package pt.nunoneto.codewars.repository

import io.reactivex.Observable
import io.reactivex.Single
import pt.nunoneto.codewars.entities.AuthoredChallenge
import pt.nunoneto.codewars.utils.LanguageColorHelper
import pt.nunoneto.codewars.entities.CompletedChallenge
import pt.nunoneto.codewars.entities.CompletedChallengePage
import pt.nunoneto.codewars.entities.Language
import pt.nunoneto.codewars.network.NetworkHelper

object ChallengesRepository {

    fun getCompletedChallengesByUser(username: String, page: Int) : Single<CompletedChallengePage> {
        return NetworkHelper.serviceInstance.getCompletedChallengesByUser(username, page)
                .map { response ->

                    // transform challenges
                    var challenges = Observable.just(response.data).flatMapIterable { item -> item }
                            .map{ responseItem ->
                                var languages = getLanguages(responseItem.completedLanguages)
                                CompletedChallenge.fromResponse(responseItem, languages)}
                            .toList().blockingGet()

                    CompletedChallengePage(challenges, response.totalPages)
                }
                .singleOrError()
    }

    fun getAuthoredChallengesByUser(username: String) : Single<List<AuthoredChallenge>> {
        return NetworkHelper.serviceInstance.getAuthoredChallengesByUser(username)
                .flatMapIterable { response -> response.data }
                .map { item -> AuthoredChallenge.fromResponse(item, getLanguages(item.languages))}
                .toList()
                .toObservable()
                .singleOrError()
    }

    private fun getLanguages(languageList: List<String>) : List<Language> {
        return languageList.asIterable()
                .map { languageString ->
                    Language(languageString, LanguageColorHelper.getColorForLanguage(languageString))}
    }
}