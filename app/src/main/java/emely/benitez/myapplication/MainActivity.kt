package emely.benitez.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import java.util.UUID

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //1 Mandar a llamar los elementos
        val txtCorreoRegis = findViewById<TextView>(R.id.txtCorreoregist)
        val txtContrasenaRegis = findViewById<TextView>(R.id.txtContrasenaRegis)
        val btnRegistrarme = findViewById<Button>(R.id.btnRegistrarme)
        val btnIrLogin = findViewById<Button>(R.id.btnLoginIr)

        btnIrLogin.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }

        //2 Programar el boton guardar
        btnRegistrarme.setOnClickListener {
            if(txtCorreoRegis.text.isEmpty() || txtContrasenaRegis.text.isEmpty())
            {
                    Toast.makeText(this@MainActivity,"complete los campos",Toast.LENGTH_SHORT).show()
            }
            else
            {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        //1- Crear un objeto de la clase conexion
                        val objConexion = ClaseConexion().CadenaConexion()

                        //2. Crear una varaible que contenga un PrepareStatement
                        val addUsuario =
                            objConexion?.prepareStatement("INSERT INTO Usuario (uuid,correo,contrasena) VALUES(?,?,?)")!!
                        addUsuario.setString(1, UUID.randomUUID().toString())
                        addUsuario.setString(2, txtCorreoRegis.text.toString())
                        addUsuario.setString(3, txtContrasenaRegis.text.toString())
                        addUsuario.executeUpdate()

                        withContext(Dispatchers.Main)
                        {
                            startActivity(Intent(this@MainActivity, Login::class.java))
                        }
                    } catch (ex: Exception) {
                        println("Error: " + ex.message)
                    }
                }
            }
        }
    }
}


