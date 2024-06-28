package modelo

import java.sql.Connection
import java.sql.DriverManager

class ClaseConexion {
    fun CadenaConexion(): Connection? {
        try {
            val url = "jdbc:oracle:thin:@192.162.12.1:1521:xe"

            val usuario = "system"
            val contrasena = ""

            val connection = DriverManager.getConnection(url, usuario, contrasena)
            return connection
        } catch (e: Exception) {
            DriverManager.println("error: $e")
            return null
        }
    }
}