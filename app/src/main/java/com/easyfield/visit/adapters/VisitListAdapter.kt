package com.easyfield.visit.adapters


import android.content.Context

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.easyfield.databinding.RowVisitlistBinding
import com.easyfield.utils.ItemClickAdapter


class VisitListAdapter(
    var ctx: Context, private val categoryList: List<com.easyfield.visit.response.visitlistresponse.Content>,
    val listener: ItemClickAdapter
) :RecyclerView.Adapter<VisitListAdapter.ViewHolder>(){




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowVisitlistBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {



        val item = categoryList[position]

        holder.binding.txtPartyName.setText(item.party_name)
        holder.binding.btnDate.setText(item.created_at.toString().split("\\s+".toRegex())[0].toString())

        holder.binding.txtDate.setText(item.time.toString())
        holder.binding.btnHours.setText(item.duration.toString())
        holder.binding.txtAddress.setText(item.place.toString())
        holder.binding.txtDiscussionPoint.setText(item.discussion_point.toString())

        holder.itemView.setOnClickListener {
            val pos=position
            listener.onItemClick(pos,"Category")
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    inner class ViewHolder(val binding: RowVisitlistBinding) :
        RecyclerView.ViewHolder(binding.root)



}