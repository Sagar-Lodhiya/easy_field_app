package com.easyfield.payment.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.easyfield.R
import com.easyfield.base.BaseFragment
import com.easyfield.databinding.FragmentAddPaymentBinding
import com.easyfield.payment.request.AddPaymentRequest
import com.easyfield.utils.CommonData
import com.easyfield.utils.extension.formatToViewDateDefaults
import com.codeflixweb.callenza.network.Resource
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.lezasolutions.callenza.utils.extension.toast
import kotlinx.coroutines.launch
import java.util.Date


class AddPaymentFragment : BaseFragment() {
    private var _binding: FragmentAddPaymentBinding? = null
    private val binding get() = _binding!!
    var selectedPartyID=0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickevents()
        setObserver()
        setAdapters()
    }
    private fun setAdapters() {
        with(binding){
            inputDate.editText?.setText(Date().formatToViewDateDefaults())
        }


        (binding.inputParty.editText as MaterialAutoCompleteTextView).setAdapter(ArrayAdapter(
            requireContext(), android.R.layout.simple_list_item_1, CommonData.dropDownResponse.data.parties
        ))

        (binding.inputParty.editText as MaterialAutoCompleteTextView).setOnItemClickListener { adapterView, view, i, l ->

            selectedPartyID=CommonData.dropDownResponse.data.parties[i].id

        }

        (binding.inputAmountType.editText as MaterialAutoCompleteTextView).setAdapter(ArrayAdapter(
            requireContext(), android.R.layout.simple_list_item_1, CommonData.dropDownResponse.data.amount_type
        ))

        (binding.inputCollectionOf.editText as MaterialAutoCompleteTextView).setAdapter(ArrayAdapter(
            requireContext(), android.R.layout.simple_list_item_1, CommonData.dropDownResponse.data.collection_of
        ))

        (binding.inputPaymentDetails.editText as MaterialAutoCompleteTextView).setAdapter(ArrayAdapter(
            requireContext(), android.R.layout.simple_list_item_1, CommonData.dropDownResponse.data.payment_details
        ))

    }



    private fun setObserver() {
        viewModel.commonResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        progressDialog.dismiss()
                        response.data?.let {
                            if (it.success){
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

    private fun clickevents() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }


        binding.txtSubmit.setOnClickListener {
            if(isFormValid()){

                val addPaymentRequest=AddPaymentRequest(binding.inputDate.editText!!.text.toString(),selectedPartyID,
                    binding.inputAmount.editText!!.text.toString().toDouble(),binding.inputAmountType.editText!!.text.toString(),
                    binding.inputAmountDetails.editText!!.text.toString(),binding.inputCollectionOf.editText!!.text.toString(),
                    binding.inputPaymentDetails.editText!!.text.toString(),binding.inputExtra.editText!!.text.toString(), viewModel.currentPhotoPath!!)

                viewModel.addPayment(addPaymentRequest)
            }
        }

        binding.llUploadPhoto.setOnClickListener {
            takeImage()
        }

        binding.ivDelete.setOnClickListener {
            binding.llUploadPhoto.visibility=View.VISIBLE
            binding.rlPhoto.visibility=View.GONE
            viewModel.currentPhotoPath=""
        }

    }





    private fun isFormValid(): Boolean {
        with(binding) {

            if (inputParty.editText?.text.toString().isEmpty()) {
                toast(getString(R.string.alert_select_party_name))
                return false
            }

            else if (inputAmount.editText?.text.toString().isEmpty()) {
                toast("Please enter Amount")
                return false
            }
            else if (inputAmountType.editText?.text.toString().isEmpty()) {
                toast("Please select Amount Type")
                return false
            }

            else if (inputAmountDetails.editText?.text.toString().isEmpty()) {
                toast("Please enter Amount Details")
                return false
            }

            else if (inputCollectionOf.editText?.text.toString().isEmpty()) {
                toast("Please select Collection Of")
                return false
            }

            else if (inputPaymentDetails.editText?.text.toString().isEmpty()) {
                toast("Please select Payment Details")
                return false
            }


            else if (inputExtra.editText?.text.toString().isEmpty()) {
                toast("Please enter Extra")
                return false
            }
            else if (viewModel.currentPhotoPath.isNullOrEmpty()) {
                toast("Please upload photo")
                return false
            }
            return true
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun takeImage() {
        lifecycleScope.launch {
            viewModel.getTmpFileUri().let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri)
            }
        }
    }
    private var latestTmpUri: Uri? = null

    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->

                    binding.ivMeeterReadingPhoto.setImageURI(uri)
                    binding.rlPhoto.visibility=View.VISIBLE
                    binding.llUploadPhoto.visibility=View.GONE
                }
            }
        }
}