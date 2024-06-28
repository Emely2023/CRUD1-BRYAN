package modelo

data class tbTickets(
    val uuid: String,
    var TituloTicket: String,
    var Descripcion: String,
    var Autor: String,
    var Email: String,
    val Creacion: String,
    var Estado: String,
    var Finalizacion: String

)
