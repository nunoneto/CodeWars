package pt.nunoneto.codewars.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import pt.nunoneto.codewars.entities.User

@Dao
interface UserDao {

    @Query("SELECT * from recentUserData order by dateOfSearch desc limit 5")
    fun getLastFiveEntries() : LiveData<List<User>>

    @Insert(onConflict = REPLACE)
    fun inssertUser(user:User)

}