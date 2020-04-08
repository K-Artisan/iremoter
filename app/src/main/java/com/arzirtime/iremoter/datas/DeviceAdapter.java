package com.arzirtime.iremoter.datas;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.arzirtime.iremoter.IRemoterApplication;
import com.arzirtime.iremoter.R;
import com.arzirtime.iremoter.activitys.GasMonitorDeviceActivity;
import com.arzirtime.iremoter.common.DeviceTypeCode;
import com.arzirtime.support.util.ToastUtils;
import com.bumptech.glide.Glide;

import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {

    private Context mContext;
    private List<Device> mDeviceList;

    public DeviceAdapter(List<Device> devices) {
        mDeviceList = devices;
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.decive_item, parent, false);
        final DeviceViewHolder holder = new DeviceViewHolder(view);

        //注册点击事件：子项最外层布局
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Device device = mDeviceList.get(position);
                goToDeviceDetailView(device);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        Device device = mDeviceList.get(position);
        holder.deviceNameView.setText(device.getName());

       /*---------------------------------------------------------------
            Glide对图片进行压缩等操作，避免：图片引起内存溢出，
            Glide开源地址:https://github.com/bumptech/glide
       ------------------------------------------------------------------*/
        //holder.deviceImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(mContext).load(device.getImageResId())
                .placeholder(R.drawable.device_default) //加载中显示d 图片
                //.error(R.drawable.device_default) //异常显示的图片
                //.fallback(R.drawable.device_default) //图片为null时显示的图片
                .centerCrop()
                .into(holder.deviceImageView);
    }

    @Override
    public int getItemCount() {
        return mDeviceList.size();
    }

    private void goToDeviceDetailView(Device device) {

        String devTypeCode = TextUtils.isEmpty(device.getDeviceTypeCode()) ? "Unknow Device" : device.getDeviceTypeCode();
        switch (devTypeCode) {
            case DeviceTypeCode.GASMONITORDEVICE:
                GasMonitorDeviceActivity.startActivity(mContext ,device);
                break;
            default:
                ToastUtils.showToast(mContext, "尚未提供该设备的详情页");
                break;
        }
    }

    static class DeviceViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private ImageView deviceImageView;
        private TextView deviceNameView;

        public DeviceViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = (CardView) itemView;
            deviceImageView = itemView.findViewById(R.id.decive_item_image);
            deviceNameView = itemView.findViewById(R.id.decive_item_name);
        }
    }

}
