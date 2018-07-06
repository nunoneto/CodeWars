package pt.nunoneto.codewars.ui.users

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class UsersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(android.R.id.content, UsersFragment.newInstance())
                    .commitNow()
        }
    }
}