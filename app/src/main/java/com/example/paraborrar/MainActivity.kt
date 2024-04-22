package com.example.paraborrar

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listacategoria.modelo.conexiones.BDFichero
import com.example.listacategoria.modelo.daos.categorias.DaoCategoriasFichero
import com.example.listacategoria.modelo.daos.tareas.DaoTareasFichero
import com.example.listacategoria.modelo.entidades.Categoria
import com.example.listacategoria.modelo.entidades.Tarea
import com.example.listacategoria.modelo.interfaces.InterfaceDaoCategorias
import com.example.listacategoria.modelo.interfaces.InterfaceDaoTareas
import com.example.paraborrar.adapters.CategoriaAdapter
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), CategoriaAdapter.OnItemClickListener,
    CategoriaAdapter.OnTextViewClickListener, NavigationView.OnNavigationItemSelectedListener{

    lateinit var recyclerview: RecyclerView

    lateinit var daoCategoria: InterfaceDaoCategorias
    lateinit var daoTarea: InterfaceDaoTareas

    //Abrir la conexion con la base de datos
    val conexion = BDFichero(this)

    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Navigation Drawer
        drawer = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)


        //toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Categorias"
        ////////////////////
        toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        daoCategoria = DaoCategoriasFichero()
        daoCategoria.createConexion(conexion)

        daoTarea = DaoTareasFichero()
        daoTarea.createConexion(conexion)

        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener {
            var intent = Intent(this, FormCat::class.java)
            startActivity(intent)
        }

        recyclerview = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerview.layoutManager = LinearLayoutManager(this)

        val adapter = CategoriaAdapter(daoCategoria.getCategorias(), this, this)
        recyclerview.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbarcat, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.eraser -> {
                conexion.borrarArchivos()
                recargarDatos()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.drawer_cat -> {
                Toast.makeText(this, "Ya estas en categorias", Toast.LENGTH_SHORT).show()
            }
            R.id.drawer_formCat->{
                var intent = Intent(this, FormCat::class.java)
                startActivity(intent)
            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }


    override fun onItemClick(position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("¡Atencion!")
        builder.setMessage("Estas a punto de borrar una categoria entera\n¿Desea continuar?")
        builder.setPositiveButton("Aceptar"){ dialog, which ->
            val categoriaSeleccionada = daoCategoria.getCategorias()[position]
            Log.d("categoria elimina", categoriaSeleccionada.nombre)
            daoCategoria.deleteCategoria(Categoria(categoriaSeleccionada.nombre))
            recargarDatos()
        }

        builder.setNegativeButton("Cancelar"){ dialog, which ->
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()

    }

    private fun recargarDatos() {
        val adapter = CategoriaAdapter(daoCategoria.getCategorias(), this, this)
        recyclerview.adapter = adapter
    }

    override fun onTextViewClick(position: Int) {
        val categoria = daoCategoria.getCategorias()[position]
        var inten = Intent(this@MainActivity, TareasActivity::class.java)
        inten.putExtra("nombreCat", categoria.nombre)
        startActivity(inten)
    }

    override fun onResume() {
        super.onResume()
        recargarDatos()
    }



}