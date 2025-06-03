FROM openjdk:21
COPY "./target/Biblioteca-1.jar" "app.jar"
EXPOSE 8101
ENTRYPOINT [ "java", "-jar", "app.jar" ]