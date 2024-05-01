package com.example.paraborrar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listacategoria.modelo.conexiones.BDFichero
import com.example.listacategoria.modelo.daos.tareas.DaoTareasFichero
import com.example.listacategoria.modelo.entidades.Categoria
import com.example.listacategoria.modelo.entidades.Item
import com.example.listacategoria.modelo.entidades.Tarea
import com.example.listacategoria.modelo.interfaces.InterfaceDaoTareas
import com.example.paraborrar.adapters.ItemAdapter
import com.google.android.material.navigation.NavigationView

class ItemsActivity : AppCompatActivity(), ItemAdapter.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener {

    lateinit var recyclerview: RecyclerView
    lateinit var daoTarea: InterfaceDaoTareas

    var nombreT: String? = null
    var nombreC: String? = null

    //Abrir la conexion con la base de datos
    val conexion = BDFichero(this)

    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)

        //Navigation Drawer
        drawer = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        daoTarea= DaoTareasFichero()
        daoTarea.createConexion(conexion)

        nombreT = intent.getStringExtra("nombreTar")
        nombreC = intent.getStringExtra("nombreCat")

        //toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title="Items"
        ////////////////////
        toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()


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
                    daoTarea.addItem(Categoria(nombreC.toString()), Tarea(nombreT.toString()), Item(nombreItem.text.toString(), false))
                    recargarDatos()
                }

                builder.setNegativeButton("Cancelar"){ dialog, which ->

                }

                val dialog: AlertDialog = builder.create()
                dialog.show()
                true
            } R.id.act->{
                    var estado=daoTarea.getItems(Categoria(nombreC.toString()), Tarea(nombreT.toString()))
                    for (item in estado){
                        daoTarea.updateItem(Categoria(nombreC.toString()), Tarea(nombreT.toString()), Item(item.accion, false), Item(item.accion, true))
                        Log.d("estadoT", "${item.activo}")
                    }
                recargarDatos()
                true
            } R.id.dest->{
                var estado=daoTarea.getItems(Categoria(nombreC.toString()), Tarea(nombreT.toString()))
                for (item in estado){
                    daoTarea.updateItem(Categoria(nombreC.toString()), Tarea(nombreT.toString()), Item(item.accion, true), Item(item.accion, false))
                    Log.d("estadoF", "${item.activo}")
                }
                recargarDatos()
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
        if(item.activo) {
            daoTarea.updateItem(Categoria(nombreC.toString()), Tarea(nombreT.toString()), Item(item.accion, true), Item(item.accion, false))
            Toast.makeText(this, "Desactivado", Toast.LENGTH_SHORT).show()
            recargarDatos()
        }else{
            daoTarea.updateItem(Categoria(nombreC.toString()), Tarea(nombreT.toString()), Item(item.accion, false), Item(item.accion, true))
            Toast.makeText(this, "Activado", Toast.LENGTH_SHORT).show()
            recargarDatos()
        }
    }

    override fun onLongClick(position: Int) {
        val item = daoTarea.getItems(Categoria(nombreC.toString()), Tarea(nombreT.toString()))[position]
        Toast.makeText(this, item.accion+" "+"presionado largo", Toast.LENGTH_SHORT).show()

        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog2, null)
        val builder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Opciones")

        val dialog = builder.create()

        val btnEdit: Button = dialogView.findViewById(R.id.btnEdit)
        val btnDelete: Button = dialogView.findViewById(R.id.btnDelete)

        btnEdit.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Editar ${item.accion}")

            val inflater =LayoutInflater.from(this)
            val dialogView =inflater.inflate(R.layout.dialog, null)
            builder.setView(dialogView)

            val nombreItem= dialogView.findViewById<EditText>(R.id.nuevoTexto)


            builder.setPositiveButton("Aceptar"){ dialog, which ->
                    daoTarea.updateItem(Categoria(nombreC.toString()), Tarea(nombreT.toString()), Item(item.accion.toString(), item.activo), Item(nombreItem.text.toString(), item.activo))
                    recargarDatos()
            }


            builder.setNegativeButton("Cancelar"){ dialog, which ->
            }

            val dialog1: AlertDialog = builder.create()
            dialog1.show()
            true
            dialog.dismiss()
        }

        btnDelete.setOnClickListener {
            daoTarea.deleteItem(Categoria(nombreC.toString()), Tarea(nombreT.toString()), Item(item.accion, item.activo))
            recargarDatos()
            dialog.dismiss()
        }

        dialog.show()
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.drawer_cat -> {
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.drawer_formCat->{
                var intent = Intent(this, FormCat::class.java)
                startActivity(intent)
            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
}