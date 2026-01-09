# Multi-stage build para otimizar a imagem final

# Stage 1: Build
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copiar apenas o pom.xml primeiro (para cache das dependências)
COPY pom.xml .

# Baixar dependências (cache será usado se pom.xml não mudar)
RUN mvn dependency:go-offline -B

# Copiar o código fonte (estrutura padrão Maven: src/main/java e src/main/resources)
COPY src ./src

# Build da aplicação (criar JAR)
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Instalar wget para healthcheck
RUN apk add --no-cache wget

# Criar usuário não-root para segurança
RUN addgroup -S spring && adduser -S spring -G spring

# Copiar o JAR do stage de build
COPY --from=build /app/target/*.jar app.jar

# Alterar propriedade do arquivo
RUN chown spring:spring app.jar

# Mudar para usuário não-root
USER spring:spring

# Expor a porta da aplicação
EXPOSE 8080

# Healthcheck (usando qualquer endpoint válido)
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/api/organizations || exit 1

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
