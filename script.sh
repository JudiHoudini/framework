#chemin vers les pages
pathPages="test-framework/web/"

#chemein vers le web.xml
pathXml="test-framework/web/WEB-INF/"

#chemin vers les .java du framework
pathFramework="Framework/src/etu2089/framework/"

export CLASSPATH=$CLASSPATH:/home/judi/Documents/tomcat/lib/servlet-api.jar:temp/jar:temp/war/web/WEB-INF/classes:/home/judi/Documents/jar/gson-2.8.2.jar


mkdir -p temp/jar

javac -d temp/jar $pathFramework"annotation/Url.java" -classpath $CLASSPATH
javac -d temp/jar $pathFramework"annotation/Authentification.java" -classpath $CLASSPATH
javac -d temp/jar $pathFramework"annotation/Singleton.java" -classpath $CLASSPATH
javac -d temp/jar $pathFramework"fileUpload/FileUpload.java" -classpath $CLASSPATH
javac -d temp/jar $pathFramework"Mapping.java" -classpath $CLASSPATH
javac -d temp/jar $pathFramework"view/ModeleView.java"  -classpath $CLASSPATH
javac -d temp/jar $pathFramework"servlet/FrontServlet.java" -classpath $CLASSPATH


cp -R Framework/build/classes temp/jar
cd temp/jar
jar cf ../../Framework.jar *
cd ../../

mkdir -p temp/war/web/META-INF temp/war/web/WEB-INF/classes temp/war/web/WEB-INF/lib

javac -d temp/war/web/WEB-INF/classes test-framework/src/java/etu2089/framework/dataObject/Emp.java -classpath $CLASSPATH
javac -d temp/war/web/WEB-INF/classes test-framework/src/java/etu2089/framework/dataObject/Login.java -classpath $CLASSPATH


cp -R Framework.jar temp/war/web/WEB-INF/lib
cp $pathXml"web.xml" temp/war/web/WEB-INF
cp test-framework/web/*.jsp temp/war/web
cd temp/war/web
jar cf ../../../test-framework.war *

cd ../../../
cp test-framework.war /home/judi/Documents/tomcat/webapps/

rm -R temp




