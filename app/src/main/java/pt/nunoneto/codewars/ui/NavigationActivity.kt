package pt.nunoneto.codewars.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.navigation_activity.*
import pt.nunoneto.codewars.R
import pt.nunoneto.codewars.ui.users.UsersFragment

class NavigationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.navigation_activity)

        setUiComponents()
    }

    private fun setUiComponents() {
        bottom_navigation.setOnNavigationItemSelectedListener { menuItem ->
            setContentFragment(menuItem.itemId)
        }

        bottom_navigation.selectedItemId = R.id.action_users
    }

    private fun setContentFragment(itemId: Number): Boolean {
        var contentFragment: Fragment = when (itemId) {
            R.id.action_users -> UsersFragment.newInstance()
            R.id.action_challenges -> ChallengesFragment.newInstance()
            else -> throw IllegalArgumentException("Invalid menu id: $itemId")
        }

        supportFragmentManager.beginTransaction()
                .replace(R.id.navigation_content, contentFragment)
                .commitNow()

        return true
    }

}