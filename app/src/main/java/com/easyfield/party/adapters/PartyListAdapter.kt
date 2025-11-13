package com.easyfield.party.adapters


import android.content.Context

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.easyfield.databinding.RowPartylistBinding
import com.easyfield.party.response.Content
import com.easyfield.utils.ItemClickAdapter


class PartyListAdapter(
    var ctx: Context, private val categoryList: List<Content>,
    val listener: ItemClickAdapter
) :RecyclerView.Adapter<PartyListAdapter.ViewHolder>(){




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowPartylistBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {



        val item = categoryList[position]
//
        holder.binding.txtPartyName.setText(item.firm_name)
        holder.binding.btnStatus.setText(item.party_category_name)
        holder.binding.txtDealerName.setText(item.dealer_name)
        holder.binding.txtMobileNumber.setText(item.dealer_phone.toString())
        holder.binding.txtCity.setText(item.city_or_town)


        holder.binding.txtDate.setText(item.created_at.toString().split("\\s+".toRegex())[0].toString())


        holder.itemView.setOnClickListener {
            val pos=position
            listener.onItemClick(pos,"Category")
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    inner class ViewHolder(val binding: RowPartylistBinding) :
        RecyclerView.ViewHolder(binding.root)



}