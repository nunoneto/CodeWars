package pt.nunoneto.codewars.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.text.TextUtils
import pt.nunoneto.codewars.network.response.UserResponse

@Entity(tableName = "recentUserData")
data class User(@PrimaryKey var username: String,
                @ColumnInfo var name: String,
                @ColumnInfo var leaderboardPosition: Int,
                @ColumnInfo var bestLanguage: String,
                @ColumnInfo var bestLanguageRank: Int,
                @ColumnInfo var dateOfSearch: Long) {

    companion object {

        fun fromUserResponse(userResponse: UserResponse, bestLanguage: String, bestLanguageRank: Int, dateOfSearch: Long) : User {
            return User(userResponse.username,
                    userResponse.name,
                    userResponse.leaderboardPosition,
                    bestLanguage,
                    bestLanguageRank, dateOfSearch)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is User) {
            return false
        }

        return TextUtils.equals(username, other.username)
                && TextUtils.equals(name, other.name)
                && leaderboardPosition == other.leaderboardPosition
                && TextUtils.equals(bestLanguage, other.bestLanguage)
                && bestLanguageRank == other.bestLanguageRank
                && dateOfSearch == other.dateOfSearch
    }
}