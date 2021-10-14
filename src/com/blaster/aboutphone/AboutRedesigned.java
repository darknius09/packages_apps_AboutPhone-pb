package com.blaster.aboutphone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class AboutRedesigned extends AppCompatActivity {
    private static final int BUFFER_SIZE = 2;
    private final List<AboutInfoClass> itemList = new ArrayList<>();
    private RecyclerView recyclerview;
    private AboutInfoAdapter mAdapter;
    private final List<AboutSpecsClass> itemList2 = new ArrayList<AboutSpecsClass>();
    private RecyclerView recyclerview2;
    private AboutSpecsAdapter mAdapter2;

    // Change these values
    String  release,
            codeName,
            securityPatch;
     String  backCam, cores;
     String  soc;
    TextView mai, pb, dn,cn ;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_redesigned);


        dn = findViewById(R.id.deviceNameTV);
        cn = findViewById(R.id.codeNameTV);
        pb = findViewById(R.id.pbVTV);
        mai = findViewById(R.id.maintainerTV);




        recyclerview = findViewById(R.id.rv_info);
        mAdapter = new AboutInfoAdapter(itemList);

        recyclerview2 = findViewById(R.id.rv_specs);
        mAdapter2 = new AboutSpecsAdapter(itemList2);

        if(getMaintainer().isEmpty()){
            mai.setText("unknown");
        }else{
            mai.setText(getMaintainer());
        }
        if(getPb().isEmpty()){
            pb.setText("unknown");
        }else{
            pb.setText(getPb());
        }
        if(getDevice().isEmpty()){
            dn.setText("unknown");
        }else{
            dn.setText(getDevice());
        }
        if(getDeviceCode().isEmpty()){
            cn.setText("unknown");
        }else{
            cn.setText(getDeviceCode());
        }
        if(getSOC().isEmpty()){
            soc="unknown";
        }else{
            soc = getSOC();
        }
        if(getCamera().isEmpty()){
            backCam="unknown";
        }else{
            backCam = getCamera();
        }if(getCores().isEmpty()){
            cores="unknown";
        }else{
            cores = getCores();            
        }




        try {
            getInfo();

        } catch (IOException e) {
            e.printStackTrace();
        }


        RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerview.setLayoutManager(mLayoutManger);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(mAdapter);
        prepareItem();

        RecyclerView.LayoutManager mLayoutManger2 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerview2.setLayoutManager(mLayoutManger2);
        recyclerview2.setItemAnimator(new DefaultItemAnimator());
        recyclerview2.setAdapter(mAdapter2);
        try {
            prepareItem2();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void prepareItem() {
        AboutInfoClass item = new AboutInfoClass(R.drawable.ic_baseline_edit_24, "Build Number", codeName);
        itemList.add(item);
        item = new AboutInfoClass(R.drawable.ic_baseline_info_24, "SELinux Status", selinux());
        itemList.add(item);
        item = new AboutInfoClass(R.drawable.ic_baseline_adb_24, "Kernel Version", readKernelVersion());
        itemList.add(item);
        item = new AboutInfoClass(R.drawable.ic_baseline_security_24, "Android Security Patch", securityPatch);
        itemList.add(item);
        item = new AboutInfoClass(R.drawable.ic_baseline_android_24, "Android Version", release);
        itemList.add(item);
        mAdapter.notifyDataSetChanged();
        recyclerview.setAdapter(mAdapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void prepareItem2() throws IOException {
        AboutSpecsClass item = new AboutSpecsClass(R.drawable.ic_baseline_camera_24, "Camera", backCam);
        itemList2.add(item);
        item = new AboutSpecsClass(R.drawable.ic_baseline_fullscreen_24, "Screen", getScreenResolution());
        itemList2.add(item);
        item = new AboutSpecsClass(R.drawable.ic_baseline_battery_charging_full_24, "Battery", String.valueOf(getBatteryCapacity()) + " mAh");
        itemList2.add(item);
        item = new AboutSpecsClass(R.drawable.ic_baseline_auto_awesome_mosaic_24, "SOC", soc);
        itemList2.add(item);
        item = new AboutSpecsClass(R.drawable.ic_baseline_auto_awesome_motion_24, "Cores", cores));
        itemList2.add(item);
        mAdapter.notifyDataSetChanged();
        recyclerview2.setAdapter(mAdapter2);
    }

    public void getInfo() throws IOException {
        getAndroidVersion();
        getCodeName();
        getPatchName();
        readKernelVersion();
        getBatteryCapacity();


    }

    public void getAndroidVersion() {
        release = Build.VERSION.RELEASE;
    }

    public void getCodeName() {
        codeName = Build.ID;

    }

    public void getPatchName() {
        securityPatch = Build.VERSION.SECURITY_PATCH;

    }

    public static String readKernelVersion() {
        try {
            Process p = Runtime.getRuntime().exec("uname -a");
            InputStream is = null;
            if (p.waitFor() == 0) {
                is = p.getInputStream();
            } else {
                is = p.getErrorStream();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is),
                    BUFFER_SIZE);
            String line = br.readLine();
            br.close();
            return line;
        } catch (Exception ex) {
            return "ERROR: " + ex.getMessage();
        }

    }

    @SuppressLint("PrivateApi")
    public int getBatteryCapacity() {
        Object mPowerProfile;
        double batteryCapacity = 0;
        final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";

        try {
            mPowerProfile = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context.class)
                    .newInstance(this);

            batteryCapacity = (double) Class
                    .forName(POWER_PROFILE_CLASS)
                    .getMethod("getBatteryCapacity")
                    .invoke(mPowerProfile);

        } catch (Exception e) {
            e.printStackTrace();
        }


        return (int) batteryCapacity;

    }

    private String getScreenResolution() {
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        return width + "x" + height;
    }

    private int getNumberOfCores() {
        return Runtime.getRuntime().availableProcessors();
    }


    private int getNumCoresOldPhones() {
        //Private Class to display only CPU devices in the directory listing
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                //Check if filename is "cpu", followed by a single digit number
                if (Pattern.matches("cpu[0-9]+", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }

        try {
            //Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            //Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());
            //Return the number of cores (virtual CPU devices)
            return files.length;
        } catch (Exception e) {
            //Default to return 1 core
            return 1;
        }
    }


    public static String selinux() {
        try {
            Process p = Runtime.getRuntime().exec("getenforce");
            InputStream is = null;
            if (p.waitFor() == 0) {
                is = p.getInputStream();
            } else {
                is = p.getErrorStream();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is),
                    BUFFER_SIZE);
            String line = br.readLine();
            String temp;
            br.close();
            temp="Failed to Read";
            return line;
        } catch (Exception ex) {
            return "ERROR: " + ex.getMessage();
        }

    }

    public static String getDeviceCode() {
        try {
            Process p = Runtime.getRuntime().exec("getprop ro.pb.codename");
            InputStream is = null;
            if (p.waitFor() == 0) {
                is = p.getInputStream();
            } else {
                is = p.getErrorStream();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is),
                    BUFFER_SIZE);
            String line = br.readLine();
            br.close();
            return line;
        } catch (Exception ex) {
            return "";
        }

    }

    public static String getMaintainer() {
        try {
            Process p = Runtime.getRuntime().exec("getprop ro.pb.maintainer");
            InputStream is = null;
            if (p.waitFor() == 0) {
                is = p.getInputStream();
            } else {
                is = p.getErrorStream();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is),
                    BUFFER_SIZE);
            String line = br.readLine();
            br.close();
            return line;
        } catch (Exception ex) {
            return "";
        }

    }

    public static String getSOC() {
        try {
            Process p = Runtime.getRuntime().exec("getprop ro.pb.soc");
            InputStream is = null;
            if (p.waitFor() == 0) {
                is = p.getInputStream();
            } else {
                is = p.getErrorStream();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is),
                    BUFFER_SIZE);
            String line = br.readLine();
            br.close();
            return line;
        } catch (Exception ex) {
            return "";
        }

    }

    public static String getCamera() {
        try {
            Process p = Runtime.getRuntime().exec("getprop ro.pb.camera");
            InputStream is = null;
            if (p.waitFor() == 0) {
                is = p.getInputStream();
            } else {
                is = p.getErrorStream();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is),
                    BUFFER_SIZE);
            String line = br.readLine();
            br.close();
            return line;
        } catch (Exception ex) {
            return "";
        }

    }

    public static String getCores() {
        try {
            Process p = Runtime.getRuntime().exec("getprop ro.pb.cores");
            InputStream is = null;
            if (p.waitFor() == 0) {
                is = p.getInputStream();
            } else {
                is = p.getErrorStream();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is),
                    BUFFER_SIZE);
            String line = br.readLine();
            br.close();
            return line;
        } catch (Exception ex) {
            return "";
        }

    }

    public static String getDevice() {
        try {
            Process p = Runtime.getRuntime().exec("getprop ro.pb.device");
            InputStream is = null;
            if (p.waitFor() == 0) {
                is = p.getInputStream();
            } else {
                is = p.getErrorStream();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is),
                    BUFFER_SIZE);
            String line = br.readLine();
            br.close();
            return line;
        } catch (Exception ex) {
            return "";
        }

    }
    public static String getPb() {
        try {
            Process p = Runtime.getRuntime().exec("getprop ro.pb.version");
            InputStream is = null;
            if (p.waitFor() == 0) {
                is = p.getInputStream();
            } else {
                is = p.getErrorStream();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is),
                    BUFFER_SIZE);
            String line = br.readLine();
            br.close();
            return line;
        } catch (Exception ex) {
            return "";
        }

    }

}