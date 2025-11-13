package com.easyfield.attendance.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.util.Pair
import androidx.navigation.fragment.findNavController
import com.easyfield.base.BaseFragment
import com.easyfield.databinding.FragmentAddLeaveBinding
import com.easyfield.utils.CommonData
import com.codeflixweb.callenza.network.Resource
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.lezasolutions.callenza.utils.extension.toast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class AddLeaveFragment : BaseFragment() {



    private var _binding: FragmentAddLeaveBinding? = null




    private val binding get() = _binding!!

    var selectedindex=-1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddLeaveBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        clickevents()

        setVehicleType()

        setObserver()



    }
    fun setObserver(){
        viewModel.commonResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        progressDialog.dismiss()
                        response.data?.let {
                            if (it.success){
                                toast(it.message)
                                findNavController().popBackStack()
                            } else {
                                toast(it.message)
                            }
                        }
                    }

                    is Resource.Error -> {
                        progressDialog.dismiss()
                        toast(response.message.toString())
                    }

                    is Resource.Loading -> {
                        progressDialog.show()
                    }

                    else -> {}
                }

            }
        }
    }


    private fun setVehicleType() {

        val adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_list_item_1, CommonData.dropDownResponse.data.leave_types
        )
        (binding.inputVehicleType.editText as MaterialAutoCompleteTextView).setAdapter(adapter)

        (binding.inputVehicleType.editText as MaterialAutoCompleteTextView).setOnItemClickListener { adapterView, view, i, l ->


            selectedindex= CommonData.dropDownResponse.data.leave_types[i].id
        }
    }

    private fun clickevents() {

        binding.dateRange.setOnClickListener {
            DatePickerdialog()
        }

        binding.ivBack.setOnClickListener {

            findNavController().popBackStack()
        }

        binding.txtSubmit.setOnClickListener {


            if(isFormValid()){
                val date=binding.textinputdaterange.editText!!.text.toString().split("/")
                Log.w("vishaldate","date1  "+ date[0].trim())
                Log.w("vishaldate","date2  "+ date[1].trim())

                viewModel.addLeave(1, date[0].trim(), date[1].trim(),binding.inputRemarks.editText!!.text.toString())
            }




        }


    }
    private fun isFormValid(): Boolean {
        with(binding) {
            if (inputVehicleType.editText?.text.toString().isEmpty()) {
                toast("Please select Leave Type")
                return false
            } else if (textinputdaterange.editText?.text.toString().isEmpty()) {
                toast("Please Select Start Date and End Date")
                return false
            }
            else if (inputRemarks.editText?.text.toString().isEmpty()) {
                toast("Please Enter Remarks")
                return false
            }
            return true
        }
    }


    private fun DatePickerdialog() {
        // Creating a MaterialDatePicker builder for selecting a date range
        val builder: MaterialDatePicker.Builder<androidx.core.util.Pair<Long, Long>> =
            MaterialDatePicker.Builder.dateRangePicker()
        builder.setTitleText("Select a date range")


        // Building the date picker dialog
        val datePicker = builder.build()
        datePicker.addOnPositiveButtonClickListener { selection: Pair<Long, Long>? ->

            // Retrieving the selected start and end dates
            val startDate = selection!!.first
            val endDate = selection.second


            // Formating the selected dates as strings
            val sdf: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val startDateString: String = sdf.format(Date(startDate))
            val endDateString: String = sdf.format(Date(endDate))


            // Creating the date range string
            val selectedDateRange = "$startDateString / $endDateString"



            binding.dateRange.setText(selectedDateRange)


        }


        // Showing the date picker dialog
        datePicker.show(childFragmentManager, "DATE_PICKER")
    }

}