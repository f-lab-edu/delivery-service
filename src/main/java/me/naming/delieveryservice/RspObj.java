package me.naming.delieveryservice;

public class RspObj {
    public Boolean    hasError;
    public int        code;
    public String     msg;
    public String     resultStr;
    public Object     resultObject;
    public long       resultLong;
    public byte[]     resultByte;

    public RspObj(){
        this.hasError = false;
        this.code = 200;
        this.msg  = "none_data";
    }

    //생성과 동시에 성공값 셋팅
    public RspObj(Object inObj){
        this.code = 200;
        this.hasError = false;
        this.resultObject = inObj;
        this.msg  = "none_data";
    };
}
