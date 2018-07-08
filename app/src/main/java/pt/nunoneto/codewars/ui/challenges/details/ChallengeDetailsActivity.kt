package pt.nunoneto.codewars.ui.challenges.details

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class ChallengeDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(android.R.id.content, ChallengeDetailsFragment.newInstance())
                    .commitNow()
        }
    }
}