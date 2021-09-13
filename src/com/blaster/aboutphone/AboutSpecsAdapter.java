package com.blaster.aboutphone;

import android.content.Context;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AboutSpecsAdapter extends RecyclerView.Adapter<AboutSpecsAdapter.MyViewHolder> {
    private List<AboutSpecsClass> itemList2;




    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, subtitle;
        public ImageView icon;
        private LinearLayout main;
        public MyViewHolder(final View parent) {
            super(parent);
            title = (TextView) parent.findViewById(R.id.info_title);
            subtitle = (TextView) parent.findViewById(R.id.info_subtext);
            icon = (ImageView) parent.findViewById(R.id.info_img);
            main = (LinearLayout) parent.findViewById(R.id.main);
            subtitle.setSelected(true);

            main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Vibrator vibrator = (Vibrator) v.getContext().getSystemService(Context.VIBRATOR_SERVICE);
                    final VibrationEffect vibrationEffect1;

                    vibrationEffect1 = VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK);

                    // it is safe to cancel other vibrations currently taking place
                    vibrator.cancel();
                    vibrator.vibrate(vibrationEffect1);
                }
            });
        }
    }
    public AboutSpecsAdapter(List<AboutSpecsClass> itemList){
        this.itemList2=itemList;
    }
    @Override
    public AboutSpecsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_card,parent,false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AboutSpecsClass row=itemList2.get(position);
        holder.title.setText(row.getTitle());
        holder.subtitle.setText(row.getSubtitle());
        holder.icon.setImageResource(row.getImageId());
    }
    @Override
    public int getItemCount() {
        return itemList2.size();
    }


}