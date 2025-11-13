package com.easyfield.visit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.easyfield.base.BaseFragment
import com.easyfield.databinding.FragmentViewVisitBinding
import com.easyfield.utils.CommonData
import com.easyfield.utils.extension.showTimePicker
import com.easyfield.visit.response.visitdetail.VisitDetailResponse
import com.codeflixweb.callenza.network.Resource
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.lezasolutions.callenza.utils.extension.toast


class VisitDetailFragment : BaseFragment() {
    private var _binding: FragmentViewVisitBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentViewVisitBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        clickevents()
        setObserver()

        setAdapters()

        val id=arguments?.getString("visitid")

        viewModel.visitDetail(id!!)


    }
    private fun setAdapters() {

        with(binding){
            inputDate.isEnabled=false

            inputTime.isEnabled=false
            inputParty.isEnabled=false
            inputPlace.isEnabled=false
            inputDuration.isEnabled=false
            inputDiscussionPoint.isEnabled=false
            inputRemark.isEnabled=false

        }



        (binding.inputParty.editText as MaterialAutoCompleteTextView).setAdapter(ArrayAdapter(
            requireContext(), android.R.layout.simple_list_item_1, CommonData.dropDownResponse.data.parties
        ))

    }

    private fun setObserver() {
        viewModel.visitDetailResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        progressDialog.dismiss()

                        response.data?.let {



                            if (it.status==200){


                                setData(it)


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

    private fun setData(it: VisitDetailResponse) {
        with(binding){
            inputDate.editText?.setText(it.data.created_at.toString().split("\\s+".toRegex())[0].toString())
            inputTime.editText?.setText(it.data.time.toString())
            inputPlace.editText?.setText(it.data.place.toString())
            inputParty.editText?.setText(it.data.party_name.toString())

            inputDuration.editText?.setText(it.data.duration.toString())

            inputDiscussionPoint.editText?.setText(it.data.discussion_point.toString())
            inputRemark.editText?.setText(it.data.remarks.toString())

        }

    }

    private fun clickevents() {


        binding.ivBack.setOnClickListener {

            findNavController().popBackStack()
        }
        binding.inputTime.editText?.setOnClickListener {
            requireActivity().showTimePicker() { viewFormat, apiFormat ->
                binding.inputTime.editText?.setText(viewFormat)
                binding.inputTime.editText?.tag = apiFormat
            }
        }





    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}