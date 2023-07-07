/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etu2089.framework.dataObject;

import etu2089.framework.annotation.Authentification;
import etu2089.framework.annotation.Singleton;
import etu2089.framework.annotation.Url;
import etu2089.framework.fileUpload.FileUpload;
import etu2089.framework.view.ModeleView;
import java.util.Vector;

/**
 *
 * @author judi
 */
@Singleton(isSingleton = true)
public class Emp {
    String nom;
    String prenom;
    FileUpload fu;
    int age = 0;

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
    

    public Emp(String nom, String prenom) {
        this.setNom(nom);
        this.setPrenom(prenom);
    }
    

    public Emp() {
    }
    
    @Authentification(reference = 1,value = "Admin")
    @Url(url="emp-all")
    public ModeleView getListeEmp(){
        ModeleView valiny = new ModeleView("listeEmp.jsp");
        Vector<Emp> ls = new Vector<>();
        ls.add(new Emp("Jean","Marc"));
        ls.add(new Emp("Jean","Yves"));
        ls.add(new Emp("Jean","Charles"));
        ls.add(new Emp("Bob","Alice"));
        valiny.addItem("liste", ls);
        //valiny.setIsJson(true);
        return valiny;
    }
    @Url(url="emp-detail")
    public ModeleView getDetailEmp(Integer id){
        ModeleView valiny = new ModeleView("listeEmp.jsp");
        Vector<Emp> ls = new Vector<>();
        ls.add(new Emp("Jean","Marc"));
        ls.add(new Emp("Jean","Yves"));
        ls.add(new Emp("Jean","Charles"));
        ls.add(new Emp("Bob","Alice"));
        valiny.addItem("liste", ls.get(id));
        return valiny;
    }
    @Url(url="emp-savy")
    public ModeleView save(){
        ModeleView valiny = new ModeleView("listeEmp.jsp");
        Vector<Emp> ls = new Vector<>();
        this.setAge(this.getAge()+1);
        ls.add(this);
        valiny.addItem("liste",ls);
        return valiny;
    }
    @Url(url="singleton")
    public ModeleView singleton(){
        ModeleView valiny = new ModeleView("listeEmp.jsp");
        Vector<Emp> ls = new Vector<>();
        this.setAge(this.getAge()+1);
        ls.add(this);
        valiny.addItem("liste",ls);
        return valiny;
    }
    @Url(url="emp-save")
    public ModeleView savy(Integer age){
        ModeleView valiny = new ModeleView("listeEmp.jsp");
        Vector<Emp> ls = new Vector<>();
        this.setAge(this.getAge()+age);
        ls.add(this);
        valiny.addItem("liste",ls);
        return valiny;
    }

    public void setFu(FileUpload fu) {
        this.fu = fu;
    }
    
}
