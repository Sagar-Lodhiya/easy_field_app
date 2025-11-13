package com.easyfield.profile.Fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.easyfield.R
import com.easyfield.base.BaseFragment
import com.easyfield.databinding.FragmentEditProfileBinding
import com.easyfield.network.RetrofitClient
import com.easyfield.profile.response.ProfileResponse
import com.easyfield.utils.Validator
import com.easyfield.utils.extension.alert
import com.easyfield.utils.extension.hide
import com.easyfield.utils.extension.items
import com.easyfield.utils.extension.negativeButton
import com.easyfield.utils.extension.visible
import com.bumptech.glide.Glide
import com.codeflixweb.callenza.network.Resource
import com.lezasolutions.callenza.utils.extension.getRealPath
import com.lezasolutions.callenza.utils.extension.toast
import kotlinx.coroutines.launch


class EditProfileFragment : BaseFragment() {



    private var _binding: FragmentEditProfileBinding? = null

    lateinit var profileResponse: ProfileResponse



    var strPhotoPath=""
    private var latestTmpUri: Uri? = null




    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickevents()

        setObserver()



        viewModel.profile()



    }

    private fun setObserver() {
        viewModel.profileResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        progressDialog.dismiss()

                        response.data?.let {



                            if (it.status==200){

                                profileResponse=it



                                setData()









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
        viewModel.commonResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        progressDialog.dismiss()

                        response.data?.let {



                            if (it.status==200){


                                toast(it.message)
                                findNavController().popBackStack()
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


    private fun isFormValid(): Boolean {
        with(binding) {

            if (inputName.editText?.text.toString().isEmpty()) {
                toast("Please Enter First Name")
                return false
            }


            else if (inputLastname.editText?.text.toString().isEmpty()) {
                toast("Please Enter Last Name")
                return false
            }

            else if (inputEmailAddress.editText?.text.toString().isEmpty()) {
                toast("Please Enter Email Address")
                return false
            }
            else if (!Validator.validateEmail(inputEmailAddress.editText?.text.toString())) {
                toast("Please enter a valid email address")
                return false
            }


            return true
        }
    }



    private fun setData() {

        val url=RetrofitClient.retrofitClientURL+profileResponse.data.profile
        Log.w("vishalurl",""+url)



        Glide.with(requireContext())
            .load(url)
            .placeholder(R.drawable.ic_profile)
            .into(binding.ivLoadedPhoto)


        binding.ivLoadedPhoto.visible()
        binding.ivPhoto.hide()



        with(binding){



            inputName.editText!!.setText(profileResponse.data.name)
            inputLastname.editText!!.setText(profileResponse.data.last_name)
            inputEmailAddress.editText!!.setText(profileResponse.data.email)
            inputMobileNumber.editText!!.setText(profileResponse.data.phone_no)

            inputMobileNumber.editText!!.isEnabled=false


        }
    }


    private fun clickevents() {

        binding.ivPhoto.setOnClickListener {
            showDialog()
        }

        binding.ivLoadedPhoto.setOnClickListener {
            showDialog()
        }

        binding.ivBack.setOnClickListener {

            findNavController().popBackStack()
        }

        binding.txtSubmit.setOnClickListener {


            if(isFormValid()){
                viewModel.updateProfile(binding.inputName.editText!!.text.toString(),binding.inputLastname.editText!!.text.toString(),
                    binding.inputEmailAddress.editText!!.text.toString(),strPhotoPath)
            }
        }

    }

    private fun showDialog() {
        requireContext().alert(R.style.AlertDialogTheme) {
            items(
                arrayOf(
                    getString(R.string.str_take_photo),
                    getString(R.string.str_choose_from_library)
                )
            ) { _, which ->
                when (which) {
                    0 -> takeImage()
                    1 -> pickImage()
                }
            }
            negativeButton(getString(R.string.str_cancel)) {
                it.dismiss()
            }
        }
    }

    private fun takeImage() {
        lifecycleScope.launch {
            viewModel.getTmpFileUri().let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri)
            }
        }
    }
    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->

                    strPhotoPath= viewModel.currentPhotoPath.toString()
                    binding.ivPhoto.setImageURI(uri)
                    binding.ivPhoto.visible()
                    binding.ivLoadedPhoto.hide()
                }
            }
        }
    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            strPhotoPath= uri.getRealPath(requireContext()).toString()
            binding.ivPhoto.setImageURI(uri)
            binding.ivPhoto.visible()
            binding.ivLoadedPhoto.hide()
        }

    }
    private fun pickImage() {
        lifecycleScope.launch {
            pickImage.launch("image/*")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}