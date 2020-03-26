package xin.heng.service.dto;

import xin.heng.service.vo.HXTransactionMemo;

import java.util.List;

public class HXTransactionRequest {
    private String asset;
    private List<String> opponent_addresses;
    private String trace_id;
    private HXTransactionMemo memo;

    public String getAsset() {
        return asset;
    }

    public List<String> getOpponent_addresses() {
        return opponent_addresses;
    }

    public String getTrace_id() {
        return trace_id;
    }

    public HXTransactionMemo getMemo() {
        return memo;
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

    public HXTransactionRequest setMemo(HXTransactionMemo memo) {
        this.memo = memo;
        return this;
    }

}
