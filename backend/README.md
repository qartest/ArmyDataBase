Работает с postgres на http://localhost:8080
Взаимодействует только http://localhost:5173 это прописано в коде, так как мне было лень прописывать безопасность. 
Перед началом убедитесь, настройте базу данных в файле: src/main/resiurces/application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ - тут указть базу данный
spring.datasource.username= - пользователя
spring.datasource.password= - пароль

Писал сам, spring-boot, postgres 

