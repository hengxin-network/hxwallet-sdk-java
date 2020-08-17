package xin.heng.service.dto;

import java.util.List;

public class HXNetworkSnapshotsRequest {
    private List<String> userIds;
    private List<String> addresses;

    public List<String> getUserIds() {
        return userIds;
    }

    public HXNetworkSnapshotsRequest setUserIds(List<String> userIds) {
        this.userIds = userIds;
        return this;
    }

    public List<String> getAddresses() {
        return addresses;
    }

    public HXNetworkSnapshotsRequest setAddresses(List<String> addresses) {
        this.addresses = addresses;
        return this;
    }
}
