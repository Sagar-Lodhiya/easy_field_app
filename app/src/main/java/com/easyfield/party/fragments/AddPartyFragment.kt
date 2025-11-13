package com.easyfield.party.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.easyfield.base.BaseFragment
import com.easyfield.databinding.FragmentAddPartyBinding
import com.easyfield.party.request.AddPartyRequest
import com.easyfield.utils.CommonData
import com.codeflixweb.callenza.network.Resource
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.lezasolutions.callenza.utils.extension.toast


class AddPartyFragment : BaseFragment() {
    private var _binding: FragmentAddPartyBinding? = null
    private val binding get() = _binding!!
    var selectedcategories=0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPartyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickevents()
        setObserver()
        setAdapters()
    }
    private fun setAdapters() {
        (binding.inputDealerCategory.editText as MaterialAutoCompleteTextView).setAdapter(ArrayAdapter(
            requireContext(), android.R.layout.simple_list_item_1, CommonData.dropDownResponse.data.categories
        ))

        (binding.inputDealerCategory.editText as MaterialAutoCompleteTextView).setOnItemClickListener { adapterView, view, i, l ->
            selectedcategories=CommonData.dropDownResponse.data.categories[i].id
        }
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
                val addPartyRequest=AddPartyRequest(binding.inputFirmName.editText!!.text.toString(),
                    binding.inputDealerName.editText!!.text.toString(),
                    selectedcategories,binding.inputPhone.editText!!.text.toString(),
                    binding.inputCity.editText!!.text.toString(),binding.inputAddress.editText!!.text.toString(),
                    binding.inputGstNo.editText!!.text.toString(),binding.inputAadhar.editText!!.text.toString()
                    )

                viewModel.addParty(addPartyRequest)
            }

        }
    }



    private fun isFormValid(): Boolean {
        with(binding) {


            if (inputFirmName.editText?.text.toString().isEmpty()) {
                toast("Please enter Firm Name")
                return false
            }
            else if (inputDealerName.editText?.text.toString().isEmpty()) {
                toast("Please enter Dealer Name")
                return false
            }

            else if (inputDealerCategory.editText?.text.toString().isEmpty()) {
                toast("Please select Dealer Categories")
                return false
            }

            else if (inputPhone.editText?.text.toString().isEmpty()) {
                toast("Please enter Contact Number")
                return false
            }

            else if (inputCity.editText?.text.toString().isEmpty()) {
                toast("Please enter City/Town")
                return false
            }

            else if (inputAddress.editText?.text.toString().isEmpty()) {
                toast("Please enter Address")
                return false
            }

            else if (inputGstNo.editText?.text.toString().isEmpty()) {
                toast("Please enter GST No")
                return false
            }
            else if (inputAadhar.editText?.text.toString().isEmpty()) {
                toast("Please enter Aadhar Number")
                return false
            }
            else if (inputAadhar.editText?.text.toString().length<12) {
                toast("Please enter valid Aadhar Number")
                return false
            }
            return true
        }
    }

//    private fun isFormValid(): Boolean {
//        with(binding) {
//
//            if (inputTime.editText?.text.toString().isEmpty()) {
//                toast("Please Choose Time")
//                return false
//            }
//
//            else if (inputPlace.editText?.text.toString().isEmpty()) {
//                toast(getString(R.string.alert_enter_place))
//                return false
//            }
//
//            else if (inputParty.editText?.text.toString().isEmpty()) {
//                toast(getString(R.string.alert_select_party_name))
//                return false
//            }
//
//            else if (inputDuration.editText?.text.toString().isEmpty()) {
//                toast(getString(R.string.alert_enter_time_duration))
//                return false
//            }
//
//            else if (inputDiscussionPoint.editText?.text.toString().isEmpty()) {
//                toast(getString(R.string.alert_enter_discussion_point))
//                return false
//            }
//
//
//            else if (inputRemark.editText?.text.toString().isEmpty()) {
//                toast(getString(R.string.alert_enter_remarks))
//                return false
//            }
//
//
//            return true
//        }
//    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}