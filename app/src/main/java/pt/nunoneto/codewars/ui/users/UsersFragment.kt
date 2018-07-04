package pt.nunoneto.codewars.ui.users

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.text.TextUtils
import android.view.*
import kotlinx.android.synthetic.main.users_fragment.*
import pt.nunoneto.codewars.R
import pt.nunoneto.codewars.entities.User

class UsersFragment : Fragment() {

    private lateinit var viewModel: UsersViewModel;

    companion object {

        fun newInstance() : UsersFragment {
            return UsersFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.users_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUiComponents()
    }

    private fun setUiComponents() {
        setupViewModel()
        setupToolbar()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(UsersViewModel::class.java)

        viewModel.mutableSearchedUser.observe(this, Observer<User> {
            user ->

                if (user != null) {
                    tv_user_name.text = user.name
                    tv_user_rank.text = getString(R.string.user_rank, user.overallRank)
                    tv_best_language.text = getString(R.string.user_best_language, user.bestLanguage, user.bestLanguageRank)

                    cv_user_search_results.visibility = View.VISIBLE
                    rl_user_search_results_wrapper.visibility = View.VISIBLE

                } else {
                    cv_user_search_results.visibility = View.GONE
                }
        })

    viewModel.searching.observe(this, Observer<Boolean> {
            searching ->

            var visibility = if (searching!!)  View.VISIBLE else View.GONE

            pb_load_user.visibility = visibility

            if (searching!! || viewModel.mutableSearchedUser.value != null) {
                cv_user_search_results.visibility = View.VISIBLE
            } else {
                cv_user_search_results.visibility = View.GONE
            }

            if (searching!!) {
                rl_user_search_results_wrapper.visibility = View.GONE
            }
        })

        viewModel.error.observe(this, Observer<Boolean> {
            error ->

            if (error!!) {
                Snackbar.make(view!!, R.string.error_search, Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupToolbar() {
        var actionBar: ActionBar = (activity as AppCompatActivity).supportActionBar ?: return

        actionBar.setTitle(R.string.menu_users)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.users_fragment_menu, menu)

        var searchItem = menu?.findItem(R.id.action_search)
        var searchView = searchItem?.actionView as SearchView

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

                viewModel.searchUser(searchQuery)
                searchItem.collapseActionView()
                return true
            }
        })
    }

}