package com.example.paraborrar

import TareaAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
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
import com.example.listacategoria.modelo.entidades.Tarea
import com.example.listacategoria.modelo.interfaces.InterfaceDaoTareas
import com.google.android.material.navigation.NavigationView

class TareasActivity : AppCompatActivity(), TareaAdapter.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener{

    lateinit var recyclerview: RecyclerView
    lateinit var daoTarea: InterfaceDaoTareas

    var nombre: String? = null

    //Abrir la conexion con la base de datos
    val conexion = BDFichero(this)

    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tareas)

        //Navigation Drawer
        drawer = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        //toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title="Tareas"
        ////////////////////
        toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        daoTarea= DaoTareasFichero()
        daoTarea.createConexion(conexion)

        nombre = intent.getStringExtra("nombreCat")


        recyclerview = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerview.layoutManager = LinearLayoutManager(this)

        val adapter = TareaAdapter(daoTarea.getTareas(Categoria(nombre.toString())), this)
        recyclerview.adapter = adapter

        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener {
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

    override fun onMenuClick(position: Int) {
        val tarea = daoTarea.getTareas(Categoria(nombre.toString()))[position]
        showPopupMenu(tarea)
    }

    private fun showPopupMenu(tarea: Tarea) {
        val imageView = findViewById<ImageView>(R.id.imgPuntos)
        val popupMenu = PopupMenu(this, imageView)
        popupMenu.inflate(R.menu.menu_recycler)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.recyclerEdit -> {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Editar tarea ${tarea.nombre}")

                    val inflater =LayoutInflater.from(this)
                    val dialogView =inflater.inflate(R.layout.dialog, null)
                    builder.setView(dialogView)

                    val nombreTar= dialogView.findViewById<EditText>(R.id.nuevoTexto)

                    builder.setPositiveButton("Aceptar"){ dialog, which ->
                        daoTarea.updateNombreTarea(Categoria(nombre.toString()), Tarea(tarea.nombre), Tarea(nombreTar.text.toString()))
                        recargarDatos()
                        Toast.makeText(this, "Tarea editada: ${tarea.nombre}", Toast.LENGTH_SHORT).show()
                    }
                    builder.setNegativeButton("Cancelar"){ dialog, which ->
                    }

                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                    true
                }
                R.id.recyclerDel -> {
                    daoTarea.deleteTarea(Categoria(nombre.toString()), Tarea(tarea.nombre))
                    Toast.makeText(this, "Tarea borrada: ${tarea.nombre}", Toast.LENGTH_SHORT).show()
                    recargarDatos()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    override fun onResume() {
        super.onResume()
        recargarDatos()
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