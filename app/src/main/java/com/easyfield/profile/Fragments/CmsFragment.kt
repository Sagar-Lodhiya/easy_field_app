package com.easyfield.profile.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.easyfield.R
import com.easyfield.base.BaseFragment
import com.easyfield.databinding.FragmentCmsBinding
import com.easyfield.utils.AppConstants


class CmsFragment : BaseFragment() {



    private var _binding: FragmentCmsBinding? = null




    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCmsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickevents()




    }
    fun clickevents(){

        val type=requireArguments().getInt("type")


        if(type==1){
            binding.wvLoadData.loadUrl(AppConstants.TERMS_CONDITION)
            binding.txtTitle.setText(resources.getString(R.string.term_amp_conditions))
        }
        else{
            binding.wvLoadData.loadUrl(AppConstants.PRIVACY_POLICY)
            binding.txtTitle.setText(resources.getString(R.string.privacy_policy))
        }




        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }







}