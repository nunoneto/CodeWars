package pt.nunoneto.codewars.network.response

data class ChallengeResponse (val id: String,
                              val slug: String,
                              val name: String,
                              val category: String,
                              val publishedAt: String?,
                              val approvedAt: String?,
                              val createdAt: String?,
                              val languages: List<String>,
                              val url: String,
                              val createdBy: UserReference?,
                              val approvedBy: UserReference?,
                              val description: String,
                              val totalAttempts: Int,
                              val totalCompleted: Int,
                              val totalStars: Int,
                              val voteScore: Int,
                              val tags: List<String>) {

    data class UserReference (val username: String, val url: String)
}