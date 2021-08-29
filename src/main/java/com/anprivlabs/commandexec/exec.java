/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anprivlabs.commandexec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author Melih
 */
public class exec {

    private final ProcessBuilder processBuilder = new ProcessBuilder();
    private Process process;

    public exec() {
    }

    public StringBuilder ExecuteCommand(ArrayList command) {
        System.out.println(command);
        processBuilder.command(command);

        StringBuilder output = new StringBuilder();
        try {
            this.process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader
                    = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            while ((line = reader.readLine()) != null || (line = errorReader.readLine()) != null) {
                output.append(line + "\n");
            }
            //  System.out.println(output);
            int exitCode = process.waitFor();
            System.out.println("\nExited with error code : " + exitCode);
            if (exitCode != 0) {
                output.append("Exited with error code : " + exitCode);
            }
            reader.close();

        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }

        return output;
    }

    public String ExecuteCommand(String command) {
        System.out.println(command);
        processBuilder.command("cmd.exe /c", command);

        StringBuilder output = new StringBuilder();
        try {
            this.process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader
                    = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            while ((line = reader.readLine()) != null || (line = errorReader.readLine()) != null) {
                output.append(line + "\n");
            }

            int exitCode = process.waitFor();
            System.out.println("\nExited with error code : " + exitCode);
            if (exitCode != 0) {
                output.append("Exited with error code : " + exitCode);
            }
            reader.close();

        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }

        return output.toString();
    }

}
