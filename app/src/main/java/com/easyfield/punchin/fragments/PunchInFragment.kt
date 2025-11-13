package com.easyfield.punchin.fragments

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.codeflixweb.callenza.network.Resource
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.easyfield.R
import com.easyfield.base.BaseFragment
import com.easyfield.databinding.FragmentPunchInBinding
import com.easyfield.punchin.request.PunchRequest
import com.easyfield.utils.AppConstants
import com.easyfield.utils.PreferenceHelper
import com.easyfield.utils.extension.getAddressString
import com.easyfield.utils.extension.hide
import com.easyfield.utils.extension.show
import com.lezasolutions.callenza.utils.extension.getDeviceBattery
import com.lezasolutions.callenza.utils.extension.getNetworkType
import com.lezasolutions.callenza.utils.extension.toast
import kotlinx.coroutines.launch
import java.util.Locale


class PunchInFragment : BaseFragment() {


    private var _binding: FragmentPunchInBinding? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient


    private val binding get() = _binding!!


    private var punchRequest: PunchRequest? = null

    var latitude: Double = 0.0
    var longitude: Double = 0.0

    var type = ""
    var punch_type = ""
    var from = ""
    var user_type: Int? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPunchInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        user_type = PreferenceHelper.get(AppConstants.PREF_KEY_USER_TYPE, 0)
        clickevents()
        getLastLocation()
        setVehicleType()
        setUserType()
        setObserver()
    }

    fun setObserver() {
        viewModel.punchInResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        progressDialog.dismiss()
                        response.data?.let {
                            if (it.status == 200) {
                                toast(it.message)
                                PreferenceHelper[AppConstants.PREF_KEY_ATTENDANCE_ID] =
                                    it.data.attendance_id.toString()
                                PreferenceHelper[AppConstants.PREF_KEY_PUNCH_IN_VEHICLE_TYPE] =
                                    binding.inputVehicleType.editText?.text.toString()
                                val sharedPreferences: SharedPreferences =
                                    requireActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE)
                                val editor = sharedPreferences.edit()
                                editor.putString("punch_id", it.data.attendance_id.toString())
                                editor.apply()
                                findNavController().popBackStack()
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

        viewModel.trackingResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        progressDialog.dismiss()
                        response.data?.let {
                            if (it.status == 200) {
                                toast(it.message)
                                PreferenceHelper[AppConstants.PREF_KEY_ATTENDANCE_ID] =
                                    it.data.attendance_id.toString()
                                PreferenceHelper[AppConstants.PREF_KEY_PUNCH_IN_VEHICLE_TYPE] =
                                    binding.inputVehicleType.editText?.text.toString()
                                val sharedPreferences: SharedPreferences =
                                    requireActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE)
                                val editor = sharedPreferences.edit()
                                editor.putString("punch_id", it.data.attendance_id.toString())
                                editor.apply()
                                findNavController().popBackStack()
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

    private fun clickevents() {

        type = requireArguments().getString("type").toString()
        punch_type = requireArguments().getString("punch_type").toString()
        from = requireArguments().getString("from").toString()


        if (type.equals("1")) {
            if (from.equals("tracking")) {
                binding.txtTitle.setText(resources.getString(R.string.start_tracking))
            } else {
                binding.txtTitle.setText(resources.getString(R.string.punch_in))
            }
            binding.switchNightStay.visibility = View.GONE

            binding.inputTourDetails.hide()
        } else {
            if (from.equals("tracking")) {
                binding.txtTitle.setText(resources.getString(R.string.stop_tracking))
            } else {
                binding.txtTitle.setText(resources.getString(R.string.punch_out))
            }
            binding.switchNightStay.visibility = View.VISIBLE

            val punchInVehicle: String? =
                PreferenceHelper[AppConstants.PREF_KEY_PUNCH_IN_VEHICLE_TYPE, "Bike"]
            punchInVehicle?.let {
                binding.inputVehicleType.editText?.setText(it)
                binding.inputVehicleType.isEnabled = false

                if (binding.inputVehicleType.editText?.text.toString() == getString(R.string.str_other)) {
                    binding.txtUploadImage.text =
                        getString(R.string.str_add_ticket_photo)
                    binding.inputMeterReading.hide()
                }
            }
        }

        binding.inputPlace.setEndIconOnClickListener {
            getLastLocation()
        }

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.ivDelete.setOnClickListener {
            binding.llUploadPhoto.visibility = View.VISIBLE
            binding.rlPhoto.visibility = View.GONE
            viewModel.currentPhotoPath = ""
        }

        binding.txtSubmit.setOnClickListener {
            if (user_type == 1) {
                if (isFormValid()) {
                    punchRequest = PunchRequest().apply {
                        this.lat = latitude
                        this.lng = longitude
                        this.place = binding.inputPlace.editText?.text.toString()
                        this.vehicleType = binding.inputVehicleType.editText?.text.toString()
                        if (!binding.inputMeterReading.editText?.text.toString().isNullOrEmpty()) {
                            this.meterReading =
                                binding.inputMeterReading.editText?.text.toString().toDouble()
                        }
                        this.battery = requireContext().getDeviceBattery().toString()
                        this.mobile_network = requireContext().getNetworkType()
                        this.meterReadingPhoto = viewModel.currentPhotoPath
                        this.tour_details = binding.inputTourDetails.editText?.text.toString()
                        this.punch_in_type = "S"
                        this.isNightStay = binding.switchNightStay.isChecked
                    }
                    viewModel.punchIn(punchRequest!!, type)
                    Log.e("SAGAR ", "PUNCH_IN  REQ: ==//==  "+punchRequest.toString() )

                }
            } else if (user_type == 2) {
                punchRequest = PunchRequest().apply {
                    this.lat = null
                    this.lng = null
                    this.place = null
                    this.vehicleType = null
                    this.meterReading = null
                    this.battery = null
                    this.mobile_network = null
                    this.meterReadingPhoto = null
                    this.tour_details = null
                    this.punch_in_type = "O"
                    this.isNightStay = null
                }
                toast(punchRequest.toString())
                viewModel.punchIn(punchRequest!!, type)
                Log.e("MP ", "PUNCH_IN  REQ: ==//==  "+punchRequest.toString() )
            } else if (user_type == 3) {
                if (isFormValid()) {
                    punchRequest = PunchRequest().apply {
                        this.lat = latitude
                        this.lng = longitude
                        this.place = binding.inputPlace.editText?.text.toString()
                        this.vehicleType = binding.inputVehicleType.editText?.text.toString()
                        if (!binding.inputMeterReading.editText?.text.toString().isNullOrEmpty()) {
                            this.meterReading =
                                binding.inputMeterReading.editText?.text.toString().toDouble()
                        }
                        this.battery = requireContext().getDeviceBattery().toString()
                        this.mobile_network = requireContext().getNetworkType()
                        this.meterReadingPhoto = viewModel.currentPhotoPath
                        this.tour_details = binding.inputTourDetails.editText?.text.toString()
                        this.punch_in_type = "S"
                        this.isNightStay = binding.switchNightStay.isChecked
                    }
                    toast(punchRequest.toString())
                    viewModel.tracking(punchRequest!!, type)
                    Log.e("MP ", "PUNCH_IN  REQ: ==//==  "+punchRequest.toString() )
                }
                Log.e("MP ", "PUNCH_IN  REQ: ==//==  "+punchRequest.toString() )
            }

        }

        binding.llUploadPhoto.setOnClickListener {
            takeImage()
        }


    }

    private fun isFormValid(): Boolean {
        with(binding) {
            if (inputPlace.editText?.text.toString().isEmpty()) {
                toast(getString(R.string.alert_enter_place))
                return false
            }

            if (inputVehicleType.editText?.text.toString().isEmpty()) {
                toast(getString(R.string.alert_select_type_of_vehicle))
                return false
            }

            if (inputVehicleType.editText?.text.toString() != getString(R.string.str_other)) {
                if (inputMeterReading.editText?.text.toString().isEmpty()) {
                    toast(getString(R.string.alert_enter_meter_reading))
                    return false
                }
            }


            if (inputVehicleType.editText?.text.toString() != getString(R.string.str_other)) {
                if (viewModel.currentPhotoPath.isNullOrEmpty()) {
                    toast(getString(R.string.alert_add_meter_reading_photo))
                    return false
                }
            }

//            if (selectUserType.editText?.text.toString() != "Sales" || selectUserType.editText?.text.toString() != "Office")
//            {
//                toast(getString(R.string.alert_select_user) + " // "+ selectUserType.editText?.text.toString()+"===")
//                return false
//            }

            return true
        }
    }


    private fun setVehicleType() {
        val list: ArrayList<String> = arrayListOf(
            getString(R.string.str_bike),
            getString(R.string.str_car),
            getString(R.string.str_other)
        )
        val adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_list_item_1, list
        )
        (binding.inputVehicleType.editText as MaterialAutoCompleteTextView).setAdapter(adapter)

        (binding.inputVehicleType.editText as MaterialAutoCompleteTextView).setOnItemClickListener { adapterView, view, i, l ->
            if (i == 2) { // Other/bus/train
                binding.txtUploadImage.text = getString(R.string.str_add_ticket_photo)
                binding.inputMeterReading.hide()
            } else { // Car/bike
                binding.inputMeterReading.show()
                binding.txtUploadImage.text =
                    getString(R.string.str_add_meter_reading_photo)
            }
        }
    }


    private fun setUserType() {
        val dropdown = binding.selectUserType.editText as MaterialAutoCompleteTextView
        val list: ArrayList<String> = arrayListOf(
            getString(R.string.str_sales),
            getString(R.string.str_office),
        )
        val adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_list_item_1, list
        )
        dropdown.setAdapter(adapter)

        if (user_type == 1) {
            binding.selectUserType.isEnabled = false
            dropdown.setText(list[0], false)
            binding.inputPlace.isEnabled = true
            binding.inputVehicleType.isEnabled = true
            binding.inputMeterReading.isEnabled = true
            binding.llUploadPhoto.isEnabled = true
            binding.inputTourDetails.isEnabled = true
            binding.switchNightStay.isEnabled = true
        } else if (user_type == 2) {
            dropdown.setText(list[1], false)
            binding.selectUserType.isEnabled = false
            binding.inputPlace.isEnabled = false
            binding.inputVehicleType.isEnabled = false
            binding.inputMeterReading.isEnabled = false
            binding.llUploadPhoto.isEnabled = false
            binding.inputTourDetails.isEnabled = false
            binding.switchNightStay.isEnabled = false
        } else if (user_type == 3 && from == "punch_in") {
            if (type == "2") {
                binding.selectUserType.isEnabled = false
                if (punch_type == "O") {
                    dropdown.setText(list[1], false)
                    binding.inputPlace.isEnabled = false
                    binding.inputVehicleType.isEnabled = false
                    binding.inputMeterReading.isEnabled = false
                    binding.llUploadPhoto.isEnabled = false
                    binding.inputTourDetails.isEnabled = false
                    binding.switchNightStay.isEnabled = false
                    user_type = 2
                } else if (punch_type == "S") {
                    dropdown.setText(list[0], false)
                    binding.inputPlace.isEnabled = true
                    binding.inputVehicleType.isEnabled = true
                    binding.inputMeterReading.isEnabled = true
                    binding.llUploadPhoto.isEnabled = true
                    binding.inputTourDetails.isEnabled = true
                    binding.switchNightStay.isEnabled = true
                    user_type = 1
                }
            } else if (type == "1") {
                binding.selectUserType.isEnabled = true
                binding.inputPlace.isEnabled = false
                binding.inputVehicleType.isEnabled = false
                binding.inputMeterReading.isEnabled = false
                binding.llUploadPhoto.isEnabled = false
                binding.inputTourDetails.isEnabled = false
                binding.switchNightStay.isEnabled = false
            }
        } else if (user_type == 3 && from == "tracking") {
            binding.selectUserType.visibility = View.GONE
            binding.inputPlace.isEnabled = true
            binding.inputVehicleType.isEnabled = true
            binding.inputMeterReading.isEnabled = true
            binding.llUploadPhoto.isEnabled = true
            binding.inputTourDetails.isEnabled = true
            binding.switchNightStay.isEnabled = true
            user_type = 3
        }

        dropdown.setOnItemClickListener { adapterView, view, i, l ->
            if (i == 0) { // Sales or S
                user_type = 1
                binding.inputPlace.isEnabled = true
                binding.inputVehicleType.isEnabled = true
                binding.inputMeterReading.isEnabled = true
                binding.llUploadPhoto.isEnabled = true
                binding.inputTourDetails.isEnabled = true
                binding.switchNightStay.isEnabled = true
            } else if (i == 1) { //office or O
                user_type = 2
                binding.inputPlace.isEnabled = false
                binding.inputVehicleType.isEnabled = false
                binding.inputMeterReading.isEnabled = false
                binding.llUploadPhoto.isEnabled = false
                binding.inputTourDetails.isEnabled = false
                binding.switchNightStay.isEnabled = false
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
//        fusedLocationClient.

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->


            location?.let {
                latitude = it.latitude
                longitude = it.longitude

                val address = it.getAddressString(requireContext())
                binding.inputPlace.editText!!.setText(address.toString())

//                getAddress(it.latitude, it.longitude)
            } ?: run {
                toast("Location not found")

            }
        }
    }

    private fun getAddress(lat: Double, lon: Double) {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        try {
            val addresses: List<Address> = geocoder.getFromLocation(lat, lon, 1)!!
            if (addresses.isNotEmpty()) {
                val address = addresses[0].getAddressLine(0)

                binding.inputPlace.editText!!.setText(address.toString())

            } else {
                toast("No address found")

            }
        } catch (e: Exception) {
            e.printStackTrace()
            toast("Geocoder failed")

        }
    }

    private fun takeImage() {
        lifecycleScope.launch {
            viewModel.getTmpFileUri().let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri)
            }
        }
    }

    private var latestTmpUri: Uri? = null

    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->

                    binding.ivMeeterReadingPhoto.setImageURI(uri)
                    binding.rlPhoto.visibility = View.VISIBLE
                    binding.llUploadPhoto.visibility = View.GONE
                }
            }
        }

}