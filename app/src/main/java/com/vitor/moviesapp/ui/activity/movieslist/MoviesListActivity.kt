package com.vitor.moviesapp.ui.activity.movieslist

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.vitor.moviesapp.R
import com.vitor.moviesapp.base.BaseActivity
import com.vitor.moviesapp.database.FavoriteMoviesObject
import com.vitor.moviesapp.model.Movie
import com.vitor.moviesapp.network.service.DiscoverService
import com.vitor.moviesapp.network.service.GenreService
import com.vitor.moviesapp.network.service.SearchService
import com.vitor.moviesapp.ui.activity.favoritemovies.FavoriteMoviesActivity
import com.vitor.moviesapp.ui.adapter.GenresAdapter
import com.vitor.moviesapp.ui.adapter.MoviesListAdapter
import com.vitor.moviesapp.util.*
import kotlinx.android.synthetic.main.activity_movies_list.*
import kotlinx.android.synthetic.main.content_movies_list.*
import kotlinx.android.synthetic.main.view_empty_list_warning.*
import kotlinx.android.synthetic.main.view_movie_details.*
import org.koin.android.ext.android.inject

class MoviesListActivity : BaseActivity(), MoviesListContract.View, RecyclerViewOnClickListener, FavoriteListUpdateListener{
    private val presenter: MoviesListContract.Presenter by inject()
    private val discoverService: DiscoverService by inject()
    private val genreService: GenreService by inject()
    private val searchService: SearchService by inject()

    private val remoteMoviesList = ArrayList<Movie>()
    private lateinit var moviesListAdapter: MoviesListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLayoutReference(R.layout.activity_movies_list)
        setSupportActionBar(toolbar)
        setupSortSpinner()
        setupRecyclerView()
        setupPresenter()
        setupUnavailableNetworkButton()
    }

    override fun onResume() {
        super.onResume()
        moviesListAdapter.notifyDataSetChanged()
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
        moviesListAdapter = MoviesListAdapter(this)
        rv_movies.adapter = moviesListAdapter
        rv_movies.addOnScrollListener(setUpRecyclerViewScrollListener())
    }

    private fun setupPresenter(){
        presenter.attachView(this)
        presenter.attachServices(discoverService, genreService, searchService)
        presenter.attachDataBase(database)

        presenter.getFavoriteMovies()
        presenter.getGenres()
    }

    private fun setUpSpinnerItemSelectedListener(): AdapterView.OnItemSelectedListener{
        return object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                presenter.sortMovies(this@MoviesListActivity, position)
            }

        }
    }

    private fun setUpRecyclerViewScrollListener(): RecyclerView.OnScrollListener{
        return object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                presenter.checkListScroll(this@MoviesListActivity, recyclerView.canScrollVertically(1))
            }
        }
    }

    private fun setUpSearchViewQueryListener(): SearchView.OnQueryTextListener{
        return object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(text: String?): Boolean {
                presenter.searchMoviesByQuery(this@MoviesListActivity, text!!)
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        }
    }

    private fun setupSearchViewQueryCloseListener(): SearchView.OnCloseListener{
        return SearchView.OnCloseListener {
            presenter.clearQueryText(this@MoviesListActivity)
            false
        }
    }

    private fun setupFavoritesMenuItemClickListener(): MenuItem.OnMenuItemClickListener{
        return MenuItem.OnMenuItemClickListener {
            startActivity(Intent(this@MoviesListActivity, FavoriteMoviesActivity::class.java))
            false
        }
    }

    private fun setupUnavailableNetworkButton(){
        btn_show_favorite.setOnClickListener{startActivity(Intent(
            this@MoviesListActivity,
            FavoriteMoviesActivity::class.java)
        )}
    }

    override fun clearRemoteMoviesArray() {
        remoteMoviesList.clear()
        moviesListAdapter.updateList(remoteMoviesList)
    }

    override fun listRemoteMovies(movies: List<Movie>) {
        remoteMoviesList.addAll(movies)
        moviesListAdapter.updateList(remoteMoviesList)
    }

    override fun showMovieDetails(movie: Movie){
        presenter.checkIsFavoriteMovie(movie, iv_favorite_details)

        iv_poster_details.loadImage(NetworkConstants.BASE_POSTER_URL + movie.posterPath)
        tv_title_details.text = movie.title
        tv_overview.text = movie.overview
        rating_bar_details.rating = movie.voteAverage/2
        tv_vote_count_details.text = getString(R.string.parentheses_style, movie.voteCount.toString())
        rv_genrers_details.adapter = GenresAdapter(this, presenter.getMovieGenres(movie))
        tv_release_year_details.text = getString(R.string.parentheses_style, DateUtils.getYear(movie.releaseDate))

        iv_favorite_details.setOnClickListener{ onFavoriteIconClickedListener(movie, iv_favorite_details) }

        panel_layout.panelState = SlidingUpPanelLayout.PanelState.ANCHORED
    }

    override fun reloadMoviesList() = moviesListAdapter.notifyDataSetChanged()

    override fun onItemClick(position: Int){
        presenter.checkPanelState(remoteMoviesList[position], panel_layout)
    }

    override fun onFavoriteIconClickedListener(movie: Movie, favoriteIcon: AppCompatImageView) = presenter
        .setFavoriteMovieAction(movie, favoriteIcon)

    override fun insertFavoriteMovie(movie: Movie) {
        database.favoriteMovieDao().insertMovie(movie)
        FavoriteMoviesObject.insertMovie(movie)
    }

    override fun removeFavoriteMovie(movie: Movie) {
        database.favoriteMovieDao().deleteMovie(movie)
        FavoriteMoviesObject.removeMovie(movie)
    }

    override fun checkFavoriteIcon(favoriteIcon: AppCompatImageView) {
        favoriteIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_checked))
    }

    override fun unCheckFavoriteIcon(favoriteIcon: AppCompatImageView) {
        favoriteIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_unchecked))
    }

    override fun showEmptyListWarning() {
        tv_empty_list.visibility = View.VISIBLE
        rv_movies.visibility = View.GONE
    }

    override fun showList() {
        tv_empty_list.visibility = View.GONE
        rv_movies.visibility = View.VISIBLE
    }

    override fun showNetworkUnavailableWarning() {
        group_network_unavailable.visibility = View.VISIBLE
    }

    override fun hideNetworkUnavailableWarning() {
        group_network_unavailable.visibility = View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.movies_list_menu, menu)

        val searchItem = menu?.findItem(R.id.menu_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.setOnQueryTextListener(setUpSearchViewQueryListener())
        searchView.setOnCloseListener(setupSearchViewQueryCloseListener())

        val favoritesItem = menu.findItem(R.id.menu_favorites)
        favoritesItem.setOnMenuItemClickListener(setupFavoritesMenuItemClickListener())

        return super.onCreateOptionsMenu(menu)
    }

}
