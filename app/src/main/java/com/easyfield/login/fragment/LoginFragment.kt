package com.easyfield.login.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.easyfield.R
import com.easyfield.activities.HelpActivity
import com.easyfield.activities.HomeActivity
import com.easyfield.base.BaseFragment
import com.easyfield.databinding.FragmentLoginBinding
import com.easyfield.location.LocationDisclosureDialogFragment
import com.easyfield.utils.AppConstants
import com.easyfield.utils.PreferenceHelper
import com.easyfield.utils.extension.alert
import com.easyfield.utils.extension.bold
import com.easyfield.utils.extension.click
import com.easyfield.utils.extension.hideKeyboard
import com.easyfield.utils.extension.log
import com.easyfield.utils.extension.positiveButton
import com.easyfield.utils.extension.underline
import com.codeflixweb.callenza.network.Resource
import com.lezasolutions.callenza.utils.extension.getAppVersionName
import com.lezasolutions.callenza.utils.extension.getDeviceInformation
import com.lezasolutions.callenza.utils.extension.getUniqueSecureDeviceId
import com.lezasolutions.callenza.utils.extension.toast


class LoginFragment : BaseFragment() {



    private var _binding: FragmentLoginBinding? = null





    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        clickevents()

        setObserver()

    }

    private fun setObserver() {
        viewModel.loginResponse.observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        progressDialog.hide()
                        response.data?.let {
                            if (it.status == 200) {
                                it.data.user.let { user ->

                                    viewModel.setAuthToken(user.access_token)
                                    viewModel.setLoginStatus(true)
                                    viewModel.setUserObject(user)
                                    PreferenceHelper.set(AppConstants.PREF_KEY_USER_TYPE,user.user_type)
                                }

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
                                it.message.let { msg -> toast(msg) }
                            }
                        }
                    }

                    is Resource.Error -> {
                        progressDialog.hide()



                    }

                    is Resource.Loading -> {
                        progressDialog.show()
                    }
                }

            }
        })
    }


    private fun clickevents() {


        
        binding.tvAppVersion.text =
            String.format(getString(R.string.app_version), getAppVersionName())


        binding.tvPrivacyPolicy.movementMethod = LinkMovementMethod.getInstance()
        binding.tvPrivacyPolicy.text = getString(R.string.str_privacy_policy)
            .underline()
            .bold()
            .click {
                val i = Intent(Intent.ACTION_VIEW)
                i.setData(Uri.parse(AppConstants.PRIVACY_POLICY))
                startActivity(i)
            }

        binding.btnLogin.setOnClickListener {
            if (binding.inputMobileNumber.text?.isEmpty()!!) {
                toast(getString(R.string.alert_enter_mobile_number))
                return@setOnClickListener
            }
            else{
                if (hasLocationPermission()) {
                    doLogin()
                } else {
                    showLocationDisclosure()
                }
                hideKeyboard()
            }
        }
    }

    private fun doLogin() {



        val fcmToken: String? = PreferenceHelper[AppConstants.PREF_KEY_FCM_TOKEN, null]

        Log.w("device_id","token"+getUniqueSecureDeviceId())

        Log.e("LOGIN_DATA"+ getUniqueSecureDeviceId().toString()+ " // "+getDeviceInformation(),getAppVersionName().toString() )
        viewModel.login(binding.inputMobileNumber.text.toString(),getUniqueSecureDeviceId(),
            getDeviceInformation(),getAppVersionName()!!,"")


    }

    private fun showLocationDisclosure() {
        val dialog = LocationDisclosureDialogFragment(object :
            LocationDisclosureDialogFragment.DisclosureEventListener {
            override fun onClickAllowPermission() {
                requestLocationPermission.launch(locationPermissions)
            }

            override fun onClickCloseApp() {
                requireActivity().finish()
            }

        })
        dialog.show(childFragmentManager, "disclosure")
    }

    private fun hasLocationPermission(): Boolean = locationPermissions.all { hasPermission(it) }

    private fun hasPermission(permission: String) =
        ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED

    private var locationPermissions: Array<String> = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private val requestLocationPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                log { "${it.key} = ${it.value}" }
                when (it.key) {
                    Manifest.permission.ACCESS_FINE_LOCATION -> {
                        handleLocationPermission(it.value)
                    }
                }
            }
        }

    private fun handleLocationPermission(isGranted: Boolean) {
        when {
            isGranted -> doLogin()
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                //this option is available starting in API 23
                requireContext().alert {
                    setMessage(getString(R.string.alert_permission_required))
                    positiveButton(getString(R.string.str_retry)) {
                        requestLocationPermission.launch(locationPermissions)
                    }
                }
            }
            else -> requireContext().alert {
                setMessage(getString(R.string.alert_permission_required))
                positiveButton(getString(R.string.str_exit)) {
                    requireActivity().finish()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}