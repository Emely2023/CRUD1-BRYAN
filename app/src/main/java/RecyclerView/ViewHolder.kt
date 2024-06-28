package RecyclerView

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import emely.benitez.myapplication.R

class ViewHolder (view: View): RecyclerView.ViewHolder(view){

    val lbtituloDeTicket  = view.findViewById<TextView>(R.id.lbtituloDeTicket)
    val lbdescripcionDeTicket = view.findViewById<TextView>(R.id.lbdescripcionDeTicket)
    val lbautorDeTicket = view.findViewById<TextView>(R.id.lbautorDeTicket)
    val lbemailDeAutor = view.findViewById<TextView>(R.id.lbemailDeAutor)
    val lbfechaDeCreacionDeTicket = view.findViewById<TextView>(R.id.lbfechaDeCreacionDeTicket)
    val lbestadoDeTicket = view.findViewById<TextView>(R.id.lbestadoDeTicket)
    val lbfechaDeFinalizacionDeTicket = view.findViewById<TextView>(R.id.lbfechaDeFinalizacionDeTicket)
    val imgEliminar = view.findViewById<ImageButton>(R.id.imgEliminar)
    val imgEditar = view.findViewById<ImageButton>(R.id.imgEditar)
}