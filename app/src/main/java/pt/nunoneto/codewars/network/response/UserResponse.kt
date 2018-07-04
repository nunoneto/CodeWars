package pt.nunoneto.codewars.network.response

data class UserResponse (var username: String,
                         var name: String,
                         var ranks: Ranks) {

    data class Ranks(
            var overall: Rank,
            var languages: Map<String, Rank>)


    data class Rank(var name: String,
                    var rank: Int,
                    var score: Int)
}


