package org.fisco.bcos.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NetconServiceImpl implements NetconService
{
    private final String OPERATOR = "wxd";
    @Autowired
    private NetconJpaRepository netconJpaRepository;

    public List<NetconData> findAll()
    {
        return netconJpaRepository.findAll();
    }

    public void saveNetcon(String NetconID, String ApplyA, String ApplyB,
                           String Addr, String Area, String Balance)
    {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String CreateDT = formatter.format(date);
        String ID = String.valueOf(date.getTime());
        NetconData netcon = new NetconData(ID, CreateDT, NetconID, ApplyA, ApplyB, Addr, Area, Balance, getOperator(), 0);
        netconJpaRepository.save(netcon);
    }

    private String getOperator(){
        return OPERATOR;
    }

    public void updateNetcon(String id){
        Optional<NetconData> data = netconJpaRepository.findById(id);
        NetconData netconData = data.get();
        netconData.IsCCed = 1;
        netconJpaRepository.save(netconData);
    }
}
