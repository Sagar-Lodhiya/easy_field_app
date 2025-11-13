package com.easyfield.payment.adapters


import android.content.Context

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.easyfield.R
import com.easyfield.databinding.RowPaymentlistBinding
import com.easyfield.payment.response.Content
import com.easyfield.utils.ItemClickAdapter
import java.text.DecimalFormat


class PaymentListAdapter(
    var ctx: Context, private val categoryList: List<Content>,
    val listener: ItemClickAdapter
) :RecyclerView.Adapter<PaymentListAdapter.ViewHolder>(){




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowPaymentlistBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {



        val item = categoryList[position]

        holder.binding.txtPartyName.setText(item.party_name)



        val df = DecimalFormat("###.#")

        holder.binding.txtAmount.setText(df.format(item.amount.toDouble()))


        holder.binding.txtDate.setText(item.created_at.toString().split("\\s+".toRegex())[0].toString())
        holder.binding.btnStatus.setText(item.status_name)
        holder.binding.txtExtra.setText(item.extra)


        if(item.status_id.toString().equals("3")){
            holder.binding.btnStatus.setBackgroundResource(R.drawable.rounded_button_red)

        }
        else{
            holder.binding.btnStatus.setBackgroundResource(R.drawable.rounded_button_status)
        }


        holder.itemView.setOnClickListener {
            val pos=position
            listener.onItemClick(pos,"Category")
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    inner class ViewHolder(val binding: RowPaymentlistBinding) :
        RecyclerView.ViewHolder(binding.root)



}