package com.vitor.moviesapp.ui

import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.vitor.moviesapp.R
import com.vitor.moviesapp.base.BaseActivity
import com.vitor.moviesapp.model.Movie
import com.vitor.moviesapp.network.service.DiscoverService
import com.vitor.moviesapp.network.service.GenreService
import com.vitor.moviesapp.network.service.SearchService
import com.vitor.moviesapp.ui.adapter.GenresAdapter
import com.vitor.moviesapp.ui.adapter.MoviesAdapter
import com.vitor.moviesapp.util.*
import kotlinx.android.synthetic.main.activity_movies.*
import kotlinx.android.synthetic.main.content_movies.*
import kotlinx.android.synthetic.main.view_movie_details.*
import org.koin.android.ext.android.inject

class MoviesActivity : BaseActivity(), MoviesContract.View, RecyclerViewOnClickListener, FavoriteListUpdateListener{

    private val presenter: MoviesContract.Presenter by inject()
    private val discoverService: DiscoverService by inject()
    private val genreService: GenreService by inject()
    private val searchService: SearchService by inject()

    private val remoteMoviesList = ArrayList<Movie>()
    private lateinit var moviesAdapter: MoviesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLayoutReference(R.layout.activity_movies)
        setSupportActionBar(toolbar)
        setupSortSpinner()
        setupRecyclerView()
        setupPresenter()
    }

    private fun setupSortSpinner(){
        val adapter = ArrayAdapter(
            this,
            R.layout.spinner_sort_item,
            resources.getStringArray(R.array.movies_sort_by_options)
        )
        spinner_sort.background.setColorFilter(ContextCompat.getColor(
            this, R.color.lightRed),
            PorterDuff.Mode.SRC_ATOP
        )
        spinner_sort.adapter = adapter
        spinner_sort.onItemSelectedListener = setUpSpinnerItemSelectedListener()
    }

    private fun setupRecyclerView(){
        moviesAdapter = MoviesAdapter(this)
        rv_movies.adapter = moviesAdapter
        rv_movies.addOnScrollListener(setUpRecyclerViewScrollListener())
    }

    private fun setupPresenter(){
        presenter.attachView(this)
        presenter.attachServices(discoverService, genreService, searchService)
        presenter.attachDataBase(database)

        presenter.getFavoriteMovies()
        presenter.getGenres()
    }

    private fun showMovieDetails(movie: Movie){
        presenter.checkIsFavoriteMovie(movie, iv_favorite_details)

        iv_poster_details.loadImage(NetworkConstants.BASE_POSTER_URL + movie.posterPath)
        tv_title_details.text = movie.title
        tv_overview.text = movie.overview
        rating_bar_details.rating = movie.voteAverage/2
        tv_vote_count_details.text = getString(R.string.parentheses_style, movie.voteCount.toString())
        rv_genrers_details.adapter = GenresAdapter(this, presenter.getMovieGenres(movie))
        tv_release_date_details.text = getString(R.string.parentheses_style, DateUtils.getYear(movie.releaseDate))

        iv_favorite_details.setOnClickListener{
            onFavoriteIconClickedListener(movie, iv_favorite_details)
        }

        panel_layout.panelState = SlidingUpPanelLayout.PanelState.ANCHORED
    }

    private fun setUpSpinnerItemSelectedListener(): AdapterView.OnItemSelectedListener{
        return object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                presenter.sortMovies(position)
            }

        }
    }

    private fun setUpRecyclerViewScrollListener(): RecyclerView.OnScrollListener{
        return object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                presenter.checkListScroll(recyclerView.canScrollVertically(1))
            }
        }
    }

    private fun setUpSearchViewQueryListener(): SearchView.OnQueryTextListener{
        return object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(text: String?): Boolean {
                presenter.searchMoviesByQuery(text!!)
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        }
    }

    private fun setupSearchViewQueryCloseListener(): SearchView.OnCloseListener{
        return SearchView.OnCloseListener {
            presenter.clearQueryText()
            false
        }
    }

    override fun clearRemoteMoviesArray() {
        remoteMoviesList.clear()
        moviesAdapter.updateList(remoteMoviesList)
    }

    override fun listRemoteMovies(movies: List<Movie>) {
        remoteMoviesList.addAll(movies)
        moviesAdapter.updateList(remoteMoviesList)
    }

    override fun reloadMoviesList() {
        moviesAdapter.notifyDataSetChanged()
    }

    override fun onItemClick(position: Int) {
        showMovieDetails(remoteMoviesList[position])
    }

    override fun onFavoriteIconClickedListener(movie: Movie, favoriteIcon: AppCompatImageView) {
        presenter.setFavoriteMovieAction(movie, favoriteIcon)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.movies_list_menu, menu)

        val searchItem = menu?.findItem(R.id.menu_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.setOnQueryTextListener(setUpSearchViewQueryListener())
        searchView.setOnCloseListener(setupSearchViewQueryCloseListener())

        return super.onCreateOptionsMenu(menu)
    }

}
