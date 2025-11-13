package com.easyfield.menu.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.easyfield.R
import com.easyfield.base.BaseFragment
import com.easyfield.databinding.FragmentMenusBinding
import com.easyfield.menu.response.MenuResponse
import com.codeflixweb.callenza.network.Resource
import com.lezasolutions.callenza.utils.extension.formatAmount
import com.lezasolutions.callenza.utils.extension.toast


class MenusFragment : BaseFragment() {



    private var _binding: FragmentMenusBinding? = null




    private val binding get() = _binding!!

    lateinit var menuResponse: MenuResponse

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMenusBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickevents()

        viewModel.menu()
        setObserver()


    }
    fun setObserver(){
        viewModel.menuResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        progressDialog.dismiss()

                        response.data?.let {



                            if (it.status==200){

                                menuResponse=it
                                setdata()



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

    private fun setdata() {
        with(binding){
            llContent.visibility=View.VISIBLE

            if(menuResponse.data.expense.toString().equals("0")){
                txtExpenses.setText(menuResponse.data.expense.toString())
            }
            else{
                txtExpenses.setText(formatAmount(menuResponse.data.expense.toDouble()))
            }

            if(menuResponse.data.payment.toString().equals("0")){
                txtPayment.setText(menuResponse.data.payment.toString())
            }
            else{
                txtPayment.setText(formatAmount(menuResponse.data.payment.toDouble()))
            }


            txtParty.setText(menuResponse.data.parties.toString())
           
            txtVisit.setText(menuResponse.data.visit.toString())

        }

    }

    private fun clickevents() {

        with(binding){
            llVisit.setOnClickListener{

                findNavController().navigate(R.id.action_menusFragment_to_visitFragment)
            }
            llParty.setOnClickListener {

                findNavController().navigate(R.id.action_menusFragment_to_partyFragment)
            }
            llPayment.setOnClickListener {

                findNavController().navigate(R.id.action_menusFragment_to_paymentFragment)
            }

            llAllexpense.setOnClickListener {

                findNavController().navigate(R.id.action_menusFragment_to_expenseAllFragment)
            }
            llTotalVisit.setOnClickListener {

                findNavController().navigate(R.id.action_menusFragment_to_visitFragment)

            }
            llTotalPayment.setOnClickListener {
                findNavController().navigate(R.id.action_menusFragment_to_paymentFragment)
            }
            llTotalParties.setOnClickListener {
                findNavController().navigate(R.id.action_menusFragment_to_partyFragment)
            }
        }


    }


}