package com.easyfield.expense.adapters


import android.content.Context
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.easyfield.R
import com.easyfield.databinding.RowExpenseListBinding
import com.easyfield.expense.response.expenselist.Content
import com.easyfield.network.RetrofitClient
import com.easyfield.utils.ItemClickAdapter
import com.bumptech.glide.Glide


class ExpenseListAdapter(
    var ctx: Context, private val categoryList: ArrayList<Content>,
    val listener: ItemClickAdapter
) :RecyclerView.Adapter<ExpenseListAdapter.ViewHolder>(){




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowExpenseListBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {



        val item: Content = categoryList[position]
        val url= RetrofitClient.retrofitClientURL+item.expense_photo
        Log.w("vishalurl",""+url)



        Glide.with(ctx)
            .load(url)
            .placeholder(R.drawable.no_img).error(R.drawable.no_img)
            .into(holder.binding.ivPhoto)


        holder.binding.txtCategpryName.text = item.category_name.toString()
        holder.binding.btnStatus.setText(item.status_name.toString())

        if(item.status_id.toString().equals("1") || item.status_id.toString().equals("2")){
            holder.binding.btnStatus.setTextColor(ctx.resources.getColor(R.color.black))

        }
        else if(item.status_id.toString().equals("3")|| item.status_id.toString().equals("4")){

            holder.binding.btnStatus.setTextColor(ctx.resources.getColor(R.color.green))

        }
        else if(item.status_id.toString().equals("5")){

            holder.binding.btnStatus.setTextColor(ctx.resources.getColor(R.color.red))
        }

        if(item.status_id.toString().equals("3")){
            holder.binding.llAcceptedAmount.visibility=View.VISIBLE
            holder.binding.txtAcceptedAmount.text = "Rs "+item.approved_amount.toString()
        }
        else{
            holder.binding.llAcceptedAmount.visibility=View.GONE
        }
        holder.binding.txtCity.text = item.city_name.toString()
        holder.binding.txtDate.text = item.expense_date.toString()
        holder.binding.txtAmount.text = "Rs "+item.requested_amount.toString()


        holder.itemView.setOnClickListener {
            val pos=position
            listener.onItemClick(pos,"Category")
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    inner class ViewHolder(val binding: RowExpenseListBinding) :
        RecyclerView.ViewHolder(binding.root)



}