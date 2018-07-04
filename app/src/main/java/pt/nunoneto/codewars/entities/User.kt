package pt.nunoneto.codewars.entities

import pt.nunoneto.codewars.network.response.UserResponse

data class User(var username: String,
                var name: String,
                var overallRank: Int,
                var bestLanguage: String,
                var bestLanguageRank: Int) {

    companion object {

        fun fromUserResponse(userResponse: UserResponse, bestLanguage: String, bestLanguageRank: Int) : User {
            return User(userResponse.username,
                    userResponse.name,
                    Math.abs(userResponse.ranks.overall.rank),
                    bestLanguage,
                    bestLanguageRank)
        }
    }
}