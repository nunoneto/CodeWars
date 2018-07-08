package pt.nunoneto.codewars.ui.challenges.details

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.res.ColorStateList
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.design.chip.Chip
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.challenge_details_fragment.*
import pt.nunoneto.codewars.R
import pt.nunoneto.codewars.databinding.ChallengeDetailsFragmentBinding
import pt.nunoneto.codewars.entities.ChallengeDetails
import pt.nunoneto.codewars.entities.Language
import pt.nunoneto.codewars.ui.challenges.details.viewmodel.ChallengeDetailsViewModel
import pt.nunoneto.codewars.ui.challenges.details.viewmodel.ChallengeDetailsViewModelFactory
import pt.nunoneto.codewars.utils.ColorHelper
import pt.nunoneto.codewars.utils.IntentValues

class ChallengeDetailsFragment : Fragment() {

    companion object {
        fun newInstance() : ChallengeDetailsFragment {
            return ChallengeDetailsFragment()
        }
    }

    private lateinit var viewModel: ChallengeDetailsViewModel
    private lateinit var binding:ChallengeDetailsFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.challenge_details_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupToolbar(getString(R.string.loading_challenge))

        val challengeId = activity?.intent?.getStringExtra(IntentValues.EXTRA_CHALLENGE_ID) ?: ""

        viewModel = ViewModelProviders
                .of(this, ChallengeDetailsViewModelFactory(challengeId))
                .get(ChallengeDetailsViewModel::class.java)

        setupObservers()
    }

    private fun setupObservers() {
        viewModel.details.observe(this, Observer<ChallengeDetails> {
            details -> setDetails(details)
        })

        viewModel.error.observe(this, Observer<Boolean> {
            error -> if (error == true) onError()
        })
    }

    private fun onError() {
        tv_error_load_challenge.setText(R.string.error_load_challenge)
        tv_error_load_challenge.visibility = View.VISIBLE
        pb_loading_challenge.visibility = View.GONE
        setupToolbar(getString(R.string.challenge_details))
    }

    @Suppress("DEPRECATION")
    private fun setDetails(details: ChallengeDetails?) {
        binding.details = details
        setupToolbar(details?.name ?: "")

        tv_description.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(details?.description ?: "", Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(details?.description ?: "")
        }

        // approved
        if (TextUtils.isEmpty(details?.approvedAt) && TextUtils.isEmpty(details?.approvedByUser)) {
            tv_approved.visibility = View.GONE
        }

        if (!TextUtils.isEmpty(details?.approvedByUrl)){
            tv_approved.setOnClickListener {  openWebUrl(details?.approvedByUrl!!) }
        } else {
            tv_approved.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0)
        }

        // created
        if (TextUtils.isEmpty(details?.createdAt) && TextUtils.isEmpty(details!!.createdByUser)) {
            tv_created.visibility = View.GONE
        }

        if (!TextUtils.isEmpty(details?.createdByUrl)){
            tv_created.setOnClickListener {  openWebUrl(details!!.createdByUrl!!) }
        } else {
            tv_created.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0)
        }

        setLanguages(details?.languages)
        setTags(details?.tags)

        cv_general_details.visibility = View.VISIBLE
        cv_general_management.visibility = View.VISIBLE
        cv_general_languages.visibility = View.VISIBLE
        cv_general_tags.visibility = View.VISIBLE
        cv_general_stats.visibility = View.VISIBLE
        pb_loading_challenge.visibility = View.GONE
    }

    private fun setLanguages(languages: List<Language>?) {
        if (languages == null) {
            return
        }

        for (language in languages) {
            val chip: Chip = layoutInflater.inflate(R.layout.generic_chip, cg_languages, false) as Chip
            chip.text = language.language
            chip.chipBackgroundColor = ColorStateList.valueOf(language.color)
            cg_languages.addView(chip)
        }
    }

    private fun setTags(tags: List<String>?) {
        if (tags == null) {
            return
        }

        for (tag in tags) {
            val chip: Chip = layoutInflater.inflate(R.layout.generic_chip, cg_tags, false) as Chip
            chip.text = tag
            chip.chipBackgroundColor = ColorStateList.valueOf(ColorHelper.getRandomColor())
            cg_tags.addView(chip)
        }
    }

    private fun setupToolbar(title: String) {
        (activity as AppCompatActivity).supportActionBar!!.title = title
    }

    private fun openWebUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = (Uri.parse(url))
        startActivity(intent)
    }

}