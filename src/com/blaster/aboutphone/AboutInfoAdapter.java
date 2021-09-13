package com.blaster.aboutphone;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.UserManager;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Random;

public class AboutInfoAdapter extends RecyclerView.Adapter<AboutInfoAdapter.MyViewHolder> {
    private List<AboutInfoClass>itemList;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, subtitle;
        public ImageView icon;
        private static final int BUFFER_SIZE = 2;
        int buil_click_counter = 0;
        int anTem = 0;

        int temp = 7;
        boolean insult_trigger = false;
        private static final int SHORT_DELAY = 700; // 700 miliseconds
        private LinearLayout main;
        final Random chance = new Random();
        String[] insults={
                "Hahaha, n00b!",
                "What are you doing??",
                "n00b alert!",
                "Congrats You Are Now Superior Head Developer!",
                "Break Your phone You Stupid!",
                "What is this...? Amateur hour!?",
                "This is not Windows",
                "Please step away from the device!",
                "error code: 1D10T",
                "Go outside",
                "Pro tip: Stop doing this!",
                "Y u no speak computer???",
                "Why are you so stupid?!",
                "Perhaps this Android thing is not for you...",
                "Don't you have anything better to do?!",
                "This is why nobody likes you...",
                "Are you even trying?!"};


        public MyViewHolder(final View parent) {
            super(parent);
            title = (TextView) parent.findViewById(R.id.info_title);
            subtitle = (TextView) parent.findViewById(R.id.info_subtext);
            icon = (ImageView) parent.findViewById(R.id.info_img);
            main = (LinearLayout) parent.findViewById(R.id.main);
            subtitle.setSelected(true);
            title.setSelected(true);

            main.setOnClickListener(v -> {
                //Toast.makeText(itemView.getContext(),subtitle.getText(), Toast.LENGTH_SHORT).show();

                final Vibrator vibrator = (Vibrator) v.getContext().getSystemService(Context.VIBRATOR_SERVICE);

                final VibrationEffect vibrationEffect2;

                int adb = Settings.Secure.getInt(v.getContext().getContentResolver(),
                        Settings.Global.DEVELOPMENT_SETTINGS_ENABLED , 0);


                // create vibrator effect with the constant EFFECT_CLICK
                vibrationEffect2 = VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK);

                // it is safe to cancel other vibrations currently taking place
                SharedPreferences prefs = v.getContext().getSharedPreferences("developer", MODE_PRIVATE);

                String click = title.getText().toString();

                if(click.equals("Android Version")){
                    if(anTem<=5){

                        anTem++;
                    }
                        else
                    {
                        // Add easter egg here
                        showToastMessage("Pixel Blaster Easter Egg", 900, v.getContext());
                        anTem=0;

                    }

                }
                if(click.equals("Build Number")){
                    if(adb==0) {

                        if (buil_click_counter < 7) {
                            buil_click_counter++;
                            String current = "Click " + temp + " Times More for Developer Settings";

                            showToastMessage(current, 700, v.getContext());

                            temp = 7 - buil_click_counter;
                        } else if (buil_click_counter > 7) {
                            buil_click_counter = 0;
                            temp = 7 - buil_click_counter;
                        } else {

                                SharedPreferences.Editor editor = v.getContext().getSharedPreferences("developer", MODE_PRIVATE).edit();
                                editor.putInt("check", 7);
                                editor.putBoolean("tri", true);
                                editor.apply();
                                   final String DEVELOPMENT_SETTINGS_CHANGED_ACTION =
                                          "com.android.settingslib.development.DevelopmentSettingsEnabler.SETTINGS_CHANGED";
                                  Settings.Global.putInt(v.getContext().getContentResolver(),
                                         Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 1);
                                  LocalBroadcastManager.getInstance(v.getContext())
                                           .sendBroadcast(new Intent(DEVELOPMENT_SETTINGS_CHANGED_ACTION));
                                insult_trigger = true;
                            Toast.makeText(v.getContext(), "You are a developer now!", Toast.LENGTH_SHORT).show();



                        }
                    }else{
                        Random r = new Random();
                        int randomNumber = r.nextInt(insults.length);
                        String newIns = (insults[randomNumber]);
                        showToastMessage(newIns, 1500, v.getContext());
                    }
                }



                vibrator.cancel();
                vibrator.vibrate(vibrationEffect2);
            });
        }
    }
    public AboutInfoAdapter(List<AboutInfoClass>itemList){
        this.itemList=itemList;
    }
    @NonNull
    @Override
    public AboutInfoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_card,parent,false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AboutInfoClass row=itemList.get(position);
        holder.title.setText(row.getTitle());
        holder.subtitle.setText(row.getSubtitle());
        holder.icon.setImageResource(row.getImageId());
    }
    @Override
    public int getItemCount() {
        return itemList.size();
    }
    public static class DevelopmentSettingsEnabler {

        public static final String DEVELOPMENT_SETTINGS_CHANGED_ACTION =
                "com.android.settingslib.development.DevelopmentSettingsEnabler.SETTINGS_CHANGED";

        private DevelopmentSettingsEnabler() {
        }

        public static void setDevelopmentSettingsEnabled(Context context, boolean enable) {
            Settings.Global.putInt(context.getContentResolver(),
                    Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, enable ? 1 : 0);
            LocalBroadcastManager.getInstance(context)
                    .sendBroadcast(new Intent(DEVELOPMENT_SETTINGS_CHANGED_ACTION));
        }

        public static boolean isDevelopmentSettingsEnabled(Context context) {
            final UserManager um = (UserManager) context.getSystemService(Context.USER_SERVICE);
            final boolean settingEnabled = Settings.Global.getInt(context.getContentResolver(),
                    Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 1) != 0;
            final boolean hasRestriction = um.hasUserRestriction(
                    UserManager.DISALLOW_DEBUGGING_FEATURES);
            //final boolean isAdmin = um.isAdminUser();
            return  !hasRestriction && settingEnabled;
        }
    }
    public void showToastMessage(String text, int duration, Context context){

        final Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, duration);
    }

}