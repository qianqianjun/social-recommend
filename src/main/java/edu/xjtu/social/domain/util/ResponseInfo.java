package edu.xjtu.social.domain.util;

public class ResponseInfo {
    private String msg;
    private Boolean ok;
    private Object data;

    public ResponseInfo(String msg, Boolean ok, Object data) {
        this.msg = msg;
        this.ok = ok;
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
