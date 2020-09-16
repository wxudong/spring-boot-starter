package org.fisco.bcos.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "t_Netcon")
public class NetconData implements Serializable {
    @Id
    @Column
    public String ID;
    @Column
    public String CreateDT;
    @Column
    public String NetconID;
    @Column
    public String ApplyA;
    @Column
    public String ApplyB;
    @Column
    public String Addr;
    @Column
    public String Area;
    @Column
    public String Balance;
    @Column
    public String Operator;
    @Column
    public int IsCCed;

    public NetconData(){}

    public NetconData(String ID, String CreateDT, String NetconID, String ApplyA, String ApplyB,
                  String Addr, String Area, String Balance, String Operator, int IsCCed){

        this.ID = ID;
        this.CreateDT = CreateDT;
        this.NetconID = NetconID;
        this.ApplyA = ApplyA;
        this.ApplyB = ApplyB;
        this.Addr = Addr;
        this.Area = Area;
        this.Balance = Balance;
        this.Operator = Operator;
        this.IsCCed = IsCCed;
    }

    @Override
    public String toString() {
        return "NetconData{" +
                "ID='" + ID + '\'' +
                ", CreateDT='" + CreateDT + '\'' +
                ", NetconID='" + NetconID + '\'' +
                ", ApplyA='" + ApplyA + '\'' +
                ", ApplyB='" + ApplyB + '\'' +
                ", Addr='" + Addr + '\'' +
                ", Area='" + Area + '\'' +
                ", Balance='" + Balance + '\'' +
                ", Operator='" + Operator + '\'' +
                ", IsCCed=" + IsCCed +
                '}';
    }
}
