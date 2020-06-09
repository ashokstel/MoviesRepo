package com.example.loadmoreexample.LinearRecyclerView

import android.content.Context
import android.content.Intent
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.loadmoreexample.Constant
import com.restodine.MovieDetailActivity
import com.restodine.R
import com.restodine.login.models.Movie
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_loading.view.*
import kotlinx.android.synthetic.main.item_movie_poster.view.*


class MoviesAdapter(private var itemsCells: ArrayList<Movie>, private var picasso: Picasso) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    lateinit var mcontext: Context

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun addData(dataViews: ArrayList<Movie>) {
        this.itemsCells.addAll(dataViews)
        notifyDataSetChanged()
    }

    fun getItemAtPosition(position: Int): Movie? {
        return itemsCells[position]
    }

    fun addLoadingView() {
        //add loading item
        Handler().post {
            //itemsCells.add(null)
            notifyItemInserted(itemsCells.size - 1)
        }
    }

    fun removeLoadingView() {
        //Remove loading item
        if (itemsCells.size != 0) {
            itemsCells.removeAt(itemsCells.size - 1)
            notifyItemRemoved(itemsCells.size)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mcontext = parent.context
        return if (viewType == Constant.VIEW_TYPE_ITEM) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_movie_poster, parent, false)
            ItemViewHolder(view)
        } else {
            val view = LayoutInflater.from(mcontext).inflate(R.layout.item_loading, parent, false)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                view.progressBar1.indeterminateDrawable.colorFilter =
                    BlendModeColorFilter(Color.WHITE, BlendMode.SRC_ATOP)
            } else {
                view.progressBar1.indeterminateDrawable.setColorFilter(
                    Color.WHITE,
                    PorterDuff.Mode.MULTIPLY
                )
            }
            LoadingViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return itemsCells.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemsCells[position] == null) {
            Constant.VIEW_TYPE_LOADING
        } else {
            Constant.VIEW_TYPE_ITEM
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == Constant.VIEW_TYPE_ITEM) {
            holder.itemView.tv_m_title.text = itemsCells[position].title
            //holder.itemView.tv_years.text = itemsCells[position].year

            Log.e("poster","poster image"+itemsCells.get(position).poster)

            picasso.load(itemsCells.get(position).poster)?.fit()?.into(holder.itemView.item_iv_poster)

            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context, MovieDetailActivity::class.java)
                intent.putExtra("id", itemsCells[position].imdbID)
                intent.putExtra("poster", itemsCells[position].poster)
                holder.itemView.context.startActivity(intent)
            }
        }
    }


}