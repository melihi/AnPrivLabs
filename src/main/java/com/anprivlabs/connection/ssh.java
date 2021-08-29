/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anprivlabs.connection;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.io.ByteArrayOutputStream;

/**
 *
 * @author Melih
 */
public class ssh {

    private static Session session;
    private static ChannelExec channel = null;
    private String data;
    private sshConnection con;

    public boolean startSSH(sshConnection con) {
        this.con = con;
        try {
            session = new JSch().getSession(con.getUsername(), con.getHost(), con.getPort());
            getSession().setPassword(String.valueOf(con.getPassword()));
            getSession().setConfig("StrictHostKeyChecking", "no");
            getSession().connect();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public String runCom(String command) {
        try {
            ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
            channel = (ChannelExec) getSession().openChannel("exec");
            channel.setCommand(command);
            channel.setOutputStream(responseStream);
            channel.connect();
            while (channel.isConnected()) {
                Thread.sleep(100);
            }
            data = new String(responseStream.toByteArray());
            System.out.println(data);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return data;
    }

    public boolean checkCon() {
        try {
            if (runCom("ls /").contains("etc") && session.isConnected()) {
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
 
    /**
     * @return the con
     */
    public sshConnection getCon() {
        return con;
    }

    /**
     * @return the session
     */
    public Session getSession() {
        return session;
    }
}
