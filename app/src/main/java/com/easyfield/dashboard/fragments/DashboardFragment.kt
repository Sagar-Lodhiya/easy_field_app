package com.easyfield.dashboard.fragments

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.easyfield.R
import com.easyfield.base.BaseFragment
import com.easyfield.dashboard.response.HomeResponse
import com.easyfield.databinding.FragmentDashboardBinding
import com.easyfield.location.NewLocationService
import com.easyfield.network.RetrofitClient
import com.easyfield.utils.AppConstants
import com.easyfield.utils.CommonData
import com.easyfield.utils.PreferenceHelper
import com.bumptech.glide.Glide
import com.codeflixweb.callenza.network.Resource
import com.lezasolutions.callenza.utils.extension.toast


class DashboardFragment : BaseFragment() {


    private var _binding: FragmentDashboardBinding? = null

    lateinit var homeResponse: HomeResponse

    private val binding get() = _binding!!
    private var user_type: Int = 0
    private var punch_type:String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickevents()

        viewModel.home()
        setObserver()

        viewModel.dropdown()

//
//        lifecycleScope.launch(Dispatchers.IO) {
//            val db = AppDatabase.getInstance(requireContext())
//            val list = db.savelocationDao().getAllLocations()
//
////            Log.w("sendLocation","Service_Startedif${list.size}")
//
//            for (i in list){
//
//                val date = Date(i.timestamp)
//
//                Log.w("sendLocation","latitude${i.latitude} longitude ${i.longitude} date ${date}")
//            }
//        }


    }

    fun setObserver() {
        viewModel.homeResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        progressDialog.dismiss()

                        response.data?.let {


                            if (it.status == 200) {

                                homeResponse = it
                                setdata()


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
        viewModel.dropdownResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {

                        response.data?.let {


                            if (it.status == 200) {

                                CommonData.dropDownResponse = it

                            } else {
                                toast(it.message)
                            }
                        }
                    }

                    is Resource.Error -> {
                        toast(response.message.toString())
                    }

                    is Resource.Loading -> {


                    }

                    else -> {}
                }

            }
        }

//        setFragmentResultListener(PunchFragment.REQUEST_KEY_PUNCH_IN) { _, bundle ->
//            val isSuccessful = bundle.getBoolean(PunchFragment.PUNCH_RESULT)
//            if (isSuccessful) {
//                viewModel.getLastAttendance()
//            }
//        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun setdata() {

        binding.llContent.visibility = View.VISIBLE

        PreferenceHelper[AppConstants.PREF_KEY_ATTENDANCE_ID] =
            homeResponse.data.attendance.punch_in_id.toString()

        val sharedPreferences: SharedPreferences =
            requireActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("punch_id", homeResponse.data.attendance.punch_in_id.toString())

        editor.apply()

        punch_type = homeResponse.data.attendance.punch_type


