package com.easyfield.expense.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.easyfield.R
import com.easyfield.base.BaseFragment
import com.easyfield.databinding.FragmentExpenselistBinding
import com.easyfield.expense.adapters.ExpenseListAdapter
import com.easyfield.expense.response.expenselist.ExpenseListResponse
import com.easyfield.utils.ItemClickAdapter
import com.codeflixweb.callenza.network.Resource
import com.lezasolutions.callenza.utils.extension.toast


class ExpenseFragment : BaseFragment() {



    private var _binding: FragmentExpenselistBinding? = null




    private val binding get() = _binding!!

    lateinit var expenseListResponse: ExpenseListResponse

    var type="Claimed"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentExpenselistBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickevents()

        setObserver()

        viewModel.expenseList(type)


    }

    private fun setObserver() {
        viewModel.expenseListResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        progressDialog.dismiss()

                        response.data?.let {



                            if (it.status==200){


                                expenseListResponse=it
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


        if(expenseListResponse.data.content.size>0){

            binding.rvExpenseList.layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL,false)
            binding.rvExpenseList.setHasFixedSize(true)
            val categoryAdapter = ExpenseListAdapter(activity as Context,  expenseListResponse.data.content,
                object : ItemClickAdapter {
                    override fun onItemClick(pos: Int, from: String)
                    {

                        if(type.equals("Claimed")){
                            val bundle=Bundle()
                            bundle.putString("expenseid",expenseListResponse.data.content[pos].id.toString())
                            findNavController().navigate(R.id.action_expenseFragment_to_expenseDetailFragment,bundle)
                        }
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

        binding.llClaimed.setOnClickListener {

            if(binding.llClaimed.tag.equals("0"))
            {

                binding.llFixed.setBackgroundResource(R.drawable.rounded_button_white)
                binding.llClaimed.setBackgroundResource(R.drawable.rounded_button_dashboard)

                binding.llClaimed.tag="1"
                binding.llFixed.tag="0"


                binding.txtClaimed.setTextColor(resources.getColor(R.color.white))
                binding.txtFixed.setTextColor(resources.getColor(R.color.black))
                type="Claimed"

                viewModel.expenseList(type)
            }

        }

        binding.llFixed.setOnClickListener {
            if(binding.llFixed.tag.equals("0")){

                binding.llFixed.setBackgroundResource(R.drawable.rounded_button_dashboard)
                binding.llClaimed.setBackgroundResource(R.drawable.rounded_button_white)


                binding.txtFixed.setTextColor(resources.getColor(R.color.white))
                binding.txtClaimed.setTextColor(resources.getColor(R.color.black))

                binding.llFixed.tag="1"
                binding.llClaimed.tag="0"


                type="Fixed"

                viewModel.expenseList(type)

            }
        }

        binding.ivAdd.setOnClickListener {
            findNavController().navigate(R.id.action_expenseFragment_to_addExpenseFragment)
        }



    }


}