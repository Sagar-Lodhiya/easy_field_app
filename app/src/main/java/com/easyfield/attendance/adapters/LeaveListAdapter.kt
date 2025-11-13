package com.easyfield.attendance.adapters


import android.content.Context

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.easyfield.R
import com.easyfield.attendance.response.leavelist.Content
import com.easyfield.databinding.RowLeavelistBinding
import com.easyfield.utils.ItemClickAdapter


class LeaveListAdapter(
    var ctx: Context, private val categoryList: List<Content>,
    val listener: ItemClickAdapter
) :RecyclerView.Adapter<LeaveListAdapter.ViewHolder>(){




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowLeavelistBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {



        val item: Content = categoryList[position]





        holder.binding.txtLeaveType.text = item.leave_type.toString()
        holder.binding.txtDate.text = item.start_date.toString()
        holder.binding.txtToDate.text =item.end_date.toString()
        holder.binding.txtReason.text="Reason : "+item.reason.toString()

        if(item.status.toString().equals("1")){
            holder.binding.btnStatus.setText("Pending")
            holder.binding.btnStatus.setTextColor(ctx.resources.getColor(R.color.black))

        }
        else if(item.status.toString().equals("2")){
            holder.binding.btnStatus.setText("Approved")
            holder.binding.btnStatus.setTextColor(ctx.resources.getColor(R.color.green))

        }
        else if(item.status.toString().equals("3")){
            holder.binding.btnStatus.setText("Rejected")
            holder.binding.btnStatus.setTextColor(ctx.resources.getColor(R.color.red))
        }


        holder.itemView.setOnClickListener {
            val pos=position
            listener.onItemClick(pos,"Category")
        }

    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    inner class ViewHolder(val binding: RowLeavelistBinding) :
        RecyclerView.ViewHolder(binding.root)



}