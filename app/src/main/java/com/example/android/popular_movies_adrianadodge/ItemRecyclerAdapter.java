package com.example.android.popular_movies_adrianadodge;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popular_movies_adrianadodge.model.Movie;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;


public class ItemRecyclerAdapter extends RecyclerView.Adapter<ItemRecyclerAdapter.ItemRecyclerViewHolder> {
    private ArrayList<Movie> mMovies;
    private final ItemRecyclerAdapterOnClickHandler mClickHandler;


    public interface ItemRecyclerAdapterOnClickHandler {
        void onClick(Movie selectedMovie);
    }

    public ItemRecyclerAdapter(ItemRecyclerAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

      public class ItemRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView mMovieImageView;

        public ItemRecyclerViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            mMovieImageView = view.findViewById(R.id.iv_photo);
        }

        @Override
        public void onClick(View view) {
            // Get the item clicked
            Movie myMovieSelected = mMovies.get(getAdapterPosition());
            // Then you can do any actions on it, for example:
            mClickHandler.onClick(myMovieSelected);
        }


    }

   /* The adapter provides access to our data. It also provides the views for the displayed items.
    We create our custom recycler adapter by extending the RecyclerView.Adapter class.
    There are three methods that you must implement:
    onCreateViewHolder() – creates a new ViewHolder containing our image
    onBindViewHolder() – displays our image at the specified position in the list
    getItemCount() – gets the number of items in the adapter*/

    @Override
    public ItemRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.item_image;
        LayoutInflater inflater = LayoutInflater.from(context);
        View mView = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new ItemRecyclerViewHolder(mView);
    }


    @Override
    public void onBindViewHolder(ItemRecyclerViewHolder holder, int position) {
        Movie movie = mMovies.get(position);
        String imageBaseUrl = "http://image.tmdb.org/t/p/w185";
        Picasso.with(holder.mMovieImageView.getContext())
                .load(imageBaseUrl+movie.getImagePath())
                .into(holder.mMovieImageView);

    }


    @Override
    public int getItemCount() {
        if (null == mMovies) return 0;
        return mMovies.size();
    }

    public void setMovieData (ArrayList<Movie> movieData){
        mMovies = movieData;
        notifyDataSetChanged();
    }


}
