package com.bosch.rcm.controller;

import com.bosch.rcm.domain.RcmLatestErrorData;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;

@RestController
@RequestMapping("/api/signalError")
public class SignalErrorController {
    @Resource(name = "latestErrorData")
    RcmLatestErrorData latestErrorData;

    public SignalErrorController() {

    }

    @GetMapping(value = "/getLatestData")
    public HashMap<String, Object> getLatestErrorData() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("countData", latestErrorData.getCountErrors());
        data.put("firstErrors", latestErrorData.getFirstErrors());
        return data;
    }

    @GetMapping(value = "/getPieChartData")
    public HashMap<String, Object> getPieChartData() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("countData", latestErrorData.getCountDataForPieChart());
        return data;
    }

    @PostMapping(value = "/resetData")
    public String resetData(@RequestParam("thresID") String thresID) {
        try {
            latestErrorData.resetData(thresID);
            return "Succeed";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
