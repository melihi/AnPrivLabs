/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anprivlabs.sysinfo;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.util.FormatUtil;

/**
 *
 * @author Melih
 */
public class sysinfo {

    private float totalRam;
    private int cores;

    public sysinfo() {
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        GlobalMemory globalMemory = hardware.getMemory();
        String total = FormatUtil.formatBytes(globalMemory.getTotal());
        String as = total.replace("GiB", "");

        totalRam = Float.valueOf(as) * 1024;
        System.out.println("Total ram :" + totalRam);
        /*
        CentralProcessor processor = systemInfo.getHardware().getProcessor();
        hardwareInfo.logicalCores = processor.getLogicalProcessorCount();
        hardwareInfo.physicalCores = processor.getPhysicalProcessorCount();
         */
        cores = Runtime.getRuntime().availableProcessors();
         
    }

    /**
     * @return the totalRam
     */
    public float getTotalRam() {
        return totalRam;
    }

    /**
     * @return the cores
     */
    public int getCores() {
        return cores;
    }

}
