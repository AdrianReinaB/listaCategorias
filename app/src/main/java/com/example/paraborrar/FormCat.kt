package com.example.paraborrar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.example.listacategoria.modelo.conexiones.BDFichero
import com.example.listacategoria.modelo.daos.categorias.DaoCategoriasFichero
import com.example.listacategoria.modelo.entidades.Categoria
import com.example.listacategoria.modelo.interfaces.InterfaceDaoCategorias

class FormCat : AppCompatActivity() {

    lateinit var daoCategoria: InterfaceDaoCategorias

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_cat)

        val conexion= BDFichero(this)

        daoCategoria = DaoCategoriasFichero()
        daoCategoria.createConexion(conexion)

        //Activity
        var res:TextView=findViewById(R.id.result)
        val nCat:EditText=findViewById(R.id.nCat)
        val bCat:Button=findViewById(R.id.catButton)

        //toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val atras: Button=findViewById(R.id.batras)
        setSupportActionBar(toolbar)

        bCat.setOnClickListener{
            daoCategoria.addCategoria(Categoria(nCat.text.toString()))
            Toast.makeText(this, "Categoria guardada", Toast.LENGTH_SHORT).show()
            nCat.setText("")
        }

        atras.setOnClickListener{
            var inten= Intent(this, MainActivity::class.java)
            startActivity(inten)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.eraser -> {
                var opcion=0
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder
                    .setTitle("Selecciona base de datos")
                    .setPositiveButton("Aceptar") { dialog, which ->
                        when (opcion) {
                            0 -> {
                                Log.d("can", "fichero")
                                Toast.makeText(this, "Fichero", Toast.LENGTH_SHORT).show()
                            }
                            1 -> {
                                Toast.makeText(this, "SQL", Toast.LENGTH_SHORT).show()
                            }
                            2 -> {
                                Toast.makeText(this, "ROOM", Toast.LENGTH_SHORT).show()
                            }
                            3 -> {
                                Toast.makeText(this, "Firebase", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    .setNegativeButton("Cancelar") { dialog, which ->

                    }
                    .setSingleChoiceItems(
                        arrayOf("Fichero", "SQL", "ROOM", "Firebase"), 0
                    ) { dialog, which ->
                        opcion=which
                    }

                val dialog: AlertDialog = builder.create()
                dialog.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}