package emely.benitez.myapplication

import RecyclerViewHelper.Adaptador
import android.os.Bundle
import android.widget.Button
import android.widget.ListAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import modelo.tbTickets
import java.util.UUID

class GestordeTickets : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gestorde_tickets)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //1 Mandar a llamar los elementos////
        val txtTitulo = findViewById<TextView>(R.id.txtTituloTicket)
        val txtDescricion = findViewById<TextView>(R.id.txtDescripcion)
        val txtAutor = findViewById<TextView>(R.id.txtAutor)
        val txtEmail = findViewById<TextView>(R.id.txtEmail)
        val txtCreacion = findViewById<TextView>(R.id.txtCreacion)
        val txtEstado = findViewById<TextView>(R.id.txtEstado)
        val txtFinalizacion = findViewById<TextView>(R.id.txtFinalizacion)
        val btnbtnGuardar = findViewById<Button>(R.id.btnGuardar)
        val rcvTickets = findViewById<RecyclerView>(R.id.rcvTickets)

        rcvTickets.layoutManager = LinearLayoutManager(this)

        /////////// TODO: mostrar datos

        fun obtenerTickets(): List<tbTickets>{

            //Creo un objeto de la clase conexion
            val objConexion = ClaseConexion().CadenaConexion()

            //2- Creo un statement
            val Statement = objConexion?.createStatement()
            val resultSet = Statement?.executeQuery("select * from Tickets")!!

            val listaTickets = mutableListOf<tbTickets>()

                while (resultSet.next()) {
                    val uuid = resultSet.getString("uuid")
                    val TituloTicket = resultSet.getString("titulo")
                    val Descripcion = resultSet.getString("descripcion")
                    val Autor = resultSet.getString("autor")
                    val Email = resultSet.getString("email_contacto")
                    val Creacion = resultSet.getString("fecha_creacion")
                    val Estado = resultSet.getString("estado")
                    val Finalizacion = resultSet.getString("fecha_finalizacion")

                    val valoresJuntos = tbTickets(
                        uuid,
                        TituloTicket,
                        Descripcion,
                        Autor,
                        Email,
                        Creacion,
                        Estado,
                        Finalizacion
                    )

                    listaTickets.add(valoresJuntos)
                }
            return listaTickets

        }

        //Asignarle el adaptador el recycler
        CoroutineScope(Dispatchers.IO).launch {
            val TicketsDB = obtenerTickets()
            withContext(Dispatchers.Main){
                val adapter = Adaptador(TicketsDB)
                rcvTickets.adapter = adapter
            }
        }

        //2 Programar el boton guardar
        btnbtnGuardar.setOnClickListener {
            if(txtTitulo.text.isEmpty() || txtDescricion.text.isEmpty() || txtAutor.text.isEmpty() || txtEmail.text.isEmpty() || txtCreacion.text.isEmpty() || txtEstado.text.isEmpty() || txtFinalizacion.text.isEmpty())
            {
                Toast.makeText(this@GestordeTickets, "Debe llenar todos los campos", Toast.LENGTH_LONG).show()
            }
            else
            {
                CoroutineScope(Dispatchers.IO).launch {

                    //1- Crear un objeto de la clase conexion
                    val objConexion = ClaseConexion().CadenaConexion()

                    //2. Crear una varaible que contenga un PrepareStatement
                    val addTickets = objConexion?.prepareStatement("insert into Tickets (uuid,titulo, descripcion, autor, email_contacto, fecha_creacion, estado, fecha_finalizacion) values (?,?,?,?,?,?,?,?)")!!
                    addTickets.setString(1,UUID.randomUUID().toString())
                    addTickets.setString(2,txtTitulo.text.toString())
                    addTickets.setString(3,txtDescricion.text.toString())
                    addTickets.setString(4,txtAutor.text.toString())
                    addTickets.setString(5,txtEmail.text.toString())
                    addTickets.setString(6,txtCreacion.text.toString())
                    addTickets.setString(7,txtEstado.text.toString())
                    addTickets.setString(8,txtFinalizacion.text.toString())
                    addTickets.executeUpdate()

                    //Refresco la lista
                    val nuevosTickets = obtenerTickets()
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@GestordeTickets, "Ticket agregado con éxito" , Toast.LENGTH_LONG).show()
                        (rcvTickets.adapter as? Adaptador)?.actualizarLista(nuevosTickets)
                    }
                    val ticket = obtenerTickets()
                    withContext(Dispatchers.Main){
                        (rcvTickets.adapter as? Adaptador)?.actualizarLista(ticket)
                        Toast.makeText(
                            this@GestordeTickets,
                            "Se agregó el ticket correctamente",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            }
        }
    }
}