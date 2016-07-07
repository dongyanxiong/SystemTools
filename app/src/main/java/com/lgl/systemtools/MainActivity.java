package com.lgl.systemtools;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.lgl.systemtools.utils.DialogUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //列表
    private ListView mListView;
    //数据
    private List<String> mList = new ArrayList<>();
    //数据源
    private ArrayAdapter adapter;
    //电话管理器
    private TelephonyManager tm;

    //百度
    private String baiduUrl = "https://www.baidu.com/s?wd=";

    //搜索数据
    private AutoCompleteTextView completeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDate();
        initView();
    }

    //初始化View
    private void initView() {

        mListView = (ListView) findViewById(R.id.mListView);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mList);
        mListView.setAdapter(adapter);

        completeTextView = (AutoCompleteTextView) findViewById(R.id.mAutoCompleteTextView);
        completeTextView.setAdapter(adapter);

        //点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == mList.size() - 1) {
                    startActivity(new Intent(MainActivity.this, AboutActivity.class));
                } else {
                    DialogUtils.showNoButtonDialog(MainActivity.this, "放大镜", mList.get(i));
                }
            }
        });

        //长按
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                DialogUtils.showDialog(MainActivity.this, "寻求答案", "Google还是百度？", getString(R.string.baidu), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent();
                        intent.putExtra("url", baiduUrl + mList.get(position));
                        intent.putExtra("title", mList.get(position));
                        intent.setClass(MainActivity.this, WebActivity.class);
                        startActivity(intent);
                    }
                }, getString(R.string.google), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "暂不开放！", Toast.LENGTH_SHORT).show();
                    }
                });
                return true;
            }
        });
    }

    //初始化数据
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initDate() {
        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        //用户
        if (isConnected()) {
            mList.add("网络连接：顺畅");
        } else {
            mList.add("网络连接：失败");
        }

        if (isWifi()) {
            mList.add("WIFI:已连接");
        } else {
            mList.add("WIFI:未连接");
        }

        mList.add("IMEI:" + tm.getDeviceId());

        if (tm.getLine1Number() != null) {
            String flag = tm.getSubscriberId();
            if (flag.startsWith("46000") || flag.startsWith("46002")) {
                mList.add("本机号码：" + tm.getLine1Number() + "(中国移动)");
            } else if (flag.startsWith("46001")) {
                mList.add("本机号码：" + tm.getLine1Number() + "(中国联通)");
            } else if (flag.startsWith("46003")) {
                mList.add("本机号码：" + tm.getLine1Number() + "(中国电信)");
            } else {
                mList.add("未检测到SIM卡");
            }
        } else {
            mList.add("未检测到SIM卡");
        }

        mList.add("IP地址:" + getLocalIpAddress());
        mList.add("MAC地址：" + getLocalMacAddress());

        mList.add("运行可用内存:" + getAvailMemory() + "  总内存:" + getTotalMemory());

        //SD卡可用内存
        isSdcard();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mList.add("分辨率：" + displayMetrics.widthPixels + "x"
                + displayMetrics.heightPixels);


        //硬件
        mList.add("主板：" + Build.BOARD);
        mList.add("系统定制商：" + Build.BRAND);
        mList.add("CPU指令集：" + Build.SUPPORTED_ABIS);
        mList.add("设备参数：" + Build.DEVICE);
        mList.add("显示屏参数：" + Build.DISPLAY);
        mList.add("唯一编号：" + Build.FINGERPRINT);
        mList.add("硬件序列号：" + Build.SERIAL);
        mList.add("修订版本列表：" + Build.ID);
        mList.add("硬件制造商：" + Build.MANUFACTURER);
        mList.add("版本：" + Build.MODEL);
        mList.add("硬件名：" + Build.HARDWARE);
        mList.add("手机产品名：" + Build.PRODUCT);
        mList.add("Build的标签:" + Build.TAGS);
        mList.add("Builder类型：" + Build.TYPE);
        mList.add("开发代号：" + Build.VERSION.CODENAME);
        mList.add("源码控制版本号：" + Build.VERSION.INCREMENTAL);
        mList.add("版本字符串：" + Build.VERSION.RELEASE);
        mList.add("版本号：" + Build.VERSION.SDK_INT);
        mList.add("Host值：" + Build.HOST);
        mList.add("用户名：" + Build.USER);
        mList.add("编译时间：" + Build.TIME);

        //软件
        mList.add("OS版本:" + System.getProperty("os.version"));
        mList.add("OS名称:" + System.getProperty("os.name"));
        mList.add("OS架构:" + System.getProperty("os.arch"));
        mList.add("Home属性:" + System.getProperty("user.home"));
        mList.add("Name属性:" + System.getProperty("user.name"));
        mList.add("Dir属性:" + System.getProperty("user.dir"));
        mList.add("时区:" + System.getProperty("user.timezone"));
        mList.add("路径分隔符:" + System.getProperty("path.separator"));
        mList.add("行分隔符:" + System.getProperty("line.separator"));
        mList.add("文件分隔符:" + System.getProperty("file.separator") + "");
        mList.add("Java vender Url属性:" + System.getProperty("java.vendor.url"));
        mList.add("Java Class属性:" + System.getProperty("java.class.path"));
        mList.add("Java Class版本:" + System.getProperty("java.class.version"));
        mList.add("Java Vender属性:" + System.getProperty("java.vendor"));
        mList.add("Java版本:" + System.getProperty("java.version"));
        mList.add("Java Home:" + System.getProperty("java.home"));

        mList.add(getString(R.string.about_me));

    }

    // 获取手机ip
    public String getLocalIpAddress() {
        // 获取wifi服务
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        // 判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ip = intToIp(ipAddress);
        return ip;
    }

    //换算IP
    private String intToIp(int i) {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
                + "." + (i >> 24 & 0xFF);
    }

    /**
     * mac地址 start
     */
    public String getLocalMacAddress() {
        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();

        return info.getMacAddress();
    }

    //SD卡大小计算
    private void isSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            // 取得sdcard文件路径
            File path = Environment.getExternalStorageDirectory();
            StatFs statfs = new StatFs(path.getPath());
            // 获取block的SIZE
            long blocSize = statfs.getBlockSize();
            // 获取BLOCK数量
            long totalBlocks = statfs.getBlockCount();
            // 空闲的Block的数量
            long availaBlock = statfs.getAvailableBlocks();
            // 计算总空间大小和空闲的空间大小
            // 存储空间大小跟空闲的存储空间大小就被计算出来了。
            long availableSize = blocSize * availaBlock;
            // (availableBlocks * blockSize)/1024 KIB 单位
            // (availableBlocks * blockSize)/1024 /1024 MIB单位
            long allSize = blocSize * totalBlocks;
            mList.add("SD卡可用：" + availableSize / 1024 / 1024 / 1024 + "GB"
                    + "  总共：" + allSize / 1024 / 1024 / 1024 + "GB");
        } else {
            mList.add("SD卡不可用");
        }
    }

    // 手机的内存信息主要在/proc/meminfo文件中，其中第一行是总内存，而剩余内存可通过ActivityManager.MemoryInfo得到。
    private String getAvailMemory() {// 获取android当前可用内存大小
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        // mi.availMem; 当前系统的可用内存
        return Formatter.formatFileSize(getBaseContext(), mi.availMem);// 将获取的内存大小规格化
    }

    //运行总内存
    private String getTotalMemory() {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");
            }

            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            localBufferedReader.close();

        } catch (IOException e) {
        }
        return Formatter.formatFileSize(getBaseContext(), initial_memory);// Byte转换为KB或者MB，内存大小规格化
    }


    /**
     * 判断网络是否连接
     *
     * @return
     */
    public boolean isConnected() {
        ConnectivityManager connectivity = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivity) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断是否是wifi连接
     */
    public boolean isWifi() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null)
            return false;
        return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            //返回
            case KeyEvent.KEYCODE_BACK:
                if(completeTextView.getText().toString() != null){
                    completeTextView.setText("");
                }else
                finish();
                break;
        }
        return true;
    }
}
