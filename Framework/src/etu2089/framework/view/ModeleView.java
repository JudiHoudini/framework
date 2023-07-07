/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etu2089.framework.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author judi
 */
public class ModeleView {
    String url;
    HashMap<String, Object> data;
    HashMap<String,Object> sessions;
    boolean json;
    boolean invalidateSession = false;
    List<String> removeSession;

    public boolean isInvalidateSession() {
        return invalidateSession;
    }

    public void setInvalidateSession(boolean invalidateSession) {
        this.invalidateSession = invalidateSession;
    }

    public List<String> getRemoveSession() {
        return removeSession;
    }

    public void setRemoveSession(List<String> removeSession) {
        this.removeSession = removeSession;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String view) {
        this.url = view;
    }

    public HashMap<String, Object> getData() {
        return data;
    }

    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }

    public boolean isJson() {
        return json;
    }

    public void setIsJson(boolean isJson) {
        this.json = isJson;
    }

    public HashMap<String, Object> getSessions() {
        return sessions;
    }

    public void setSessions(HashMap<String, Object> sessions) {
        this.sessions = sessions;
    }
    

    
    public ModeleView(String view) {
        this.setUrl(view);
        this.setData(new HashMap<>());
        this.setSessions(new HashMap<>());
        this.setRemoveSession(new ArrayList<>());
    }
    public void addItem(String key,Object Value){
        this.getData().put(key, Value);
    }
     public void addSession(String cle, Object objet){
        this.getSessions().put(cle, objet);
    }
     public void addrmSession(String sessionName){
        this.getRemoveSession().add(sessionName);
    }
    
}
