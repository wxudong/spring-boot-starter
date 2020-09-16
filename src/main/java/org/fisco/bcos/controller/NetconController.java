package org.fisco.bcos.controller;

import com.alibaba.fastjson.JSON;
import org.fisco.bcos.client.NetconClient;
import org.fisco.bcos.jpa.NetconService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class NetconController {

    @Autowired
    private NetconService netconService;
    @Autowired
    private NetconClient netconClient;

    @PostMapping("/api/netcon/create")
    @ResponseBody
    public String create(@RequestParam(value="netconid") String netconid,
                         @RequestParam(value="applya") String applya,
                         @RequestParam(value="applyb") String applyb,
                         @RequestParam(value="addr") String addr,
                         @RequestParam(value="area") String area,
                         @RequestParam(value="balance") String balance) {
        netconClient.deployNetconAndRecordAddr();
        netconService.saveNetcon(netconid, applya, applyb, addr, area, balance);
        ResResult res = new ResResult(0, "Save netcon to database success");
        return JSON.toJSONString(res);
    }

    @GetMapping("/api/netcon/queryall")
    @ResponseBody
    public String queryall() {
        ResResult res = new ResResult(0, netconService.findAll());
        return JSON.toJSONString(res);
    }

    @PostMapping("/api/netcon/tocc")
    @ResponseBody
    public String tocc(@RequestParam(value="uuid") String uuid,
                       @RequestParam(value="netconid") String netconid,
                       @RequestParam(value="applya") String applya,
                       @RequestParam(value="applyb") String applyb,
                       @RequestParam(value="addr") String addr,
                       @RequestParam(value="area") String area,
                       @RequestParam(value="balance") String balance) {
        ResResult res = netconClient.saveNetcon(uuid, netconid, applya, applyb, addr, area, balance);
        if(res.getCode() == 0){
            netconService.updateNetcon(uuid);
        }
        return JSON.toJSONString(res);
    }

    @GetMapping("/api/cc/netcon/querybynetconid")
    @ResponseBody
    public String querybynetconid(String netconid) {
        ResResult res = netconClient.querybynetconid(netconid);
        return JSON.toJSONString(res);
    }

    @GetMapping("/api/cc/netcon/queryallid")
    @ResponseBody
    public String queryallid() {
        ResResult res = netconClient.queryAllid();
        return JSON.toJSONString(res);
    }

    @GetMapping("/api/cc/netcon/queryall")
    @ResponseBody
    public String queryallcc() {
        ResResult res = netconClient.queryAll();
        return JSON.toJSONString(res);
    }

}