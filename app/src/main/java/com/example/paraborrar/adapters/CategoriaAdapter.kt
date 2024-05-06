package com.example.paraborrar.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.listacategoria.modelo.entidades.Categoria
import com.example.paraborrar.R

class CategoriaAdapter(
    private val mList: MutableList<Categoria>,
    private val mListener: OnItemClickListener,
    private val textListener: OnTextViewClickListener
) : RecyclerView.Adapter<CategoriaAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun borradoCat(position: Int)
        fun editarCat(position: Int)
    }

    interface OnTextViewClickListener {
        fun onTextViewClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tarjetas, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val cat = mList[position]

        holder.textView.text = cat.nombre
        holder.textView.setOnClickListener {
            textListener.onTextViewClick(position)
        }
        //////////
        holder.borrado.setOnClickListener {
            mListener.borradoCat(position)
        }
        ///////////

        //////////
        holder.editar.setOnClickListener {
            mListener.editarCat(position)
        }
        ///////////
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textView: TextView = itemView.findViewById(R.id.textView)
        val borrado: ImageView = itemView.findViewById(R.id.borraCat)
        val editar: ImageView = itemView.findViewById(R.id.editaCat)
    }


}