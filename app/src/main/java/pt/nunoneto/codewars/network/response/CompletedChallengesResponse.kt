package pt.nunoneto.codewars.network.response

data class CompletedChallengesResponse(val totalPages:Int,
                                       val totalItems: Int,
                                       val data: List<CompletedChallengeItem>) {

    data class CompletedChallengeItem (val id: String,
                                       val name: String?,
                                       val completedAt: String?,
                                       val completedLanguages: List<String>)
}