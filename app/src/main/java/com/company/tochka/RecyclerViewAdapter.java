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
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.company.tochka.databinding.FragmentUserBinding;
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

                FragmentUserBinding itemBinding =
                        FragmentUserBinding.inflate(layoutInflater, parent, false);

                viewHolder = new ItemViewHolder(itemBinding);
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

                UserModel currentUser = arrayList.get(position);
                currentUser.setNumberInList(Integer.toString(position + 1));

                if(position == arrayList.size() - 1)
                    currentUser.setLastInList(true);

                ((ItemViewHolder) holder).bind(currentUser);

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

        private final FragmentUserBinding binding;

        private TextView textView;

        public ItemViewHolder(FragmentUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(UserModel userModel){
            binding.setUser(userModel);
            binding.executePendingBindings();

            binding.view.setOnClickListener(v -> mCallback.openFullUserInformation(userModel.getLogin()));
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