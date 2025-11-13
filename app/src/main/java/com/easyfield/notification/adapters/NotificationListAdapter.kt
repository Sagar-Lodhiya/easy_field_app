package com.easyfield.notification.adapters


import android.content.Context
import android.util.Log

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.easyfield.databinding.RowNotificationBinding
import com.easyfield.notification.response.Content
import com.easyfield.utils.ItemClickAdapter


class NotificationListAdapter(
    var ctx: Context, private val categoryList: ArrayList<Content>,
    val listener: ItemClickAdapter
) :RecyclerView.Adapter<NotificationListAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowNotificationBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false)
        return ViewHolder(binding)
    }
    fun removeAt(pos: Int) {
        categoryList.removeAt(pos)

        Log.w("size",""+categoryList.size)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item= categoryList[position]

        holder.binding.txtContent.text = item.text.toString()

        holder.binding.ivDelete.setOnClickListener {
            val pos=position
            listener.onItemClick(pos,"Delete")
        }


//        holder.itemView.setOnClickListener {
//            val pos=position
//            listener.onItemClick(pos,"Category")
//        }

    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    inner class ViewHolder(val binding: RowNotificationBinding) :
        RecyclerView.ViewHolder(binding.root)



}