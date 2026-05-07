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
import com.futurecode.ghostfinderradardetector.ads.native_ad.NativeAdsHelper
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
    private var nativeAdsHelper: NativeAdsHelper? = null
    lateinit var fullScreenAdsHelper: FullScreenAdsHelper


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fullScreenAdsHelper= FullScreenAdsHelper(requireActivity())

        initData()
        initViews()
        setupRecyclerView()
        setupSearch()

    }

    private fun initData() {
        val currentLang = prefManager.selectedLanguage
        languages.clear()
        languages.add(LanguageModel(getString(R.string.english), "en", R.drawable.eng, currentLang == "en"))
        languages.add(LanguageModel(getString(R.string.spanish), "es", R.drawable.spanish_flag, currentLang == "es"))
        languages.add(LanguageModel(getString(R.string.french), "fr", R.drawable.french_flag, currentLang == "fr"))
        languages.add(LanguageModel(getString(R.string.portuguese), "pt", R.drawable.portuguese_glag, currentLang == "pt"))
        languages.add(LanguageModel(getString(R.string.german), "de", R.drawable.german_flag, currentLang == "de"))
        languages.add(LanguageModel(getString(R.string.turkish), "tr", R.drawable.turkish_flag, currentLang == "tr"))
        languages.add(LanguageModel(getString(R.string.korean), "ko", R.drawable.korean_flag, currentLang == "ko"))
        languages.add(LanguageModel(getString(R.string.japanese), "ja", R.drawable.japanese_flag, currentLang == "ja"))
        
        filteredLanguages.clear()
        filteredLanguages.addAll(languages)
    }

    private fun initViews() {
        // Hide back button on first run (Splash -> Language)
        if (!prefManager.isOnboardingDone) {
            binding.ivBack.visibility = View.GONE
        } else {
            binding.ivBack.visibility = View.VISIBLE
        }

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
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
        // 1. Wrap the list with Ads (e.g., every 5 items for the language screen)
        //val languagesWithAds = AdViewTypeManager.wrapList(filteredLanguages, interval = 3)

        val languagesWithAds = AdViewTypeManager.wrapList(filteredLanguages, interval = 3, isSingleAd = true)
        // 2. Initialize the updated Adapter
//        languageAdapter = LanguageAdapter(requireActivity(), languagesWithAds) { selectedLanguage ->
//
//            // 1. Save language
//            prefManager.selectedLanguage = selectedLanguage.code
//
//            // 2. Apply Locale immediately
//            MyApplication.setLocale(requireContext())
//
//            if (!prefManager.isOnboardingDone) {
//                // If first time, proceed
//                findNavController().navigate(R.id.action_languageFragment_to_onBoardingFragment)
//            } else {
//                // IMPORTANT: Restart Activity to see changes immediately
//                requireActivity().finish()
//                startActivity(requireActivity().intent)
//            }
//        }


        // Inside setupRecyclerView(), update your adapter click listener:
        languageAdapter = LanguageAdapter(requireActivity(), languagesWithAds) { selectedLanguage ->

            // 1. Save language to preferences
            prefManager.selectedLanguage = selectedLanguage.code

            // 2. Apply Locale immediately (updates resources for this session)
            MyApplication.setLocale(requireContext())

            if (!prefManager.isOnboardingDone) {
                // If it's the first run, just move to Onboarding
                findNavController().navigate(R.id.action_languageFragment_to_onBoardingFragment)
            } else {
                // If user changed language from Settings, restart MainActivity
                val intent = Intent(requireActivity(), MainActivity::class.java)

                // This clears the entire app stack so the language applies everywhere
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

                startActivity(intent)
                requireActivity().finish()
            }
        }

        // 3. Bind to RecyclerView
        binding.rvLanguages.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = languageAdapter
        }
    }
    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filter(s.toString())
            }
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

    fun onSearch(query: String) {
        val filtered = languages.filter { it.name.contains(query, true) }
        // Always wrap again before updating the adapter!
       // languageAdapter.updateList(AdViewTypeManager.wrapList(filtered, 5))
    }
}