/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anprivlabs.parse;

import com.sun.jna.platform.FileUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.util.Map;
import java.util.Scanner;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author Melih
 *
 *
 * This class is parsing vulnerable machines from Files/Vuln/vuln.txt path .
 *
 *
 *
 */
public class ParseModule {

    Map<String, Object> obj;
    Map<String, Object> settings;
    Yaml yaml = new Yaml();

    public ParseModule() throws IOException {
        readSettings();
        readvulns();

    }

    private void parse() throws IOException {
         

    }

    private StringBuilder readData(String path) throws FileNotFoundException, IOException {

        FileInputStream inputStream = null;
        Scanner sc = null;
        StringBuilder data = new StringBuilder();
        try {
            inputStream = new FileInputStream(path);
            sc = new Scanner(inputStream, "UTF-8");
            while (sc.hasNextLine()) {
                String line = sc.nextLine();

                data.append(line + "\n");
            }
            // note that Scanner suppresses exceptions
            if (sc.ioException() != null) {
                throw sc.ioException();
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (sc != null) {
                sc.close();
            }

        }
        return data;

    }

    private void readSettings() throws IOException {
        String path = "Files/settings.yaml";

        settings = yaml.load(readData(path).toString());
    }

    public void readvulns() throws IOException {
        String path = "Files/Vuln/vuln.yaml";
        parseVulns(readData(path).toString());
    }

    private void parseVulns(String vuln) {

        obj = yaml.load(vuln);
        System.out.println(obj);

    }

    public String[] vulnTypes() {
        String keys = obj.keySet().toString();

        return keys.split(",");
    }

    public String[] returnVulns(String vuln) {
        String value = obj.get(vuln).toString();
        return value.split(",");
    }

    public Map<String, Object> getObject() {

        return obj;
    }
    public String getSettings(String attr){
        
        return settings.get(attr).toString();
    }
    
}
