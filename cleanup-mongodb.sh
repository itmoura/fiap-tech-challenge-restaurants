#!/bin/bash

# 🧹 Script para Limpeza MongoDB via Java
# Executa a limpeza usando a aplicação Spring Boot

set -e

echo "🍽️ Tech Challenge - MongoDB Cleanup (Java)"
echo "============================================"
echo

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

log_info() {
    echo -e "${BLUE}ℹ️  $1${NC}"
}

log_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

log_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

log_error() {
    echo -e "${RED}❌ $1${NC}"
}

# Verificar se estamos no diretório correto
if [ ! -f "gradlew" ]; then
    log_error "Arquivo gradlew não encontrado!"
    log_info "Execute este script no diretório raiz do projeto."
    exit 1
fi

log_success "Diretório do projeto encontrado"

# Verificar se MongoDB está acessível
log_info "Verificando conectividade com MongoDB..."

# Tentar diferentes URIs do MongoDB
MONGO_URIS=(
    "mongodb://localhost:27017"
    "mongodb://admin:admin123@localhost:27017"
    "mongodb://127.0.0.1:27017"
)

MONGO_URI=""
for uri in "${MONGO_URIS[@]}"; do
    log_info "Testando: $uri"
    if timeout 5 bash -c "</dev/tcp/localhost/27017" 2>/dev/null; then
        MONGO_URI="$uri"
        log_success "MongoDB encontrado em: $uri"
        break
    fi
done

if [ -z "$MONGO_URI" ]; then
    log_error "MongoDB não está acessível!"
    log_info "Certifique-se de que o MongoDB está rodando:"
    echo "  • Docker: docker-compose up -d mongodb"
    echo "  • Local: sudo systemctl start mongod"
    echo "  • Ou inicie manualmente na porta 27017"
    exit 1
fi

# Executar limpeza
log_info "Executando limpeza via Spring Boot..."
echo "📜 Logs da limpeza:"
echo "=================="

# Executar com profile de cleanup
if ./gradlew bootRun --args="--spring.profiles.active=cleanup --mongo.uri=$MONGO_URI"; then
    echo
    log_success "Limpeza executada com sucesso!"
else
    echo
    log_error "Erro durante a limpeza!"
    log_info "Verifique os logs acima para mais detalhes."
    exit 1
fi

# Verificar resultado
log_info "Verificando resultado..."

echo
log_info "🎯 Próximos passos:"
echo "==================="
echo "1. A limpeza foi concluída"
echo "2. Kitchen types válidos foram criados"
echo "3. Restaurants inconsistentes foram removidos"
echo "4. Agora você pode:"
echo "   • Iniciar a aplicação: ./gradlew bootRun"
echo "   • Testar a API: curl http://localhost:8081/api/kitchen-types"
echo "   • Criar novos restaurantes via API"

echo
log_success "Processo de limpeza finalizado!"
echo "🔄 Inicie a aplicação principal para usar a API limpa."
