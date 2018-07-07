package pt.nunoneto.codewars.entities

import pt.nunoneto.codewars.network.response.CompletedChallengesResponse
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

data class Challenge (val name: String,
                      val completedAt: Date?,
                      val languages: List<Language>){

    companion object {

        private val dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss")

        fun fromResponse(challengeResponse: CompletedChallengesResponse.CompletedChallengeItem,
                         languages: List<Language>) : Challenge {
            val completedDate: Date? = try {
                dateFormat.parse(challengeResponse.completedAt)
            } catch (e: ParseException) {
                null
            }

            return Challenge(challengeResponse.name ?: "", completedDate, languages)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Challenge || other == null) {
            return false
        }

        return name == other.name &&
                completedAt == other.completedAt &&
                languages == other.languages
    }
}