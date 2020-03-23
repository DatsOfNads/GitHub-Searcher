package com.company.tochka;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

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
                String stringId = "id: " + currentUser.getId(); //todo стринги
                String stringType = "type: " + currentUser.getType();

                String stringItemPosition = Integer.toString(position + 1);

                itemViewHolder.textViewUserName.setText(stringLogin);
                itemViewHolder.textViewId.setText(stringId);
                itemViewHolder.textViewItemPosition.setText(stringItemPosition);
                itemViewHolder.textViewType.setText(stringType);

                Picasso.get().load(currentUser.getAvatarURL()).into(itemViewHolder.imageView);

                if(position == arrayList.size() - 1){
                    itemViewHolder.viewLine.setVisibility(View.INVISIBLE);
                } else
                    itemViewHolder.viewLine.setVisibility(View.VISIBLE);

                itemViewHolder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCallback.openFullUserInformation(stringLogin);
                    }
                });

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
        if(!isLoadingAdded)
            return;

        isLoadingAdded = false;

        int position = arrayList.size() - 1;
        UserModel user = arrayList.get(position);

        if (user != null) {
            arrayList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void removeAll(){
        removeLoadingFooter();
        arrayList.clear();
        notifyDataSetChanged();
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

        ConstraintLayout constraintLayout;

        TextView textViewUserName,
                textViewId, textViewItemPosition, textViewType;

        ImageView imageView;

        ProgressBar progressBar;

        View viewLine;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            constraintLayout = itemView.findViewById(R.id.view);

            textViewUserName = itemView.findViewById(R.id.textViewLogin);
            textViewId = itemView.findViewById(R.id.textViewId);
            textViewItemPosition = itemView.findViewById(R.id.textViewItemPosition);
            textViewType = itemView.findViewById(R.id.textViewType);

            imageView = itemView.findViewById(R.id.imageView);

            progressBar = itemView.findViewById(R.id.progress);

            viewLine = itemView.findViewById(R.id.viewLine);
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
        void openFullUserInformation(String login);
    }
}