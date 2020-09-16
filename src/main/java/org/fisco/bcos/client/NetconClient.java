package org.fisco.bcos.client;

import org.fisco.bcos.channel.client.Service;
import org.fisco.bcos.channel.handler.GroupChannelConnectionsConfig;
import org.fisco.bcos.constants.GasConstants;
import org.fisco.bcos.contract.Netcon;
import org.fisco.bcos.contract.Table;
import org.fisco.bcos.controller.ResResult;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.tuples.generated.Tuple1;
import org.fisco.bcos.web3j.tuples.generated.Tuple8;
import org.fisco.bcos.web3j.tx.gas.StaticGasProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


@Component
public class NetconClient {

    public @Autowired GroupChannelConnectionsConfig groupChannelConnectionsConfig;
    public @Autowired Service service;
    public @Autowired Web3j web3j;
    public @Autowired Credentials credentials;

    public Netcon netcon;

    public void deployNetconAndRecordAddr(){
        try {
            if(loadNetconAddr() == null) {
                netcon = Netcon.deploy(web3j, credentials, new StaticGasProvider(GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT)).send();
                System.out.println(" deploy Netcon success, contract address is " + netcon.getContractAddress());
                Table table = Table.deploy(web3j, credentials, new StaticGasProvider(GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT)).send();
                System.out.println(" deploy Table success, contract address is " + table.getContractAddress());
                recordAddr(netcon.getContractAddress(), table.getContractAddress());
            }
        } catch (Exception e) {
            System.out.println(" deploy Netcon contract failed, error message is  " + e.getMessage());
        }
    }

    public void recordAddr(String addressA, String addressB) throws IOException {
        System.out.println("recordAddr");
        Properties prop = new Properties();
        prop.setProperty("addressNetcon", addressA);
        prop.setProperty("addressTable", addressB);
        final Resource contractResource = new ClassPathResource("contract.properties");
        FileOutputStream fileOutputStream = new FileOutputStream(contractResource.getFile());
        prop.store(fileOutputStream, "contract address");
    }

    public String loadNetconAddr() throws Exception {
        Properties prop = new Properties();
        final Resource contractResource = new ClassPathResource("contract.properties");
        prop.load(contractResource.getInputStream());

        String contractAddress = prop.getProperty("addressNetcon");
        if (contractAddress == null || contractAddress.trim().equals("")) {
            return null;
        }
        System.out.println(" load Netcon address from contract.properties, address is {}" + contractAddress);
        return contractAddress;
    }

    public ResResult saveNetcon(String uuid, String netconid, String applya, String applyb, String addr, String area, String balance) {
        try {
            String contractAddress = loadNetconAddr();
            netcon = Netcon.load(contractAddress, web3j, credentials, new StaticGasProvider(GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT));
            TransactionReceipt receipt = netcon.insert(uuid, netconid, applya, applyb, addr, area, balance).send();
            List<org.fisco.bcos.contract.Netcon.SaveEventEventResponse> response = netcon.getSaveEventEvents(receipt);
            if (!response.isEmpty()) {
                if (response.get(0).ret.compareTo(new BigInteger("0")) == 0) {
                    System.out.printf(" Netcon save success => NetconID: %s\n", netconid);

                    return new ResResult(0, "save success");
                } else {
                    System.out.printf(" Netcon save failed, ret code is %s \n", response.get(0).ret.toString());
                }
            } else {
                System.out.println(" event log not found, maybe transaction not exec. ");
            }
        } catch (Exception e) {
            System.out.printf(" Netcon save failed, error message is %s\n", e.getMessage());
        }
        return new ResResult(-1, "save failed");
    }


    public ResResult querybynetconid(String netconid) {
        try {
            String contractAddress = loadNetconAddr();
            netcon = Netcon.load(contractAddress, web3j, credentials, new StaticGasProvider(GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT));
            Tuple8<BigInteger, String, String, String, String, String, String, String> result = netcon.queryByNetconId(netconid).send();
            if (result.getValue1().compareTo(new BigInteger("0")) == 0) {
                CNetconData cNetconData = new CNetconData(result.getValue3(), result.getValue4(), result.getValue5(), result.getValue6(), result.getValue7(), result.getValue8());
                List<CNetconData> list = new ArrayList<>();
                list.add(cNetconData);
                return new ResResult(0, list);
            } else {
                System.out.printf(" querybynetconid failed error code" + result.getValue1());
            }
        } catch (Exception e) {
            System.out.printf(" Netcon querybynetconid failed, error message is %s\n", e.getMessage());
        }
        return new ResResult(-1, "querybynetconid failed");
    }

    public ResResult queryAll() {
        try {
            String contractAddress = loadNetconAddr();
            netcon = Netcon.load(contractAddress, web3j, credentials, new StaticGasProvider(GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT));
            TransactionReceipt transactionReceipt = netcon.getNumber().send();
            Tuple1<BigInteger> number = netcon.getGetNumberOutput(transactionReceipt);
            List<CNetconData> list = new ArrayList<>();
            for(int i=1; i<=number.getValue1().intValue(); i++){
                TransactionReceipt transactionReceipt1 = netcon.getIdByNumber(new BigInteger(String.valueOf(i))).send();
                Tuple1<String> id = netcon.getGetIdByNumberOutput(transactionReceipt1);
                Tuple8<BigInteger, String, String, String, String, String, String, String> result = netcon.queryById(id.getValue1()).send();
                if (result.getValue1().compareTo(new BigInteger("0")) == 0) {
                    CNetconData cNetconData = new CNetconData(result.getValue3(), result.getValue4(), result.getValue5(), result.getValue6(), result.getValue7(), result.getValue8());
                    list.add(cNetconData);
                } else {
                    System.out.printf(" queryAll failed error code" + result.getValue1());
                }
            }
            return new ResResult(0, list);
        } catch (Exception e) {
            System.out.printf(" Netcon queryAll failed, error message is %s\n", e.getMessage());
        }
        return new ResResult(-1, "queryAll failed");
    }

    public ResResult queryAllid() {
        try {
            String contractAddress = loadNetconAddr();
            netcon = Netcon.load(contractAddress, web3j, credentials, new StaticGasProvider(GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT));
            TransactionReceipt transactionReceipt = netcon.getNumber().send();
            Tuple1<BigInteger> number = netcon.getGetNumberOutput(transactionReceipt);
            List<String> list = new ArrayList<>();
            for(int i=1; i<=number.getValue1().intValue(); i++){
                TransactionReceipt transactionReceipt1 = netcon.getIdByNumber(new BigInteger(String.valueOf(i))).send();
                Tuple1<String> id = netcon.getGetIdByNumberOutput(transactionReceipt1);
                Tuple8<BigInteger, String, String, String, String, String, String, String> result = netcon.queryById(id.getValue1()).send();
                if (result.getValue1().compareTo(new BigInteger("0")) == 0) {
                    String netconid = result.getValue3();
                    list.add(netconid);
                } else {
                    System.out.printf(" queryAll failed error code" + result.getValue1());
                }
            }
            return new ResResult(0, list);
        } catch (Exception e) {
            System.out.printf(" Netcon queryAll failed, error message is %s\n", e.getMessage());
        }
        return new ResResult(-1, "queryAll failed");
    }
}
