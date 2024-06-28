package RecyclerViewHelper

import RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import emely.benitez.myapplication.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import modelo.tbTickets


class Adaptador(private var Datos: List<tbTickets>) : RecyclerView.Adapter<ViewHolder>() {

    fun actualizarLista(nuevaLista:List<tbTickets>) {
        Datos = nuevaLista
    }
    ////////////////////TODO: Eliminar Datos
    fun eliminarDatos(TituloTicket: String, position: Int){
        //Actualizo la lista de datos y notifico al adaptador
        val ListaDatos = Datos.toMutableList()
        ListaDatos.removeAt(position)

    GlobalScope.launch(Dispatchers.IO) {
        //1- Creamos un objeto de la clase conexion
        val objConexion = ClaseConexion().CadenaConexion()
        //2- Crear una variable que contenga un PrepareStatement
        val delDato = objConexion?.prepareStatement("delete from Tickets where titulo = ?")!!
        delDato.setString(1, TituloTicket)
        delDato.executeUpdate()

        val commit = objConexion.prepareStatement("commit")!!
        commit.executeUpdate()
    }
        Datos = ListaDatos.toList()
        // Notificar al adaptador sobre los cambios
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    fun actualizarPantalla(
        Titulo: String,
        Descripcion: String,
        Autor: String,
        Autoremail: String,
        TicketStatus: String,
        Finishdate: String,
        UUID_Tickets: String
    ) {
        val index = Datos.indexOfFirst { it.uuid == UUID_Tickets }
        Datos[index].TituloTicket = Titulo
        Datos[index].Descripcion = Descripcion
        Datos[index].Autor = Autor
        Datos[index].Email = Autoremail
        Datos[index].Estado = TicketStatus
        Datos[index].Finalizacion = Finishdate

        notifyDataSetChanged()
    }

    fun actualizarDato(
        Titulo: String,
        Descripcion: String,
        Autor: String,
        Autoremail: String,
        TicketStatus: String,
        Finishdate: String,
        UUID_Tickets: String
    ) {
        GlobalScope.launch(Dispatchers.IO) {

            //1- Creo un objeto de la clase de conexion
            val objConexion = ClaseConexion().CadenaConexion()

            //2- creo una variable que contenga un PrepareStatement
            val addTicket =
                objConexion?.prepareStatement("UPDATE Tickets SET titulo = ?, descripcion = ?, autor = ?, email_contacto = ?, estado = ?,fecha_finalizacion = ?  WHERE uuid = ?")!!
            addTicket.setString(1, Titulo)
            addTicket.setString(2, Descripcion)
            addTicket.setString(3, Autor)
            addTicket.setString(4, Autoremail)
            addTicket.setString(5, TicketStatus)
            addTicket.setString(6, Finishdate)
            addTicket.setString(7, UUID_Tickets)
            addTicket.executeUpdate()

            withContext(Dispatchers.Main) {
                actualizarPantalla(
                    Titulo,
                    Descripcion,
                    Autor,
                    Autoremail,
                    TicketStatus,
                    Finishdate,
                    UUID_Tickets
                )
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_itemcard, parent, false)

        return ViewHolder(vista)
    }
    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ticket = Datos[position]
        holder.lbtituloDeTicket.text = ticket.TituloTicket
        holder.lbdescripcionDeTicket.text = ticket.Descripcion
        holder.lbautorDeTicket.text = ticket.Autor
        holder.lbemailDeAutor.text = ticket.Autor
        holder.lbfechaDeCreacionDeTicket.text = ticket.Creacion
        holder.lbestadoDeTicket.text = ticket.Estado
        holder.lbfechaDeFinalizacionDeTicket.text = ticket.Finalizacion

        holder.imgEliminar.setOnClickListener {

            //Creamos un Alert Dialog
            val context = holder.itemView.context

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Eliminar")
            builder.setMessage("¿Desea eliminar el ticket?")

            //Botones
            builder.setPositiveButton("Si") { dialog, which ->
                eliminarDatos(ticket.TituloTicket, position)
                Toast.makeText(context, "Ticket eliminado correctamente", Toast.LENGTH_SHORT).show()
            }

            builder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()

        }

        holder.imgEditar.setOnClickListener {
            val context = holder.itemView.context

            val layout = LinearLayout(context)
            layout.orientation = LinearLayout.VERTICAL

            val txt1 = EditText(context)
            layout.addView(txt1)
            txt1.setText(ticket.TituloTicket)
            val txt2 = EditText(context)
            layout.addView(txt2)
            txt2.setText(ticket.Descripcion)
            val txt3 = EditText(context)
            layout.addView(txt3)
            txt3.setText(ticket.Autor)
            val txt4 = EditText(context)
            layout.addView(txt4)
            txt4.setText(ticket.Autor)
            val txt5 = EditText(context)
            txt5.setText(ticket.Estado)
            layout.addView(txt5)
            val txt6 = EditText(context)
            txt6.setText(ticket.Finalizacion)
            layout.addView(txt6)

            val uuid = ticket.uuid

            val builder = AlertDialog.Builder(context)
            builder.setView(layout)
            builder.setTitle("Editar Ticket")


            builder.setPositiveButton("Aceptar") { dialog, which ->
                actualizarDato(
                    txt1.text.toString(),
                    txt2.text.toString(),
                    txt3.text.toString(),
                    txt4.text.toString(),
                    txt5.text.toString(),
                    txt6.text.toString(),
                    uuid
                )
                Toast.makeText(context, "Ticket editado correctamente", Toast.LENGTH_SHORT).show()

            }

            builder.setNegativeButton("Cancelar") { dialog, which ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()

        }
    }

}
