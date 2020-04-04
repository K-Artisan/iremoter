package com.arzirtime.iremoter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.arzirtime.iremoter.common.AppConstanct;
import com.arzirtime.iremoter.common.CommandContant;
import com.arzirtime.support.socket.ConnectClient;
import com.arzirtime.support.socket.ConnectManager;
import com.arzirtime.support.socket.ConnectMessageType;
import com.arzirtime.support.util.StringUtils;
import com.arzirtime.support.util.ToastUtils;

public class MainActivity extends AppCompatActivity {

    //region 控件变量

    private EditText edt_ip_and_port;   //IP和端口
    private Button btn_connect;         //连接按钮
    private Button btn_disconnect;      //关闭按钮

    private EditText edt_command;       //指令输入框
    private Button btn_send_command;    //发送指令按钮
    private TextView tv_showMsg;        //显示发送的消息和接收到的消息
    private Button btn_clear_log;       //清除日志

    private Button btn_heatup;          //升温按钮
    private Button btn_cooling;         //降温按钮
    private Button btn_wind_direnction; //风向按钮
    private Button btn_model;           //模式按钮

    //一个指令显示的text
    private TextView show_command;
    //ScrollView
    private ScrollView sc;

    //endregion

    //region 服务器通讯

    private String ip;
    private int port;
    private ConnectManager connectManager = ConnectManager.getInstance();
    private ConnectClient connectClient= null;

    // 定义Handler对象(异步消息机制，用于解决子线程不能进行UI操作的问题)
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // 处理UI
            switch (msg.what) {
                case ConnectMessageType.SEND_MSG:
                    setShowMsg((String) msg.obj);
                    break;

                case ConnectMessageType.RECEIVE_MSG:
                    setShowMsg((String) msg.obj);
                    break;

                case ConnectMessageType.CONNECT_SUCCESS:
                    setShowMsg((String) msg.obj);
                    setButtonOnStartState(false);
                    break;

                case ConnectMessageType.CONNECT_FAILSE:
                    setShowMsg((String) msg.obj);
                    setButtonOnStartState(true);
                    break;

                case ConnectMessageType.CONNECT_TIMEOUT:
                    setShowMsg((String) msg.obj);
                    setButtonOnStartState(true);
                    break;

                case ConnectMessageType.SYSTEM_EORROR:
                    setShowMsg((String) msg.obj);
                    setButtonOnStartState(true);
                    break;

                default:
                    break;
            }
        }

    };

    //endregion

    //region Activity类方法

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    //endregion

    //region 初始化

    private void  init(){
        initView();
        initOnclick();
        initConnectClient();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        edt_ip_and_port = findViewById(R.id.edt_ip_and_port);
        btn_connect = findViewById(R.id.btn_connect);
        btn_disconnect =findViewById(R.id.btn_disconnect);

        edt_command = findViewById(R.id.edt_command);
        btn_send_command = findViewById(R.id.btn_send_command);
        tv_showMsg = findViewById(R.id.tv_showMsg);
        btn_clear_log = findViewById(R.id.btn_clear_log);

        btn_heatup = findViewById(R.id.btn_heatup);
        btn_cooling = findViewById(R.id.btn_cooling);
        btn_wind_direnction = findViewById(R.id.btn_wind_direnction);
        btn_model = findViewById(R.id.btn_model);

        edt_ip_and_port.setText("192.168.1.129:6666");
    }

    private boolean tryGetIpAndPort() {
        String input = edt_ip_and_port.getText().toString();
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

    private void initOnclick() {

        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean success = tryGetIpAndPort();
                if (success) {
                    connect();
                }
            }
        });

        btn_disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectClient.close();
            }
        });

        btn_send_command.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String command = edt_command.getText().toString();
                if (!StringUtils.isNullOrEmpty(command) && !StringUtils.isNullOrEmpty(command.trim())) {
                    sendCommand(command);
                } else {
                    ToastUtils.showToast(MainActivity.this, "请输入指令");
                }
            }
        });

        btn_clear_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_showMsg.setText("");
            }
        });

        btn_heatup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCommand(CommandContant.HEAPUP);
            }
        });

        btn_cooling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCommand(CommandContant.COOLING);
            }
        });

        btn_wind_direnction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCommand(CommandContant.WIND_DIRENCTION);
            }
        });

        btn_model.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCommand(CommandContant.MODEL);
            }
        });
    }

    //endregion

    private  void initConnectClient(){
        Intent intent  = getIntent();

        String connectClientId = intent.getStringExtra(AppConstanct.CONNECTCLIENTID);
        connectClient = connectManager.findConnectClientById(connectClientId);

        if (connectClient != null){
            connectClient.registerConnectThreadHandler(mHandler);
            connectClient.registerSendMsgThreadThreadHandler(mHandler);
            connectClient.registerReceiveMsgThreadHandler(mHandler);
        }else {
            connectClient = connectManager.CreateConnectClient(ip, port);
        }
        connectClient.tryAutoConnect();

        setButtonOnStartState(!connectClient.isConnected());
    }

    //连接远程服务器
    private void connect() {
        connectClient.tryAutoConnect();
    }

    //发送指令
    protected void sendCommand(String command) {
        try {
            connectClient.sendMsgToServer(command);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToast(this, "发送失败");
        }
    }

    protected void setShowMsg(String msg) {
        tv_showMsg.setText(msg + "\n" + tv_showMsg.getText().toString());
    }

    private void setButtonOnStartState(boolean flag) {//设置按钮的状态

        btn_connect.setEnabled(flag);
        btn_connect.setVisibility(flag ? View.VISIBLE:View.GONE);
        btn_disconnect.setVisibility(flag ? View.GONE:View.VISIBLE);

       edt_command.setEnabled(!flag);
        btn_send_command.setEnabled(!flag);
        btn_heatup.setEnabled(!flag);
        btn_cooling.setEnabled(!flag);
        btn_wind_direnction.setEnabled(!flag);
        btn_model.setEnabled(!flag);
    }

}
