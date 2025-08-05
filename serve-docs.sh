#!/bin/bash

# 📚 Tech Challenge - Local Documentation Server
# Script para servir a documentação localmente

set -e

echo "🍽️ Tech Challenge - Restaurant Management API"
echo "=============================================="
echo "📚 Iniciando servidor de documentação local..."
echo

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Função para log colorido
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

# Verificar se Python está instalado
if ! command -v python3 &> /dev/null; then
    log_error "Python 3 não encontrado. Por favor, instale Python 3."
    exit 1
fi

log_success "Python 3 encontrado: $(python3 --version)"

# Verificar se pip está instalado
if ! command -v pip3 &> /dev/null; then
    log_error "pip3 não encontrado. Por favor, instale pip3."
    exit 1
fi

log_success "pip3 encontrado: $(pip3 --version)"

# Criar ambiente virtual se não existir
if [ ! -d "venv" ]; then
    log_info "Criando ambiente virtual..."
    python3 -m venv venv
    log_success "Ambiente virtual criado"
fi

# Ativar ambiente virtual
log_info "Ativando ambiente virtual..."
source venv/bin/activate
log_success "Ambiente virtual ativado"

# Instalar dependências
log_info "Instalando dependências do MkDocs..."
pip install --upgrade pip > /dev/null 2>&1

# Instalar dependências básicas
pip install mkdocs>=1.5.0 > /dev/null 2>&1
pip install mkdocs-material>=9.4.0 > /dev/null 2>&1
pip install pymdown-extensions>=10.0.0 > /dev/null 2>&1

# Tentar instalar dependências opcionais
log_info "Instalando plugins opcionais..."
pip install mkdocs-mermaid2-plugin > /dev/null 2>&1 || log_warning "mkdocs-mermaid2-plugin não pôde ser instalado"
pip install mkdocs-minify-plugin > /dev/null 2>&1 || log_warning "mkdocs-minify-plugin não pôde ser instalado"

# Tentar instalar geração de PDF (pode falhar em alguns sistemas)
log_info "Tentando instalar suporte a PDF..."
pip install mkdocs-with-pdf > /dev/null 2>&1 || log_warning "Geração de PDF não disponível (dependências não instaladas)"

log_success "Dependências instaladas com sucesso"

# Verificar se mkdocs.yml existe
if [ ! -f "mkdocs.yml" ]; then
    log_error "Arquivo mkdocs.yml não encontrado!"
    log_info "Certifique-se de estar no diretório raiz do projeto."
    exit 1
fi

log_success "Configuração MkDocs encontrada"

# Verificar estrutura de documentação
if [ ! -d "docs" ]; then
    log_error "Diretório 'docs' não encontrado!"
    exit 1
fi

log_success "Estrutura de documentação encontrada"

# Mostrar estatísticas da documentação
echo
log_info "📊 Estatísticas da Documentação:"
echo "================================"
echo "📄 Arquivos Markdown: $(find docs -name "*.md" | wc -l)"
echo "📁 Diretórios: $(find docs -type d | wc -l)"
echo "🖼️  Imagens: $(find docs -name "*.png" -o -name "*.jpg" -o -name "*.gif" -o -name "*.svg" | wc -l)"
echo "📦 Tamanho total: $(du -sh docs | cut -f1)"
echo

# Função para cleanup
cleanup() {
    log_info "Parando servidor..."
    deactivate 2>/dev/null || true
    exit 0
}

# Capturar Ctrl+C
trap cleanup SIGINT

# Verificar se a porta 8000 está em uso
if lsof -Pi :8000 -sTCP:LISTEN -t >/dev/null 2>&1; then
    log_warning "Porta 8000 já está em uso"
    log_info "Tentando usar porta alternativa..."
    PORT=8001
    while lsof -Pi :$PORT -sTCP:LISTEN -t >/dev/null 2>&1; do
        PORT=$((PORT + 1))
        if [ $PORT -gt 8010 ]; then
            log_error "Não foi possível encontrar uma porta disponível"
            exit 1
        fi
    done
    log_info "Usando porta $PORT"
else
    PORT=8000
fi

# Iniciar servidor
echo
log_success "🚀 Iniciando servidor MkDocs..."
echo
echo "📍 URLs disponíveis:"
echo "   Local:    http://localhost:$PORT"
echo "   Network:  http://$(hostname -I | awk '{print $1}'):$PORT"
echo
echo "📚 Seções disponíveis:"
echo "   🔧 Instalação:     http://localhost:$PORT/installation/prerequisites/"
echo "   📚 API Reference:  http://localhost:$PORT/api/overview/"
echo "   🏗️  Arquitetura:   http://localhost:$PORT/architecture/overview/"
echo "   👤 Sobre:         http://localhost:$PORT/about/author/"
echo
echo "💡 Dicas:"
echo "   • A documentação será recarregada automaticamente quando você editar os arquivos"
echo "   • Use Ctrl+C para parar o servidor"
echo "   • Logs de build aparecerão abaixo"
echo
echo "=============================================="
echo

# Iniciar servidor com live reload
if [ $PORT -eq 8000 ]; then
    mkdocs serve --dev-addr=0.0.0.0:8000
else
    mkdocs serve --dev-addr=0.0.0.0:$PORT
fi
