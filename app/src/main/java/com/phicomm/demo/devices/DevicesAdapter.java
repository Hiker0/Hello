package com.phicomm.demo.devices;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.phicomm.demo.R;
import com.phicomm.demo.data.Device;
import com.phicomm.demo.devices.DevicesAdapter.ViewHolder;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.common.base.Preconditions.checkNotNull;

public class DevicesAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<Device> mDevices;

    public DevicesAdapter() {
        mDevices = new LinkedList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View deviceView = inflater.inflate(R.layout.item_device, parent, false);
        ViewHolder viewHolder = new ViewHolder(deviceView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Device device = mDevices.get(position);

        holder.bind(device);
    }

    @Override
    public int getItemCount() {
        return mDevices.size();
    }

    public void replaceData(List<Device> devices) {
        checkNotNull(devices);
        mDevices = devices;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_device_bssid)
        TextView mTvBssid;
        @BindView(R.id.tv_device_type)
        TextView mTvType;
        @BindView(R.id.tv_device_local)
        TextView mTvLocal;
        View mItemView;

        ViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            ButterKnife.bind(this, itemView);
        }

        public void bind(Device device) {
            mItemView.setTag(device);

            mTvBssid.setText(device.getBssid());
            mTvType.setText(device.getType().name());
            String localStr = device.getAddress();
            if(localStr == null || localStr.isEmpty()){
                localStr = device.getToken();
            }
            Log.d("lichunya","device.getToken()="+device.getToken());
            mTvLocal.setText(localStr);
        }
    }
}
