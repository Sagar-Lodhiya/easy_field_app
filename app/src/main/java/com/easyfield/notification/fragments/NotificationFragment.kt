package com.easyfield.notification.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.easyfield.R
import com.easyfield.base.BaseFragment
import com.easyfield.databinding.FragmentNotificationBinding
import com.easyfield.notification.adapters.NotificationListAdapter
import com.easyfield.notification.response.NotificationListResponse
import com.easyfield.utils.ItemClickAdapter
import com.easyfield.utils.extension.alert
import com.easyfield.utils.extension.negativeButton
import com.easyfield.utils.extension.positiveButton
import com.codeflixweb.callenza.network.Resource
import com.lezasolutions.callenza.utils.extension.toast


class NotificationFragment : BaseFragment() {


    private var _binding: FragmentNotificationBinding? = null


    private val binding get() = _binding!!

    var deletedPosition = -1


    private lateinit var notificationListResponse: NotificationListResponse

    lateinit var notificationListAdapter: NotificationListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickevents()


        setObserver()


        viewModel.notificationList()


    }

    private fun setObserver() {
        
        viewModel.notificationListResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        progressDialog.dismiss()
                        response.data?.let {
                            if (it.status == 200) {
                                notificationListResponse = it
                                setData()
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
        viewModel.commonResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        progressDialog.dismiss()

                        response.data?.let {


                            if (it.status == 200) {

                                if (deletedPosition != -1) {

                                    notificationListAdapter.removeAt(deletedPosition)
                                    deletedPosition = -1

                                    if (notificationListAdapter.itemCount == 0) {
                                        binding.ivNoData.visibility = View.VISIBLE
                                        binding.rvAttendanceList.visibility = View.GONE
                                    } else {
                                        binding.ivNoData.visibility = View.GONE
                                        binding.rvAttendanceList.visibility = View.VISIBLE
                                    }
                                } else {
                                    binding.ivNoData.visibility = View.VISIBLE
                                    binding.rvAttendanceList.visibility = View.GONE
                                    binding.txtClearAll.visibility = View.GONE
                                }


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

    private fun setData() {
        if (notificationListResponse.data.content.isNotEmpty()) {
            binding.ivNoData.visibility = View.GONE
            binding.rvAttendanceList.visibility = View.VISIBLE
        } else {
            binding.ivNoData.visibility = View.VISIBLE
            binding.rvAttendanceList.visibility = View.GONE
            binding.txtClearAll.visibility = View.GONE
        }
        binding.rvAttendanceList.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL, false
        )
        binding.rvAttendanceList.setHasFixedSize(true)
        notificationListAdapter = NotificationListAdapter(
            activity as Context, notificationListResponse.data.content,
            object : ItemClickAdapter {
                override fun onItemClick(pos: Int, from: String) {
                    requireContext().alert(R.style.AlertDialogTheme) {
                        setTitle(getString(R.string.notifications))
                        setMessage(getString(R.string.alert_msg_notification))
                        negativeButton(getString(R.string.str_no)) {
                            it.dismiss()
                        }
                        positiveButton(getString(R.string.str_yes)) {

                            deletedPosition = pos
                            viewModel.clearNotification(notificationListResponse.data.content[pos].id)
                        }
                    }

                }
            })
        binding.rvAttendanceList.adapter = notificationListAdapter
    }

    fun clickevents() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.txtClearAll.setOnClickListener {


            requireContext().alert(R.style.AlertDialogTheme) {
                setTitle(getString(R.string.notifications))
                setMessage(getString(R.string.alert_msg_notification))
                negativeButton(getString(R.string.str_no)) {
                    it.dismiss()
                }
                positiveButton(getString(R.string.str_yes)) {


                    viewModel.clearNotification()


                }
            }
        }
    }
}