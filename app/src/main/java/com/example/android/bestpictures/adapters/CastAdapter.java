package com.example.android.bestpictures.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bestpictures.R;
import com.example.android.bestpictures.objects.CastMember;
import com.squareup.picasso.Picasso;

import java.util.List;

/******
 * Created by Victor on 3/15/2018.
 ******/

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {

    private final Context context;
    private List<CastMember> members;

    public CastAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CastAdapter.CastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_cast, parent, false);
        view.setFocusable(true);
        return new CastAdapter.CastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CastAdapter.CastViewHolder holder, int position) {
        String castName = members.get(position).getCastName();
        String castSubtitle = members.get(position).getCastSubtitle();
        String castProfile = members.get(position).getCastProfile();

        holder.vhTitleView.setText(castName);
        holder.vhSubtitleView.setText(castSubtitle);
        Picasso.with(context)
                .load(castProfile)
                .into(holder.vhProfileView);

    }

    @Override
    public int getItemCount() {
        if (members != null) {
            return members.size();
        }
        return 0;
    }

    public void addAll(List<CastMember> members) {
        this.members = members;
        notifyDataSetChanged();
    }

    public class CastViewHolder extends RecyclerView.ViewHolder {
        private final TextView vhTitleView;
        private final TextView vhSubtitleView;
        private final ImageView vhProfileView;

        public CastViewHolder(View itemView) {
            super(itemView);
            vhTitleView = itemView.findViewById(R.id.card_cast_name_view);
            vhSubtitleView = itemView.findViewById(R.id.card_cast_subtitle_view);
            vhProfileView = itemView.findViewById(R.id.card_cast_profile_view);
        }
    }
}
