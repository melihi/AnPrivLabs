/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anprivlabs.vmdeploy;

import com.anprivlabs.parse.ParseModule;
import java.util.HashMap;
import com.anprivlabs.machine.machine;
import com.anprivlabs.commandexec.exec;
import com.anprivlabs.connection.ssh;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Melih
 */
public class vmDeploy {

    private ParseModule settings;
    private String path;
    private HashMap<String, machine> ActiveMachines;
    private exec execModule = new exec();
    private ArrayList<String> commands = new ArrayList<String>();
    private String curPath = System.getProperty("user.dir");
    private String[] regex = {"/VirtualBox/GuestInfo/OS/Product", "/VirtualBox/GuestInfo/OS/Release",
        "/VirtualBox/GuestInfo/Net/0/MAC", "/VirtualBox/GuestInfo/Net/0/V4/IP"};
    private Pattern patPackageDescription = Pattern.compile("[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}");
    private Matcher matPackageDescription;

    public vmDeploy() {
        try {
            settings = new ParseModule();
        } catch (Exception e) {
        }

        path = settings.getSettings("VirtualBoxPath");

    }

    public boolean startVm(machine mac) {
        //makine isminin valied olup oladigina bak
        if (!check(mac)) {
            System.out.println("ERROR");
            return false;

        }

        if (mac.getVulnType() == "VulnHub") {
            if (check(mac)) {
                if (startVulnHub(mac)) {
                    mac.setStatus(1);
                    return true;
                }
            }
        }
        if (mac.getVulnType() == "Windows") {
            return false;
        }
        boolean res = runAnsible(mac);
        if (res) {
            mac.setStatus(1);

            return res;
        }

        return false;
    }

    public boolean check(machine mac) {

        String id = grapid(mac);
        if (id == "") {

            return true;
        }

        return false;
    }

    private String grapid(machine mac) {
        String lon = vminfo(mac);

        matPackageDescription = patPackageDescription.matcher(lon);
        String data = "";
        while (matPackageDescription.find()) {
            data = matPackageDescription.group();

            break;
        }

        return data;
    }

    private String grapid(String name) {
        String lon = vminfo(name);

        matPackageDescription = patPackageDescription.matcher(lon);
        String data = "";
        while (matPackageDescription.find()) {
            data = matPackageDescription.group();

            break;
        }

        return data;
    }

    private String vminfo(machine mac) {
        ArrayList<String> commands = new ArrayList<String>();
        commands.add("cmd.exe");
        commands.add("/c");
        commands.add(path + "/VBoxManage.exe showvminfo \"" + mac.getVmName() + "\"");
        String lon = execModule.ExecuteCommand(commands).toString();
        commands.clear();
        return lon;

    }

    public String vminfo(String name) {
        ArrayList<String> commands = new ArrayList<String>();
        commands.add("cmd.exe");
        commands.add("/c");
        commands.add(path + "/VBoxManage.exe showvminfo \"" + name + "\"");
        String lon = execModule.ExecuteCommand(commands).toString();
        commands.clear();
        return lon;

    }

    public ArrayList listAllVms() {
        ArrayList<String> commands = new ArrayList<String>();
        commands.add("cmd.exe");
        commands.add("/c");
        String comm = path + "/VBoxManage.exe list vms";
        ArrayList<String> list = new ArrayList<>();
        commands.add(comm);
        String[] data;
        String result = execModule.ExecuteCommand(commands).toString();

        commands.clear();
        Pattern p = Pattern.compile("\"([^\"]*)\"");
        Matcher m = p.matcher(result);
        while (m.find()) {
            System.out.println(m.group(1));
            list.add(m.group(1));
        }
        return list;

    }

