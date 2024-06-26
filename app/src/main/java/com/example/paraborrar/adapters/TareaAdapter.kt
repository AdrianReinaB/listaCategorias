import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.listacategoria.modelo.entidades.Tarea
import com.example.paraborrar.R

class TareaAdapter(private val mList: MutableList<Tarea>, private val mListener: OnItemClickListener) : RecyclerView.Adapter<TareaAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onTextViewClick(position: Int)

        fun onMenuClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tarjetas_tareas, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tar = mList[position]

        holder.tar.text = tar.nombre
        holder.tar.setOnClickListener {
            mListener.onTextViewClick(position)
        }
        holder.nTar.text = tar.items.size.toString()

        holder.menu.setOnClickListener {
            mListener.onMenuClick(position)
        }

    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tar: TextView = itemView.findViewById(R.id.tar)
        val nTar: TextView = itemView.findViewById(R.id.numTar)
        val menu: ImageView = itemView.findViewById(R.id.imgPuntos)
    }


}
