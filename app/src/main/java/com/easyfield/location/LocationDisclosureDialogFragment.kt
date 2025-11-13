package com.easyfield.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.easyfield.R

import com.easyfield.databinding.DialogFragmentLocationDisclosureBinding

class LocationDisclosureDialogFragment(
    private val disclosureEventListener: DisclosureEventListener
) : DialogFragment() {

    private var _binding: DialogFragmentLocationDisclosureBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        isCancelable = false
        _binding = DialogFragmentLocationDisclosureBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme_Fullscreen
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnAllowPermission.setOnClickListener {
            dismiss()
            disclosureEventListener.onClickAllowPermission()
        }

        binding.btnClose.setOnClickListener {
            dismiss()
            disclosureEventListener.onClickCloseApp()
        }
    }

    interface DisclosureEventListener {
        fun onClickAllowPermission()
        fun onClickCloseApp()
    }
}