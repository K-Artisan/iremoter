package com.arzirtime.iremoter.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.arzirtime.iremoter.MainActivity;
import com.arzirtime.iremoter.R;
import com.arzirtime.iremoter.common.AppConstanct;
import com.arzirtime.iremoter.common.LogTag;
import com.arzirtime.support.socket.ConnectClient;
import com.arzirtime.support.socket.ConnectManager;
import com.arzirtime.support.socket.ConnectMessageType;
import com.arzirtime.support.util.LogUtil;
import com.arzirtime.support.util.StringUtils;
import com.arzirtime.support.util.ToastUtils;

public class LoginActivity extends AppCompatActivity  {

    EditText edt_ipAndPort;
    Button btn_login;

    private String ip;
    private int port;
    private ConnectManager connectManager = ConnectManager.getInstance();

    private ConnectClient connectClient = null;

    // 定义Handler对象(异步消息机制，用于解决子线程不能进行UI操作的问题)
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            LogUtil.v(LogTag.LOGTAG, String.format("%s->mHandler> msg.what:%d | msg.obj:%s",this.getClass().getName(),msg.what, (String) msg.obj));

            switch (msg.what) {

                case ConnectMessageType.CONNECT_SUCCESS:
                    doWhenLoginSuccess((String) msg.obj);
                    break;

                case ConnectMessageType.SEND_MSG:
                    break;

                case ConnectMessageType.RECEIVE_MSG:
                    break;

                case ConnectMessageType.CONNECT_FAILSE:
                case ConnectMessageType.CONNECT_TIMEOUT:
                case ConnectMessageType.SYSTEM_EORROR:
                    doWhenLoginFails(msg.what, (String)msg.obj);
                    break;

                default:
                    break;
            }
        }

    };

    //region Activity 方法

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    //endregion

    private void init(){
        initView();
    }

    private void initView(){
        edt_ipAndPort = findViewById(R.id.edt_ip_and_port);
        btn_login = findViewById(R.id.btn_login);

        edt_ipAndPort.setText("192.168.1.129:6666");
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLoginOnclick();
            }
        });
    }

    private  void btnLoginOnclick(){
        boolean success = tryGetIpAndPort();
        if (success) {
            connect();
        }
    }

    //连接远程服务器
    private void connect() {
        connectClient = connectManager.CreateConnectClient(ip, port);
        connectClient.registerConnectThreadHandler(mHandler);
        connectClient.registerSendMsgThreadThreadHandler(mHandler);
        connectClient.registerReceiveMsgThreadHandler(mHandler);
        connectClient.connect();
    }

    private void doWhenLoginSuccess(String msg){
        Log.d("k:", "doWhenLoginSuccess>" + "msg:" + msg);
        setShowMsg(msg);
        jumpToNextActivty(MainActivity.class);
    }

    //跳转到下一个Activity
    private void jumpToNextActivty(Class<?> cls ){
        Intent intent = new Intent(this, cls);
        intent.putExtra(AppConstanct.CONNECTCLIENTID, connectClient.getId());
        startActivity(intent);

        finish();
    }

    private void doWhenLoginFails(int ConnectMessageType, String msg){
        setShowMsg(msg);
    }

    private boolean tryGetIpAndPort() {
        String input = edt_ipAndPort.getText().toString();
        if (StringUtils.isNullOrEmpty(input)) {
            ToastUtils.showToast(this, "请输入IP和端口，格式为：IP:端口");
            return false;
        }
        String[] ipAndPort = StringUtils.getIPandPortFrom(input);
        String ipStr = ipAndPort[0];
        String portStr = ipAndPort[1];

        if (!StringUtils.isIP(ipStr)) {
            ToastUtils.showToast(this, "IP地址格式有误");
            return false;
        }

        ip = ipStr;
        port = Integer.parseInt(portStr);

        return true;
    }

    protected void setShowMsg(String msg) {
        ToastUtils.showToast(this, msg);
    }


}
