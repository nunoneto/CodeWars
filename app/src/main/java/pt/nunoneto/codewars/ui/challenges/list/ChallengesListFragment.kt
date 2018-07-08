package pt.nunoneto.codewars.ui.challenges.list

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.challenges_list_fragment.*
import pt.nunoneto.codewars.R
import pt.nunoneto.codewars.entities.Challenge
import pt.nunoneto.codewars.ui.challenges.list.viewmodel.Challenge.ChallengeType
import pt.nunoneto.codewars.ui.challenges.list.viewmodel.Challenge.Companion.CHALLENGE_TYPE_COMPLETED
import pt.nunoneto.codewars.ui.challenges.list.viewmodel.ChallengeListViewModel
import pt.nunoneto.codewars.ui.challenges.list.viewmodel.ChallengeViewModelFactory
import pt.nunoneto.codewars.utils.IntentValues

class ChallengesListFragment : Fragment(), View.OnClickListener{

    companion object {

        fun newInstance(challengeType: Int) : ChallengesListFragment {
            val fragment = ChallengesListFragment()
            val bundle = Bundle(1)
            bundle.putInt(IntentValues.EXTRA_CHALLENGE_TYPE, challengeType)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var viewModel: ChallengeListViewModel
    private lateinit var challengesAdapter: ChallengesListAdapter

    @ChallengeType
    private var challengeType: Int = CHALLENGE_TYPE_COMPLETED

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.challenges_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val username = when (activity?.intent == null) {
            true -> ""
            false -> activity!!.intent.getStringExtra(IntentValues.EXTRA_USER_USERNAME)
        }

        challengeType = arguments?.getInt(IntentValues.EXTRA_CHALLENGE_TYPE, CHALLENGE_TYPE_COMPLETED) ?: CHALLENGE_TYPE_COMPLETED

        viewModel = ViewModelProviders
                .of(this, ChallengeViewModelFactory(username, challengeType))
                .get(ChallengeListViewModel::class.java)

        setObservers()
        setUiComponents()
    }

    fun loadNextPage() {
        viewModel.loadNextPage()
    }

    private fun setUiComponents() {
        challengesAdapter = ChallengesListAdapter(this)
        challengesAdapter.adapterMode = if (challengeType == CHALLENGE_TYPE_COMPLETED
                && viewModel.noMorePages.value == false)
            ChallengesListAdapter.ADAPTER_LOAD_MORE
        else
            ChallengesListAdapter.ADAPTER_DONT_LOAD_MORE


        val layoutManager = LinearLayoutManager(context)
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

        viewModel.error.observe(this, Observer<Boolean> {
            error -> if (error == true)  onError()
        })
    }

    private fun onError() {
        pb_loading_challenges.visibility = View.GONE
        tv_error.setText(R.string.error_load_challenges)
        tv_error.visibility = View.VISIBLE
    }

    private fun noMorePages() {
        Snackbar.make(view!!, R.string.no_more_results, Snackbar.LENGTH_SHORT).show()
        rv_challenges.post{
            challengesAdapter.adapterMode = ChallengesListAdapter.ADAPTER_DONT_LOAD_MORE
            challengesAdapter.notifyItemRemoved(challengesAdapter.itemCount)
        }
    }

    private fun updateAdapter(challengesList: List<Challenge>?) {
        if (challengesList == null) {
            return
        }

        if (challengesAdapter.challenges.isEmpty() && challengesList.isEmpty()) {
            tv_error.setText(R.string.challenges_no_results)
            tv_error.visibility = View.VISIBLE
            rv_challenges.visibility = View.GONE
            pb_loading_challenges.visibility = View.GONE
            return
        } else {
            tv_error.visibility = View.GONE
            rv_challenges.visibility = View.VISIBLE
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

    override fun onClick(view: View?) {
        if (view == null) {
            return
        }

        val position = rv_challenges.getChildAdapterPosition(view)
        if (position == RecyclerView.NO_POSITION) {
            return
        }

        val challenge = challengesAdapter.challenges[position]
        viewModel.onChallengeSelected(challenge, context)
    }
}
