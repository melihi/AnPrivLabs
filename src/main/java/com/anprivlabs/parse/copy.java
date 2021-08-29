/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anprivlabs.parse;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

/**
 *
 * @author Melih
 */
public class copy {

    public void copyClip(String data) {
        Toolkit toolkit2 = Toolkit.getDefaultToolkit();
        Clipboard clipboard2 = toolkit2.getSystemClipboard();
        StringSelection strSel2 = new StringSelection(data);
        clipboard2.setContents(strSel2, null);
    }

}
