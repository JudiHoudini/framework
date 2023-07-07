/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package etu2089.framework.servlet;

import com.google.gson.Gson;
import etu2089.framework.Mapping;
import etu2089.framework.annotation.Authentification;
import etu2089.framework.annotation.Singleton;
import etu2089.framework.annotation.Url;
import etu2089.framework.fileUpload.FileUpload;
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
import jakarta.servlet.http.Part;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Date;
import java.util.ArrayList;

/**
 *
 * @author judi
 */
public class FrontServlet extends HttpServlet {

    HashMap<String, Mapping> mappingUrls;
    HashMap<String, Object> singleton;
    HashMap<String, Object> sessions;

    public HashMap<String, Mapping> getMappingUrls() {
        return mappingUrls;
    }

    public void setMappingUrls(HashMap<String, Mapping> mappingUrls) {
        this.mappingUrls = mappingUrls;
    }

    public HashMap<String, Object> getSingleton() {
        return singleton;
    }

    public void setSingleton(HashMap<String, Object> singleton) {
        this.singleton = singleton;
    }

    public HashMap<String, Object> getSessions() {
        return sessions;
    }

    public void setSessions(HashMap<String, Object> sessions) {
        this.sessions = sessions;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        mappingUrls = new HashMap<>();
        singleton = new HashMap<>();
        String rootPackage = config.getInitParameter("rootPackage");
        File folder = new File(rootPackage);
        File[] files = folder.listFiles();
        Enumeration sessionNames = config.getInitParameterNames();
        HashMap<String, Object> session = new HashMap<>();
        while (sessionNames.hasMoreElements()) {
            String name = (String) sessionNames.nextElement();
            if (!name.equals("sourceFile")) {
                String parameterValue = config.getInitParameter(name);
                session.put(name, parameterValue);
            }
        }
        this.setSessions(session);
        for (File file : files) {
            String fileName = file.getName().split(".java")[0];
            Class<?> classTemp = null;
            try {
                classTemp = Class.forName("etu2089.framework.dataObject." + fileName);
                this.checkSingleton(classTemp);
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

    public void checkSingleton(Class check) {
        if (check.isAnnotationPresent(Singleton.class)) {

            String className = check.getName();
            this.getSingleton().put(className, null);
        }
    }

    public void reset(Object objet) throws IllegalAccessException, InvocationTargetException {
        Field[] fields = objet.getClass().getDeclaredFields();
        for (Field field : fields) {
            String fieldName = upperFirst(field.getName());
            Method methodSet = null;
            try {
                methodSet = objet.getClass().getMethod("set" + fieldName, field.getType());
            } catch (Exception e) {
                continue;
            }
            if (field.getType().equals(int.class)) {
                methodSet.invoke(objet, 0);
            }
            if (field.getType().equals(double.class)) {
                methodSet.invoke(objet, 0);
            }
            if (field.getType().equals(float.class)) {
                methodSet.invoke(objet, 0);
            }
            if (field.getType().equals(Object.class)) {
                methodSet.invoke(objet, null);
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

        out.println(url);
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
            if (getModeleView(url, attributs, values, request, out) != null) {
                vue = this.getModeleView(url, attributs, values, request, out);
                String page = vue.getUrl();
                for (Map.Entry m : vue.getData().entrySet()) {
                    request.setAttribute((String) m.getKey(), m.getValue());
                }
                if (!vue.getSessions().isEmpty()) {
                    fillSessions(request, vue.getSessions());
                }
                this.invalidateSession(request, vue);
                if (vue.isJson()) {
                    Gson json = new Gson();
                    String jsonString = json.toJson(vue.getData());
                    out.print(jsonString);
                } else {
                    RequestDispatcher dispatch = request.getRequestDispatcher(page);
                    dispatch.forward(request, response);
                }
            }
            int x = 8;
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }

    public void fillSessions(HttpServletRequest req, HashMap<String, Object> sessionsFromDataObject) {
        for (Map.Entry<String, Object> entry : sessionsFromDataObject.entrySet()) {
            if (sessions.containsValue(entry.getKey())) {
                req.getSession().setAttribute(entry.getKey(), entry.getValue());
            }
        }
    }

    public void checkAuthorisation(Method m, HttpServletRequest req, PrintWriter out) throws Exception {
        if (m.isAnnotationPresent(Authentification.class)) {
            int reference = m.getAnnotation(Authentification.class).reference();
            String sessionProfilName = (String) this.sessions.get("sessionName");
            String sessionProfil = (String) this.sessions.get("sessionProfil");
            if (req.getSession().getAttribute(sessionProfilName) == null) {
                throw new Exception("Vous devriez vous connecter");
            }
            int userProfil = 0;
            if(req.getSession().getAttribute(sessionProfil) != null){
                userProfil = (int) req.getSession().getAttribute(sessionProfil);
            }
            if (reference > userProfil) {
                String exceptionMessage = "Vous n'etes pas en mesure d'appeller cette fonction";
                throw new Exception(exceptionMessage);
            }
        }
    }

    public void invalidateSession(HttpServletRequest req, ModeleView mv) {
        if (mv.isInvalidateSession()) {
            req.getSession().invalidate();
        }
        int y = 0;
        Object rs = mv.getRemoveSession();
        if (!((ArrayList)rs).isEmpty()) {
            int x = 0;
            for (String sessionRm : mv.getRemoveSession()) {
                Object sessionAttributes = req.getSession().getAttributeNames();
                Object sessionAttribute = req.getSession().getAttribute(sessionRm);
                req.getSession().removeAttribute(sessionRm);
            }
        }
    }

    public void getFileByInput(HttpServletRequest request, Field field) throws IOException, ServletException {
        Part part = null;
        part = request.getPart(field.getName());
        if (part != null) {
            InputStream inStream = part.getInputStream();
            byte[] fileBytes = inStream.readAllBytes();
            inStream.close();
            String fileName = part.getSubmittedFileName();
            FileUpload fu = new FileUpload();
            fu.setBits(fileBytes);
            fu.setFileName(fileName);
            System.out.println(fu.getFileName() + "===" + fu.getBits());
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

    public Object getInClassInstance(String className, Class<?> classe) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Object objet = null;
        if (this.getSingleton().containsKey(className)) {
            Object obj = this.getSingleton().get(className);
            if (obj == null) {
                obj = classe.newInstance();
                objet = obj;
                this.getSingleton().put(className, objet);
            } else {
                reset(obj);
                objet = obj;
            }
        } else {
            objet = classe.newInstance();
        }
        return objet;
    }

    public ModeleView getModeleView(String url, String[] params, String[] values, HttpServletRequest req, PrintWriter out) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, IOException, ServletException {
        ModeleView valiny = null;
        if (getMappingUrls().get(url) instanceof Mapping) {
            Mapping util = getMappingUrls().get(url);
            Class classname = Class.forName(util.getClassName());
            Object test = this.getInClassInstance(classname.getName(), classname);
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
                        if (type != null) {
                            allparm[allparm.length - 1] = type.getClass().getName();
                        }
                        setobject.invoke(test, type);
                    }
                }
            }
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
            try {
                this.checkAuthorisation(m, req, out);
                valiny = (ModeleView) m.invoke(test, tableau);
            } catch (Exception e) {
                out.print(e.getMessage());
            }

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
                    if (type != null) {
                        allparm[allparm.length - 1] = type.getClass().getName();
                    }
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
