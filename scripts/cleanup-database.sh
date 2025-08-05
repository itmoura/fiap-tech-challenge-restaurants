#!/bin/bash

# 🧹 Script de Limpeza do MongoDB
# Corrige problemas de UUID e dados inconsistentes

set -e

echo "🍽️ Tech Challenge - MongoDB Cleanup"
echo "===================================="
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

# Verificar se MongoDB está rodando
log_info "Verificando se MongoDB está disponível..."

if command -v mongosh &> /dev/null; then
    MONGO_CMD="mongosh"
elif command -v mongo &> /dev/null; then
    MONGO_CMD="mongo"
else
    log_error "MongoDB client não encontrado!"
    log_info "Instale mongosh ou mongo para continuar."
    exit 1
fi

log_success "MongoDB client encontrado: $MONGO_CMD"

# Verificar conexão
log_info "Testando conexão com MongoDB..."

if $MONGO_CMD --eval "db.adminCommand('ping')" --quiet > /dev/null 2>&1; then
    log_success "Conexão com MongoDB estabelecida"
else
    log_error "Não foi possível conectar ao MongoDB!"
    log_info "Certifique-se de que o MongoDB está rodando:"
    echo "  • Docker: docker-compose up -d mongodb"
    echo "  • Local: sudo systemctl start mongod"
    exit 1
fi

# Fazer backup antes da limpeza
log_info "Criando backup antes da limpeza..."

BACKUP_DIR="./backups/$(date +%Y%m%d_%H%M%S)"
mkdir -p "$BACKUP_DIR"

if command -v mongodump &> /dev/null; then
    mongodump --db tech_challenge_restaurants --out "$BACKUP_DIR" > /dev/null 2>&1
    log_success "Backup criado em: $BACKUP_DIR"
else
    log_warning "mongodump não encontrado. Continuando sem backup."
fi

# Executar script de limpeza
log_info "Executando script de limpeza..."

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CLEANUP_SCRIPT="$SCRIPT_DIR/cleanup-mongodb.js"

if [ ! -f "$CLEANUP_SCRIPT" ]; then
    log_error "Script de limpeza não encontrado: $CLEANUP_SCRIPT"
    exit 1
fi

echo "📜 Executando limpeza do MongoDB..."
echo "=================================="

if $MONGO_CMD < "$CLEANUP_SCRIPT"; then
    log_success "Script de limpeza executado com sucesso!"
else
    log_error "Erro ao executar script de limpeza!"
    
    if [ -d "$BACKUP_DIR" ] && [ "$(ls -A $BACKUP_DIR)" ]; then
        log_info "Para restaurar o backup:"
        echo "  mongorestore --db tech_challenge_restaurants $BACKUP_DIR/tech_challenge_restaurants"
    fi
    
    exit 1
fi

# Verificação pós-limpeza
log_info "Verificando resultado da limpeza..."

KITCHEN_TYPES_COUNT=$($MONGO_CMD --eval "db.kitchen_types.countDocuments()" tech_challenge_restaurants --quiet)
RESTAURANTS_COUNT=$($MONGO_CMD --eval "db.restaurants.countDocuments()" tech_challenge_restaurants --quiet)

echo
log_info "📊 Estatísticas pós-limpeza:"
echo "=============================="
echo "🍳 Kitchen Types: $KITCHEN_TYPES_COUNT"
echo "🏪 Restaurants: $RESTAURANTS_COUNT"

# Verificar se ainda há problemas de UUID
INVALID_KITCHEN_TYPES=$($MONGO_CMD --eval "
db.kitchen_types.find({
    '_id': {\$not: /^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$/i}
}).count()
" tech_challenge_restaurants --quiet)

if [ "$INVALID_KITCHEN_TYPES" = "0" ]; then
    log_success "Todos os UUIDs estão válidos!"
else
    log_warning "$INVALID_KITCHEN_TYPES kitchen types ainda têm UUIDs inválidos"
fi

# Instruções finais
echo
log_info "🔄 Próximos passos:"
echo "==================="
echo "1. Reinicie a aplicação Spring Boot"
echo "2. Teste os endpoints da API"
echo "3. Verifique se o erro de UUID foi resolvido"
echo

if [ -d "$BACKUP_DIR" ] && [ "$(ls -A $BACKUP_DIR)" ]; then
    log_info "💾 Backup disponível em: $BACKUP_DIR"
    echo "   Para restaurar: mongorestore --db tech_challenge_restaurants $BACKUP_DIR/tech_challenge_restaurants"
fi

echo
log_success "Limpeza concluída!"

# Opcional: Reiniciar aplicação se estiver rodando via Docker
if docker ps | grep -q "fiap-tech-challenge-app"; then
    log_info "Reiniciando aplicação Docker..."
    docker-compose restart app
    log_success "Aplicação reiniciada!"
fi

echo
echo "🎉 Processo de limpeza finalizado!"
echo "   Se o problema persistir, verifique os logs da aplicação."
