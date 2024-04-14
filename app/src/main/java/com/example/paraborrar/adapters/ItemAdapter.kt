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

class ItemAdapter (private val mList: List<Item>) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    ///////////////////////////////////////////
    private var mListener: onItemClickListener?=null


    interface onItemClickListener{
        fun onItemCLick(nombreItem: String)
    }

    fun setOnItemClickListener(listener: onItemClickListener?){
        mListener=listener

    }
    /////////////////////////////////////////////////

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tarjetas_items, parent, false)

        val listener = mListener ?: object : ItemAdapter.onItemClickListener {
            override fun onItemCLick(nombreItem: String) {
            }
        }

        return ViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mList[position]

        holder.item.text = item.accion

        if (item.activo) {
            holder.lineLayout.setBackgroundColor(Color.GREEN)
        } else {
            holder.lineLayout.setBackgroundColor(Color.RED)
        }


    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View, listener: ItemAdapter.onItemClickListener) : RecyclerView.ViewHolder(ItemView) {
        val item: TextView = itemView.findViewById(R.id.textView)
        val lineLayout: LinearLayout = itemView.findViewById(R.id.tarjeta)

        /////////////////////////////////////////
        init {
            itemView.setOnClickListener{
                listener.onItemCLick(item.text.toString())
            }
        }
        //////////////////////////////////////////
    }


}