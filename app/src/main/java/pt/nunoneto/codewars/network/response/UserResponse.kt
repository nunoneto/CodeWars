package pt.nunoneto.codewars.network.response

data class UserResponse (var username: String,
                         var name: String,
                         var leaderboardPosition: Int,
                         var ranks: Ranks) {

    data class Ranks(var languages: Map<String, Rank>)


    data class Rank(var name: String,
                    var score: Int)
}


