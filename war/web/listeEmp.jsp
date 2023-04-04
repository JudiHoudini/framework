<%-- 
    Document   : listeEmp
    Created on : 28 mars 2023, 06:49:44
    Author     : judi
--%>

<%@page import="etu2089.framework.dataObject.Emp"%>
<%@page import="java.util.Vector"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>liste des employee</h1>
        <%
            Vector<Emp> liste = (Vector<Emp>)request.getAttribute("liste");
        %>
        <table border="1">
            <thead>
                <tr>
                    <th>Nom</th>
                    <th>Prenom</th>
                </tr>
            </thead>
            <tbody>
                <%
                    for (Emp elem : liste) {
                        out.print("<tr>");
                        out.print("<td>"+elem.getNom()+"</td>");
                        out.print("<td>"+elem.getPrenom()+"</td>");
                        out.print("</tr>");
                    }
                %>
            </tbody>
        </table>

    </body>
</html>
