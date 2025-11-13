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
import com.easyfield.databinding.FragmentAllexpenselistBinding
import com.easyfield.expense.adapters.ExpenseListAdapter
import com.easyfield.expense.response.expenselist.ExpenseListResponse
import com.easyfield.utils.ItemClickAdapter
import com.codeflixweb.callenza.network.Resource
import com.lezasolutions.callenza.utils.extension.toast


class ExpenseAllFragment : BaseFragment() {



    private var _binding: FragmentAllexpenselistBinding? = null




    private val binding get() = _binding!!

    lateinit var expenseListResponse: ExpenseListResponse

    var statusID=""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllexpenselistBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickevents()

        setObserver()

        try {
            statusID= requireArguments().getString("status").toString()
        }
        catch (e:Exception){
        }
        viewModel.expenseList("", statusID)
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

                        if(expenseListResponse.data.content[pos].type.equals("claimed")){
                            val bundle=Bundle()
                            bundle.putString("expenseid",expenseListResponse.data.content[pos].id.toString())
                            findNavController().navigate(R.id.action_expenseAllFragment_to_expenseDetailFragment,bundle)
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

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }


        binding.ivAdd.setOnClickListener {
            findNavController().navigate(R.id.action_expenseAllFragment_to_addExpenseFragment)
        }



    }


}