package xin.heng.service.vo;

public class HXUserInfo {
    public String user_id;
    public String created_at;
    public String address;
    public String private_key;
    public String hex_private_key;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrivate_key() {
        return private_key;
    }

    public void setPrivate_key(String private_key) {
        this.private_key = private_key;
    }

    public String getHex_private_key() {
        return hex_private_key;
    }

    public void setHex_private_key(String hex_private_key) {
        this.hex_private_key = hex_private_key;
    }
}
