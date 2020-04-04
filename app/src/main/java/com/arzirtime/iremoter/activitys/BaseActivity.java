package com.arzirtime.iremoter.activitys;

import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.arzirtime.iremoter.common.AppConstanct;
import com.arzirtime.iremoter.receiver.ForceOfflineReceiver;
import com.arzirtime.iremoter.receiver.NetWorkChangeReciever;

/**
 * Activity的基础类
 */
public class BaseActivity extends AppCompatActivity {

    private ForceOfflineReceiver forceOfflineReceiver;
    private NetWorkChangeReciever netWorkChangeReciever;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityManager.addActivity(this);
    }

    /**
     * 前台生存期 。活动在 onResume() 方法和 onPause()方法之间所经历的就是前台生存期。在前台生存期内，
     * 活动总是处于运行状态的，此时的活动是可以和用户进行交互的，我们平时看到和接触最多的也就是这个状态下的活动
     * */
    @Override
    protected void onResume() {
        super.onResume();

        /*--------------------------------------------------
         活动进入活动栈顶端，注册广播接收器
        * -------------------------------------------------*/

        //注册网络播接收器:强制下线
        IntentFilter ifOffline = new IntentFilter();
        ifOffline.addAction(AppConstanct.BROADCAST_FORCE_OFFLINE);
        forceOfflineReceiver = new ForceOfflineReceiver();
        registerReceiver(forceOfflineReceiver, ifOffline);

        //注册广播接收器：网络变化，当网络变化时系统发送该广播
        IntentFilter ifNetwork = new IntentFilter();
        ifNetwork.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        netWorkChangeReciever = new NetWorkChangeReciever();
        registerReceiver(netWorkChangeReciever, ifNetwork);
    }

    /**
     * 前台生存期 。活动在 onResume() 方法和 onPause()方法之间所经历的就是前台生存期。在前台生存期内，
     * 活动总是处于运行状态的，此时的活动是可以和用户进行交互的，我们平时看到和接触最多的也就是这个状态下的活动
     * */
    @Override
    protected void onPause() {
        super.onPause();

        /*--------------------------------------------------
         活动离开活动栈顶端，注销广播接收器
        * -------------------------------------------------*/
        if (forceOfflineReceiver != null){
            unregisterReceiver(forceOfflineReceiver); //注销广播接收器
            forceOfflineReceiver = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ActivityManager.removeActivity(this);
    }
}

