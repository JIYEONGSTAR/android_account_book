package com.example.food_account.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_account.PostInfo;
import com.example.food_account.R;
import com.example.food_account.listener.OnPostListener;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;

public class PopupAdapter extends RecyclerView.Adapter<PopupAdapter.ViewHolder>{
    private  static final String TAG="PopupAdapter";
    private ArrayList<PostInfo> mDataset;
    private Activity activity;
    private FirebaseFirestore firebaseFirestore;
    private OnPostListener onPostListener;

    static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;

        public ViewHolder(CardView view) {
            super(view);
            cardView = view;
        }
    }

    public PopupAdapter(Activity activity, ArrayList<PostInfo> myDataset) {
        mDataset = myDataset;
        this.activity = activity;
        firebaseFirestore = FirebaseFirestore.getInstance();
    }
    public void setOnPostListener(OnPostListener onPostListener){
        this.onPostListener = onPostListener;
    }
    @Override
    public int getItemViewType(int position){
        return position;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public PopupAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        CardView cardView =(CardView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_post, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        cardView.findViewById(R.id.content_menu).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showPopup(view,viewHolder.getAdapterPosition());
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Log.d(TAG, String.valueOf(mDataset));
        TextView titleView = viewHolder.cardView.findViewById(R.id.item_title_textView);
        titleView.setText(mDataset.get(position).getTitle());

        TextView priceView = viewHolder.cardView.findViewById(R.id.item_price_textView);
        priceView.setText(mDataset.get(position).getPrice().toString()+"원");

        TextView createdAtView = viewHolder.cardView.findViewById(R.id.item_createdAt_textView);
//        createdAtView.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(mDataset.get(position).getDate()));
        createdAtView.setText(mDataset.get(position).getDate());

        TextView keywordView = viewHolder.cardView.findViewById(R.id.item_keyword_textView);
        keywordView.setText(mDataset.get(position).getKeyword());

    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    //삭제
    private void showPopup(View v, int position) {
        PopupMenu popup = new PopupMenu(activity, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                String documentId = mDataset.get(position).getDocumentId();
                switch (menuItem.getItemId()) {
                    case R.id.delete:
                        Log.d(TAG,documentId);
                        onPostListener.onDelete(documentId);
                        return true;
                    default:
                        return false;
                }
            }
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.post, popup.getMenu());
        popup.show();
    }

}
