package pt.nunoneto.codewars.entities

import pt.nunoneto.codewars.network.response.ChallengeResponse
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

data class ChallengeDetails(val id: String,
                            val slug: String,
                            val name: String,
                            val category: String,
                            val publishedAt: String,
                            val approvedAt: String,
                            val createdAt: String,
                            val languages: List<Language>,
                            val url: String,
                            val createdByUser: String?,
                            val createdByUrl: String?,
                            val approvedByUser: String?,
                            val approvedByUrl: String?,
                            val description: String,
                            val totalAttempts: Int,
                            val totalCompleted: Int,
                            val totalStars: Int,
                            val voteScore: Int,
                            val tags: List<String>) {
    companion object {

        private val inputDateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.getDefault())
        private val outputDateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        fun fromResponse(response: ChallengeResponse, languages: List<Language>) : ChallengeDetails{


            val publishedDate: String = parseDate(response.publishedAt)
            val approvedDate: String = parseDate(response.approvedAt)
            val createdDate: String = parseDate(response.createdAt)

            return ChallengeDetails(response.id,
                        response.slug,
                        response.name,
                        response.category,
                        publishedDate,
                        approvedDate,
                        createdDate,
                        languages,
                        response.url,
                        response.createdBy?.username ?: "",
                        response.createdBy?.url,
                        response.approvedBy?.username ?: "",
                        response.approvedBy?.url,
                        response.description,
                        response.totalAttempts,
                        response.totalCompleted,
                        response.totalStars,
                        response.voteScore,
                        response.tags
                    )
        }

        private fun parseDate(date: String?) : String {
            return try {
                val date = inputDateFormat.parse(date ?: "")
                outputDateFormat.format(date)
            } catch (e: ParseException) {
                ""
            }
        }
    }
}