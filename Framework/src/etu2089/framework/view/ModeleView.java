/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etu2089.framework.view;

import java.util.HashMap;

/**
 *
 * @author judi
 */
public class ModeleView {
    String url;
    HashMap<String, Object> data;
    boolean json;

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
    

    
    public ModeleView(String view) {
        this.setUrl(view);
        this.setData(new HashMap<>());
    }
    public void addItem(String key,Object Value){
        this.getData().put(key, Value);
    }
    
}
