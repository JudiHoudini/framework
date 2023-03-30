cp -R Framework/build/classes jar
cd jar/classes
jar cf ../../Framework.jar *
cd ../../
cp Framework.jar test-framework/build/web/WEB-INF/lib
cp -R test-framework/build/web/ war
cd war/web
jar cf ../../test-framework.war *
cp ../../test-framework.war /home/judi/Documents/apache-tomcat/webapps


