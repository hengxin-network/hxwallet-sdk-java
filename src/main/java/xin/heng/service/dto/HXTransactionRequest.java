package xin.heng.service.dto;

import xin.heng.service.vo.HXFileInfo;
import xin.heng.service.vo.HXPubData;

import java.util.List;
import java.util.Map;

public class HXTransactionRequest {
    private boolean async = false;
    private String asset;
    private List<String> opponent_addresses;
    private String trace_id;
    private HXPubData pub_data;
    private Map<String, Object> priv_data;
    private boolean senders_required;
    private boolean receivers_required;
    private List<HXFileInfo> files;

    public String getAsset() {
        return asset;
    }

    public List<String> getOpponent_addresses() {
        return opponent_addresses;
    }

    public String getTrace_id() {
        return trace_id;
    }

    public HXTransactionRequest setAsset(String asset) {
        this.asset = asset;
        return this;
    }

    public HXTransactionRequest setOpponent_addresses(List<String> opponent_addresses) {
        this.opponent_addresses = opponent_addresses;
        return this;
    }

    public HXTransactionRequest setTrace_id(String trace_id) {
        this.trace_id = trace_id;
        return this;
    }

    public HXPubData getPub_data() {
        return pub_data;
    }

    public HXTransactionRequest setPub_data(HXPubData pub_data) {
        this.pub_data = pub_data;
        return this;
    }

    public Map<String, Object> getPriv_data() {
        return priv_data;
    }

    public HXTransactionRequest setPriv_data(Map<String, Object> priv_data) {
        this.priv_data = priv_data;
        return this;
    }

    public boolean isSenders_required() {
        return senders_required;
    }

    public HXTransactionRequest setSenders_required(boolean senders_required) {
        this.senders_required = senders_required;
        return this;
    }

    public boolean isReceivers_required() {
        return receivers_required;
    }

    public HXTransactionRequest setReceivers_required(boolean receivers_required) {
        this.receivers_required = receivers_required;
        return this;
    }

    public List<HXFileInfo> getFiles() {
        return files;
    }

    public HXTransactionRequest setFiles(List<HXFileInfo> files) {
        this.files = files;
        return this;
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }
}
