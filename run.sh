#!/bin/bash

# Script para executar o projeto Tech Challenge Restaurants

echo "=== Tech Challenge - Sistema de Gestão de Restaurantes ==="
echo ""

# Função para mostrar ajuda
show_help() {
    echo "Uso: ./run.sh [COMANDO]"
    echo ""
    echo "Comandos disponíveis:"
    echo "  start     - Inicia a aplicação com Docker Compose"
    echo "  stop      - Para a aplicação"
    echo "  restart   - Reinicia a aplicação"
    echo "  logs      - Mostra os logs da aplicação"
    echo "  test      - Executa os testes"
    echo "  build     - Faz o build da aplicação"
    echo "  clean     - Limpa containers e volumes"
    echo "  help      - Mostra esta ajuda"
    echo ""
}

# Função para iniciar a aplicação
start_app() {
    echo "🚀 Iniciando a aplicação..."
    docker-compose up -d
    echo ""
    echo "✅ Aplicação iniciada com sucesso!"
    echo ""
    echo "📋 Serviços disponíveis:"
    echo "   • API: http://localhost:8081"
    echo "   • Swagger UI: http://localhost:8081/swagger-ui.html"
    echo "   • Health Check: http://localhost:8081/actuator/health"
    echo "   • MongoDB: localhost:27017"
    echo ""
    echo "📊 Para ver os logs: ./run.sh logs"
}

# Função para parar a aplicação
stop_app() {
    echo "🛑 Parando a aplicação..."
    docker-compose down
    echo "✅ Aplicação parada com sucesso!"
}

# Função para reiniciar a aplicação
restart_app() {
    echo "🔄 Reiniciando a aplicação..."
    docker-compose down
    docker-compose up -d
    echo "✅ Aplicação reiniciada com sucesso!"
}

# Função para mostrar logs
show_logs() {
    echo "📋 Mostrando logs da aplicação..."
    docker-compose logs -f app
}

# Função para executar testes
run_tests() {
    echo "🧪 Executando testes..."
    export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
    ./gradlew test
    echo "✅ Testes executados!"
}

# Função para fazer build
build_app() {
    echo "🔨 Fazendo build da aplicação..."
    export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
    ./gradlew build
    echo "✅ Build concluído!"
}

# Função para limpeza
clean_app() {
    echo "🧹 Limpando containers e volumes..."
    docker-compose down -v
    docker system prune -f
    echo "✅ Limpeza concluída!"
}

# Verificar comando
case "$1" in
    start)
        start_app
        ;;
    stop)
        stop_app
        ;;
    restart)
        restart_app
        ;;
    logs)
        show_logs
        ;;
    test)
        run_tests
        ;;
    build)
        build_app
        ;;
    clean)
        clean_app
        ;;
    help|--help|-h)
        show_help
        ;;
    "")
        echo "❌ Nenhum comando especificado."
        echo ""
        show_help
        ;;
    *)
        echo "❌ Comando '$1' não reconhecido."
        echo ""
        show_help
        exit 1
        ;;
esac
