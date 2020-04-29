package xin.heng.service.vo;

import java.util.List;
import java.util.Map;

public class HXSnapshot<T> {
    long id;
    String created_at;
    String asset;
    String user_id;
    String opponent_id;
    String amount;
    String memo;
    String tx_hash;
    List<String> senders;
    List<String> receivers;
    HXPubData<T> pub_data;
    Map<String, Object> priv_data;
    List<HXFileInfo> files;
    long height;

    public long getId() {
        return id;
    }

    public HXSnapshot<T> setId(long id) {
        this.id = id;
        return this;
    }

    public String getCreated_at() {
        return created_at;
    }

    public HXSnapshot<T> setCreated_at(String created_at) {
        this.created_at = created_at;
        return this;
    }

    public String getAsset() {
        return asset;
    }

    public HXSnapshot<T> setAsset(String asset) {
        this.asset = asset;
        return this;
    }

    public String getUser_id() {
        return user_id;
    }

    public HXSnapshot<T> setUser_id(String user_id) {
        this.user_id = user_id;
        return this;
    }

    public String getOpponent_id() {
        return opponent_id;
    }

    public HXSnapshot<T> setOpponent_id(String opponent_id) {
        this.opponent_id = opponent_id;
        return this;
    }

    public String getAmount() {
        return amount;
    }

    public HXSnapshot<T> setAmount(String amount) {
        this.amount = amount;
        return this;
    }

    public String getMemo() {
        return memo;
    }

    public HXSnapshot<T> setMemo(String memo) {
        this.memo = memo;
        return this;
    }

    public String getTx_hash() {
        return tx_hash;
    }

    public HXSnapshot<T> setTx_hash(String tx_hash) {
        this.tx_hash = tx_hash;
        return this;
    }

    public long getHeight() {
        return height;
    }

    public HXSnapshot<T> setHeight(long height) {
        this.height = height;
        return this;
    }

    public HXPubData<T> getPub_data() {
        return pub_data;
    }

    private HXSnapshot<T> setPub_data(HXPubData<T> pub_data) {
        this.pub_data = pub_data;
        return this;
    }

    public Map<String, Object> getPriv_data() {
        return priv_data;
    }

    public HXSnapshot<T> setPriv_data(Map<String, Object> priv_data) {
        this.priv_data = priv_data;
        return this;
    }

    public List<HXFileInfo> getFiles() {
        return files;
    }

    public HXSnapshot<T> setFiles(List<HXFileInfo> files) {
        this.files = files;
        return this;
    }

    public List<String> getSenders() {
        return senders;
    }

    public HXSnapshot<T> setSenders(List<String> senders) {
        this.senders = senders;
        return this;
    }

    public List<String> getReceivers() {
        return receivers;
    }

    public HXSnapshot<T> setReceivers(List<String> receivers) {
        this.receivers = receivers;
        return this;
    }
}
