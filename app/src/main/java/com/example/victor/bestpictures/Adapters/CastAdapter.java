package com.example.victor.bestpictures.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.victor.bestpictures.Model.CastMember;
import com.example.victor.bestpictures.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Victor on 3/15/2018.
 */

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {

    private Context context;
    private List<CastMember> members;

    public CastAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CastAdapter.CastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_cast, parent, false);
        view.setFocusable(true);
        return new CastAdapter.CastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CastAdapter.CastViewHolder holder, int position) {
        holder.vhTitleView.setText(members.get(position).getCastName());
        holder.vhSubtitleView.setText(members.get(position).getCastSubtitle());
            Picasso.with(context)
                    .load(members.get(position).getCastProfile())
                    .into(holder.vhProfileView);
    }

    @Override
    public int getItemCount() {
        if (members != null) {
            return members.size();
        }
        return 0;
    }

    public class CastViewHolder extends RecyclerView.ViewHolder {
        private TextView vhTitleView;
        private TextView vhSubtitleView;
        private ImageView vhProfileView;

        public CastViewHolder(View itemView) {
            super(itemView);
            vhTitleView = itemView.findViewById(R.id.card_cast_name_view);
            vhSubtitleView = itemView.findViewById(R.id.card_cast_subtitle_view);
            vhProfileView = itemView.findViewById(R.id.card_cast_profile_view);
        }
    }

    public void addAll(List<CastMember> members) {
        this.members = members;
        notifyDataSetChanged();
    }
}
