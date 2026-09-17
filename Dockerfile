FROM eclipse-temurin:21-jdk

# Copia todo o diretório do projeto para dentro de /app/src no container
COPY . /app/src

# Entra no diretório /app/src do container
WORKDIR /app/src

RUN apt-get update && \
    apt-get install -y wget gnupg && \
    wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | gpg --dearmor -o /usr/share/keyrings/google-chrome.gpg && \
    echo "deb [arch=amd64 signed-by=/usr/share/keyrings/google-chrome.gpg] http://dl.google.com/linux/chrome/deb/ stable main" > /etc/apt/sources.list.d/google-chrome.list && \
    apt-get update && \
    apt-get install -y google-chrome-stable && \
    rm -rf /var/lib/apt/lists/*

# Gerando o .jar da aplicação
RUN ./gradlew build

COPY build/libs/monitor-vagas-0.0.1-SNAPSHOT.jar /app/monitor-vagas.jar

WORKDIR /app
EXPOSE 8089

# Comandos finais que serão executados no CMD
CMD ["java", "-jar", "/app/monitor-vagas.jar"]