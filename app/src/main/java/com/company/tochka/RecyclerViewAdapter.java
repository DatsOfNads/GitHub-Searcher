package com.company.tochka;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;

    private ArrayList<UserModel> arrayList;

    RecyclerViewAdapter(Context context, ArrayList<UserModel> userModelArrayList){

        this.arrayList = userModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.fragment_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        UserModel currentUser = arrayList.get(position);

        String stringLogin = currentUser.getLogin();
        String stringScore = "Рейтинг - " + currentUser.getScore();//todo стринги
        String stringId = "id: " + currentUser.getId();


        String stringItemPosition = Integer.toString(position + 1);

        ((ViewHolder) holder).textViewUserName.setText(stringLogin);
        ((ViewHolder) holder).textViewUserScore.setText(stringScore);
        ((ViewHolder) holder).textViewId.setText(stringId);
        ((ViewHolder) holder).textViewItemPosition.setText(stringItemPosition);

        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        builder.build().load(arrayList.get(position).getAvatarURL())
                .placeholder((R.drawable.ic_launcher_background))
                .error(R.drawable.ic_launcher_background)
                .into(((ViewHolder) holder).imageView);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder{

        TextView textViewUserName, textViewUserScore,
                textViewId, textViewItemPosition;

        ImageView imageView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewUserName = itemView.findViewById(R.id.textViewUserName);
            textViewUserScore = itemView.findViewById(R.id.textViewUserScore);
            textViewId = itemView.findViewById(R.id.textViewId);
            textViewItemPosition = itemView.findViewById(R.id.textViewItemPosition);

            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
