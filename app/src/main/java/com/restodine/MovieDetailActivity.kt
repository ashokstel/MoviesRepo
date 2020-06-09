package com.restodine

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.restodine.common.network.Status
import com.restodine.login.di.ComponentsProvider
import com.restodine.login.models.Movie
import com.restodine.login.viewmodel.MovieViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.detail_item.*
import kotlinx.android.synthetic.main.fragment_first.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieDetailActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var picasso: Picasso

    private val movieViewModel: MovieViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(MovieViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movie_detail_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle("Movie Detail")

        ComponentsProvider.getLoginComponent().inject(this)
        val id = intent?.extras?.getString("id")

        fetchMovieDetail(id ?: "")

    }


    fun fetchMovieDetail(id: String) {
        movieViewModel.getMovieDetails(id).observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { detail ->

                        picasso.load(detail.poster)?.fit()?.into(iv_poster)
                        tv_title.setText(detail.title)
                        tv_year.setText(detail.year)

                        tv_category.setText(detail.type)
                        tv_running.setText(detail.runtime)
                        tv_rating.setText(""+detail.imdbRating)

                        tv_synposis.setText(detail.plot)

                        tv_score.setText(detail.metascore)
                        tv_reviews.setText(detail.imdbVotes)
                        tv_populating.setText(detail.production)

                        tv_director.setText(detail.director)
                        tv_writer.setText(detail.writer)
                        tv_actor.setText(detail.actors)

                        pb_loading_detail.visibility = View.GONE
                    }

                }
                Status.ERROR -> {
                    pb_loading_detail.visibility = View.GONE
                }
                Status.LOADING -> {
                    pb_loading_detail.visibility = View.VISIBLE
                }
            }
        })
    }
}