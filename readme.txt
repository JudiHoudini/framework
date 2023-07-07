Version java:17
Version tomcat:10
Gson librarie : 2.8

contenu web.xml obligatoire:
<init-param>
    <param-name>rootPackage</param-name>
    <param-value>chemin vers votre .java</param-value>
</init-param>
<servlet-mapping>
	<servlet-name>FrontServlet</servlet-name>
	<url-pattern>/</url-pattern>
</servlet-mapping>

contenu pour utiliser les sessions
<init-param>
    <param-name>sessionName</param-name>
    <param-value>nom de session</param-value>
</init-param>
<init-param>
    <param-name>sessionProfil</param-name>
    <param-value>profil du session</param-value>
</init-param> 

liste des annotation utilisable:
@Url(url="lien"):pour chaque fonction
@Singleton(isSingleton = true):pour les classes singleton
@Authentification(reference = 11,value = "Admin"):pour les fonction qui necessite des authentification avec comme reference le niveau d'accreditation minimale et
						value pour le type d'utilisateur
						
Chaque fonction doit retourner des variables de types ModeleView;
si on souhaite retourner des json mais non pas effectuer de redirection on utilise alors l'attribut de ModeleView isJson  qui prends des variables booleens



