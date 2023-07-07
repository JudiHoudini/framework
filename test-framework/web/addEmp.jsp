<%-- 
    Document   : addEmp
    Created on : 25 avr. 2023, 07:13:27
    Author     : judi
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <form action="emp-savy" method="POST">
            <input type="text" id="id" name="nom">
            <input type="text" id="id" name="prenom">
            <input type="number" id="id" name="age">
            <input type="submit" value="enregistrer">
        </form>
        <a href="logout"><input type="button" value="Button"></a>
    </body>
</html>
