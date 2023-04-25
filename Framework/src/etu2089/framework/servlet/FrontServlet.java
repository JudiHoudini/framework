/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package etu2089.framework.servlet;

import etu2089.framework.Mapping;
import etu2089.framework.annotation.Url;
import etu2089.framework.view.ModeleView;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author judi
 */
public class FrontServlet extends HttpServlet {
    HashMap<String, Mapping> mappingUrls;

    public HashMap<String, Mapping> getMappingUrls() {
        return mappingUrls;
    }

    public void setMappingUrls(HashMap<String, Mapping> mappingUrls) {
        this.mappingUrls = mappingUrls;
    }
    @Override
    public void init(ServletConfig config) throws ServletException {
        mappingUrls=new HashMap<>();
        String rootPackage = config.getInitParameter("rootPackage");
        File folder=new File(rootPackage);
        File[]files=folder.listFiles();
        for(File file:files){
            String fileName=file.getName().split(".java")[0];
            Class<?> classTemp=null;
            try {
                classTemp = Class.forName("etu2089.framework.dataObject."+fileName);
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Object obj=null;
            try {
                obj = classTemp.newInstance();
            } catch (InstantiationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Method[] methods=obj.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if(method.isAnnotationPresent(Url.class)){
                    String url=method.getAnnotation(Url.class).url();
                    String className=obj.getClass().getName();
                    String methodName=method.getName();
                    mappingUrls.put(url,new Mapping(className,methodName));
                }
            }
        } 
    }
    
    
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet FrontServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet FrontServlet at " + request.getContextPath() + "</h1>");
            //out.print(mappingUrls.values());
            String url = request.getServletPath();
            String[] link = url.split("/");
            url = link[1];
            out.print(url);
            ModeleView vue;
            try {
                if(getView(url)!=null){
                    if(url.contains("save")){
                        Enumeration<String> parameters = request.getParameterNames();
                        String[] attributs = new String[0];
                        String[] values = new String[0];
                        while(parameters.hasMoreElements()){
                            String parametre = parameters.nextElement();
                            String value = request.getParameter(parametre);
                            attributs = Arrays.copyOf(attributs, attributs.length + 1);
                            values = Arrays.copyOf(values, values.length + 1);
                            attributs[attributs.length - 1] = parametre;
                            values[values.length - 1]  = value;
                        }
                        vue = this.save(url, attributs, values);
                    }else{
                        vue = getView(url);
                    }
                    String page = vue.getUrl();
                    for (Map.Entry m: vue.getData().entrySet()) {
                        request.setAttribute((String) m.getKey(), m.getValue());
                    }
                    RequestDispatcher dispatch = request.getRequestDispatcher(page);
                    dispatch.forward(request, response);
                }else{
                    out.print("url inconnu");
                }
            } catch (Exception e) {
                
            }
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    public ModeleView getView(String url) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException{
        ModeleView valiny= null;
        if(getMappingUrls().get(url) instanceof Mapping){
            Mapping util = getMappingUrls().get(url);
            Class classname =  Class.forName(util.getClassName());
            Object test = classname.newInstance();
            Method method = test.getClass().getMethod(util.getMethod());
            Object page = method.invoke(test);
            valiny = (ModeleView) page;
        }
        return valiny;
    }
    public ModeleView save(String url,String[] params,String[] values) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException{
        String classname = this.getMappingUrls().get(url).getClassName();
        String methode = this.getMappingUrls().get(url).getMethod();
        Class<?> classe = Class.forName(classname);
        Object objet = classe.newInstance();
        Field[] fields = classe.getDeclaredFields();
        String[] allparm = new String[0];
        for(Field field : fields){
            for(int i = 0;i < params.length;i++){
                if(params[i].equals(field.getName())){
                    Method setobject = classe.getMethod("set"+this.upperFirst(params[i]),field.getType());
                    Object type = null;
                    if (field.getType() == String.class) {
                        type = values[i];
                    } else if (field.getType() == int.class || field.getType() == Integer.class) {
                        type = Integer.valueOf(values[i]);
                    } else if (field.getType() == double.class || field.getType() == Double.class) {
                        type = Double.valueOf(values[i]);
                    } else if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                        type = Boolean.valueOf(values[i]);
                    }
                    allparm = Arrays.copyOf(allparm,allparm.length + 1);
                    allparm[allparm.length - 1] = type.getClass().getName();
                    setobject.invoke(objet, type);
                }
            }
        }
        Method method = classe.getDeclaredMethod(methode);
        ModeleView mv = (ModeleView)method.invoke(objet);
        return mv;
    }
    public String upperFirst(String text){
        return text.substring(0,1).toUpperCase()+text.substring(1);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
