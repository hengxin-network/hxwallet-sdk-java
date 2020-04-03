package xin.heng.service.vo;

import java.util.List;

public class HXSnapshot {
    long id;
    String created_at;
    String asset;
    String user_id;
    String opponent_id;
    String amount;
    String memo;
    HXPubData pub_data;
    List<HXFileInfo> files;
    long height;

    public long getId() {
        return id;
    }

    public HXSnapshot setId(long id) {
        this.id = id;
        return this;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getOpponent_id() {
        return opponent_id;
    }

    public void setOpponent_id(String opponent_id) {
        this.opponent_id = opponent_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    public HXPubData getPub_data() {
        return pub_data;
    }

    private void setPub_data(HXPubData pub_data) {
        this.pub_data = pub_data;
    }

    public List<HXFileInfo> getFiles() {
        return files;
    }

    public HXSnapshot setFiles(List<HXFileInfo> files) {
        this.files = files;
        return this;
    }
}
