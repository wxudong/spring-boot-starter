package org.fisco.bcos.client;


public class CNetconData {
    public String netconid;
    public String applya;
    public String applyb;
    public String addr;
    public String area;
    public String balance;

    public CNetconData(String netconid, String applya, String applyb, String addr, String area, String balance){
        this.netconid = netconid;
        this.applya = applya;
        this.applyb = applyb;
        this.addr = addr;
        this.area = area;
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "CNetconData{" +
                "NetconID='" + netconid + '\'' +
                ", ApplyA='" + applya + '\'' +
                ", ApplyB='" + applyb + '\'' +
                ", Addr='" + addr + '\'' +
                ", Area=" + area +
                ", Balance=" + balance +
                '}';
    }
}
