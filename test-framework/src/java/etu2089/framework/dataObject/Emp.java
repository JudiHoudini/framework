/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etu2089.framework.dataObject;

import etu2089.framework.annotation.Url;
import etu2089.framework.view.ModeleView;
import java.util.Vector;

/**
 *
 * @author judi
 */
public class Emp {
    String nom;
    String prenom;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Emp(String nom, String prenom) {
        this.setNom(nom);
        this.setPrenom(prenom);
    }
    

    public Emp() {
    }
    
    
    @Url(url="emp-all")
    public ModeleView getListeEmp(){
        ModeleView valiny = new ModeleView("listeEmp.jsp");
        Vector<Emp> ls = new Vector<>();
        ls.add(new Emp("Jean","Marc"));
        ls.add(new Emp("Jean","Yves"));
        ls.add(new Emp("Jean","Charles"));
        ls.add(new Emp("Bob","Alice"));
        valiny.addItem("liste", ls);
        return valiny;
    }
    @Url(url="emp-save")
    public ModeleView save(){
        ModeleView valiny = new ModeleView("listeEmp.jsp");
        Vector<Emp> ls = new Vector<>();
        ls.add(this);
        valiny.addItem("liste",ls);
        return valiny;
    }
}
