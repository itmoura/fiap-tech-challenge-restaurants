# 🔧 Pré-requisitos

Antes de executar o projeto, certifique-se de ter os seguintes componentes instalados em seu ambiente de desenvolvimento.

## ☕ Java Development Kit (JDK)

### Versão Requerida
- **Java 21** ou superior

### Instalação

=== "Windows"
    
    **Opção 1: Oracle JDK**
    ```bash
    # Baixe do site oficial
    https://www.oracle.com/java/technologies/downloads/#java21
    ```
    
    **Opção 2: OpenJDK via Chocolatey**
    ```bash
    choco install openjdk21
    ```

=== "macOS"
    
    **Opção 1: Homebrew**
    ```bash
    brew install openjdk@21
    ```
    
    **Opção 2: SDKMAN!**
    ```bash
    curl -s "https://get.sdkman.io" | bash
    sdk install java 21.0.1-open
    ```

=== "Linux (Ubuntu/Debian)"
    
    ```bash
    # Atualizar repositórios
    sudo apt update
    
    # Instalar OpenJDK 21
    sudo apt install openjdk-21-jdk
    
    # Verificar instalação
    java -version
    javac -version
    ```

=== "Linux (CentOS/RHEL)"
    
    ```bash
    # Instalar OpenJDK 21
    sudo yum install java-21-openjdk-devel
    
    # Ou para versões mais recentes
    sudo dnf install java-21-openjdk-devel
    ```

### Verificação da Instalação

```bash
java -version
# Saída esperada:
# openjdk version "21.0.1" 2023-10-17
# OpenJDK Runtime Environment (build 21.0.1+12-29)
# OpenJDK 64-Bit Server VM (build 21.0.1+12-29, mixed mode, sharing)

javac -version
# Saída esperada:
# javac 21.0.1
```

## 🐳 Docker & Docker Compose

### Versões Requeridas
- **Docker**: 20.10+ 
- **Docker Compose**: 2.0+

### Instalação

=== "Windows"
    
    **Docker Desktop**
    ```bash
    # Baixe do site oficial
    https://www.docker.com/products/docker-desktop/
    ```
    
    !!! tip "Dica"
        Docker Desktop para Windows inclui Docker Compose automaticamente.

=== "macOS"
    
    **Docker Desktop**
    ```bash
    # Homebrew
    brew install --cask docker
    
    # Ou baixe do site oficial
    https://www.docker.com/products/docker-desktop/
    ```

=== "Linux (Ubuntu/Debian)"
    
    ```bash
    # Remover versões antigas
    sudo apt-get remove docker docker-engine docker.io containerd runc
    
    # Instalar dependências
    sudo apt-get update
    sudo apt-get install ca-certificates curl gnupg lsb-release
    
    # Adicionar chave GPG oficial do Docker
    sudo mkdir -m 0755 -p /etc/apt/keyrings
    curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
    
    # Configurar repositório
    echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
    
    # Instalar Docker Engine
    sudo apt-get update
    sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
    
    # Adicionar usuário ao grupo docker
    sudo usermod -aG docker $USER
    ```

### Verificação da Instalação

```bash
docker --version
# Saída esperada: Docker version 24.0.x, build xxxxx

docker-compose --version
# Saída esperada: Docker Compose version v2.x.x
```

## 📦 Git

### Instalação

=== "Windows"
    
    ```bash
    # Chocolatey
    choco install git
    
    # Ou baixe do site oficial
    https://git-scm.com/download/win
    ```

=== "macOS"
    
    ```bash
    # Homebrew
    brew install git
    
    # Ou usar Xcode Command Line Tools
    xcode-select --install
    ```

=== "Linux"
    
    ```bash
    # Ubuntu/Debian
    sudo apt install git
    
    # CentOS/RHEL
    sudo yum install git
    # ou
    sudo dnf install git
    ```

### Verificação da Instalação

```bash
git --version
# Saída esperada: git version 2.x.x
```

## 🗄️ MongoDB (Opcional)

!!! info "Informação"
    O MongoDB é **opcional** para desenvolvimento local, pois o projeto inclui configuração Docker que provisiona automaticamente uma instância MongoDB.

### Para instalação local (opcional):

=== "Windows"
    
    ```bash
    # Chocolatey
    choco install mongodb
    
    # Ou baixe do site oficial
    https://www.mongodb.com/try/download/community
    ```

=== "macOS"
    
    ```bash
    # Homebrew
    brew tap mongodb/brew
    brew install mongodb-community
    ```

=== "Linux (Ubuntu/Debian)"
    
    ```bash
    # Importar chave pública
    wget -qO - https://www.mongodb.org/static/pgp/server-7.0.asc | sudo apt-key add -
    
    # Criar arquivo de lista
    echo "deb [ arch=amd64,arm64 ] https://repo.mongodb.org/apt/ubuntu jammy/mongodb-org/7.0 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-7.0.list
    
    # Atualizar e instalar
    sudo apt-get update
    sudo apt-get install -y mongodb-org
    ```

## 🔧 Ferramentas de Desenvolvimento (Opcionais)

### IDE Recomendadas

- **IntelliJ IDEA** (Community ou Ultimate)
- **Visual Studio Code** com extensões Java
- **Eclipse IDE**

### Extensões Úteis para VS Code

```bash
# Instalar extensões via linha de comando
code --install-extension vscjava.vscode-java-pack
code --install-extension redhat.java
code --install-extension vscjava.vscode-spring-boot-dashboard
code --install-extension ms-vscode.vscode-json
```

## ✅ Verificação Final

Execute o seguinte script para verificar se todos os pré-requisitos estão instalados:

```bash
#!/bin/bash

echo "🔍 Verificando pré-requisitos..."
echo

# Java
if command -v java &> /dev/null; then
    echo "✅ Java: $(java -version 2>&1 | head -n 1)"
else
    echo "❌ Java não encontrado"
fi

# Docker
if command -v docker &> /dev/null; then
    echo "✅ Docker: $(docker --version)"
else
    echo "❌ Docker não encontrado"
fi

# Docker Compose
if command -v docker-compose &> /dev/null; then
    echo "✅ Docker Compose: $(docker-compose --version)"
else
    echo "❌ Docker Compose não encontrado"
fi

# Git
if command -v git &> /dev/null; then
    echo "✅ Git: $(git --version)"
else
    echo "❌ Git não encontrado"
fi

echo
echo "🎉 Verificação concluída!"
```

## 🚀 Próximos Passos

Após instalar todos os pré-requisitos, você pode prosseguir para:

- [Configuração Local](local-setup.md) - Para desenvolvimento local
- [Docker](docker.md) - Para execução com containers

## ❓ Problemas Comuns

### Java não encontrado após instalação

```bash
# Verificar JAVA_HOME
echo $JAVA_HOME

# Configurar JAVA_HOME (Linux/macOS)
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

# Adicionar ao ~/.bashrc ou ~/.zshrc para persistir
echo 'export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64' >> ~/.bashrc
echo 'export PATH=$JAVA_HOME/bin:$PATH' >> ~/.bashrc
```

### Docker permission denied (Linux)

```bash
# Adicionar usuário ao grupo docker
sudo usermod -aG docker $USER

# Reiniciar sessão ou executar
newgrp docker
```

### Porta 8081 já em uso

```bash
# Verificar processo usando a porta
sudo lsof -i :8081

# Matar processo se necessário
sudo kill -9 <PID>

# Ou alterar porta no application.yml
server:
  port: 8082
```
