package com.easyfield.visit.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.NumberPicker
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.easyfield.R
import com.easyfield.base.BaseFragment
import com.easyfield.databinding.FragmentAddVisitBinding
import com.easyfield.utils.CommonData
import com.easyfield.utils.extension.formatToViewDateDefaults
import com.easyfield.utils.extension.showTimePicker
import com.easyfield.visit.request.AddVisitRequest
import com.codeflixweb.callenza.network.Resource
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.lezasolutions.callenza.utils.extension.toast
import java.util.Date
import java.util.Locale


class AddVisitFragment : BaseFragment() {



    private var _binding: FragmentAddVisitBinding? = null




    private val binding get() = _binding!!


    var selectedPartyID=0

    var latitude:Double= 0.0
    var longitude:Double= 0.0

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddVisitBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        clickevents()
        setObserver()

        setAdapters()

        getLastLocation()
    }
    private fun setAdapters() {

        with(binding){
            inputDate.editText?.setText(Date().formatToViewDateDefaults())
        }


        (binding.inputParty.editText as MaterialAutoCompleteTextView).setAdapter(ArrayAdapter(
            requireContext(), android.R.layout.simple_list_item_1, CommonData.dropDownResponse.data.parties
        ))

        (binding.inputParty.editText as MaterialAutoCompleteTextView).setOnItemClickListener { adapterView, view, i, l ->

            selectedPartyID=CommonData.dropDownResponse.data.parties[i].id

        }
    }



    private fun setObserver() {
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

        binding.inputPlace.setEndIconOnClickListener {
            getLastLocation()
        }

        binding.ivBack.setOnClickListener {

            findNavController().popBackStack()
        }



        binding.inputTime.editText?.setOnClickListener {
            requireActivity().showTimePicker() { viewFormat, apiFormat ->
                binding.inputTime.editText?.setText(viewFormat)
                binding.inputTime.editText?.tag = apiFormat
            }
        }

        binding.inputDuration.editText?.setOnClickListener {
            showMonthYearPicker()
        }

        binding.txtSubmit.setOnClickListener {

            if(isFormValid()){

                val addVisitRequest = AddVisitRequest().apply {

                    created_at=binding.inputDate.editText!!.text.toString()
                    time=binding.inputTime.editText!!.text.toString()
                    place=binding.inputPlace.editText!!.text.toString()
                    partyId= selectedPartyID.toString()
                    duration=binding.inputDuration.editText!!.text.toString()
                    discussionPoint=binding.inputDiscussionPoint.editText!!.text.toString()
                    remark=binding.inputRemark.editText!!.text.toString()
                    lat=latitude
                    long=longitude

                    toast("points "+discussionPoint)
                }

                viewModel.addVisit(addVisitRequest)
            }

        }


    }

    private fun getLastLocation() {
    if (ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
        return
    }
    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
        location?.let {
            latitude=it.latitude
            longitude=it.longitude

            getAddress(it.latitude, it.longitude)
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

    private fun showMonthYearPicker() {
        val dialog = BottomSheetDialog(requireActivity())
        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_duration_picker, null)
        dialog.setContentView(view)

        val monthPicker = view.findViewById<NumberPicker>(R.id.monthPicker)
        val yearPicker = view.findViewById<NumberPicker>(R.id.yearPicker)
        val btnOk = view.findViewById<Button>(R.id.btnOk)
        val ivBack=view.findViewById<ImageView>(R.id.ivBack)

        // Month values
        val hours = arrayOf(
            "00", "01", "02", "03", "04", "05", "06","07","08","09","10",
            "11", "12", "13", "14", "15", "16","17","18","19","20","21","22","23"
        )
        val minitues = arrayOf(
            "00", "01", "02", "03", "04", "05", "06","07","08","09","10",
            "11", "12", "13", "14", "15", "16","17","18","19","20","21","22","23","24","25","26","27","28","29","30",
            "31,32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50,51","52","53","54","55","56","57","58","59"
        )







        monthPicker.minValue = 0
        monthPicker.maxValue = hours.size - 1
        monthPicker.displayedValues = hours


        yearPicker.minValue = 0
        yearPicker.maxValue = minitues.size - 1
        yearPicker.displayedValues = minitues



        btnOk.setOnClickListener {
            val Month = hours[monthPicker.value]

            val Year = minitues[yearPicker.value]


            val monthFormat= Month +" Hour "+Year+" Minute"

            binding.inputDuration.editText!!.setText(monthFormat)

            dialog.dismiss()
        }
        ivBack.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
    private fun isFormValid(): Boolean {
        with(binding) {

            if (inputTime.editText?.text.toString().isEmpty()) {
                toast("Please Choose Time")
                return false
            }

            else if (inputPlace.editText?.text.toString().isEmpty()) {
                toast(getString(R.string.alert_enter_place))
                return false
            }

            else if (inputParty.editText?.text.toString().isEmpty()) {
                toast(getString(R.string.alert_select_party_name))
                return false
            }

            else if (inputDuration.editText?.text.toString().isEmpty()) {
                toast(getString(R.string.alert_enter_time_duration))
                return false
            }

            else if (inputDiscussionPoint.editText?.text.toString().isEmpty()) {
                toast(getString(R.string.alert_enter_discussion_point))
                return false
            }


            else if (inputRemark.editText?.text.toString().isEmpty()) {
                toast(getString(R.string.alert_enter_remarks))
                return false
            }


            return true
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}