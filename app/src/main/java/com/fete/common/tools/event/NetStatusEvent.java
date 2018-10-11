package com.fete.common.tools.event;

import com.fete.basemodel.utils.NetUtil;

public class NetStatusEvent {
    private boolean isHasNetwork;
    private NetUtil.ConnectStatus connectStatus;

    public NetStatusEvent(boolean isHasNetwork) {
        this.isHasNetwork = isHasNetwork;
    }

    public NetStatusEvent(boolean isHasNetwork, NetUtil.ConnectStatus connectStatus) {
        this.isHasNetwork = isHasNetwork;
        this.connectStatus = connectStatus;
    }

    public boolean isHasNetwork() {
        return isHasNetwork;
    }

    public void setHasNetwork(boolean hasNetwork) {
        isHasNetwork = hasNetwork;
    }

    public NetUtil.ConnectStatus getConnectStatus() {
        return connectStatus;
    }

    public void setConnectStatus(NetUtil.ConnectStatus connectStatus) {
        this.connectStatus = connectStatus;
    }

    @Override
    public String toString() {
        return "NetStatusEvent{" +
                "isHasNetwork=" + isHasNetwork +
                ", connectStatus=" + connectStatus +
                '}';
    }
}