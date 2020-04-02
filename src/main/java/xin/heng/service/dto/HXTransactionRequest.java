package xin.heng.service.dto;

import xin.heng.service.vo.HXFileInfo;
import xin.heng.service.vo.HXPubData;

import java.util.List;

public class HXTransactionRequest {
    private String asset;
    private List<String> opponent_addresses;
    private String trace_id;
    private HXPubData pub_data;
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

    public List<HXFileInfo> getFiles() {
        return files;
    }

    public HXTransactionRequest setFiles(List<HXFileInfo> files) {
        this.files = files;
        return this;
    }
}
