package pt.nunoneto.codewars.network.response

data class AuthoredChallengesResponse (val data: List<AuthoredChallengesResponseItem>) {

    data class AuthoredChallengesResponseItem(val id: String,
                                              val name: String,
                                              val description: String,
                                              val rank: Int?,
                                              val rankName: String?,
                                              val tags: List<String>,
                                              val languages: List<String>)
}
