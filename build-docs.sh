#!/bin/bash

# 📚 Tech Challenge - Documentation Builder
# Script para gerar a documentação estática

set -e

echo "🍽️ Tech Challenge - Restaurant Management API"
echo "=============================================="
echo "🏗️ Gerando documentação estática..."
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

# Criar ambiente virtual se não existir
if [ ! -d "venv" ]; then
    log_info "Criando ambiente virtual..."
    python3 -m venv venv
fi

# Ativar ambiente virtual
source venv/bin/activate

# Instalar dependências
log_info "Instalando dependências..."
pip install --upgrade pip > /dev/null 2>&1
pip install mkdocs>=1.5.0 > /dev/null 2>&1
pip install mkdocs-material>=9.4.0 > /dev/null 2>&1
pip install pymdown-extensions>=10.0.0 > /dev/null 2>&1

# Plugins opcionais
pip install mkdocs-mermaid2-plugin > /dev/null 2>&1 || log_warning "Mermaid plugin não disponível"
pip install mkdocs-minify-plugin > /dev/null 2>&1 || log_warning "Minify plugin não disponível"

# PDF generation (opcional)
if [ "$ENABLE_PDF" = "true" ]; then
    log_info "Instalando suporte a PDF..."
    pip install mkdocs-with-pdf > /dev/null 2>&1 || log_warning "PDF generation não disponível"
fi

log_success "Dependências instaladas"

# Limpar build anterior
if [ -d "site" ]; then
    log_info "Limpando build anterior..."
    rm -rf site
fi

# Verificar estrutura
if [ ! -f "mkdocs.yml" ]; then
    log_error "mkdocs.yml não encontrado!"
    exit 1
fi

if [ ! -d "docs" ]; then
    log_error "Diretório docs não encontrado!"
    exit 1
fi

# Estatísticas pré-build
echo
log_info "📊 Estatísticas da Documentação:"
echo "================================"
echo "📄 Arquivos Markdown: $(find docs -name "*.md" | wc -l)"
echo "📁 Diretórios: $(find docs -type d | wc -l)"
echo "🖼️  Imagens: $(find docs -name "*.png" -o -name "*.jpg" -o -name "*.gif" -o -name "*.svg" | wc -l)"
echo "📦 Tamanho fonte: $(du -sh docs | cut -f1)"
echo

# Build da documentação
log_info "🏗️ Gerando site estático..."

if [ "$ENABLE_PDF" = "true" ]; then
    log_info "Gerando com suporte a PDF..."
    ENABLE_PDF_EXPORT=true mkdocs build --clean --verbose
else
    mkdocs build --clean --verbose
fi

if [ $? -eq 0 ]; then
    log_success "Build concluído com sucesso!"
else
    log_error "Falha no build!"
    exit 1
fi

# Verificar resultado
if [ ! -d "site" ]; then
    log_error "Diretório site não foi criado!"
    exit 1
fi

# Estatísticas pós-build
echo
log_info "📊 Resultado do Build:"
echo "======================"
echo "📄 Arquivos HTML: $(find site -name "*.html" | wc -l)"
echo "📱 Arquivos CSS: $(find site -name "*.css" | wc -l)"
echo "⚡ Arquivos JS: $(find site -name "*.js" | wc -l)"
echo "🖼️  Imagens: $(find site -name "*.png" -o -name "*.jpg" -o -name "*.gif" -o -name "*.svg" | wc -l)"
echo "📦 Tamanho total: $(du -sh site | cut -f1)"

# Verificar PDF se habilitado
if [ "$ENABLE_PDF" = "true" ] && [ -f "site/pdf/documentation.pdf" ]; then
    echo "📄 PDF gerado: $(du -sh site/pdf/documentation.pdf | cut -f1)"
fi

echo

# Verificar páginas principais
log_info "🔍 Verificando páginas principais..."

required_pages=(
    "site/index.html"
    "site/installation/prerequisites/index.html"
    "site/api/overview/index.html"
    "site/architecture/overview/index.html"
    "site/about/author/index.html"
)

all_found=true
for page in "${required_pages[@]}"; do
    if [ -f "$page" ]; then
        log_success "Encontrado: $(basename $(dirname $page))/$(basename $page)"
    else
        log_error "Faltando: $page"
        all_found=false
    fi
done

if [ "$all_found" = true ]; then
    log_success "Todas as páginas principais foram geradas!"
else
    log_error "Algumas páginas estão faltando!"
    exit 1
fi

# Verificar links quebrados (básico)
log_info "🔗 Verificando links básicos..."
broken_links=$(find site -name "*.html" -exec grep -l "href.*#.*404" {} \; 2>/dev/null | wc -l)
if [ $broken_links -eq 0 ]; then
    log_success "Nenhum link quebrado óbvio encontrado"
else
    log_warning "$broken_links possíveis links quebrados encontrados"
fi

# Gerar relatório
echo
log_info "📋 Gerando relatório..."

cat > site/build-report.txt << EOF
Tech Challenge - Restaurant Management API
Build Report
Generated: $(date)

Source Statistics:
- Markdown files: $(find docs -name "*.md" | wc -l)
- Directories: $(find docs -type d | wc -l)
- Images: $(find docs -name "*.png" -o -name "*.jpg" -o -name "*.gif" -o -name "*.svg" | wc -l)
- Source size: $(du -sh docs | cut -f1)

Build Results:
- HTML files: $(find site -name "*.html" | wc -l)
- CSS files: $(find site -name "*.css" | wc -l)
- JS files: $(find site -name "*.js" | wc -l)
- Images: $(find site -name "*.png" -o -name "*.jpg" -o -name "*.gif" -o -name "*.svg" | wc -l)
- Total size: $(du -sh site | cut -f1)

Pages Generated:
$(find site -name "index.html" | sed 's|site/||' | sed 's|/index.html||' | sort)

Build Status: SUCCESS
EOF

log_success "Relatório salvo em site/build-report.txt"

# Instruções finais
echo
echo "🎉 Documentação gerada com sucesso!"
echo "=================================="
echo
echo "📁 Arquivos gerados em: ./site/"
echo
echo "🌐 Para visualizar localmente:"
echo "   cd site && python3 -m http.server 8080"
echo "   Acesse: http://localhost:8080"
echo
echo "🚀 Para deploy:"
echo "   • GitHub Pages: Commit os arquivos em site/ na branch gh-pages"
echo "   • Netlify: Faça upload da pasta site/"
echo "   • Vercel: Configure build command: ./build-docs.sh"
echo
echo "📄 Relatório completo: site/build-report.txt"
echo

# Desativar ambiente virtual
deactivate

log_success "Build concluído!"
