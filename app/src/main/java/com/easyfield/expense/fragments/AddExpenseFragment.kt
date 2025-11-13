package com.easyfield.expense.fragments

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.easyfield.R
import com.easyfield.base.BaseFragment
import com.easyfield.databinding.FragmentAddExpenseBinding
import com.easyfield.utils.CommonData
import com.easyfield.utils.extension.alert
import com.easyfield.utils.extension.items
import com.easyfield.utils.extension.negativeButton
import com.easyfield.utils.extension.visible
import com.codeflixweb.callenza.network.Resource
import com.easyfield.utils.extension.hideKeyboard
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.lezasolutions.callenza.utils.extension.getRealPath
import com.lezasolutions.callenza.utils.extension.toast
import kotlinx.coroutines.launch
import java.util.Calendar


class AddExpenseFragment : BaseFragment() {



    private var _binding: FragmentAddExpenseBinding? = null




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

        _binding = FragmentAddExpenseBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        clickevents()
        setObserver()

        setAdapters()

        binding.inputDate.editText?.setOnClickListener {
            showDatePicker()
        }

    }
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireActivity(), { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                binding.inputDate.editText?.setText(selectedDate)

            }, year, month, day
        )
        datePickerDialog.show()
    }

    private fun setAdapters() {

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
        viewModel.commonResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        progressDialog.dismiss()

                        response.data?.let {



                            if (it.status==200){


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
    private fun hideKeyboard(activity: Activity) {
        val view = activity.currentFocus ?: View(activity)
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }

    private fun isKeyboardVisible(activity: Activity): Boolean {
        val rootView = activity.window.decorView
        val rect = Rect()
        rootView.getWindowVisibleDisplayFrame(rect)
        val screenHeight = rootView.height
        val keypadHeight = screenHeight - rect.bottom
        return keypadHeight > screenHeight * 0.15 // threshold for visibility
    }

    override fun onResume() {
        super.onResume()

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val activity = requireActivity()

                if (isKeyboardVisible(requireActivity())) {
                    hideKeyboard(requireActivity())
                } else {
                    // ✅ disable callback first to avoid recursion
                    isEnabled = false
                    activity.onBackPressedDispatcher.onBackPressed()
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
    private fun clickevents() {


        binding.llUploadPhoto.setOnClickListener {
            showDialog()
        }

        binding.ivBack.setOnClickListener {

            findNavController().popBackStack()
        }

        binding.txtSubmit.setOnClickListener {

            if(isFormValid()){
                viewModel.addExpense(selectedCity,selectedExpenseType,binding.inputExpenseAmount.editText!!.text.toString(),
                    binding.inputExpenseDetail.editText!!.text.toString(),"Claimed",strPhotoPath,
                    binding.inputDate.editText?.text.toString())
            }

        }


    }

    private fun isFormValid(): Boolean {
        with(binding) {

            if (inputDate.editText?.text.toString().isEmpty()) {
                toast("Please Choose Date")
                return false
            }

            else if (inputExpenseDetail.editText?.text.toString().isEmpty()) {
                toast("Please Enter Expense Detail")
                return false
            }

            if (selectedExpenseType==0) {
                toast("Please Choose Expense")
                return false
            }

            if (inputExpenseAmount.editText?.text.toString().isEmpty()) {
                toast("Please Enter Amount")
                return false
            }

            if (selectedCity==0) {
                toast("Please Enter City")
                return false
            }

            if (strPhotoPath.isEmpty()) {
                toast("Please Choose  Photo")
                return false
            }

            return true
        }
    }
    private fun showDialog() {
        requireContext().alert(R.style.AlertDialogTheme) {
            items(
                arrayOf(
                    getString(R.string.str_take_photo),
                    getString(R.string.str_choose_from_library)
                )
            ) { _, which ->
                when (which) {
                    0 -> takeImage()
                    1 -> pickImage()
                }
            }
            negativeButton(getString(R.string.str_cancel)) {
                it.dismiss()
            }
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
    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->

                    strPhotoPath= viewModel.currentPhotoPath.toString()
                    binding.ivPhoto.setImageURI(uri)
                    binding.ivPhoto.visible()

                }
            }
        }
    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            strPhotoPath= uri.getRealPath(requireContext()).toString()
            binding.ivPhoto.setImageURI(uri)
            binding.ivPhoto.visible()
        }

    }
    private fun pickImage() {
        lifecycleScope.launch {
            pickImage.launch("image/*")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}