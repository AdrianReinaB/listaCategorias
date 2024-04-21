package com.example.paraborrar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listacategoria.modelo.conexiones.BDFichero
import com.example.listacategoria.modelo.daos.tareas.DaoTareasFichero
import com.example.listacategoria.modelo.entidades.Categoria
import com.example.listacategoria.modelo.entidades.Tarea
import com.example.listacategoria.modelo.interfaces.InterfaceDaoTareas
import com.example.paraborrar.adapters.CategoriaAdapter
import com.example.paraborrar.adapters.TareaAdapter

class TareasActivity : AppCompatActivity(), TareaAdapter.OnItemClickListener {

    lateinit var recyclerview: RecyclerView
    lateinit var daoTarea: InterfaceDaoTareas

    var nombre: String? = null

    //Abrir la conexion con la base de datos
    val conexion = BDFichero(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tareas)

        //toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title="Tareas"

        daoTarea= DaoTareasFichero()
        daoTarea.createConexion(conexion)

        nombre = intent.getStringExtra("nombreCat")


        recyclerview = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerview.layoutManager = LinearLayoutManager(this)

        val adapter = TareaAdapter(daoTarea.getTareas(Categoria(nombre.toString())), this)
        recyclerview.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_tarea, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nuevaTarea -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Nueva tarea para $nombre")

                val inflater =LayoutInflater.from(this)
                val dialogView =inflater.inflate(R.layout.dialog, null)
                builder.setView(dialogView)

                val nombreTar= dialogView.findViewById<EditText>(R.id.nuevoTexto)

                builder.setPositiveButton("Aceptar"){ dialog, which ->
                    daoTarea.addTarea(Categoria(nombre.toString()), Tarea(nombreTar.text.toString()))
                    recargarDatos()
                }

                builder.setNegativeButton("Cancelar"){ dialog, which ->
                }

                val dialog: AlertDialog = builder.create()
                dialog.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun recargarDatos() {
        val adapter = TareaAdapter(daoTarea.getTareas(Categoria(nombre.toString())), this)
        recyclerview.adapter = adapter
    }

    override fun onTextViewClick(position: Int) {
        val tarea = daoTarea.getTareas(Categoria(nombre.toString()))[position]
        var intent = Intent(this@TareasActivity, ItemsActivity::class.java)
        intent.putExtra("nombreTar", tarea.nombre)
        intent.putExtra("nombreCat", nombre)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        recargarDatos()
    }


}