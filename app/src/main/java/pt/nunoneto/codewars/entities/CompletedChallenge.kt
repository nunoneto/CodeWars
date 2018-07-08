package pt.nunoneto.codewars.entities

import pt.nunoneto.codewars.network.response.CompletedChallengesResponse
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class CompletedChallenge (id:String,
                           name: String,
                           val completedAt: Date?,
                           languages: List<Language>) : Challenge(id, name, languages) {

    companion object {

        private val dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.getDefault())

        fun fromResponse(challengeResponse: CompletedChallengesResponse.CompletedChallengeItem,
                         languages: List<Language>) : CompletedChallenge {
            val completedDate: Date? = try {
                dateFormat.parse(challengeResponse.completedAt)
            } catch (e: ParseException) {
                null
            }

            return CompletedChallenge(challengeResponse.id, challengeResponse.name ?: "", completedDate, languages)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is CompletedChallenge || other == null) {
            return false
        }

        return name == other.name &&
                completedAt == other.completedAt &&
                languages == other.languages
    }
}