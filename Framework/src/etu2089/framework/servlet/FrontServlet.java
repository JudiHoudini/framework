/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package etu2089.framework.servlet;

import etu2089.framework.Mapping;
import etu2089.framework.annotation.Url;
import etu2089.framework.exception.UrlInconue;
import etu2089.framework.view.ModeleView;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Date;

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
        mappingUrls = new HashMap<>();
        String rootPackage = config.getInitParameter("rootPackage");
        File folder = new File(rootPackage);
        File[] files = folder.listFiles();
        for (File file : files) {
            String fileName = file.getName().split(".java")[0];
            Class<?> classTemp = null;
            try {
                classTemp = Class.forName("etu2089.framework.dataObject." + fileName);
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Object obj = null;
            try {
                obj = classTemp.newInstance();
            } catch (InstantiationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Method[] methods = obj.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Url.class)) {
                    String url = method.getAnnotation(Url.class).url();
                    String className = obj.getClass().getName();
                    String methodName = method.getName();
                    mappingUrls.put(url, new Mapping(className, methodName));
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
        String[] parts = request.getServletPath().split("/");
        String url = parts[parts.length - 1];
        ModeleView mv = null;
        PrintWriter out = response.getWriter();
        Enumeration<String> parametres = request.getParameterNames();
        response.setContentType("text/html;charset=UTF-8");

        out.print(url);
        ModeleView vue;
        try {
            Enumeration<String> parameters = request.getParameterNames();
            String[] attributs = new String[0];
            String[] values = new String[0];
            while (parameters.hasMoreElements()) {
                String parametre = parameters.nextElement();
                String value = request.getParameter(parametre);
                attributs = Arrays.copyOf(attributs, attributs.length + 1);
                values = Arrays.copyOf(values, values.length + 1);
                attributs[attributs.length - 1] = parametre;
                values[values.length - 1] = value;
            }
            if (getModeleView(url, attributs, values) != null) {
                vue = this.getModeleView(url, attributs, values);
                String page = vue.getUrl();
                for (Map.Entry m : vue.getData().entrySet()) {
                    request.setAttribute((String) m.getKey(), m.getValue());
                }
                RequestDispatcher dispatch = request.getRequestDispatcher(page);
                dispatch.forward(request, response);
            }
            int x = 8;
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }

    public Method getMethod(String method, Object obj) {
        Method[] listeMethod = obj.getClass().getDeclaredMethods();
        Method valiny = null;
        for (Method method1 : listeMethod) {
            if (method1.getName().equals(method)) {
                valiny = method1;
            }
        }
        return valiny;
    }

    public ModeleView getModeleView(String url, String[] params, String[] values) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        ModeleView valiny = null;
        if (getMappingUrls().get(url) instanceof Mapping) {
            Mapping util = getMappingUrls().get(url);
            Class classname = Class.forName(util.getClassName());
            Object test = classname.newInstance();
            Method m = this.getMethod(util.getMethod(), test);
            Field[] fields = classname.getDeclaredFields();
            Parameter[] parametres = m.getParameters();
            Object[] tableau = new Object[parametres.length];
            String[] allparm = new String[0];
            for (Field field : fields) {
                for (int i = 0; i < params.length; i++) {
                    if (params[i].equals(field.getName())) {
                        Method setobject = classname.getMethod("set" + this.upperFirst(params[i]), field.getType());
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
                        allparm = Arrays.copyOf(allparm, allparm.length + 1);
                        allparm[allparm.length - 1] = type.getClass().getName();
                        setobject.invoke(test, type);
                    }
                }
            }
            int x = 0;
            for (int j = 0; j < parametres.length; j++) {
                for (int i = 0; i < params.length; i++) {
                    if (params[i].equals(parametres[j].getName())) {
                        Object type = null;
                        if (parametres[j].getType() == String.class) {
                            type = values[i];
                        } else if (parametres[j].getType() == int.class || parametres[j].getType() == Integer.class) {
                            type = Integer.valueOf(values[i]);
                        } else if (parametres[j].getType() == double.class || parametres[j].getType() == Double.class) {
                            type = Double.valueOf(values[i]);
                        } else if (parametres[j].getType() == boolean.class || parametres[j].getType() == Boolean.class) {
                            type = Boolean.valueOf(values[i]);
                        }
                        tableau[j] = type;
                        int f = 0;
                    }
                }
            }
            valiny = (ModeleView) m.invoke(test, tableau);
        }
        return valiny;
    }

    public ModeleView getView(String url) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
        ModeleView valiny = null;
        if (getMappingUrls().get(url) instanceof Mapping) {
            Mapping util = getMappingUrls().get(url);
            Class classname = Class.forName(util.getClassName());
            Object test = classname.newInstance();
            Method method = test.getClass().getMethod(util.getMethod());
            Object page = method.invoke(test);
            valiny = (ModeleView) page;
        }
        return valiny;
    }

    public ModeleView save(String url, String[] params, String[] values) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
        String classname = this.getMappingUrls().get(url).getClassName();
        String methode = this.getMappingUrls().get(url).getMethod();
        Class<?> classe = Class.forName(classname);
        Object objet = classe.newInstance();
        Field[] fields = classe.getDeclaredFields();
        String[] allparm = new String[0];
        for (Field field : fields) {
            for (int i = 0; i < params.length; i++) {
                if (params[i].equals(field.getName())) {
                    Method setobject = classe.getMethod("set" + this.upperFirst(params[i]), field.getType());
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
                    allparm = Arrays.copyOf(allparm, allparm.length + 1);
                    allparm[allparm.length - 1] = type.getClass().getName();
                    setobject.invoke(objet, type);
                }
            }
        }
        Method method = classe.getDeclaredMethod(methode);
        ModeleView mv = (ModeleView) method.invoke(objet);
        return mv;
    }

    public String upperFirst(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    public ModeleView det(String url, String[] params, String[] values, PrintWriter out) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, UrlInconue, InstantiationException {
        String classname = this.getMappingUrls().get(url).getClassName();
        String methode = this.getMappingUrls().get(url).getMethod();
        Class<?> classe = Class.forName(classname);
        Object objet = classe.newInstance();
        Method method = classe.getDeclaredMethod(methode, Integer.class);
        Parameter[] de = method.getParameters();
        Object[] type = new Object[de.length];
        for (Parameter param : de) {
            String nom = param.getName();
            //out.print("<h3>"+nom+"</h3>");
            for (int i = 0; i < params.length; i++) {
                if (params[i].equals(nom)) {
                    if (param.getType() == Integer.class) {
                        type[i] = Integer.valueOf(values[0]);
                    } else if (param.getType() == String.class) {
                        type[i] = Integer.valueOf(values[i]);
                    } else if (param.getType() == Double.class) {
                        type[i] = Double.valueOf(values[i]);
                    } else if (param.getType() == boolean.class || param.getType() == Boolean.class) {
                        type[i] = Boolean.valueOf(values[i]);
                    } else if (param.getType() == Date.class) {
                        type[i] = Date.valueOf(values[i]);
                    }
                }
            }
        }
        ModeleView mv = (ModeleView) method.invoke(objet, type);
        return mv;
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
