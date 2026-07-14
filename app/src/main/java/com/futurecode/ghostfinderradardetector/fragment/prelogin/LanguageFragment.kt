package com.futurecode.ghostfinderradardetector.fragment.prelogin

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.futurecode.ghostfinderradardetector.R
import com.futurecode.ghostfinderradardetector.activity.HomeActivity
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
    private var isFromSettings = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fullScreenAdsHelper = FullScreenAdsHelper(requireActivity())

        isFromSettings = arguments?.getBoolean("isFromSettings", false) ?: false

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
        binding.ivBack.visibility = if (isFromSettings) View.VISIBLE else View.GONE
        binding.ivBack.setOnClickListener { 
            if (isAdded) findNavController().popBackStack() 
        }

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
        languageAdapter = LanguageAdapter(requireActivity(), buildLanguageAdapterList()) { selectedLanguage ->
            prefManager.selectedLanguage = selectedLanguage.code
            updateSelectedLanguage(selectedLanguage.code)
            
            MyApplication.setLocale(requireContext())

            if (isAdded) {
                if (isFromSettings) {
                    prefManager.isLanguageChangedFromSetting = true
                    val intent = Intent(requireActivity(), HomeActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    requireActivity().finish()
                } else {
                    prefManager.isLanguageChangedFromSplash = true
                    // Restart MainActivity to apply language to the activity's base context
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.putExtra("skip_splash", true)
                    startActivity(intent)
                    requireActivity().finish()
                }
            }
        }

        binding.rvLanguages.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = languageAdapter
        }
    }

    private fun buildLanguageAdapterList(): List<Any> {
        return AdViewTypeManager.wrapList(filteredLanguages, interval = 3, isSingleAd = true)
    }

    private fun updateSelectedLanguage(languageCode: String) {
        languages.forEach { language ->
            language.isSelected = language.code == languageCode
        }
        filter(binding.etSearch.text?.toString().orEmpty())
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
        languageAdapter.submitList(buildLanguageAdapterList())
    }
}
