# 构建阶段
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /workspace/app

# 安装必要的构建工具和依赖
RUN apt-get update && \
    apt-get install -y maven

# 复制源代码和配置文件
COPY pom.xml .
COPY src ./src

# Maven构建
RUN mvn clean package -DskipTests

# 运行阶段
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# 从构建阶段复制编译好的jar
COPY --from=build /workspace/app/target/*.jar app.jar

EXPOSE 1122
EXPOSE 9555
ENTRYPOINT ["java", "-jar", "/app/app.jar"]