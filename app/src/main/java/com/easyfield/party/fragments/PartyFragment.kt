package com.easyfield.party.fragments

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
import com.easyfield.party.adapters.PartyListAdapter
import com.easyfield.party.response.PartyListResponse
import com.easyfield.utils.ItemClickAdapter
import com.codeflixweb.callenza.network.Resource
import com.lezasolutions.callenza.utils.extension.toast


class PartyFragment : BaseFragment() {



    private var _binding: FragmentVisitBinding? = null




    private val binding get() = _binding!!

    lateinit var partyListResponse: PartyListResponse

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

        viewModel.partyList()


    }

    private fun setObserver() {
        viewModel.partyListResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        progressDialog.dismiss()

                        response.data?.let {



                            if (it.status==200){


                                partyListResponse=it
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


        if(partyListResponse.data.content.size>0){

            binding.rvExpenseList.layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL,false)
            binding.rvExpenseList.setHasFixedSize(true)
            val categoryAdapter = PartyListAdapter(activity as Context,  partyListResponse.data.content,
                object : ItemClickAdapter {
                    override fun onItemClick(pos: Int, from: String)
                    {

                        val bundle=Bundle()
                        bundle.putString("partyid",partyListResponse.data.content[pos].id.toString())
                        findNavController().navigate(R.id.action_partyFragment_to_partyDetailFragment,bundle)


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
        binding.txtTitle.setText(resources.getString((R.string.party)))

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }


        binding.ivAdd.setOnClickListener {
            findNavController().navigate(R.id.action_partyFragment_to_addPartyFragment)
        }



    }


}