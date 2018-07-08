package pt.nunoneto.codewars.ui.users

import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import pt.nunoneto.codewars.R
import pt.nunoneto.codewars.entities.User

class RecentUserAdapter(private var fragment: UsersFragment?, var users: List<User>) : RecyclerView.Adapter<RecentUserAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(fragment?.context).inflate(R.layout.recent_user_item, parent, false)
        view.setOnClickListener(fragment)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val user = users[position]

        viewHolder.name.text = when (TextUtils.isEmpty(user.name)) {
            true -> user.username
            else -> user.name
        }

        viewHolder.rank.text = fragment?.context?.getString(R.string.user_rank, user.leaderboardPosition)
        viewHolder.bestLanguage.text = fragment?.context?.getString(R.string.user_best_language, user.bestLanguage, user.bestLanguageRank)

        viewHolder.separator.visibility = when (position != itemCount -1) {
            true -> VISIBLE
            false -> GONE
        }
    }

    override fun getItemCount(): Int = users.size

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.tv_user_name)
        var rank: TextView = view.findViewById(R.id.tv_user_rank)
        var bestLanguage: TextView = view.findViewById(R.id.tv_best_language)
        var separator: View = view.findViewById(R.id.v_separator)
    }
}