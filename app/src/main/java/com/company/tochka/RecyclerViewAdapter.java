package com.company.tochka;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private boolean isLoadingAdded;

    private Context context;

    private ArrayList<UserModel> arrayList = new ArrayList<>();

    RecyclerViewAdapterCallback mCallback;

    RecyclerViewAdapter(Context context){
        this.context = context;
        this.mCallback = (RecyclerViewAdapterCallback) context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        switch (viewType){

            case ITEM:

                View viewItem = layoutInflater.inflate(R.layout.fragment_user, parent, false);
                viewHolder = new ItemViewHolder(viewItem);

                return viewHolder;

            case LOADING:

                View viewLoading = layoutInflater.inflate(R.layout.fragment_loading, parent, false);
                viewHolder = new LoadingViewHolder(viewLoading);

                return viewHolder;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)){

            case ITEM:

                final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

                UserModel currentUser = arrayList.get(position);

                String stringLogin = currentUser.getLogin();
                String stringScore = "Рейтинг - " + currentUser.getScore();//todo стринги
                String stringId = "id: " + currentUser.getId();


                String stringItemPosition = Integer.toString(position + 1);

                itemViewHolder.textViewUserName.setText(stringLogin);
                itemViewHolder.textViewUserScore.setText(stringScore);
                itemViewHolder.textViewId.setText(stringId);
                itemViewHolder.textViewItemPosition.setText(stringItemPosition);

                Picasso.get().load(currentUser.getAvatarURL()).into(itemViewHolder.imageView);

            case LOADING:

                //final LoadingViewHolder loadingViewHolder = (RecyclerViewAdapter.LoadingViewHolder) holder;


        }
    }
    
    void addAll(ArrayList<UserModel> arrayList){

        for (UserModel userModel : arrayList) {
            this.arrayList.add(userModel);
            notifyItemChanged(arrayList.size() - 1);
        }
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        arrayList.add(new UserModel());
        notifyItemInserted(arrayList.size() - 1);
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = arrayList.size() - 1;
        UserModel user = arrayList.get(position);

        if (user != null) {
            arrayList.remove(position);
            notifyItemRemoved(position);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList == null ? 0 : arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {

        if(position == arrayList.size() - 1 && isLoadingAdded)
            return LOADING;
         else
            return ITEM;

    }

    private class ItemViewHolder extends RecyclerView.ViewHolder{

        TextView textViewUserName, textViewUserScore,
                textViewId, textViewItemPosition;

        ImageView imageView;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewUserName = itemView.findViewById(R.id.textViewUserName);
            textViewUserScore = itemView.findViewById(R.id.textViewUserScore);
            textViewId = itemView.findViewById(R.id.textViewId);
            textViewItemPosition = itemView.findViewById(R.id.textViewItemPosition);

            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder{

        private ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);

            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    public interface RecyclerViewAdapterCallback {
        void retryPageLoad();
    }
}