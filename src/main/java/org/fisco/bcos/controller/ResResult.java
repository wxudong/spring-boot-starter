package org.fisco.bcos.controller;
import com.alibaba.fastjson.annotation.JSONField;

public class ResResult {
    @JSONField(name = "Code")
    public int Code;
    @JSONField(name = "Status")
    public Object Status;

    public ResResult(int Code, Object Status){
        this.Code = Code;
        this.Status = Status;
    }

    public void setCode(int Code){
        this.Code = Code;
    }

    public int getCode(){
        return Code;
    }

    public void setStatus(Object Status){
        this.Status = Status;
    }

    public Object getStatus() {
        return Status;
    }

}

