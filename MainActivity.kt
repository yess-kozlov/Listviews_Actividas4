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

    // Son los elementos de la interfaz
    private lateinit var listView: ListView
    private lateinit var buscarEditText: EditText

    // metemos los datos de los platillos y sus respectivos países de origen
    private val platillos = arrayOf(
        "Tacos", "Sushi", "Pizza", "Hamburguesa", "Paella",
        "Ceviche", "Falafel", "Biryani", "Dumplings", "Pasta"
    )

    private val paises = arrayOf(
        "México", "Japón", "Italia", "Estados Unidos", "España",
        "Perú", "Medio Oriente", "India", "China", "Italia"
    )

    // Referencias de imagen
    private val imagenes = intArrayOf(
        R.drawable.hamburguesa, R.drawable.sushi, R.drawable.pizza, R.drawable.paella,
        R.drawable.ceviche, R.drawable.falafel,
        R.drawable.biryani, R.drawable.dumplings,
        R.drawable.pasta
    )

    // Adaptador para mostrar los platillos en el ListView
    private lateinit var itemsAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Vincular los elementos con los del layout
        listView = findViewById(R.id.listView)
        buscarEditText = findViewById(R.id.buscarEditText)

        // Inicializar el adaptador para el ListView con los nombres de los platillos
        itemsAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, platillos)
        listView.adapter = itemsAdapter

        // Agregar un listener al ListView para detectar cuando el usuario selecciona un platillo
        listView.setOnItemClickListener { parent, view, position, id ->
            // Al hacer clic en un platillo, se muestra un diálogo con la información del platillo seleccionado
            mostrarDialogo(platillos[position], paises[position], imagenes[position])
        }

        // Añadir un TextWatcher al campo de búsqueda para filtrar los platillos según el texto ingresado (no estoy segura si esta bien implementado)
        buscarEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Filtra los platillos en el adaptador según el texto ingresado
                itemsAdapter.filter.filter(s)
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    // Función para mostrar un cuadro de diálogo con la información del platillo seleccionado
    private fun mostrarDialogo(nombre: String, pais: String, imagenResId: Int) {
        val dialogBuilder = AlertDialog.Builder(this) // Crear un constructor de diálogos
        val inflater: LayoutInflater = layoutInflater
        val dialogView: View = inflater.inflate(R.layout.dialog_layout, null) // Inflar el layout personalizado para el diálogo

        // apartado para vincular los elementos del layout del diálogo con las vistas
        val nombrePlatillo: TextView = dialogView.findViewById(R.id.nombrePlatillo)
        val paisOrigen: TextView = dialogView.findViewById(R.id.paisOrigen)
        val imagenPlatillo: ImageView = dialogView.findViewById(R.id.imagenPlatillo)

        // Asignar los valores correspondientes al platillo seleccionado
        nombrePlatillo.text = nombre
        paisOrigen.text = "Origen: $pais"
        imagenPlatillo.setImageResource(imagenResId) // Mostrar la imagen correspondiente al platillo

        // Configurar el diálogo para que tenga un botón de "Compartir" y otro de "Cerrar"
        dialogBuilder.setView(dialogView)
            .setPositiveButton("Compartir") { dialog, _ ->
                // Si se presiona el botón "Compartir", se llama a la función compartirInfo para enviar un Intent
                compartirInfo(nombre)
            }
            .setNegativeButton("Cerrar") { dialog, _ -> dialog.dismiss() } // Cerrar el diálogo cuando se presiona el botón "Cerrar"

        dialogBuilder.create().show() // Mostrar el diálogo
    }

    // Función para compartir la información del platillo mediante un Intent de tipo "text/plain"
    private fun compartirInfo(nombre: String) {
        val compartirIntent = Intent().apply {
            action = Intent.ACTION_SEND // Definir la acción como enviar información
            putExtra(Intent.EXTRA_TEXT, "¡Prueba este platillo delicioso: $nombre!") // Mensaje a compartir
            type = "text/plain" // Tipo de contenido a enviar
        }
        // Mostrar el diálogo para elegir con qué aplicación se desea compartir el mensaje
        startActivity(Intent.createChooser(compartirIntent, "Compartir con"))
    }
}
