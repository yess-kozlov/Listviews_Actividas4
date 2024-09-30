package com.example.a4act


import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var buscarEditText: EditText

    // Datos de los platillos
    private val platillos = arrayOf(
        "Tacos", "Sushi", "Pizza", "Hamburguesa", "Paella",
        "Ceviche", "Falafel", "Biryani", "Dumplings", "Pasta"
    )

    private val paises = arrayOf(
        "México", "Japón", "Italia", "Estados Unidos", "España",
        "Perú", "Medio Oriente", "India", "China", "Italia"
    )

    private val imagenes = intArrayOf(
        R.drawable.hamburguesa, R.drawable.sushi, R.drawable.pizza, R.drawable.paella,
        R.drawable.ceviche, R.drawable.falafel,
        R.drawable.biryani, R.drawable.dumplings,
        R.drawable.pasta
    )

    private lateinit var itemsAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listView)
        buscarEditText = findViewById(R.id.buscarEditText)

        // Inicializar el adaptador
        itemsAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, platillos)
        listView.adapter = itemsAdapter

        // Listener para el ListView
        listView.setOnItemClickListener { parent, view, position, id ->
            mostrarDialogo(platillos[position], paises[position], imagenes[position])
        }

        // TextWatcher para el EditText de búsqueda
        buscarEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                itemsAdapter.filter.filter(s)
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun mostrarDialogo(nombre: String, pais: String, imagenResId: Int) {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater: LayoutInflater = layoutInflater
        val dialogView: View = inflater.inflate(R.layout.dialog_layout, null)

        // Inicializar elementos del diálogo
        val nombrePlatillo: TextView = dialogView.findViewById(R.id.nombrePlatillo)
        val paisOrigen: TextView = dialogView.findViewById(R.id.paisOrigen)
        val imagenPlatillo: ImageView = dialogView.findViewById(R.id.imagenPlatillo)

        nombrePlatillo.text = nombre
        paisOrigen.text = "Origen: $pais"
        imagenPlatillo.setImageResource(imagenResId)

        dialogBuilder.setView(dialogView)
            .setPositiveButton("Compartir") { dialog, _ ->
                compartirInfo(nombre)
            }
            .setNegativeButton("Cerrar") { dialog, _ -> dialog.dismiss() }

        dialogBuilder.create().show()
    }

    private fun compartirInfo(nombre: String) {
        val compartirIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "¡Prueba este platillo delicioso: $nombre!")
            type = "text/plain"
        }
        startActivity(Intent.createChooser(compartirIntent, "Compartir con"))
    }
}
