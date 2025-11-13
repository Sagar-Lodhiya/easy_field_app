package com.easyfield.attendance.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.NumberPicker
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.easyfield.R
import com.easyfield.attendance.adapters.AttendanceListAdapter
import com.easyfield.attendance.response.attendancelist.AttendanceListResponse
import com.easyfield.base.BaseFragment
import com.easyfield.databinding.FragmentAttendancelistBinding
import com.easyfield.utils.ItemClickAdapter
import com.codeflixweb.callenza.network.Resource
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lezasolutions.callenza.utils.extension.toast
import java.util.Calendar


class AttendanceFragment : BaseFragment() {



    private var _binding: FragmentAttendancelistBinding? = null




    private val binding get() = _binding!!


    var selectedMonth=3
    var selectedYear=2025

    lateinit var attendanceListResponse:AttendanceListResponse

    val months = arrayOf(
        "January", "February", "March", "April", "May", "June", "July",
        "August", "September", "October", "November", "December"
    )


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAttendancelistBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickevents()
        setObserver()

        val calendar = Calendar.getInstance()
        val monthIndex = calendar.get(Calendar.MONTH)

        selectedMonth=monthIndex+1
        val currentYear = calendar.get(Calendar.YEAR)
        selectedYear=currentYear


        binding.txtMothYear.text = "${months[monthIndex].substring(0,3).toString()} , $currentYear"



        viewModel.attandanceList(selectedMonth,selectedYear)


    }

    private fun setObserver() {
        viewModel.attendanceListResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        progressDialog.dismiss()

                        response.data?.let {



                            if (it.status==200){


                                attendanceListResponse=it
                                setData()






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

    private fun setData()
    {
        binding.llContent.visibility=View.VISIBLE

        binding.txtTotalLeaves.text = attendanceListResponse.data.analytics.leaves.toString()
        binding.txtTotalAbsent.text = attendanceListResponse.data.analytics.absent.toString()
        binding.txtTotalPublicHolidays.text = attendanceListResponse.data.analytics.public_holiday.toString()
        binding.txtTotalWorkingDays.text = attendanceListResponse.data.analytics.working_days.toString()

        binding.rvAttendanceList.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL,false)
        binding.rvAttendanceList.setHasFixedSize(true)
        val categoryAdapter = AttendanceListAdapter(activity as Context,  attendanceListResponse.data.attendance,
            object : ItemClickAdapter {
                override fun onItemClick(pos: Int, from: String)
                {

                    if (attendanceListResponse.data.attendance[pos].type.equals("punch")){

                        val bundle=Bundle()
                        bundle.putString("attendanceid",attendanceListResponse.data.attendance[pos].id.toString())
                        findNavController().navigate(R.id.action_attendanceFragment_to_attendanceViewFragment,bundle)
                    }

                }
            })
        binding.rvAttendanceList.adapter = categoryAdapter
    }

    private fun showMonthYearPicker() {
        val dialog = BottomSheetDialog(requireActivity())
        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_month_year_picker, null)
        dialog.setContentView(view)

        val monthPicker = view.findViewById<NumberPicker>(R.id.monthPicker)
        val yearPicker = view.findViewById<NumberPicker>(R.id.yearPicker)
        val btnOk = view.findViewById<Button>(R.id.btnOk)
        val ivBack=view.findViewById<ImageView>(R.id.ivBack)

        // Month values

        monthPicker.minValue = 0
        monthPicker.maxValue = months.size - 1
        monthPicker.displayedValues = months

        // Year values
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        yearPicker.minValue = currentYear - 50
        yearPicker.maxValue = currentYear + 50
        yearPicker.value = currentYear

        btnOk.setOnClickListener {
            val Month = months[monthPicker.value]

            val Year = yearPicker.value
            val isSelectedIndex=isSelectedIndex(Month)

            selectedYear=Year
            selectedMonth=isSelectedIndex

            viewModel.attandanceList(selectedMonth,selectedYear)


            binding.txtMothYear.text = "${Month.substring(0,3).toString()} , $Year"
            dialog.dismiss()
        }

        ivBack.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


    private fun isSelectedIndex(value:String):Int {


        return when (value) {
            "January" -> 1
            "February" -> 2
            "March" -> 3
            "April" -> 4
            "May" -> 5
            "June" -> 6
            "July" -> 7
            "August" -> 8
            "September" -> 9
            "October" -> 10
            "November" -> 11
            "December" -> 12
            else -> 0
        }

    }

    private fun clickevents() {

        binding.txtMothYear.setOnClickListener {
            showMonthYearPicker()
        }

        binding.llLeaves.setOnClickListener {
            findNavController().navigate(R.id.action_attendanceFragment_to_leaveFragment)
        }
        binding.llAbsent.setOnClickListener {
            findNavController().navigate(R.id.action_attendanceFragment_to_leaveFragment)
        }
        binding.llPublicHoliday.setOnClickListener {
            findNavController().navigate(R.id.action_attendanceFragment_to_leaveFragment)
        }
        binding.llWorkingDays.setOnClickListener {
            findNavController().navigate(R.id.action_attendanceFragment_to_leaveFragment)
        }


        binding.ivAdd.setOnClickListener {
            findNavController().navigate(R.id.action_attendanceFragment_to_addLeaveFragment)
        }
    }




}