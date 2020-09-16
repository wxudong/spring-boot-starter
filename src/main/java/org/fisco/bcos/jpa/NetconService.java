package org.fisco.bcos.jpa;

import java.util.List;

public interface NetconService {

    List<NetconData> findAll();
    void saveNetcon(String netconid, String applya, String applyb, String addr, String area, String balance);
    void updateNetcon(String uuid);
}
