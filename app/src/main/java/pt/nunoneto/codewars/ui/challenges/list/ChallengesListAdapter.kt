package pt.nunoneto.codewars.ui.challenges.list

import android.content.res.ColorStateList
import android.support.design.chip.Chip
import android.support.design.chip.ChipGroup
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import pt.nunoneto.codewars.R
import pt.nunoneto.codewars.entities.Challenge
import pt.nunoneto.codewars.entities.Language
import java.text.SimpleDateFormat

class ChallengesListAdapter(private val fragment: ChallengesListFragment) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val CHALLENGE_ITEM = 0
        const val LOAD_MORE_ITEM = 1

        const val ADAPTER_MORE_LOAD_MORE = 0
        const val ADAPTER_MORE_NO_MORE_RESULTS = 1
    }

    var challenges: ArrayList<Challenge> = ArrayList()
    private var inflater: LayoutInflater = LayoutInflater.from(fragment.context)
    private val dateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
    var adapterMode = ADAPTER_MORE_LOAD_MORE

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == LOAD_MORE_ITEM) {
            LoadMoreViewHolder(inflater.inflate(R.layout.challenge_load_more_item, parent, false))
        } else {
            ChallengeViewHolder(inflater.inflate(R.layout.challenge_list_item, parent, false))
        }
    }

    // Last item is the Load More item
    override fun getItemCount(): Int {
        if (adapterMode == ADAPTER_MORE_NO_MORE_RESULTS) {
            return challenges.size
        }

        return challenges.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1 && adapterMode == ADAPTER_MORE_LOAD_MORE)
            LOAD_MORE_ITEM
        else
            CHALLENGE_ITEM
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (position < itemCount - 1 || adapterMode == ADAPTER_MORE_NO_MORE_RESULTS) {
            var challenge = challenges[position]
            val challengeViewHolder = viewHolder as ChallengeViewHolder
            // name
            challengeViewHolder.name.text = challenge.name

            //date
            var completedDate = fragment.getString(R.string.completed_at, dateFormat.format(challenge.completedAt))
            challengeViewHolder.completedDate.text = completedDate

            // languages
            var index = -1
            for (language in challenge.languages) {
                index = index.inc()
                buildLanguageChip(challengeViewHolder, language, index)
            }

            hideUnusedChips(index, challengeViewHolder.languagesWrapper)

        } else {
            fragment.loadNextPage()
        }
    }

    private fun hideUnusedChips(lastActiveIndex: Int, languageWrapper: ChipGroup) {
        for (i in lastActiveIndex.inc() .. languageWrapper.childCount.dec()) {
            languageWrapper.getChildAt(i).visibility = View.GONE
        }
    }

    private fun buildLanguageChip(viewHolder: ChallengeViewHolder, language: Language, index: Int) {
        val view: View? = viewHolder.languagesWrapper.getChildAt(index)
        val chip: Chip
        if (view == null) {
            chip = inflater.inflate(R.layout.language_chip, viewHolder.languagesWrapper, false) as Chip
            viewHolder.languagesWrapper.addView(chip)
        } else {
            chip = view as Chip
            chip.visibility = View.VISIBLE
        }

        chip.text = language.language
        chip.chipBackgroundColor = ColorStateList.valueOf(language.color)
    }

    class ChallengeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name: TextView = itemView.findViewById(R.id.tv_challenge_name)
        val completedDate: TextView = itemView.findViewById(R.id.tv_challenge_complete_date)
        val languagesWrapper: ChipGroup = itemView.findViewById(R.id.cg_languages_wrapper)
    }

   class LoadMoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}