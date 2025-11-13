package com.easyfield.payment.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.easyfield.R
import com.easyfield.base.BaseFragment
import com.easyfield.databinding.FragmentViewPaymentBinding
import com.easyfield.network.RetrofitClient
import com.easyfield.payment.response.paymentDetailresponse.PaymentDetailResponse
import com.easyfield.utils.CommonData
import com.easyfield.utils.extension.formatToViewDateDefaults
import com.bumptech.glide.Glide
import com.codeflixweb.callenza.network.Resource
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.lezasolutions.callenza.utils.extension.toast
import java.text.DecimalFormat
import java.util.Date


class PaymentDetailFragment : BaseFragment() {


    private var _binding: FragmentViewPaymentBinding? = null


    private val binding get() = _binding!!


    var selectedPartyID = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentViewPaymentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        clickevents()
        setObserver()

        setAdapters()

        val partyid = arguments?.getString("paymentid")

        viewModel.paymentDetail(partyid!!)


    }

    private fun setAdapters() {

        with(binding) {
            inputDate.editText?.setText(Date().formatToViewDateDefaults())
            inputDate.isEnabled = false
            inputParty.isEnabled = false
            inputAmount.isEnabled = false
            inputAmountType.isEnabled = false
            inputAmountDetails.isEnabled = false
            inputCollectionOf.isEnabled = false
            inputPaymentDetails.isEnabled = false
            inputExtra.isEnabled = false
        }


        (binding.inputParty.editText as MaterialAutoCompleteTextView).setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                CommonData.dropDownResponse.data.parties
            )
        )

        (binding.inputParty.editText as MaterialAutoCompleteTextView).setOnItemClickListener { adapterView, view, i, l ->

            selectedPartyID = CommonData.dropDownResponse.data.parties[i].id

        }

        (binding.inputAmountType.editText as MaterialAutoCompleteTextView).setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                CommonData.dropDownResponse.data.amount_type
            )
        )

        (binding.inputCollectionOf.editText as MaterialAutoCompleteTextView).setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                CommonData.dropDownResponse.data.collection_of
            )
        )

        (binding.inputPaymentDetails.editText as MaterialAutoCompleteTextView).setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                CommonData.dropDownResponse.data.payment_details
            )
        )

    }


    private fun setObserver() {
        viewModel.paymentDetailResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        progressDialog.dismiss()

                        response.data?.let {


                            if (it.status == 200) {


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

    private fun setData(it: PaymentDetailResponse) {
        with(binding) {
            inputDate.editText?.setText(
                it.data.created_at.toString().split("\\s+".toRegex())[0].toString()
            )
            inputParty.editText?.setText(it.data.party_name.toString())

            val df = DecimalFormat("###.#")

            inputAmount.editText?.setText(df.format(it.data.amount.toDouble()))
            inputAmountType.editText?.setText(it.data.amount_type.toString())
            inputAmountDetails.editText?.setText(it.data.amount_details.toString())
            inputCollectionOf.editText?.setText(it.data.collection_of.toString())
            inputPaymentDetails.editText?.setText(it.data.payments_details.toString())
            inputExtra.editText?.setText(it.data.extra.toString())

            val url = RetrofitClient.retrofitClientURL + it.data.payments_photo
            Log.w("vishalurl", "" + url)



            Glide.with(requireContext())
                .load(url)
                .placeholder(R.drawable.ic_profile)
                .into(ivMeeterReadingPhoto)

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