package com.example.android.popular_movies_adrianadodge;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popular_movies_adrianadodge.model.Review;

import java.util.ArrayList;

public class ReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewRecyclerAdapter.ReviewRecyclerViewHolder> {
    private ArrayList<Review> mReviews;


    public class ReviewRecyclerViewHolder extends RecyclerView.ViewHolder   {
        public final TextView mReviewTextView;
        public final TextView mReviewAuthorTextView;
        public ReviewRecyclerViewHolder(View view) {
            super(view);
            mReviewTextView = view.findViewById(R.id.review_tv);
            mReviewAuthorTextView = view.findViewById(R.id.author_tv);
        }
    }

   /* The adapter provides access to our data. It also provides the views for the displayed items.
    We create our custom recycler adapter by extending the RecyclerView.Adapter class.
    There are three methods that you must implement:
    onCreateViewHolder() – creates a new ViewHolder containing our image
    onBindViewHolder() – displays our image at the specified position in the list
    getItemCount() – gets the number of items in the adapter*/

    @Override
    public ReviewRecyclerAdapter.ReviewRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem2 = R.layout.item_review;
        LayoutInflater inflater = LayoutInflater.from(context);
        View mView = inflater.inflate(layoutIdForListItem2, viewGroup, false);
        return new ReviewRecyclerAdapter.ReviewRecyclerViewHolder(mView);
    }


    @Override
    public void onBindViewHolder(ReviewRecyclerAdapter.ReviewRecyclerViewHolder holder, int position) {
        Review review = mReviews.get(position);

        TextView textViewAuthor =  holder.mReviewAuthorTextView;
        TextView textViewReview = holder.mReviewTextView;
        textViewAuthor.append("Review By "+ review.getAuthor());
        textViewReview.append(review.getContent());

    }


    @Override
    public int getItemCount() {
        if (null == mReviews) return 0;
        return mReviews.size();
    }

    public void setReviewData (ArrayList<Review> reviewData){
        mReviews = reviewData;
        notifyDataSetChanged();
    }

}