//        Log.w("TagVIshal","punch_id_dashboard"+homeResponse.data.attendance.punch_in_id.toString())


        binding.txtUserName.setText(homeResponse.data.user.name + " , Good Morning")
        binding.txtDate.setText(homeResponse.data.user.date.split("\\s".toRegex())[0].toString())

        val url = RetrofitClient.retrofitClientURL + homeResponse.data.user.profile

        Glide.with(requireContext())
            .load(url)
            .placeholder(R.drawable.ic_profile)
            .into(binding.ivPhoto)


        if (homeResponse.data.attendance.is_on_leave) {
            binding.llPunchInOut.visibility = View.GONE
            binding.btnPunchIn.visibility = View.GONE
        } else {

            if (homeResponse.data.attendance.can_punch_in) {
                binding.llPunchInOut.visibility = View.VISIBLE
                binding.btnPunchIn.visibility = View.VISIBLE
                binding.btnPunchIn.setText(resources.getString(R.string.punch_in))
                binding.btnPunchIn.setTag("1")
            } else if (homeResponse.data.attendance.can_punch_out) {
                binding.llPunchInOut.visibility = View.VISIBLE
                binding.btnPunchIn.visibility = View.VISIBLE
                binding.btnPunchIn.setText(resources.getString(R.string.punch_out))
                binding.btnPunchIn.setTag("2")
            } else {
                binding.btnPunchIn.visibility = View.GONE
            }
            if (homeResponse.data.attendance.can_start_tracking) {
                binding.llPunchInOut.visibility = View.VISIBLE
                binding.btnTrip.visibility = View.VISIBLE
                binding.btnTrip.setText(resources.getString(R.string.start_trip))
                binding.btnTrip.setTag("1")
            } else if (homeResponse.data.attendance.can_stop_tracking) {
                binding.llPunchInOut.visibility = View.VISIBLE
                binding.btnTrip.visibility = View.VISIBLE
                binding.btnTrip.setText(resources.getString(R.string.stop_trip))
                binding.btnTrip.setTag("2")
            } else {
                binding.btnTrip.visibility = View.GONE
            }

            user_type = homeResponse.data.user.user_type
            PreferenceHelper.set(
                AppConstants.PREF_KEY_USER_TYPE,
                user_type
            )

        }

        if (homeResponse.data.attendance.punch_in_time.isNotEmpty() && homeResponse.data.attendance.punch_out_time.isEmpty() && user_type == 1) {
            requireActivity().startForegroundService(
                Intent(
                    requireContext(),
                    NewLocationService::class.java
                )
            )
            toast("location service started")
        } else if (homeResponse.data.attendance.punch_in_time.isNotEmpty() && homeResponse.data.attendance.punch_out_time.isNotEmpty() && user_type == 1) {
            requireActivity().stopService(
                Intent(
                    requireContext(),
                    NewLocationService::class.java
                )
            )
        }

        if (homeResponse.data.attendance.punch_in_time.isNotEmpty()) {
            binding.txtPunchIn.setText(homeResponse.data.attendance.punch_in_time.toString())
        } else {
            binding.txtPunchIn.setText("--:--")
        }

        if (homeResponse.data.attendance.punch_out_time.isNotEmpty()) {
            binding.txtPunchOut.setText(homeResponse.data.attendance.punch_out_time)
        } else {
            binding.txtPunchOut.setText("--:--")
        }

        if (homeResponse.data.analytics[0].title.isNotEmpty()) {
            binding.txtPendingExpenseTitle.setText(homeResponse.data.analytics[0].title)
        } else {
            binding.txtPendingExpenseTitle.setText("0")
        }

        binding.txtTodayVisitTitle.setText(homeResponse.data.analytics[1].title)
        binding.txtTravelKMTravel.setText(homeResponse.data.analytics[2].title)
        binding.txtWorkingDaysTitle.setText(homeResponse.data.analytics[3].title)

        if (homeResponse.data.analytics[0].value.isNotEmpty()) {
            binding.txtPendingExpense.setText(homeResponse.data.analytics[0].value)
        } else {
            binding.txtPendingExpense.setText("0")
        }


        if (homeResponse.data.analytics[1].value.isNotEmpty()) {
            binding.txtTodayVisit.setText(homeResponse.data.analytics[1].value)
        } else {
            binding.txtTodayVisit.setText("0")
        }

        if (homeResponse.data.analytics[2].value.isNotEmpty()) {
            binding.txtTravelKM.setText(homeResponse.data.analytics[2].value)
        } else {
            binding.txtTravelKM.setText("0")
        }

        if (homeResponse.data.analytics[3].value.isNotEmpty()) {
            binding.txtWorkingDays.setText(homeResponse.data.analytics[3].value)
        } else {
            binding.txtTravelKM.setText("0")
        }
    }


    private fun clickevents() {

        binding.ivNotification.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_notificationFragment)
        }

        binding.ivProfile.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_profileFragment)

        }

        binding.ivPhoto.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_profileFragment)
        }

        binding.btnPunchIn.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("type", binding.btnPunchIn.getTag().toString())
            bundle.putString("punch_type",punch_type)
            bundle.putString("from","punch_in")
            findNavController().navigate(R.id.action_dashboardFragment_to_punchInFragment, bundle)
        }

        binding.llExpense.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("status", "1")
            findNavController().navigate(
                R.id.action_dashboardFragment_to_expenseAllFragment,
                bundle
            )
        }

        binding.llVisit.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_visitFragment)
        }

        binding.btnTrip.setOnClickListener {
            if (homeResponse.data.attendance.punch_in_time.isNotEmpty() && homeResponse.data.attendance.punch_out_time.isEmpty() && user_type == 3 && binding.btnTrip.getTag().toString() == "1") {
                requireActivity().startForegroundService(
                    Intent(
                        requireContext(),
                        NewLocationService::class.java
                    )
                )
                toast("location service started")
            } else if (homeResponse.data.attendance.punch_in_time.isNotEmpty() && homeResponse.data.attendance.punch_out_time.isNotEmpty() && user_type == 3 && binding.btnTrip.getTag().toString() == "2") {
                requireActivity().stopService(
                    Intent(
                        requireContext(),
                        NewLocationService::class.java
                    )
                )
            }

            val bundle = Bundle()
            bundle.putString("type", binding.btnTrip.getTag().toString())
            bundle.putString("punch_type",punch_type)
            bundle.putString("from","tracking")
            findNavController().navigate(R.id.action_dashboardFragment_to_punchInFragment, bundle)
        }
    }


}