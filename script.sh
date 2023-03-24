jar cf Framework/dist/Framework.jar Framework/build/classes/etu2089
cp Framework/dist/Framework.jar test-framework/build/web/WEB-INF/lib
jar cf test-framework/build/test-framework.war test-framework/build/web
cp test-framework/build/test-framework.war /home/judi/Documents/apache-tomcat/webapps


