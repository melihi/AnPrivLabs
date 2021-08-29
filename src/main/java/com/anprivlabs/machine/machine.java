/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anprivlabs.machine;

/**
 *
 * @author Melih
 */
public class machine {

    private String vm;//makina turu kioptrix , ubuntu .... gibi
    private String vmUUID;// vm uuidsi sonradan alinip ekleniyor
    private String vmName;// vm adi 
    private String VulnType;//vulnhub ya da digerleri
    private int cpu;//cu cekirdekleri
    private int ram;//ram miktari

    private int status;// status sonradan ekleniyor 1 acik 0 kapali

    public machine(String vm, String vmName, String VulnType, int cpu, int ram) {

        this.vmName = vmName;
        this.VulnType = VulnType;
        this.cpu = cpu;
        this.ram = ram;

        vm = vm.replace(" ", "");
        if (vm.charAt(0) == '[') {
            this.vm = vm.substring(1, vm.length());
        } else if (vm.charAt(vm.length() - 1) == ']') {
            this.vm = vm.substring(0, vm.length() - 1);
        } else {
            this.vm = vm;
        }

    }

    /**
     * @return the vm
     */
    public String getVm() {
        return vm;
    }

    /**
     * @return the cpu
     */
    public int getCpu() {
        return cpu;
    }

    /**
     * @return the ram
     */
    public int getRam() {
        return ram;
    }

    /**
     * @return the vmName
     */
    public String getVmName() {
        return vmName;
    }

    /**
     * @return the VulnType
     */
    public String getVulnType() {
        return VulnType;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return the vmUUID
     */
    public String getVmUUID() {
        return vmUUID;
    }

    /**
     * @param vmUUID the vmUUID to set
     */
    public void setVmUUID(String vmUUID) {
        this.vmUUID = vmUUID;
    }
}
