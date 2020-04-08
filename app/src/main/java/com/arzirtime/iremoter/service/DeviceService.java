package com.arzirtime.iremoter.service;

import com.arzirtime.iremoter.common.DeviceTypeCode;
import com.arzirtime.iremoter.datas.Device;
import com.arzirtime.iremoter.datas.dto.PageResult;
import com.arzirtime.support.util.ListUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DeviceService {
    public static List<Device> deviceList = new ArrayList<Device>();

    public DeviceService() {
        //TODO:需要删除掉，只是用来模拟数据库存储的数据
        for (int i = 1; i <= 30; i++) {
            Device device = new Device(UUID.randomUUID().toString(), "192.168.1.130", 6666, "Device-" + i, 0);

            if (i == 1) {
                device.setDeviceTypeCode(DeviceTypeCode.GASMONITORDEVICE);
            }

            deviceList.add(device);
        }
    }

    public List<Device> getAllDevices() {
        return deviceList;
    }

    public List<Device> getDevices(int pageIndex, int pageSize) {
        return ListUtils.getPage(deviceList, pageIndex, pageSize);
    }

    public PageResult getAllDevicesPageResult() {
        PageResult result = new PageResult();
        result.totalCount = deviceList.size();
        result.rows = new ArrayList<>(deviceList);

        return result;
    }

    public PageResult getDevicesPageResult(int pageIndex, int pageSize) {

        int totalCount = deviceList.size();
        List<Device> rows = getDevices(pageIndex, pageSize);
        PageResult result = new PageResult(rows, pageIndex, pageSize, totalCount);

        return result;
    }

    public void addDevice(Device device) {
        deviceList.add(device);
    }

    public void removeDevice(Device device) {
        deviceList.remove(device);
    }
}
