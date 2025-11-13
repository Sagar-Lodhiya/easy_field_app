package com.easyfield.payment.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.easyfield.R
import com.easyfield.base.BaseFragment
import com.easyfield.databinding.FragmentVisitBinding
import com.easyfield.payment.adapters.PaymentListAdapter
import com.easyfield.payment.response.PaymentListResponse
import com.easyfield.utils.ItemClickAdapter
import com.codeflixweb.callenza.network.Resource
import com.lezasolutions.callenza.utils.extension.toast


class PaymentFragment : BaseFragment() {



    private var _binding: FragmentVisitBinding? = null




    private val binding get() = _binding!!

    lateinit var paymentListResponse: PaymentListResponse

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentVisitBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickevents()

        setObserver()

        viewModel.paumentList()


    }

    private fun setObserver() {
        viewModel.paymentListResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        progressDialog.dismiss()

                        response.data?.let {



                            if (it.status==200){


                                paymentListResponse=it
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
    }

    private fun setData() {


        if(paymentListResponse.data.content.size>0){

            binding.rvExpenseList.layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL,false)
            binding.rvExpenseList.setHasFixedSize(true)
            val categoryAdapter = PaymentListAdapter(activity as Context,  paymentListResponse.data.content,
                object : ItemClickAdapter {
                    override fun onItemClick(pos: Int, from: String)
                    {
                        val bundle=Bundle()
                        bundle.putString("paymentid",paymentListResponse.data.content[pos].id.toString())
                        findNavController().navigate(R.id.action_paymentFragment_to_paymentDetailFragment,bundle)

                    }
                })
            binding.rvExpenseList.adapter = categoryAdapter

            binding.rvExpenseList.visibility=View.VISIBLE
            binding.ivNoData.visibility=View.GONE

        }
        else{
            binding.rvExpenseList.visibility=View.GONE
            binding.ivNoData.visibility=View.VISIBLE
        }


    }

    private fun clickevents() {
        binding.txtTitle.setText(resources.getString(R.string.payment))

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }


        binding.ivAdd.setOnClickListener {
            findNavController().navigate(R.id.action_paymentFragment_to_addPaymentFragment)
        }



    }


}