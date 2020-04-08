package com.arzirtime.iremoter.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.arzirtime.iremoter.R;
import com.arzirtime.iremoter.datas.Device;
import com.arzirtime.support.util.LogUtil;

public class GasMonitorDeviceActivity extends AppCompatActivity {

    private static final String TAG ="GasMonitorDeviceActivity";
    private static final String KEY_INTENT_DEVICE ="keyIntent_device";

    //region AppCompatActivity 接口函数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogUtil.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_monitor_device);

        TextView textView = findViewById(R.id.gasdev_tv);
        Device device = (Device) getIntent().getSerializableExtra(KEY_INTENT_DEVICE);
        textView.setText(device.getName());
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.v(TAG, "onStart");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        LogUtil.v(TAG, "onPostResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.v(TAG, "onStop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.v(TAG, "onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.v(TAG, "onDestroy");
    }

    //endregion

    /**
     * 提供一个静态方法，用于开启该Activity
     * */
    public static void startActivity(Context context, Device device){
        Intent intent = new Intent(context, GasMonitorDeviceActivity.class);
        intent.putExtra(KEY_INTENT_DEVICE, device);
        context.startActivity(intent);
    }
}
