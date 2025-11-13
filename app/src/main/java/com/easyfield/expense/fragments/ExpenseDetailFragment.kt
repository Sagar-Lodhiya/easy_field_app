package com.easyfield.expense.fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.easyfield.R
import com.easyfield.base.BaseFragment
import com.easyfield.databinding.FragmentViewExpenseBinding
import com.easyfield.expense.response.expensedetail.ExpenseDetailResponse
import com.easyfield.network.RetrofitClient
import com.easyfield.utils.CommonData
import com.easyfield.utils.extension.visible
import com.bumptech.glide.Glide
import com.codeflixweb.callenza.network.Resource
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.lezasolutions.callenza.utils.extension.toast
import java.text.DecimalFormat


class ExpenseDetailFragment : BaseFragment() {



    private var _binding: FragmentViewExpenseBinding? = null




    private val binding get() = _binding!!

    var strPhotoPath=""
    var selectedCity=0
    var selectedExpenseType=0

    private var latestTmpUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentViewExpenseBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        clickevents()
        setObserver()

        setAdapters()

        val partyid=arguments?.getString("expenseid")

        viewModel.expenseDetail(partyid!!)

    }
    private fun setAdapters() {
        with(binding){
            inputDate.isEnabled=false
            inputExpenseDetail.isEnabled=false
            inputExpense.isEnabled =false
            inputExpenseAmount.isEnabled=false
            inputPlace.isEnabled=false
            ivPhoto.visible()


        }

        (binding.inputPlace.editText as MaterialAutoCompleteTextView).setAdapter(ArrayAdapter(
            requireContext(), android.R.layout.simple_list_item_1, CommonData.dropDownResponse.data.cities
        ))

        (binding.inputPlace.editText as MaterialAutoCompleteTextView).setOnItemClickListener { adapterView, view, i, l ->

            selectedCity=CommonData.dropDownResponse.data.cities[i].id

        }

        (binding.inputExpense.editText as MaterialAutoCompleteTextView).setAdapter(ArrayAdapter(
            requireContext(), android.R.layout.simple_list_item_1, CommonData.dropDownResponse.data.expense_categories
        ))

        (binding.inputExpense.editText as MaterialAutoCompleteTextView).setOnItemClickListener { adapterView, view, i, l ->

            selectedExpenseType=CommonData.dropDownResponse.data.expense_categories[i].id

        }
    }

    private fun setObserver() {
        viewModel.expenseDetailResponse.observe(viewLifecycleOwner) { event ->
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

    private fun setData(it: ExpenseDetailResponse) {
        val df = DecimalFormat("###.#")
        with(binding){
            inputDate.editText?.setText(it.data.expense_date.toString())
            inputExpenseAmount.editText?.setText(df.format(it.data.requested_amount.toDouble()))
            inputExpenseDetail.editText?.setText(it.data.expense_details.toString())
            inputExpense.editText?.setText(it.data.category_name.toString())
            inputPlace.editText?.setText(it.data.city_name.toString())

            val url= RetrofitClient.retrofitClientURL+it.data.expense_photo
            Log.w("vishalurl",""+url)


            Glide.with(requireContext())
                .load(url)
                .placeholder(R.drawable.ic_profile)
                .into(ivPhoto)
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