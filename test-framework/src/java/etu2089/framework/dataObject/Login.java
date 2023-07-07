/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etu2089.framework.dataObject;

import etu2089.framework.annotation.Url;
import etu2089.framework.view.ModeleView;


/**
 *
 * @author rado
 */
public class Login {
    String name, pass;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Login(String u_name, String pass) {
        this.setName(u_name);
        this.setPass(pass);
    }

    public Login() {
    }
    
    @Url(url="login")
    public ModeleView login(){
        String view = "addEmp.jsp";
        ModeleView mv = new ModeleView(view);
        mv.setIsJson(false);
        if(this.getName().equals("Admin") && this.getPass().equals("Admin")){
            mv.addSession("isConnected", true);
            mv.addSession("profil", 11);
        } else if(this.getName().equals("Autre") && this.getPass().equals("qwertz")){
            mv.addSession("isConnected", true);
            mv.addSession("profil", 1);
        }
        return mv;
    }
    @Url(url="logout")
    public ModeleView logout(){
        String view = "index.jsp";
        ModeleView mv = new ModeleView(view);
        mv.setIsJson(false);
        mv.setInvalidateSession(true);
        return mv;
    }
    
    
}
