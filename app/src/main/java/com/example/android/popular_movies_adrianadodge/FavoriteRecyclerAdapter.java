package com.example.android.popular_movies_adrianadodge;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.example.android.popular_movies_adrianadodge.database.FavoriteEntry;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoriteRecyclerAdapter  extends RecyclerView.Adapter<FavoriteRecyclerAdapter.FavoriteRecyclerViewHolder> {
    private List<FavoriteEntry> mFavorites;
    private final FavoriteRecyclerAdapterOnClickHandler mClickHandlerFav;




    public interface FavoriteRecyclerAdapterOnClickHandler {
        void onClick(FavoriteEntry myFavSelected);
    }

    public FavoriteRecyclerAdapter(FavoriteRecyclerAdapterOnClickHandler clickHandler) {
        mClickHandlerFav = clickHandler;
    }
    public class FavoriteRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


       public final ImageView mMovieImageView;

        public FavoriteRecyclerViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            mMovieImageView = view.findViewById(R.id.iv_photo);

        }

        @Override
        public void onClick(View view) {
            // Get the item clicked
            FavoriteEntry myFavSelected = mFavorites.get(getAdapterPosition());
            // Then you can do any actions on it, for example:
            mClickHandlerFav.onClick(myFavSelected);
        }





    }

   /* The adapter provides access to our data. It also provides the views for the displayed items.
    We create our custom recycler adapter by extending the RecyclerView.Adapter class.
    There are three methods that you must implement:
    onCreateViewHolder() – creates a new ViewHolder containing our image
    onBindViewHolder() – displays our image at the specified position in the list
    getItemCount() – gets the number of items in the adapter*/

    @Override
    public FavoriteRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.item_image;
        LayoutInflater inflater = LayoutInflater.from(context);
        View mView = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new FavoriteRecyclerViewHolder(mView);
    }


    @Override
    public void onBindViewHolder(FavoriteRecyclerViewHolder holder, int position) {
        FavoriteEntry favorite = mFavorites.get(position);
        String imageBaseUrl = "http://image.tmdb.org/t/p/w185";
        Picasso.with(holder.mMovieImageView.getContext())
                .load(imageBaseUrl+favorite.getMovieImage())
                .into(holder.mMovieImageView);
    }


    @Override
    public int getItemCount() {
        if (null == mFavorites) return 0;
        return mFavorites.size();
    }

    public void setFavoriteMovieData (List<FavoriteEntry> favoritesData){
        mFavorites = favoritesData;
        notifyDataSetChanged();
    }


}

