package com.example.paraborrar.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.listacategoria.modelo.entidades.Tarea
import com.example.paraborrar.R

class TareaAdapter (private val mList: List<Tarea>) : RecyclerView.Adapter<TareaAdapter.ViewHolder>() {

    ///////////////////////////////////////////
    private var mListener: onItemClickListener?=null


    interface onItemClickListener{
        fun onItemCLick(nombreTarea: String)
    }

    fun setOnItemClickListener(listener: onItemClickListener?){
        mListener=listener

    }
    /////////////////////////////////////////////////

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tarjetas_tareas, parent, false)

        val listener = mListener ?: object : onItemClickListener {
            override fun onItemCLick(nombreTarea: String) {
                }
        }

        return ViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val tar = mList[position]

        holder.tar.text = tar.nombre
        holder.nTar.text = tar.nItems.toString()

    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(ItemView) {
        val tar: TextView = itemView.findViewById(R.id.tar)
        val nTar: TextView = itemView.findViewById(R.id.numTar)

        /////////////////////////////////////////
        init {
            itemView.setOnClickListener{
                listener.onItemCLick(tar.text.toString())
            }
        }
        //////////////////////////////////////////
    }


}