package pt.nunoneto.codewars.ui.challenges

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.challenges_fragment.*
import pt.nunoneto.codewars.R
import pt.nunoneto.codewars.ui.challenges.list.ChallengesListFragment
import pt.nunoneto.codewars.utils.IntentValues

class ChallengesFragment : Fragment(), ViewPager.OnPageChangeListener {

    companion object {

        fun newInstance() : ChallengesFragment {
            return ChallengesFragment()
        }
    }

    private lateinit var pagerAdapter: ChallengesPagerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.challenges_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUiComponents()
    }

    private fun setUiComponents() {
        setupToolbar()
        setupPager()
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_authored_challenges -> {
                    vp_challenges.currentItem = 1
                    true
                }

                R.id.action_completed_challenges -> {
                    vp_challenges.currentItem = 0
                    true
                }
                else -> false
            }
        }
    }

    private fun setupPager() {
        val list = listOf(
                ChallengesListFragment.newInstance(ChallengesListFragment.CHALLENGE_TYPE_COMPLETED),
                ChallengesListFragment.newInstance(ChallengesListFragment.CHALLENGE_TYPE_AUTHORED))

        pagerAdapter = ChallengesPagerAdapter(fragmentManager!!, list)
        vp_challenges.adapter = pagerAdapter
        vp_challenges.addOnPageChangeListener(this)
    }

    private fun setupToolbar() {
        val username: String? = activity?.intent?.getStringExtra(IntentValues.EXTRA_USER_USERNAME)
        val name: String? = activity?.intent?.getStringExtra(IntentValues.EXTRA_USER_NAME)

        val finalName = if (name != null && !TextUtils.isEmpty(name)) name else username

        (activity as AppCompatActivity).supportActionBar!!.title = getString(R.string.menu_challenges, finalName)
    }

    override fun onPageScrollStateChanged(position: Int) {
        // do nothing
    }

    override fun onPageScrolled(from: Int, p1: Float, to: Int) {
        // do nothing
    }

    override fun onPageSelected(position: Int) {
        bottom_navigation.selectedItemId = when (position) {
            0 -> R.id.action_completed_challenges
            1 -> R.id.action_authored_challenges
            else -> R.id.action_completed_challenges
        }
    }
}
