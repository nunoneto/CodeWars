package pt.nunoneto.codewars.repository

import io.reactivex.Observable
import io.reactivex.Single
import pt.nunoneto.codewars.entities.*
import pt.nunoneto.codewars.utils.ColorHelper
import pt.nunoneto.codewars.network.NetworkHelper

object ChallengesRepository {

    fun getCompletedChallengesByUser(username: String, page: Int) : Single<CompletedChallengePage> {
        return NetworkHelper.serviceInstance.getCompletedChallengesByUser(username, page)
                .map { response ->

                    // transform challenges
                    val challenges = Observable.just(response.data).flatMapIterable { item -> item }
                            .map{ responseItem ->
                                val languages = getLanguages(responseItem.completedLanguages)
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

    fun getChallengeDetails(challengeId: String) : Single<ChallengeDetails> {
        return NetworkHelper.serviceInstance
                .getChallenge(challengeId)
                .map { response ->
                    ChallengeDetails.fromResponse(response, getLanguages(response.languages))
                }
                .singleOrError()
    }

    private fun getLanguages(languageList: List<String>) : List<Language> {
        return languageList.asIterable()
                .map { languageString ->
                    Language(languageString, ColorHelper.getColorForLanguage(languageString))}
    }
}