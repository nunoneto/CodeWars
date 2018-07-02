package pt.nunoneto.codewars.ui.users

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pt.nunoneto.codewars.R

class UsersFragment : Fragment() {

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
        setupToolbar()
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).supportActionBar!!.setTitle(R.string.menu_users)
    }
}