package com.easyfield.splash.fragments

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.easyfield.R
import com.easyfield.activities.HelpActivity
import com.easyfield.activities.HomeActivity
import com.easyfield.databinding.FragmentSplashBinding
import com.easyfield.utils.AppConstants
import com.easyfield.utils.CommonData
import com.easyfield.utils.ItemClickAdapter
import com.easyfield.utils.PreferenceHelper
import com.easyfield.utils.agreeButton
import com.easyfield.utils.disagreeButton
import com.easyfield.utils.extension.alert
import com.easyfield.utils.extension.negativeButton
import com.easyfield.utils.extension.noInternet
import com.easyfield.utils.extension.positiveButton
import com.easyfield.utils.extension.setProgressDialog
import com.easyfield.utils.showTermsAndConditionDialog
import com.easyfield.viewmodels.UserViewModel
import com.codeflixweb.callenza.network.Resource
import com.lezasolutions.callenza.utils.extension.getAppVersionCode
import com.lezasolutions.callenza.utils.extension.toast


class SplashFragment : Fragment() {
    private var _binding: FragmentSplashBinding? = null
    val viewModel: UserViewModel by viewModels()

    lateinit var progressDialog : Dialog

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog=setProgressDialog(requireContext())
        viewModel.settings()
        setObserver()
    }


    private fun movetotransaction(){

        val shouldShowAgreement: Boolean? = PreferenceHelper[AppConstants.PREF_KEY_SHOW_AGREEMENT, true]

        if (shouldShowAgreement!!) {
            showAgreement()
        } else {
            observeLogin()
        }
    }

    fun setObserver(){

        viewModel.settingResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        response.data?.let {
                            if (it.status==200){
                                if(it.data.under_maintenance.equals("1")){
                                    toast("Under Maintenance")
                                }
                                else{
                                    val latestVersionCode = it.data.app_version.toInt()
                                    if (getAppVersionCode()!! < latestVersionCode) {
                                        showUpdateAlert()
                                    }
                                    else{
                                        viewModel.dropdown()
                                    }
                                }
                            } else {
                                toast(it.message)
                            }
                        }
                    }

                    is Resource.Error -> try {
                        noInternet(object : ItemClickAdapter {
                            override fun onItemClick(pos: Int, from: String) {
                                viewModel.settings()
                            }
                        })
                    }catch(e:Exception) {
                        e.printStackTrace()
                        toast("Please check your internet connection !!!")
                    }

                    is Resource.Loading -> {
                    }

                    else -> {}
                }

            }
        }
        viewModel.dropdownResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {


                        response.data?.let {



                            if (it.status==200){

                                CommonData.dropDownResponse=it

                                movetotransaction()

                            } else {
                                toast(it.message)
                            }
                        }
                    }

                    is Resource.Error -> {

                        noInternet(object : ItemClickAdapter {
                            override fun onItemClick(pos: Int, from: String) {
                                viewModel.dropdown()
                            }
                        })
                    }

                    is Resource.Loading -> {



                    }

                    else -> {}
                }

            }
        }
    }

    private fun showAgreement() {
        requireContext().showTermsAndConditionDialog(
            R.style.AlertDialogTheme,
            AppConstants.TERMS_CONDITION
        ) {
            agreeButton {
                PreferenceHelper[AppConstants.PREF_KEY_SHOW_AGREEMENT] = false
                observeLogin()
            }
            disagreeButton {
                requireActivity().finish()
            }
        }
    }
    private fun showUpdateAlert() {
        requireContext().alert(R.style.AlertDialogTheme) {
            setTitle(getString(R.string.str_title_app_update_available))
            setMessage(getString(R.string.str_msg_app_update))
            positiveButton(getString(R.string.str_update_now)) {

                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.axisseedcrop")))

                requireActivity().finish()
            }
            negativeButton(getString(R.string.str_exit)) {
                requireActivity().finish()
            }
        }
    }
    private fun observeLogin() {

        viewModel.launch().observe(viewLifecycleOwner) { isLoggedIn ->
            if (isLoggedIn) {

                val HELPSCREEN = PreferenceHelper[AppConstants.PREF_KEY_IS_HELP_SCREEN, false]

                if(!HELPSCREEN!!){
                    startActivity(Intent(requireContext(), HelpActivity::class.java))
                    requireActivity().finish()
                }
                else{
                    startActivity(Intent(requireContext(), HomeActivity::class.java))
                    requireActivity().finish()
                }


            } else {


                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)


            }
        }
    }

}