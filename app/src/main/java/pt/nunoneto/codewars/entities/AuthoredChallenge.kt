package pt.nunoneto.codewars.entities

import pt.nunoneto.codewars.network.response.AuthoredChallengesResponse

class AuthoredChallenge (id: String,
                          name: String,
                          val description:String,
                          val rank: Int,
                          val rankName: String,
                          val tags: List<String>,
                          languages: List<Language>) : Challenge(id, name, languages) {

    companion object {

        fun fromResponse(response: AuthoredChallengesResponse.AuthoredChallengesResponseItem, languages: List<Language>) : AuthoredChallenge{
            return AuthoredChallenge(response.id,
                                    response.name,
                                    response.description,
                                    response.rank,
                                    response.rankName,
                                    response.tags,
                                    languages)
        }
    }
}