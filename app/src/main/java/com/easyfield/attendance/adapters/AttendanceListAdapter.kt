package com.easyfield.attendance.adapters


import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.easyfield.attendance.response.attendancelist.Attendance
import com.easyfield.databinding.RowAttendancelistBinding
import com.easyfield.utils.ItemClickAdapter


class AttendanceListAdapter(
    var ctx: Context, private val categoryList: List<Attendance>,
    val listener: ItemClickAdapter
) :RecyclerView.Adapter<AttendanceListAdapter.ViewHolder>(){




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowAttendancelistBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {



        val item: Attendance = categoryList[position]





        holder.binding.txtDay.text = item.day_number.toString()
        holder.binding.txtWeekDay.text = item.day.toString()



        if(item.type.equals("punch"))
        {
            holder.binding.llLeaveType.visibility=View.GONE
            holder.binding.llPunchIn.visibility=View.VISIBLE
            holder.binding.llPunchInOut.visibility=View.VISIBLE


            if(item.punch_in.toString().isNotEmpty()){
                holder.binding.txtPunchIn.text = item.punch_in.toString()
            }
            else{
                holder.binding.txtPunchIn.setText("--:--")
            }



            if(item.punch_out.length>0){
                holder.binding.txtPunchOut.text = item.punch_out.toString()
            }
            else {
                holder.binding.txtPunchOut.setText("--:--")
            }



//            if(item.punch_out.toString().isNotEmpty()){
//                holder.binding.llPunchInOut.visibility=View.VISIBLE
//
//            }
//            else{
//                holder.binding.llPunchInOut.visibility=View.GONE
//            }

        }
        else {
            holder.binding.llPunchInOut.visibility=View.GONE
            holder.binding.llPunchIn.visibility=View.GONE

            holder.binding.llLeaveType.visibility=View.VISIBLE


            if(item.name.toString().isNotEmpty()){
                holder.binding.txtLeaveType.text = item.name.toString()
            }
            else{
                holder.binding.txtLeaveType.text = item.type.toString()
            }

            if(item.status.toString().isNotEmpty()){

                if(item.status.toString().equals("1")){
                    holder.binding.btnStatus.setText("Pending")

                }
                else if(item.status.toString().equals("2")){
                    holder.binding.btnStatus.setText("Approved")

                }
                else if(item.status.toString().equals("3")){
                    holder.binding.btnStatus.setText("Rejected")
                }

                holder.binding.btnStatus.visibility=View.VISIBLE
            }
            else{
                holder.binding.btnStatus.visibility=View.GONE
            }




        }






        holder.itemView.setOnClickListener {
            val pos=position
            listener.onItemClick(pos,"Category")
        }

    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    inner class ViewHolder(val binding: RowAttendancelistBinding) :
        RecyclerView.ViewHolder(binding.root)



}