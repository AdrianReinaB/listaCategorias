package com.example.paraborrar.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.listacategoria.modelo.entidades.Item
import com.example.paraborrar.R

class ItemAdapter (private val mList: MutableList<Item>, private val mListener: OnItemClickListener) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tarjetas_items, parent, false)


        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mList[position]

        holder.item.text = item.accion
        holder.lineLayout.setOnClickListener{
            mListener.onItemClick(position)
        }

        if (item.activo) {
            holder.lineLayout.setBackgroundColor(Color.GREEN)
        } else {
            holder.lineLayout.setBackgroundColor(Color.RED)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val item: TextView = itemView.findViewById(R.id.textView)
        val lineLayout: LinearLayout = itemView.findViewById(R.id.tarjeta)
    }


}