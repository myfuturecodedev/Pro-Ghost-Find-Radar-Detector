package com.futurecode.ghostfinderradardetector.fragment.prelogin

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.futurecode.ghostfinderradardetector.R
import com.futurecode.ghostfinderradardetector.activity.MainActivity
import com.futurecode.ghostfinderradardetector.activity.MyApplication
import com.futurecode.ghostfinderradardetector.adapter.LanguageAdapter
import com.futurecode.ghostfinderradardetector.ads.interstitial_ad.FullScreenAdsHelper
import com.futurecode.ghostfinderradardetector.base.BaseFragment
import com.futurecode.ghostfinderradardetector.databinding.FragmentLanguageBinding
import com.futurecode.ghostfinderradardetector.model.LanguageModel
import com.futurecode.ghostfinderradardetector.utils.AdViewTypeManager
import com.futurecode.ghostfinderradardetector.utils.Utils.setAdClickListener
import java.util.Locale

class LanguageFragment : BaseFragment<FragmentLanguageBinding>(FragmentLanguageBinding::inflate) {
    private lateinit var languageAdapter: LanguageAdapter
    private val languages = mutableListOf<LanguageModel>()
    private val filteredLanguages = mutableListOf<LanguageModel>()
    lateinit var fullScreenAdsHelper: FullScreenAdsHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fullScreenAdsHelper = FullScreenAdsHelper(requireActivity())

        initData()
        initViews()
        setupRecyclerView()
        setupSearch()
    }

    private fun initData() {
        val currentLang = prefManager.selectedLanguage
        languages.clear()
        languages.add(LanguageModel("English", "en", R.drawable.eng, currentLang == "en"))
        languages.add(LanguageModel("हिंदी (Hindi)", "hi", R.drawable.eng, currentLang == "hi"))
        languages.add(LanguageModel("Español (Spanish)", "es", R.drawable.spanish_flag, currentLang == "es"))
        languages.add(LanguageModel("Français (French)", "fr", R.drawable.french_flag, currentLang == "fr"))
        languages.add(LanguageModel("Português (Portuguese)", "pt", R.drawable.portuguese_glag, currentLang == "pt"))
        languages.add(LanguageModel("Deutsch (German)", "de", R.drawable.german_flag, currentLang == "de"))
        languages.add(LanguageModel("Türkçe (Turkish)", "tr", R.drawable.turkish_flag, currentLang == "tr"))
        languages.add(LanguageModel("한국어 (Korean)", "ko", R.drawable.korean_flag, currentLang == "ko"))
        languages.add(LanguageModel("日本語 (Japanese)", "ja", R.drawable.japanese_flag, currentLang == "ja"))
        
        filteredLanguages.clear()
        filteredLanguages.addAll(languages)
    }

    private fun initViews() {
        binding.ivBack.visibility = if (!prefManager.isOnboardingDone) View.GONE else View.VISIBLE
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }

        binding.ivSearch.setAdClickListener(requireActivity(), fullScreenAdsHelper) {
            if (binding.etSearch.visibility == View.VISIBLE) {
                binding.etSearch.visibility = View.GONE
                binding.tvTitle.visibility = View.VISIBLE
                binding.etSearch.text.clear()
            } else {
                binding.etSearch.visibility = View.VISIBLE
                binding.tvTitle.visibility = View.GONE
                binding.etSearch.requestFocus()
            }
        }
    }

    private fun setupRecyclerView() {
        val languagesWithAds = AdViewTypeManager.wrapList(filteredLanguages, interval = 3, isSingleAd = true)
        languageAdapter = LanguageAdapter(requireActivity(), languagesWithAds) { selectedLanguage ->
            
            // 1. Save language
            prefManager.selectedLanguage = selectedLanguage.code

            // 2. BREAK THE LOOP: If first run, go to Onboarding. If settings, restart app.
            if (!prefManager.isOnboardingDone) {
                // Apply locale immediately for this session
                MyApplication.setLocale(requireContext())
                findNavController().navigate(R.id.action_languageFragment_to_onBoardingFragment)
            } else {
                // Restart MainActivity to apply changes app-wide
                val intent = Intent(requireActivity(), MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                requireActivity().finish()
            }
        }

        binding.rvLanguages.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = languageAdapter
        }
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { filter(s.toString()) }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filter(text: String) {
        filteredLanguages.clear()
        if (text.isEmpty()) {
            filteredLanguages.addAll(languages)
        } else {
            val searchText = text.lowercase(Locale.getDefault())
            languages.forEach {
                if (it.name.lowercase(Locale.getDefault()).contains(searchText)) {
                    filteredLanguages.add(it)
                }
            }
        }
        languageAdapter.notifyDataSetChanged()
    }
}
