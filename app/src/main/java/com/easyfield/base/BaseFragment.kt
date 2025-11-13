package com.easyfield.base



import android.Manifest
import android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.easyfield.R
import com.easyfield.utils.extension.alert
import com.easyfield.utils.extension.positiveButton
import com.easyfield.utils.extension.setProgressDialog
import com.easyfield.viewmodels.UserViewModel

import com.codeflixweb.callenza.network.Resource
import com.lezasolutions.callenza.utils.extension.toast
import com.permissionx.guolindev.PermissionX


open class BaseFragment : Fragment() {




    val viewModel: UserViewModel by viewModels()


    lateinit var progressDialog : Dialog




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog=setProgressDialog(requireContext())


        setAuthObserver()


    }

    private val requestMultiplePermissionsNew =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val allGranted = permissions.all { it.value } // Check if all are granted
            if (allGranted) {
                Toast.makeText(requireActivity(), "All permissions granted!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireActivity(), "Some permissions were denied.", Toast.LENGTH_SHORT).show()
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.w("axispermission","permission")


        PermissionX.init(requireActivity())
            .permissions(permissionsarraylist)
            .request { allGranted, grantedList, deniedList ->
                if (!allGranted) {
                    val stringBuilder=StringBuilder()


                    if (deniedList.contains("android.permission.CAMERA")){
                        stringBuilder.append("Camera Permission Required.\n")
                    }
                    else if (deniedList.contains("android.permission.READ_PHONE_STATE")) {
                        stringBuilder.append("Read Phone State Permission Required.\n")
                    }

                    else if (deniedList.contains("android.permission.POST_NOTIFICATIONS")) {
                        stringBuilder.append("Notification Permission Required.\n")
                    }
                    else if(deniedList.contains("android.permission.ACCESS_FINE_LOCATION") || deniedList.contains("android.permission.ACCESS_COARSE_LOCATION")){
                        stringBuilder.append("Location Permission Required.\n")
                    }
                    else{
                        stringBuilder.append("Write External Storage Permission Required")
                    }
                    requireContext().alert {
                        setMessage(stringBuilder.toString())
                        positiveButton(getString(R.string.str_exit)) {
                            requireActivity().finish()
                        }
                    }
                }
            }

//        if(!hasAllPermissions()){
//            requestMultiplePermissions.launch(permissions)
//        }
    }


    override fun onResume() {
        super.onResume()
    }
    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->

            Log.w("axispermission","called")


            permissions.entries.forEach {
                Log.w("axispermission","${it.key} = ${it.value}")

                when (it.key) {
                    Manifest.permission.CAMERA -> {
                        handleCameraPermission(it.value)
                    }

                    Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
                        handleWritePermission(it.value)
                    }

                    Manifest.permission.POST_NOTIFICATIONS -> {
                        handleWritePermission(it.value)
                    }

                    Manifest.permission.ACCESS_FINE_LOCATION -> {
                        handleLocationPermission(it.value)
                    }

                    Manifest.permission.READ_PHONE_STATE -> {
                        handleReadPhoneStatePermission(it.value)
                    }
                }
            }
        }
    fun setAuthObserver() {
        viewModel.authResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {

                    }

                    is Resource.Error -> {

                        progressDialog.dismiss()

                        toast("Unauthorized")


                        val act=activity as BaseActivity
                        act.doLogout()

                    }

                    is Resource.Loading -> {

                        progressDialog.show()

                    }

                    else -> {}
                }

            }
        }

    }


    private var permissionsarraylist: ArrayList<String> =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayListOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,ACCESS_BACKGROUND_LOCATION
            )
        }
        else{
            arrayListOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,ACCESS_BACKGROUND_LOCATION
            )
        }

    private var permissions: Array<String> =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        } else {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }

    private fun handleCameraPermission(isGranted: Boolean) {
        when {
            isGranted -> onCameraPermissionGranted()
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                //this option is available starting in API 23
                requireContext().alert {
                    setMessage(getString(R.string.alert_permission_required))
                    positiveButton(getString(R.string.str_retry)) {
                        requestMultiplePermissions.launch(permissions)
                    }
                }
            }
            else -> requireContext().alert {
                setMessage(getString(R.string.alert_permission_required))
                positiveButton(getString(R.string.str_exit)) {
                    requireActivity().finish()
                }
            }
        }

    }
    private fun handleWritePermission(isGranted: Boolean) {
        when {
            isGranted -> onWritePermissionGranted()
            shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                //this option is available starting in API 23
                requireContext().alert {
                    setMessage(getString(R.string.alert_permission_required))
                    positiveButton(getString(R.string.str_retry)) {
                        requestMultiplePermissions.launch(permissions)
                    }
                }
            }

            else -> requireContext().alert {
                setMessage(getString(R.string.alert_permission_required))
                positiveButton(getString(R.string.str_exit)) {
                    requireActivity().finish()
                }
            }
        }
    }

    private fun handleLocationPermission(isGranted: Boolean) {
        when {
            isGranted -> onLocationPermissionGranted()
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                //this option is available starting in API 23
                requireContext().alert {
                    setMessage(getString(R.string.alert_permission_required))
                    positiveButton(getString(R.string.str_retry)) {
                        requestMultiplePermissions.launch(permissions)
                    }
                }
            }

            else -> requireContext().alert {
                setMessage(getString(R.string.alert_permission_required))
                positiveButton(getString(R.string.str_exit)) {
                    requireActivity().finish()
                }
            }
        }
    }
    private fun handleReadPhoneStatePermission(isGranted: Boolean) {
        when {
            isGranted -> onLocationPermissionGranted()
            shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE) -> {
                //this option is available starting in API 23
                requireContext().alert {
                    setMessage(getString(R.string.alert_permission_required))
                }
            }

            else -> requireContext().alert {
                setMessage(getString(R.string.alert_permission_required))
                positiveButton(getString(R.string.str_exit)) {
                    requireActivity().finish()
                }
            }
        }
    }
    private fun hasAllPermissions(): Boolean = permissions.all { hasPermission(it) }

    private fun hasPermission(permission: String) =
        ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    open fun onCameraPermissionGranted() {}
    open fun onWritePermissionGranted() {}

    open fun onLocationPermissionGranted() {}


    private fun hasPermissions(): Boolean {


        return permissions.all {
            ContextCompat.checkSelfPermission(requireActivity(), it) == PackageManager.PERMISSION_GRANTED
        }
    }
}