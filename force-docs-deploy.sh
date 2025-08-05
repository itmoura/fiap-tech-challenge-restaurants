#!/bin/bash

# Script para forçar o deploy da documentação no GitHub Pages
# Uso: ./force-docs-deploy.sh

echo "🚀 Forçando deploy da documentação..."

# Verificar se estamos na branch main
CURRENT_BRANCH=$(git branch --show-current)
if [ "$CURRENT_BRANCH" != "main" ]; then
    echo "⚠️ Você precisa estar na branch main para fazer deploy da documentação"
    echo "Branch atual: $CURRENT_BRANCH"
    exit 1
fi

# Fazer uma pequena alteração no README para triggerar o workflow
echo "📝 Fazendo alteração mínima para triggerar o workflow..."

# Adicionar timestamp ao final do README
echo "" >> README.md
echo "<!-- Last docs update: $(date) -->" >> README.md

# Commit e push
git add README.md
git commit -m "docs: Force documentation deployment

Adiciona timestamp para triggerar workflow de documentação.
Deploy será executado automaticamente no GitHub Pages."

git push origin main

echo "✅ Alteração enviada para a branch main"
echo "🔄 O workflow de documentação será executado automaticamente"
echo "📚 Documentação estará disponível em:"
echo "   https://$(git config --get remote.origin.url | sed 's/.*github.com[:/]\([^/]*\)\/\([^.]*\).*/\1.github.io\/\2/')/"

echo ""
echo "🔍 Para acompanhar o progresso:"
echo "   1. Acesse: https://github.com/$(git config --get remote.origin.url | sed 's/.*github.com[:/]\([^/]*\)\/\([^.]*\).*/\1\/\2/')/actions"
echo "   2. Procure pelo workflow '📚 Documentation Deployment'"
echo "   3. Aguarde a conclusão (geralmente 2-5 minutos)"

echo ""
echo "🛠️ Se o deploy falhar, execute manualmente:"
echo "   1. Vá para Actions no GitHub"
echo "   2. Selecione '📚 Documentation Deployment'"
echo "   3. Clique em 'Run workflow' > 'Run workflow'"
