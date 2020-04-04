package com.arzirtime.support.socket;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.arzirtime.iremoter.common.AppConstanct;
import com.arzirtime.support.util.MessageUtils;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ConnectManager {

    private static ConnectManager instance = null;
    private HashMap<String, ConnectClient> connectClients = new HashMap<String, ConnectClient>();

    private ConnectManager() {
    }

    public static ConnectManager getInstance() {
        if (instance == null) {
            instance = new ConnectManager();
        }
        return instance;
    }

    /**
     * 创建 ConnectClient
     */
    public ConnectClient CreateConnectClient(String ip, int port) {
        return CreateConnectClient(ip, port, AppConstanct.SOCKET_TIMEOUT);
    }

    /**
     * 创建 ConnectClient
     */
    public ConnectClient CreateConnectClient(String ip, int port, int timeout) {
        ConnectClient client = new ConnectClient(ip, port, timeout);
        connectClients.put(client.getId(), client);

        return client;
    }

    /**
     * 根据Id获取 ConnectClient
     */
    public ConnectClient findConnectClientById(String connectClientId) {
        if (connectClients.containsKey(connectClientId)) {
            return connectClients.get(connectClientId);
        } else {
            return null;
        }
    }

}

