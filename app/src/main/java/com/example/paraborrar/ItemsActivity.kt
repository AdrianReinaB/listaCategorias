package com.example.paraborrar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listacategoria.modelo.conexiones.BDFichero
import com.example.listacategoria.modelo.daos.tareas.DaoTareasFichero
import com.example.listacategoria.modelo.entidades.Categoria
import com.example.listacategoria.modelo.entidades.Item
import com.example.listacategoria.modelo.entidades.Tarea
import com.example.listacategoria.modelo.interfaces.InterfaceDaoTareas
import com.example.paraborrar.adapters.CategoriaAdapter
import com.example.paraborrar.adapters.ItemAdapter

class ItemsActivity : AppCompatActivity(), ItemAdapter.OnItemClickListener {

    lateinit var recyclerview: RecyclerView
    lateinit var daoTarea: InterfaceDaoTareas

    var nombreT: String? = null
    var nombreC: String? = null

    //Abrir la conexion con la base de datos
    val conexion = BDFichero(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.items_vista)

        daoTarea= DaoTareasFichero()
        daoTarea.createConexion(conexion)

        nombreT = intent.getStringExtra("nombreTar")
        nombreC = intent.getStringExtra("nombreCat")

        //toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title="Items"


        recyclerview = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerview.layoutManager = LinearLayoutManager(this)
        val adapter = ItemAdapter(daoTarea.getItems(Categoria(nombreC.toString()), Tarea(nombreT.toString())), this)
        recyclerview.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nItem -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Nuevo item para $nombreT")

                val inflater = LayoutInflater.from(this)
                val dialogView =inflater.inflate(R.layout.dialog, null)
                builder.setView(dialogView)

                val nombreItem= dialogView.findViewById<EditText>(R.id.nuevoTexto)

                builder.setPositiveButton("Aceptar"){ dialog, which ->
                    daoTarea.addItem(Categoria(nombreC.toString()), Tarea(nombreT.toString()), Item(nombreItem.text.toString(), true))
                    recargarDatos()
                }

                builder.setNegativeButton("Cancelar"){ dialog, which ->

                }

                val dialog: AlertDialog = builder.create()
                dialog.show()
                true
            } R.id.act->{
                    var estado=daoTarea.getItems(Categoria(nombreC.toString()), Tarea(nombreT.toString()))
                    for (est in estado){
                        est.activo=true
                        Log.d("estadoT", "${est.activo}")
                    }
                true
            } R.id.dest->{
                var estado=daoTarea.getItems(Categoria(nombreC.toString()), Tarea(nombreT.toString()))
                for (est in estado){
                    est.activo=false
                    Log.d("estadoF", "${est.activo}")
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun recargarDatos() {
        val adapter = ItemAdapter(daoTarea.getItems(Categoria(nombreC.toString()), Tarea(nombreT.toString())), this)
        recyclerview.adapter = adapter
    }

    override fun onItemClick(position: Int) {
        val item = daoTarea.getItems(Categoria(nombreC.toString()), Tarea(nombreT.toString()))[position]
        if(!item.activo) {
            item.activo = true
            Toast.makeText(this, item.activo.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}