package www.iesmurgi.postdeleteapi

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Insercion:AppCompatActivity(){

    private var allMovies = mutableListOf<Movies>()
    private lateinit var btnAniadir:Button
    private lateinit var etId:TextView
    private lateinit var etNombre:EditText
    private lateinit var etFecha:EditText


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.insercion)
        //Se inicia al editar
        metodoEditar()
    }

    fun metodoEditar() {

        val datos = intent.extras

        if (datos != null) {
            btnAniadir = findViewById(R.id.btAniadir)
            btnAniadir.setText("MODIFICAR")

            etId = findViewById(R.id.tvIDPeliculaCoger)
            etNombre = findViewById(R.id.editTextTextPersonName)
            etFecha = findViewById(R.id.editTextDate)


            val retrofit = Retrofit.Builder()
                .baseUrl("https://apiejemplo-2a720-default-rtdb.europe-west1.firebasedatabase.app/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val myApi = retrofit.create(APIService::class.java)

            //miAdapter = MoviesAdapter(allMovies, this)

            myApi.getMovies("peliculas.json/").enqueue(object : Callback<MoviesResponse> {
                override fun onResponse(call: Call<MoviesResponse>, response: Response<MoviesResponse>) {
                    val movies = response.body()?.movies
                    allMovies.addAll(movies!!)
                    println("TAMA??O:" + allMovies.size)
                    //etId.text = (allMovies.size + 1).toString()
                    etId.setText(datos?.getString("ID"))
                    etNombre.setText(datos?.getString("NOMBRE"))
                    etFecha.setText(datos?.getString("FECHA"))
                    // Aqu?? actualizas la interfaz de usuario con los datos cargados de la API.
                    // Por ejemplo:
                    // myTextView.text = myData.someValue
                }

                override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
                    Log.e("MainActivity", "Error al cargar los datos de la API", t)
                }
            })

            btnAniadir = findViewById(R.id.btAniadir)
            btnAniadir.setOnClickListener {
                var numero = etId.getText().toString().toInt()-1
                val peliculaId = etId.text.toString()
                val nombrePelicula = etNombre.text.toString()
                val fechaPelicula = etFecha.text.toString()
                val pelicula = Movies(peliculaId, nombrePelicula, fechaPelicula)
                val position = numero.toString()

                myApi.updatePelicula("peliculas/pelicula/"+position+".json", pelicula).enqueue(object : Callback<Movies> {
                    override fun onResponse(call: Call<Movies>, response: Response<Movies>) {
                        Toast.makeText(this@Insercion, "Pelicula Modificada", Toast.LENGTH_SHORT).show()
                        otraPantalla()
                    }

                    override fun onFailure(call: Call<Movies>, t: Throwable) {
                        // Manejo de errores en la conexi??n o petici??n
                    }
                })
            }
        } else {
            etId = findViewById(R.id.tvIDPeliculaCoger)
            etNombre = findViewById(R.id.editTextTextPersonName)
            etFecha = findViewById(R.id.editTextDate)

            val retrofit = Retrofit.Builder()
                .baseUrl("https://apiejemplo-2a720-default-rtdb.europe-west1.firebasedatabase.app/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val myApi = retrofit.create(APIService::class.java)

            //miAdapter = MoviesAdapter(allMovies, this)

            myApi.getMovies("peliculas.json/").enqueue(object : Callback<MoviesResponse> {
                override fun onResponse(call: Call<MoviesResponse>, response: Response<MoviesResponse>) {
                    val movies = response.body()?.movies
                    allMovies.addAll(movies!!)
                    println("TAMA??O:" + allMovies.size)
                    etId.text = (allMovies.size + 1).toString()
                    // Aqu?? actualizas la interfaz de usuario con los datos cargados de la API.
                    // Por ejemplo:
                    // myTextView.text = myData.someValue
                }

                override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
                    Log.e("MainActivity", "Error al cargar los datos de la API", t)
                }
            })

            btnAniadir = findViewById(R.id.btAniadir)
            btnAniadir.setOnClickListener {

                val peliculaId = etId.text.toString()
                val nombrePelicula = etNombre.text.toString()
                val fechaPelicula = etFecha.text.toString()
                val pelicula = Movies(peliculaId, nombrePelicula, fechaPelicula)
                val position = allMovies.size

                myApi.updatePelicula("peliculas/pelicula/"+position+".json", pelicula).enqueue(object : Callback<Movies> {
                    override fun onResponse(call: Call<Movies>, response: Response<Movies>) {
                        Toast.makeText(this@Insercion, "Pelicula A??adida", Toast.LENGTH_SHORT).show()
                        otraPantalla()
                    }

                    override fun onFailure(call: Call<Movies>, t: Throwable) {
                        // Manejo de errores en la conexi??n o petici??n
                    }
                })
            }

        }


    }

    fun otraPantalla() {
        val enviar= Intent(this, MainActivity::class.java)
        startActivity(enviar)
    }
}