package com.example.paraborrar.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.listacategoria.modelo.entidades.Categoria
import com.example.paraborrar.R

class CategoriaAdapter (private val mList: List<Categoria>) : RecyclerView.Adapter<CategoriaAdapter.ViewHolder>() {
    ///////////////////////////////////////////
    private lateinit var mListener: onItemClickListener


    interface onItemClickListener{
        fun onItemCLick(nombreCategoria: String)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener=listener

    }
    /////////////////////////////////////////////////
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tarjetas, parent, false)

        return ViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val cat = mList[position]

        holder.textView.text = cat.nombre

    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View, listener:onItemClickListener) : RecyclerView.ViewHolder(ItemView) {
        val textView: TextView = itemView.findViewById(R.id.textView)
        /////////////////////////////////////////
        init {
            itemView.setOnClickListener{
                listener.onItemCLick(textView.text.toString())
            }
        }
        //////////////////////////////////////////
    }


}