package pt.nunoneto.codewars.ui.users

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.annotation.IntDef
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.text.TextUtils
import android.view.*
import kotlinx.android.synthetic.main.users_fragment.*
import pt.nunoneto.codewars.R
import pt.nunoneto.codewars.entities.User

class UsersFragment : Fragment(), View.OnClickListener {

    companion object {

        fun newInstance() : UsersFragment {
            return UsersFragment()
        }

        // Sort Types
        const val DATE_OF_SEARCH = 0
        const val RANK = 1
    }

    @IntDef(DATE_OF_SEARCH, RANK)
    annotation class Sort

    private lateinit var viewModel: UsersViewModel
    private lateinit var recentUserAdapter: RecentUserAdapter

    @Sort
    private var sortType: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.users_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUiComponents()
    }

    private fun setUiComponents() {
        setupToolbar()
        setupRecycler()
        setupViewModel()
    }

    private fun setupRecycler() {
        rv_recent_searches.layoutManager = LinearLayoutManager(context)
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(UsersViewModel::class.java)

        viewModel.mutableSearchedUser.observe(this, Observer<User> {
            user -> setupUser(user)
        })

    viewModel.searching.observe(this, Observer<Boolean> {
            searching ->

            val visibility = if (searching!!)  View.VISIBLE else View.GONE

            pb_load_user.visibility = visibility

            if (searching || viewModel.mutableSearchedUser.value != null) {
                cv_user_search_results.visibility = View.VISIBLE
            } else {
                cv_user_search_results.visibility = View.GONE
            }

            if (searching) {
                rl_user_search_results_wrapper.visibility = View.GONE
            }
        })

        viewModel.error.observe(this, Observer<Boolean> {
            error ->

            if (error!!) {
                Snackbar.make(view!!, R.string.error_search, Snackbar.LENGTH_SHORT).show()
            }
        })

        viewModel.getRecentUserSearches(context!!)?.observe(this,
                Observer<List<User>> { userList ->updateRecentUserList(userList) })
    }

    private fun setupUser(user: User?) {
        if (user != null) {
            tv_user_name.text = when (TextUtils.isEmpty(user.name)) {
                true -> user.username
                else -> user.name
            }

            tv_user_rank.text = getString(R.string.user_rank, user.leaderboardPosition)
            tv_best_language.text = getString(R.string.user_best_language, user.bestLanguage, user.bestLanguageRank)

            cv_user_search_results.visibility = View.VISIBLE
            rl_user_search_results_wrapper.visibility = View.VISIBLE

            cv_user_search_results.setOnClickListener {
                viewModel.onRecentListPositionSelected(user.username, user.name, context)
            }

        } else {
            cv_user_search_results.visibility = View.GONE
        }
    }

    private fun getToolbar(): ActionBar? {
        return (activity as AppCompatActivity).supportActionBar
    }

    private fun setupToolbar() {
        getToolbar()?.setTitle(R.string.menu_users)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)

        if (::recentUserAdapter.isInitialized) {
            menu?.findItem(R.id.action_sort)?.isVisible = recentUserAdapter.users.isNotEmpty()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.users_fragment_menu, menu)

        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextChange(p0: String?): Boolean {
                // do nothing
                return false
            }

            override fun onQueryTextSubmit(searchQuery: String?): Boolean {
                if (TextUtils.isEmpty(searchQuery) || searchQuery!!.length < 3) {
                    Snackbar.make(view!!, R.string.error_query_too_short, Snackbar.LENGTH_SHORT).show()
                    return false
                }

                viewModel.searchUser(searchQuery, context)
                searchItem.collapseActionView()
                return true
            }
        })
    }

    private fun updateRecentUserList(userList: List<User>?) {
        var sortedUsers: List<User> = userList ?: ArrayList()

        // set no items view
        if (sortedUsers.isEmpty()) {
            tv_no_users_found.visibility = View.VISIBLE
            rv_recent_searches.visibility = View.GONE
        } else {
            tv_no_users_found.visibility = View.GONE
            rv_recent_searches.visibility = View.VISIBLE
        }

        // sort users
        sortedUsers = when (sortType) {
            DATE_OF_SEARCH -> sortedUsers.sortedByDescending { it.dateOfSearch }
            RANK -> sortedUsers.sortedBy { it.leaderboardPosition }
            else -> sortedUsers.sortedByDescending { it.dateOfSearch }
        }

        // init adapter if needed
        if (!::recentUserAdapter.isInitialized) {
            recentUserAdapter = RecentUserAdapter(this, sortedUsers)
            rv_recent_searches.adapter = recentUserAdapter
            return
        }

        // update adapter
        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(p0: Int, p1: Int): Boolean {
                return true
            }

            override fun getOldListSize(): Int {
                return recentUserAdapter.users.size
            }

            override fun getNewListSize(): Int {
                return sortedUsers.size
            }

            override fun areContentsTheSame(p0: Int, p1: Int): Boolean {
                return recentUserAdapter.users[p0] == sortedUsers[p1]
            }
        })

        recentUserAdapter.users = sortedUsers
        diff.dispatchUpdatesTo(recentUserAdapter)

        // update menu actions
        activity?.invalidateOptionsMenu()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_sort -> {
                toggleSort()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun toggleSort() {
        sortType = when (sortType) {
            RANK -> DATE_OF_SEARCH
            DATE_OF_SEARCH -> RANK
            else -> DATE_OF_SEARCH
        }

        // show sort type msg
        val sortMsg =  when (sortType) {
            RANK -> getString(R.string.sorted_by_rank)
            DATE_OF_SEARCH -> getString(R.string.sorted_by_date)
            else -> getString(R.string.sorted_by_date)
        }

        Snackbar.make(view!!,getString(R.string.sorted_by_message, sortMsg), Snackbar.LENGTH_SHORT).show()

        updateRecentUserList(recentUserAdapter.users)
    }

    // recent item click
    override fun onClick(view: View?) {
        if (view == null) {
            return
        }

        val position = rv_recent_searches.getChildAdapterPosition(view)
        if (position == RecyclerView.NO_POSITION) {
            return
        }

        val selectedUser = recentUserAdapter.users[position]
        viewModel.onRecentListPositionSelected(selectedUser.username, selectedUser.name, context)
    }

}