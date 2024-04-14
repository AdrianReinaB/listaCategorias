package com.example.paraborrar

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listacategoria.modelo.conexiones.BDFichero
import com.example.listacategoria.modelo.daos.categorias.DaoCategoriasFichero
import com.example.listacategoria.modelo.daos.tareas.DaoTareasFichero
import com.example.listacategoria.modelo.interfaces.InterfaceDaoCategorias
import com.example.listacategoria.modelo.interfaces.InterfaceDaoTareas
import com.example.paraborrar.adapters.CategoriaAdapter


class MainActivity : AppCompatActivity() {

    lateinit var recyclerview: RecyclerView

    lateinit var daoCategoria: InterfaceDaoCategorias
    lateinit var daoTarea: InterfaceDaoTareas

    //Abrir la conexion con la base de datos
    val conexion = BDFichero(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title="Categorias"

        daoCategoria = DaoCategoriasFichero()
        daoCategoria.createConexion(conexion)

        daoTarea=DaoTareasFichero()
        daoTarea.createConexion(conexion)

        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener {
            var intent = Intent(this, FormCat::class.java)

            startActivity(intent)
        }

        recyclerview = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerview.layoutManager = LinearLayoutManager(this)

        val adapter = CategoriaAdapter(daoCategoria.getCategorias())
        recyclerview.adapter = adapter
        adapter.setOnItemClickListener(object : CategoriaAdapter.onItemClickListener{
            override fun onItemCLick(nombreCategoria: String) {
                //daoTarea.addTarea(Categoria(nombreCategoria), Tarea("sdgf"))
                //daoTarea.addItem(Categoria(nombreCategoria), Tarea("sdgf"), Item("fw", true))
                var inten = Intent(this@MainActivity, TareasActivity::class.java)
                inten.putExtra("nombreCat", nombreCategoria)
                startActivity(inten)
            }

        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbarcat, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.eraser -> {
                conexion.borrarArchivos()
                recyclerview = findViewById<RecyclerView>(R.id.recyclerview)
                recyclerview.layoutManager = LinearLayoutManager(this)
                val adapter = CategoriaAdapter(daoCategoria.getCategorias())
                recyclerview.adapter = adapter
                true
            }
            else -> super.onOptionsItemSelected(item)

        }
    }

    /*override fun onResume(){
        super.onResume()
        recyclerview = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerview.layoutManager = LinearLayoutManager(this)
        val adapter = CategoriaAdapter(daoCategoria.getCategorias())
        recyclerview.adapter = adapter
    }*/

}