package com.restodine

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loadmoreexample.LinearRecyclerView.MoviesAdapter
import com.example.loadmoreexample.OnLoadMoreListener
import com.example.loadmoreexample.RecyclerViewLoadMoreScroll
import com.google.gson.Gson
import com.restodine.common.network.Status
import com.restodine.login.di.ComponentsProvider
import com.restodine.login.models.Movie
import com.restodine.login.viewmodel.MovieViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_first.*
import timber.log.Timber
import javax.inject.Inject


class MoviesListFragment : Fragment() {

    lateinit var adapterLinear: MoviesAdapter
    lateinit var scrollListener: RecyclerViewLoadMoreScroll
    lateinit var mLayoutManager: RecyclerView.LayoutManager
    private var pageNo: Int = 1
    private var searchText: String = "Marvel"
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var picasso: Picasso
    private val movieViewModel: MovieViewModel by lazy {
        ViewModelProvider(this.requireActivity(), viewModelFactory).get(MovieViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ComponentsProvider.getLoginComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapterLinear = MoviesAdapter(ArrayList(), picasso)
        //** Set the Layout Manager of the RecyclerView
        setRVLayoutManager()
        fetchMoviesList(pageNo, searchText)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.menu_main, menu)
        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        searchView.queryHint = "Search movie here ......"

        searchView.maxWidth = Integer.MAX_VALUE
        // Auto-complete view
        val searchAutoComplete =
            searchView.findViewById<View>(androidx.appcompat.R.id.search_src_text) as SearchView.SearchAutoComplete
        searchAutoComplete.textSize = 16f
        // setting cursor color
        try {
            val mCursorDrawableRes = TextView::class.java.getDeclaredField("mCursorDrawableRes")
            mCursorDrawableRes.isAccessible = true
        } catch (e: Throwable) {
            Timber.e(e)
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchText = query
                pageNo = 1
                fetchMoviesList(pageNo, searchText)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {

                return true
            }
        })

    }

    fun fetchMoviesList(pageNum: Int, searchText: String) {
        movieViewModel.getMoviesList(searchText).observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { data ->
                        if (data.search?.isEmpty() ?: false && pageNum == 1) {
                            tv_no_data.visibility = View.VISIBLE
                            recycler_view.visibility = View.GONE
                            pb_loading.visibility = View.GONE
                            return@Observer
                        }
                        recycler_view.visibility = View.VISIBLE
                        tv_no_data.visibility = View.GONE

                        Log.e("data ", "respone is " + Gson().toJson(data))
                        setAdapter(data.search as ArrayList<Movie>, picasso)
                        pb_loading.visibility = View.GONE

                        //** Set the Layout Manager of the RecyclerView
                        //setRVLayoutManager()
                        //** Set the scrollListerner of the RecyclerView
                        //setRVScrollListener()


                    }
                }
                Status.ERROR -> {
                    pb_loading.visibility = View.GONE

                    showToast("Some thing went wrong")
                }
                Status.LOADING -> {
                    pb_loading.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT)
            .show();
    }

    private fun setAdapter(items: ArrayList<Movie>, picasso: Picasso) {
        adapterLinear = MoviesAdapter(items, picasso)
        adapterLinear.notifyDataSetChanged()
        recycler_view.adapter = adapterLinear
    }

    private fun setRVLayoutManager() {
        //mLayoutManager = LinearLayoutManager(this.requireContext())

        val mLayoutManager =
            GridLayoutManager(this.requireContext(), 2, GridLayoutManager.VERTICAL, false);

        recycler_view.layoutManager = mLayoutManager
        recycler_view.setHasFixedSize(true)
    }

    private fun setRVScrollListener() {
        mLayoutManager = LinearLayoutManager(this.requireContext())
        scrollListener = RecyclerViewLoadMoreScroll(mLayoutManager as LinearLayoutManager)
        scrollListener.setOnLoadMoreListener(object :
            OnLoadMoreListener {
            override fun onLoadMore() {
                LoadMoreData()
            }
        })
        recycler_view.addOnScrollListener(scrollListener)
    }

    private fun LoadMoreData() {
        pageNo = pageNo + 1
        fetchMoviesList(pageNo, searchText)
    }
}