    public String vmData(String name) {
        ArrayList<String> commands = new ArrayList<String>();
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < regex.length; i++) {
            commands.add("cmd.exe");
            commands.add("/c");
            String comm = path + "/VBoxManage.exe  guestproperty get \"" + name + "\" \"" + regex[i] + "\"";
            commands.add(comm);
            String result = execModule.ExecuteCommand(commands).toString();
            result = result.replaceAll("Value: ", "");
            System.out.println(result);
            commands.clear();
            out.append(result).append(";");
            if (i == 0) {
                out.append(name + ";");
                out.append(grapid(name) + ";");
            }
            if (i == 3) {
                commands.clear();
                commands.add("cmd.exe");
                commands.add("/c");
                commands.add(path + "/VBoxManage.exe showvminfo \"" + name + "\"");
                result = execModule.ExecuteCommand(commands).toString();

                commands.clear();
                if (result.contains("running")) {
                    out.append("RUNNING;");

                } else if (result.contains("saved")) {
                    out.append("SAVED;");
                } else if (result.contains("aborted")) {
                    out.append("ABORTED;");
                } else {
                    out.append("POWERED OFF;");
                }
            }
        }
        return out.toString();
    }

    private boolean startVulnHub(machine mac) {
        ArrayList<String> commands = new ArrayList<String>();

        commands.add("cmd.exe");
        commands.add("/c");
        String comm = path + "/VBoxManage.exe import  \"" + curPath + "/Files/Vuln/VulnHub/" + mac.getVm() + ".ova\" --vsys 0 --vmname \"" + mac.getVmName() + "\" --cpus " + mac.getCpu()
                + " --memory " + mac.getRam();

        commands.add(comm);

        String result = execModule.ExecuteCommand(commands).toString();
        System.out.println(result);
        System.out.println(comm);
        commands.clear();
        if (result.contains("Successfully imported the appliance.")) {
            System.out.println("machine deployed " + mac.getVm() + mac.getVmName());
            mac.setVmUUID(grapid(mac));

            return true;
        }
        return false;

    }

    private boolean startLinux(machine mac) {
        ArrayList<String> commands = new ArrayList<String>();

        commands.add("cmd.exe");
        commands.add("/c");
        String comm = path + "/VBoxManage.exe import  \"" + curPath + "/Files/Vuln/Linux/Ubuntu20.04.ova\" --vsys 0 --vmname \"" + mac.getVmName() + "\" --cpus " + mac.getCpu()
                + " --memory " + mac.getRam();

        commands.add(comm);

        String result = execModule.ExecuteCommand(commands).toString();
        System.out.println(result);
        System.out.println(comm);
        commands.clear();
        if (result.contains("Successfully imported the appliance.")) {
            System.out.println("machine deployed " + mac.getVm() + mac.getVmName());
            mac.setVmUUID(grapid(mac));
            

            return true;
        }
        return false;

    }

    private boolean runAnsible(machine mac) {

        if (startLinux(mac)) {
            RunVm(mac.getVmName(), "gui");
            try {
                Thread.sleep(60000);
            } catch (Exception e) {
            }

            System.out.println("ansible insideeeee");
            ssh ans = new ssh();
           
            String ip = vmData(mac.getVmName()).split(";")[5].replace("\n", " ");
            System.out.println("MAKINA ADI ANSIBLE" + ip);
            String te = "echo '" + mac.getVmName() + " ansible_host=" + ip + "  ansible_connection=ssh ansible_ssh_pass=hotdog123456789 ansible_ssh_user=anprivlabs ansible_sudo_pass=hotdog123456789' >> /etc/ansible/hosts".trim();
            if (te.contains("VBoxManage.exe: error:")) {
                return false;

            }
            System.out.println(te);
            ans.runCom(te);
            try {
                Thread.sleep(6000);
            } catch (Exception e) {
            }
            String vm = mac.getVm().replaceAll("\\s", "");
            String ansib = ans.runCom("ansible-playbook --limit=\"" +  mac.getVmName() + "\" /etc/ansible/playbooks/" + vm + ".yml");
            System.out.println("ansible-playbook --limit=" + mac.getVmName() + " /etc/ansible/playbooks/" + vm + ".yml" + "ANSIBLE PLAYBOOK SONUCC");
            /*
            if (!ansib.contains("failed=0")){
                return false;
            }*/

        }
        return true;

    }

    public boolean RunVm(String name, String type) {
        ArrayList<String> commands = new ArrayList<String>();
        commands.add("cmd.exe");
        commands.add("/c");
        String comm = path + "/VBoxManage.exe startvm \"" + name + "\" --type " + type;

        commands.add(comm);
        String result = execModule.ExecuteCommand(commands).toString();
        if (!result.contains("been successfully started.")) {
            return false;
        }
        return true;
    }

    public boolean StopVm(String name, String type) {
        ArrayList<String> commands = new ArrayList<String>();
        commands.add("cmd.exe");
        commands.add("/c");
        String comm = path + "/VBoxManage.exe controlvm  \"" + name + "\" " + type;

        commands.add(comm);
        String result = execModule.ExecuteCommand(commands).toString();
        if (!result.contains("100%")) {
            return false;
        }
        return true;
    }

    public HashMap<String, machine> getQueue() {

        return ActiveMachines;
    }

}
