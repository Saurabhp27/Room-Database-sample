package com.example.roomdemo

import android.animation.ValueAnimator.AnimatorUpdateListener
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.persistableBundleOf
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdemo.databinding.ActivityMainBinding
import com.example.roomdemo.databinding.ItemsRowBinding

class ItemAdapter (private val items: ArrayList<EmployeeEntity>,
                   private val updateListener : (id: Int) -> Unit,
                   private val deleteListener : (id: Int) -> Unit

): RecyclerView.Adapter<ItemAdapter.VIewHolder>(){

    class VIewHolder(binding: ItemsRowBinding) :RecyclerView.ViewHolder(binding.root){
        val llmain = binding.llMain
        val tvname = binding.tvName
        val tvEmail = binding.tvEmail
        val ivEdit = binding.ivEdit
        val ivDelete = binding.ivDelete

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VIewHolder {
        return VIewHolder(ItemsRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VIewHolder, position: Int) {
        val context = holder.itemView.context
        val item = items [position]

        holder.tvname.text = item.name
        holder.tvEmail.text = item.email

        if (position % 2 == 0){
            holder.llmain.setBackgroundColor(ContextCompat.getColor(context,R.color.grey))
        }else{
            holder.llmain.setBackgroundColor(ContextCompat.getColor(context,R.color.white))
        }

        holder.ivEdit.setOnClickListener(){
            updateListener.invoke(item.id)
        }
        holder.ivDelete.setOnClickListener(){
            deleteListener.invoke(item.id)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
