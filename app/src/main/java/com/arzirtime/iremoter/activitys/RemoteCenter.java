package com.arzirtime.iremoter.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.arzirtime.iremoter.R;
import com.arzirtime.iremoter.common.LogTag;
import com.arzirtime.iremoter.datas.Device;
import com.arzirtime.iremoter.datas.DeviceAdapter;
import com.arzirtime.iremoter.datas.dto.PageResult;
import com.arzirtime.iremoter.service.DeviceService;
import com.arzirtime.support.util.ListUtils;
import com.arzirtime.support.util.LogUtil;
import com.arzirtime.support.util.ToastUtils;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RemoteCenter extends BaseActivity {

    private DrawerLayout drawerLayout;
    private SwipeRefreshLayout devSwipRefrhLayout;

    private DeviceService deviceService;

    private int pageIndex = 1;
    private int pageSize = 10;
    DeviceAdapter deviceAdapter;
    List<Device> deviceList = new ArrayList<>(pageSize * 2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_center);

        initService();
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu); //创建系统菜单
        return true;
    }

    @SuppressLint("WrongConstant")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(Gravity.START);
                break;
            case R.id.toolbar_add_device:
                //Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
                addDevice(); //TODO:暂时模拟添加设备
                break;
            case R.id.toolbar_setting:
                Toast.makeText(this, "设置", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

        return true;
    }

    private void initService() {
        deviceService = new DeviceService();
    }

    private void initView() {
        drawerLayout = findViewById(R.id.drawer_layout);
        devSwipRefrhLayout = findViewById(R.id.devSwipRefrhLayout);

        initToolbar();
        initNavigationView();
        initDeviceView();
        initRefreshLayout();
    }

    private void initToolbar() {
        //使用Toolbar替换系统的ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); //启用HomeUp按钮，其id永远为：android.R.id.home
            actionBar.setHomeAsUpIndicator(R.drawable.icon_caidan);//改变HomeUp按钮的图标
        }
    }

    private void initNavigationView() {
        //滑块视图(屏幕侧边划出)
        NavigationView navigationView = findViewById(R.id.nav_view);
        //navigationView.setCheckedItem(R.id.nav_menu_message); //设置默认选择项
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                //收回滑块视图(屏幕侧边划出),gravity要与layout中设置的值一致，否则抛出异常
                drawerLayout.closeDrawer(GravityCompat.START);

                switch (menuItem.getItemId()) {
                    case R.id.nav_menu_message:
                        Toast.makeText(RemoteCenter.this, "消息中心", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_menu_systemlog:
                        Toast.makeText(RemoteCenter.this, "系统日志", Toast.LENGTH_SHORT).show();
                        break;
                }

                return true;
            }
        });
    }

    private void initDeviceView() {
        deviceList.addAll(deviceService.getDevices(pageIndex, pageSize));

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        deviceAdapter = new DeviceAdapter(deviceList);
        recyclerView.setAdapter(deviceAdapter);
    }

    private void initRefreshLayout() {
        devSwipRefrhLayout.setColorSchemeResources(R.color.colorPrimary);
        devSwipRefrhLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshDevicesByRefreshLayout();
            }
        });

    }

    /**
     * 通过布局控件进行刷新
     */
    private void refreshDevicesByRefreshLayout() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() { //更新UI线程
                    @Override
                    public void run() {
                        PageResult result = deviceService.getDevicesPageResult(++pageIndex, pageSize);
                        pageIndex = ListUtils.getAutoFixCurrentPageIndex(result.totalCount, pageIndex, pageSize); //修正当前页码

                        if (result.rows != null) {
                            deviceList.addAll(result.rows);
                            deviceAdapter.notifyItemChanged((pageIndex - 1) * pageSize, pageSize); //局部刷新
                        } else {
                            ToastUtils.showToast(RemoteCenter.this, "已经全部加载");
                        }
                        devSwipRefrhLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    private void addDevice() {
        Device device = new Device(UUID.randomUUID().toString(), "192.168.1.130", 6666, "Device-" + (deviceList.size() + 1), 0);
        deviceService.addDevice(device);
        deviceList.clear();
        PageResult result = deviceService.getAllDevicesPageResult();
        deviceList.addAll(result.rows);
        deviceAdapter.notifyDataSetChanged();//全部刷新
    }
}
