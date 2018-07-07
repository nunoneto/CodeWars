package pt.nunoneto.codewars.ui.challenges.list

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.challenges_list_fragment.*
import pt.nunoneto.codewars.R
import pt.nunoneto.codewars.entities.Challenge
import pt.nunoneto.codewars.ui.challenges.list.viewmodel.ChallengeListViewModel
import pt.nunoneto.codewars.ui.challenges.list.viewmodel.ChallengeViewModelFactory
import pt.nunoneto.codewars.utils.IntentValues

class ChallengesListFragment : Fragment() {

    companion object {

        fun newInstance() : ChallengesListFragment {
            return ChallengesListFragment()
        }
    }

    private lateinit var viewModel: ChallengeListViewModel
    private lateinit var challengesAdapter: ChallengesListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.challenges_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val username = when (activity?.intent == null) {
            true -> ""
            false -> activity!!.intent.getStringExtra(IntentValues.EXTRA_USER_USERNAME)
        }

        viewModel = ViewModelProviders.of(this, ChallengeViewModelFactory(username)).get(ChallengeListViewModel::class.java)

        setObservers()
        setUiComponents()
    }

    fun loadNextPage() {
        viewModel.loadNextPage()
    }

    private fun setUiComponents() {
        val layoutManager = LinearLayoutManager(context)
        challengesAdapter = ChallengesListAdapter(this)
        rv_challenges.layoutManager = layoutManager
        rv_challenges.adapter = challengesAdapter
    }

    private fun setObservers() {
        viewModel.challenges.observe(this, Observer<List<Challenge>> { challenges ->
            updateAdapter(challenges)
        })

        viewModel.noMorePages.observe(this, Observer<Boolean> {
            noMorePages ->
                if (noMorePages == true)  noMorePages()
        })
    }

    private fun noMorePages() {
        Snackbar.make(view!!, R.string.no_more_results, Snackbar.LENGTH_SHORT).show()
        rv_challenges.post{
            challengesAdapter.adapterMode = ChallengesListAdapter.ADAPTER_MORE_NO_MORE_RESULTS
            challengesAdapter.notifyItemRemoved(challengesAdapter.itemCount)
        }
    }

    private fun updateAdapter(challengesList: List<Challenge>?) {
        if (challengesList == null) {
            return
        }

        if (challengesAdapter.challenges.isEmpty()) {
            challengesAdapter.challenges.addAll(challengesList)
            challengesAdapter.notifyDataSetChanged()

            pb_loading_challenges.visibility = View.GONE
            rv_challenges.visibility = View.VISIBLE
            return
        }

        val startRange = challengesAdapter.itemCount

        // update list
        challengesAdapter.challenges.clear()
        challengesAdapter.challenges.addAll(challengesList)
        challengesAdapter.notifyItemRangeInserted(startRange, challengesAdapter.itemCount - 1)
    }
}
