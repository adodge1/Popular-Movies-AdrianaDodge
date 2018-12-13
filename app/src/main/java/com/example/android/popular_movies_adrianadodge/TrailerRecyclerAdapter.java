package com.example.android.popular_movies_adrianadodge;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.example.android.popular_movies_adrianadodge.model.Trailer;


import java.util.ArrayList;

public class TrailerRecyclerAdapter extends RecyclerView.Adapter<TrailerRecyclerAdapter.TrailerRecyclerViewHolder> {
    private ArrayList<Trailer> mTrailers;
    private final TrailerRecyclerAdapter.TrailerRecyclerAdapterOnClickHandler mClickHandler;


    public interface TrailerRecyclerAdapterOnClickHandler {
        void onClick(Trailer selectedTrailer);
    }

    public TrailerRecyclerAdapter(TrailerRecyclerAdapter.TrailerRecyclerAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class TrailerRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


      public final Button mTrailerButton;

        public TrailerRecyclerViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);

            mTrailerButton = view.findViewById(R.id.btn_trailer);
            //Since this is a Button we need to set this!
            mTrailerButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // Get the item clicked
            Trailer myTrailerSelected = mTrailers.get(getAdapterPosition());
            // Then you can do any actions on it, for example:
            mClickHandler.onClick(myTrailerSelected);
        }


    }

   /* The adapter provides access to our data. It also provides the views for the displayed items.
    We create our custom recycler adapter by extending the RecyclerView.Adapter class.
    There are three methods that you must implement:
    onCreateViewHolder() – creates a new ViewHolder containing our image
    onBindViewHolder() – displays our image at the specified position in the list
    getItemCount() – gets the number of items in the adapter*/

    @Override
    public TrailerRecyclerAdapter.TrailerRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.item_trailer;
        LayoutInflater inflater = LayoutInflater.from(context);
        View mView = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new TrailerRecyclerAdapter.TrailerRecyclerViewHolder(mView);
    }


    @Override
    public void onBindViewHolder(TrailerRecyclerAdapter.TrailerRecyclerViewHolder holder, int position) {
        Trailer trailer = mTrailers.get(position);
        Button button = holder.mTrailerButton;
        button.setText(trailer.getName());

    }


    @Override
    public int getItemCount() {
        if (null == mTrailers) return 0;
        return mTrailers.size();
    }

    public void setTrailerData (ArrayList<Trailer> trailerData){
        mTrailers = trailerData;
        notifyDataSetChanged();
    }


}
