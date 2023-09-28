package com.fuchsia.shotokanwkf.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fuchsia.shotokanwkf.R
import com.fuchsia.shotokanwkf.itemDs

class ItmAdapter(private val itmLst:ArrayList<itemDs>):RecyclerView.Adapter<ItmAdapter.itmHolder>() {
    private lateinit var mListener: OnItemClickListener
    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener = listener
    }
    class itmHolder(itmView: View,listener: OnItemClickListener):RecyclerView.ViewHolder(itmView){
        val subName: TextView =itmView.findViewById(R.id.tvSubPlan)
        val price: TextView =itmView.findViewById(R.id.tvPlanPrice)

        init {
            itmView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): itmHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.itemlst,
            parent,false)
        return itmHolder(itemView,mListener)
    }

    override fun getItemCount(): Int {
        return itmLst.size
    }

    override fun onBindViewHolder(holder: itmHolder, position: Int) {
        val currentItem = itmLst[position]
        holder.subName.text = currentItem.subsName
        holder.price.text = currentItem.formattedPrice
    }

}