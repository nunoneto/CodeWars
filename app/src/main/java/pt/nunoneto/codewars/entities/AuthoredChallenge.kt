package pt.nunoneto.codewars.entities

import pt.nunoneto.codewars.network.response.AuthoredChallengesResponse

class AuthoredChallenge (id: String,
                          name: String,
                          val description:String,
                          languages: List<Language>) : Challenge(id, name, languages) {

    companion object {

        fun fromResponse(response: AuthoredChallengesResponse.AuthoredChallengesResponseItem, languages: List<Language>) : AuthoredChallenge{
            return AuthoredChallenge(response.id,
                                    response.name,
                                    response.description,
                                    languages)
        }
    }

}