package com.easyfield.visit.fragments

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
import com.easyfield.utils.ItemClickAdapter
import com.easyfield.visit.adapters.VisitListAdapter
import com.easyfield.visit.response.visitlistresponse.VisitListResponse
import com.codeflixweb.callenza.network.Resource
import com.lezasolutions.callenza.utils.extension.toast


class VisitFragment : BaseFragment() {



    private var _binding: FragmentVisitBinding? = null




    private val binding get() = _binding!!

    lateinit var visitListResponse: VisitListResponse

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

        viewModel.visitList()


    }

    private fun setObserver() {
        viewModel.visitListResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        progressDialog.dismiss()

                        response.data?.let {



                            if (it.status==200){


                                visitListResponse=it
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


        if(visitListResponse.data.content.size>0){

            binding.rvExpenseList.layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL,false)
            binding.rvExpenseList.setHasFixedSize(true)
            val categoryAdapter = VisitListAdapter(activity as Context,  visitListResponse.data.content,
                object : ItemClickAdapter {
                    override fun onItemClick(pos: Int, from: String)
                    {
                        val bundle=Bundle()
                        bundle.putString("visitid",visitListResponse.data.content[pos].id.toString())

                        findNavController().navigate(R.id.action_visitFragment_to_visitDetailFragment,bundle)

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
            findNavController().navigate(R.id.action_visitFragment_to_addVisitFragment)
        }



    }


}