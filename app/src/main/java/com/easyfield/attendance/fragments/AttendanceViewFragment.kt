package com.easyfield.attendance.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.easyfield.R
import com.easyfield.attendance.response.attendancedetail.AttendanceDetailResponse
import com.easyfield.base.BaseFragment
import com.easyfield.databinding.FragmentViewAttendanceBinding
import com.easyfield.network.RetrofitClient
import com.bumptech.glide.Glide
import com.codeflixweb.callenza.network.Resource
import com.lezasolutions.callenza.utils.extension.toast


class AttendanceViewFragment : BaseFragment() {

    private var _binding: FragmentViewAttendanceBinding? = null
    private val binding get() = _binding!!
    lateinit var attendanceDetailResponse: AttendanceDetailResponse

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewAttendanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickevents()
        setObserver()
        val partyid=arguments?.getString("attendanceid")
        viewModel.attendanceDetail(partyid!!)
    }

    fun setObserver(){
        viewModel.attendanceDetailResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        progressDialog.dismiss()
                        response.data?.let {
                            if (it.status==200){
                                attendanceDetailResponse=it
                                setData()
                            }
                            else
                            {
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

    private fun setData() {

       with(binding){

           llContent.visibility=View.VISIBLE
           txtDAAmount.text=attendanceDetailResponse.data.analytics.da_amount
           txtTAAmount.text=attendanceDetailResponse.data.analytics.ta_amount
           txtTravelKM.text=attendanceDetailResponse.data.analytics.traveled_km
           txttotalhours.text=attendanceDetailResponse.data.analytics.total_hours

           Glide.with(requireActivity())
               .load(RetrofitClient.retrofitClientURL+attendanceDetailResponse.data.analytics.punch_in_image)
                .placeholder(R.drawable.no_img).error(R.drawable.no_img)
               .into(ivPunchInPhoto)

           txtPunchInTime.text=attendanceDetailResponse.data.analytics.punch_in_time
           txtPunchInAddress.text=attendanceDetailResponse.data.analytics.punch_in_place

           if(attendanceDetailResponse.data.analytics.punch_out_time.isEmpty()){
               llPunchOut.visibility=View.GONE
           }else{
               llPunchOut.visibility=View.VISIBLE
           }

           Glide.with(requireActivity())
               .load(RetrofitClient.retrofitClientURL+attendanceDetailResponse.data.analytics.punch_out_image)
               .placeholder(R.drawable.no_img).error(R.drawable.no_img)
               .into(ivPunchOutPhoto)

           txtPunchOutTime.text=attendanceDetailResponse.data.analytics.punch_out_time
           txtPunchOutAddress.text=attendanceDetailResponse.data.analytics.punch_out_place

       }
    }

    private fun clickevents() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}