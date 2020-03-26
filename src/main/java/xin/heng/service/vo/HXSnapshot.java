package xin.heng.service.vo;

public class HXSnapshot {
    String created_at;
    String asset;
    String user_id;
    String opponent_id;
    String amount;
    String memo;
    HXTransactionMemo parsed_memo;
    long height;

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

    public HXTransactionMemo getParsed_memo() {
        return parsed_memo;
    }

    public void setParsed_memo(HXTransactionMemo parsed_memo) {
        this.parsed_memo = parsed_memo;
    }
}
