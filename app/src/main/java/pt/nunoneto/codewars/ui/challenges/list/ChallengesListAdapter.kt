package pt.nunoneto.codewars.ui.challenges.list

import android.content.res.ColorStateList
import android.os.Build
import android.support.annotation.IntDef
import android.support.design.chip.Chip
import android.support.design.chip.ChipGroup
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import pt.nunoneto.codewars.R
import pt.nunoneto.codewars.entities.AuthoredChallenge
import pt.nunoneto.codewars.entities.Challenge
import pt.nunoneto.codewars.entities.CompletedChallenge
import pt.nunoneto.codewars.entities.Language
import java.text.SimpleDateFormat
import java.util.*

class ChallengesListAdapter(private val fragment: ChallengesListFragment) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        // item types
        const val CHALLENGE_ITEM = 0
        const val LOAD_MORE_ITEM = 1

        // adapter modes
        const val ADAPTER_LOAD_MORE = 0
        const val ADAPTER_DONT_LOAD_MORE = 1
    }

    @IntDef(ADAPTER_DONT_LOAD_MORE, ADAPTER_LOAD_MORE)
    annotation class AdapterMode

    @IntDef(CHALLENGE_ITEM, LOAD_MORE_ITEM)
    annotation class ViewType

    var challenges: ArrayList<Challenge> = ArrayList()
    private var inflater: LayoutInflater = LayoutInflater.from(fragment.context)
    private val dateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    @AdapterMode
    var adapterMode = ADAPTER_LOAD_MORE

    override fun onCreateViewHolder(parent: ViewGroup, @ViewType viewType: Int): RecyclerView.ViewHolder {
        val viewHolder = if (viewType == LOAD_MORE_ITEM) {
            LoadMoreViewHolder(inflater.inflate(R.layout.challenge_load_more_item, parent, false))
        } else {
            ChallengeViewHolder(inflater.inflate(R.layout.challenge_list_item, parent, false))
        }

        if (viewHolder is ChallengeViewHolder) {
            viewHolder.itemView.setOnClickListener(fragment)
        }

        return viewHolder
    }

    // Last item is the Load More item
    override fun getItemCount(): Int {
        if (adapterMode == ADAPTER_DONT_LOAD_MORE) {
            return challenges.size
        }

        return challenges.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1 && adapterMode == ADAPTER_LOAD_MORE)
            LOAD_MORE_ITEM
        else
            CHALLENGE_ITEM
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (isChallengeItem(position)) {
            val challenge = challenges[position]
            val challengeViewHolder = viewHolder as ChallengeViewHolder

            if (challenge is AuthoredChallenge) {
                setAuthoredItem(challenge, challengeViewHolder)
            } else if (challenge is CompletedChallenge){
                setCompletedItem(challenge, challengeViewHolder)
            }

        } else {
            fragment.loadNextPage()
        }
    }

    //region items

    private fun setCompletedItem(challenge: CompletedChallenge, challengeViewHolder: ChallengeViewHolder) {
        // name
        challengeViewHolder.name.text = challenge.name

        //date
        val completedDate = fragment.getString(R.string.completed_at, dateFormat.format(challenge.completedAt))
        challengeViewHolder.subtitle.text = completedDate

        // languages
        var index = -1
        for (language in challenge.languages) {
            index = index.inc()
            buildLanguageChip(challengeViewHolder, language, index)
        }

        hideUnusedChips(index, challengeViewHolder.languagesWrapper)
    }

    private fun setAuthoredItem(challenge: AuthoredChallenge, challengeViewHolder: ChallengeViewHolder) {
        // name
        challengeViewHolder.name.text = challenge.name

        //description
        challengeViewHolder.subtitle.text =  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(challenge.description, Html.FROM_HTML_MODE_COMPACT)
        } else {
            @Suppress("DEPRECATION")
            Html.fromHtml(challenge.description)
        }

        // languages
        var index = -1
        for (language in challenge.languages) {
            index = index.inc()
            buildLanguageChip(challengeViewHolder, language, index)
        }

        hideUnusedChips(index, challengeViewHolder.languagesWrapper)
    }

    // endregion items

    // region chips

    private fun buildLanguageChip(viewHolder: ChallengeViewHolder, language: Language, index: Int) {
        val view: View? = viewHolder.languagesWrapper.getChildAt(index)
        val chip: Chip
        if (view == null) {
            chip = inflater.inflate(R.layout.generic_chip, viewHolder.languagesWrapper, false) as Chip
            viewHolder.languagesWrapper.addView(chip)
        } else {
            chip = view as Chip
            chip.visibility = View.VISIBLE
        }

        chip.text = language.language
        chip.chipBackgroundColor = ColorStateList.valueOf(language.color)
    }

    private fun hideUnusedChips(lastActiveIndex: Int, languageWrapper: ChipGroup) {
        for (i in lastActiveIndex.inc() .. languageWrapper.childCount.dec()) {
            languageWrapper.getChildAt(i).visibility = View.GONE
        }
    }

    // endregion chips

    private fun isChallengeItem(position: Int) : Boolean{
        return position < itemCount - 1 || adapterMode == ADAPTER_DONT_LOAD_MORE
    }

    class ChallengeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name: TextView = itemView.findViewById(R.id.tv_challenge_name)
        val subtitle: TextView = itemView.findViewById(R.id.tv_subtitle)
        val languagesWrapper: ChipGroup = itemView.findViewById(R.id.cg_languages_wrapper)
    }

   class LoadMoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}