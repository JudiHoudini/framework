/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etu2089.framework.dataObject;

import etu2089.framework.annotation.Url;
import etu2089.framework.view.ModeleView;

/**
 *
 * @author judi
 */
public class Emp {
    @Url(url="appelMoi")
    public ModeleView getListeEmp(){
        ModeleView valiny = new ModeleView("listeEmp.jsp");
        return valiny;
    }
}
