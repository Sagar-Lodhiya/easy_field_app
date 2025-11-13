package com.easyfield.party.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.easyfield.base.BaseFragment
import com.easyfield.databinding.FragmentViewPartyBinding
import com.easyfield.party.response.partydetailresponse.PartyDetailResponse
import com.easyfield.utils.CommonData
import com.codeflixweb.callenza.network.Resource
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.lezasolutions.callenza.utils.extension.toast


class PartyDetailFragment : BaseFragment() {



    private var _binding: FragmentViewPartyBinding? = null




    private val binding get() = _binding!!






    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentViewPartyBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        clickevents()
        setObserver()

        setAdapters()

        val partyid=arguments?.getString("partyid")



        viewModel.partyDetail(partyid!!)



    }
    private fun setAdapters() {

        with(binding){
            inputFirmName.isEnabled=false
            inputDealerName.isEnabled=false
            inputDealerCategory.isEnabled=false

            inputPhone.isEnabled=false
            inputCity.isEnabled=false
            inputAddress.isEnabled=false
            inputGstNo.isEnabled=false
            inputAadhar.isEnabled=false
        }


        (binding.inputDealerCategory.editText as MaterialAutoCompleteTextView).setAdapter(ArrayAdapter(
            requireContext(), android.R.layout.simple_list_item_1, CommonData.dropDownResponse.data.categories
        ))

    }



    private fun setObserver() {
        viewModel.partyDetailResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        progressDialog.dismiss()

                        response.data?.let {



                            if (it.status==200){


                                setdata(it)


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
    private fun setdata(it: PartyDetailResponse) {
        with(binding){
            inputFirmName.editText!!.setText(it.data.firm_name.toString())
            inputDealerName.editText!!.setText(it.data.dealer_name.toString())
            inputDealerCategory.editText!!.setText(it.data.party_category_name.toString())

            inputPhone.editText!!.setText(it.data.dealer_phone.toString())
            inputCity.editText!!.setText(it.data.city_or_town.toString())
            try {
                inputAddress.editText!!.setText(it.data.address.toString())
            }
            catch (e:Exception){

            }

            inputGstNo.editText!!.setText(it.data.gst_number.toString())
            inputAadhar.editText!!.setText(it.data.dealer_aadhar.toString())
        }

    }

    private fun clickevents() {


        binding.ivBack.setOnClickListener {

            findNavController().popBackStack()
        }
    }





    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}