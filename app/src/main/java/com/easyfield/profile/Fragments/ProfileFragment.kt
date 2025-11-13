package com.easyfield.profile.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.easyfield.R
import com.easyfield.base.BaseActivity
import com.easyfield.base.BaseFragment
import com.easyfield.databinding.FragmentProfileBinding
import com.easyfield.network.RetrofitClient
import com.easyfield.utils.AppConstants
import com.easyfield.utils.ItemClickAdapter
import com.easyfield.utils.PreferenceHelper
import com.easyfield.utils.extension.alert
import com.easyfield.utils.extension.negativeButton
import com.easyfield.utils.extension.noInternet
import com.easyfield.utils.extension.positiveButton
import com.bumptech.glide.Glide
import com.codeflixweb.callenza.network.Resource
import com.lezasolutions.callenza.utils.extension.toast


class ProfileFragment : BaseFragment() {



    private var _binding: FragmentProfileBinding? = null




    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickevents()
        setObserver()


        viewModel.profile()
    }

    fun setObserver(){
        viewModel.logoutResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        progressDialog.dismiss()

                        response.data?.let {



                            if (it.status==200){


                                toast(it.message)
                                val act=activity as BaseActivity
                                act.doLogout()


                            } else {
                                toast(it.message)
                            }
                        }
                    }

                    is Resource.Error -> {

                        progressDialog.dismiss()

                        noInternet(object : ItemClickAdapter {
                            override fun onItemClick(pos: Int, from: String) {
                                viewModel.logout()
                            }
                        })
                    }

                    is Resource.Loading -> {

                        progressDialog.show()

                    }

                    else -> {}
                }

            }
        }
        viewModel.profileResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        progressDialog.dismiss()

                        response.data?.let {



                            if (it.status==200){

                                val url= RetrofitClient.retrofitClientURL+it.data.profile
                                Log.w("vishalurl",""+url)



                                Glide.with(requireContext())
                                    .load(url)
                                    .placeholder(R.drawable.ic_profile)
                                    .into(binding.ivPhoto)



                                binding.txtUserName.setText(it.data.name)

                                binding.txtMobileNumber.setText(it.data.phone_no)









                            } else {
                                toast(it.message)
                            }
                        }
                    }

                    is Resource.Error -> {

                        progressDialog.dismiss()

                    }

                    is Resource.Loading -> {

                        progressDialog.show()

                    }

                    else -> {}
                }

            }
        }
    }

    private fun clickevents() {

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }

        binding.rlTerms.setOnClickListener {

            val  bundle=Bundle()
            bundle.putInt("type",1)
            findNavController().navigate(R.id.action_profileFragment_to_cmsFragment,bundle)
        }
        binding.rlPrivacyPolicy.setOnClickListener {
            val  bundle=Bundle()
            bundle.putInt("type",2)
            findNavController().navigate(R.id.action_profileFragment_to_cmsFragment,bundle)
        }

        binding.rlLogout.setOnClickListener {

            val punchInTime: String? =
                PreferenceHelper[AppConstants.PREF_KEY_PUNCH_IN_TIME]
            val punchOutTime: String? =
                PreferenceHelper[AppConstants.PREF_KEY_PUNCH_OUT_TIME]

            if (!punchInTime.isNullOrEmpty() && punchOutTime.isNullOrEmpty()) {
                toast(getString(R.string.alert_punch_out_first_to_logout))
            } else {
                requireContext().alert(R.style.AlertDialogTheme) {
                    setTitle(getString(R.string.alert_title_logout))
                    setMessage(getString(R.string.alert_msg_logout))
                    negativeButton(getString(R.string.str_no)) {
                        it.dismiss()
                    }
                    positiveButton(getString(R.string.str_yes)) {


                        viewModel.logout()




                        //        val userViewModel: UserViewModel by viewModels()
                        //        setLogoutObserver(userViewModel)
                        //        userViewModel.logout()


                    }
                }
            }
        }
    }



}