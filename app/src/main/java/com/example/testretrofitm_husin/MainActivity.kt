package com.example.testretrofitm_husin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.testretrofitm_husin.Adafter.MovieAdapter
import com.example.testretrofitm_husin.Model.Movie.Movie
import com.example.testretrofitm_husin.Model.MovieResponse.MovieResponse
import com.example.testretrofitm_husin.Service.ApiClient
import com.example.testretrofitm_husin.Service.ApiInterface
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val TAG : String = MainActivity::class.java.canonicalName
    private lateinit var movies : ArrayList<Movie>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        rvMovies.layoutManager = GridLayoutManager(applicationContext, 2)

        val apiKey = getString(R.string.api_key)
        val apiInterface : ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        getLatestMovie(apiInterface, apiKey)
        getPopularMovies(apiInterface, apiKey)

        collapseImage.setOnClickListener {
            Toast.makeText(applicationContext, "Poster Gede", Toast.LENGTH_SHORT).show()
        }
    }

    fun getPopularMovies(apiInterface: ApiInterface, apiKey : String) {
        val call : Call<MovieResponse> = apiInterface.getPopularMovie(apiKey)
        call.enqueue(object : Callback<MovieResponse> {
            override fun onFailure(call: Call<MovieResponse>?, t: Throwable?) {
                Log.d("$TAG", call.toString())
            }

            override fun onResponse(call: Call<MovieResponse>?, response: Response<MovieResponse>?) {
                movies = response!!.body()!!.results
                Log.d("$TAG", "Movie size ${movies.size}")
                rvMovies.adapter = MovieAdapter(movies)
            }

        })
    }

    fun getLatestMovie(apiInterface: ApiInterface, apiKey : String) : Movie? {
        val movie : Movie? = null
        val call : Call<Movie> = apiInterface.getMovieLatest(apiKey)
        call.enqueue(object : Callback<Movie> {
            override fun onFailure(call: Call<Movie>?, t: Throwable?) {
                Log.d("$TAG", "Gagal Fetch Popular Movie")
            }

            override fun onResponse(call: Call<Movie>?, response: Response<Movie>?) {
                if (response != null) {
                    var originalTitle : String? = response.body()?.originalTitle
                    var posterPath : String? = response.body()?.posterPath

                    collapseToolbar.title = originalTitle
                    if (posterPath == null) {
                        collapseImage.setImageResource(R.drawable.bg2)
                    } else {
                        val imageUrl = StringBuilder()
                        imageUrl.append(getString(R.string.base_path_poster)).append(posterPath)
                        Glide.with(applicationContext).load(imageUrl.toString()).into(collapseImage)
                    }
                }
            }

        })

        return movie
    }
}