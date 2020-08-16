package com.edu.sun.oereminder.ui.settings

import android.os.Bundle
import android.view.View
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.bumptech.glide.Glide
import com.edu.sun.oereminder.R
import com.edu.sun.oereminder.data.SourceCallback
import com.edu.sun.oereminder.data.model.Account
import com.edu.sun.oereminder.data.repository.UserRepository
import com.edu.sun.oereminder.utils.InjectorUtils
import com.edu.sun.oereminder.utils.toast
import kotlinx.android.synthetic.main.layout_profile.*

class SettingsFragment : PreferenceFragmentCompat() {

    private var timeSheetPreference: Preference? = null
    private var mentionPreference: Preference? = null
    private var tokenPreference: Preference? = null
    private var roomPreference: Preference? = null
    private var aboutPreference: Preference? = null

    private val userRepository: UserRepository by lazy {
        InjectorUtils.getUserRepository(requireContext())
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setupProfile()
        initListener()
    }

    private fun initView() {
        timeSheetPreference = findPreference(getString(R.string.key_time_sheet))
        mentionPreference = findPreference(getString(R.string.key_mention))
        tokenPreference = findPreference(getString(R.string.key_token))
        roomPreference = findPreference(getString(R.string.key_room))
        aboutPreference = findPreference(getString(R.string.key_about))
    }

    private fun setupProfile() {
        userRepository.getProfile(object : SourceCallback<Account?> {
            override fun onSuccess(data: Account?) {
                data?.run {
                    textDisplayName.text = name
                    textEmail.text = loginMail
                    context?.run {
                        Glide.with(this).load(avatarUrl).circleCrop().into(imageAvatar)
                    }
                }
            }

            override fun onError(e: Exception) {
                context?.toast(e.message.toString())
            }
        })
    }

    private fun initListener() {

    }
}
