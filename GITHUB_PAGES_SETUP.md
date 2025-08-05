# 📚 GitHub Pages Setup & Troubleshooting

## 🎯 Configuração Necessária

### 1. 🔧 Habilitar GitHub Pages no Repositório

1. Acesse o repositório no GitHub
2. Vá em **Settings** > **Pages**
3. Em **Source**, selecione **GitHub Actions**
4. Salve as configurações

### 2. 🔑 Verificar Permissões

Certifique-se de que as seguintes permissões estão habilitadas:

- **Settings** > **Actions** > **General**
- **Workflow permissions**: `Read and write permissions`
- **Allow GitHub Actions to create and approve pull requests**: ✅ Habilitado

## 🚀 Como Fazer Deploy da Documentação

### Método 1: Automático (Recomendado)
```bash
# Na branch main, faça qualquer alteração na documentação
echo "# Update" >> docs/index.md
git add docs/index.md
git commit -m "docs: update documentation"
git push origin main
```

### Método 2: Script Automático
```bash
# Use o script fornecido
./force-docs-deploy.sh
```

### Método 3: Manual via GitHub
1. Acesse **Actions** no GitHub
2. Selecione **📚 Documentation Deployment**
3. Clique **Run workflow** > **Run workflow**

## 🔍 Verificar Status do Deploy

### 1. 📊 Acompanhar Execução
- **URL**: `https://github.com/SEU_USUARIO/REPO/actions`
- Procure pelo workflow **📚 Documentation Deployment**
- Status: ✅ Success / ❌ Failure / 🟡 In Progress

### 2. 🌐 Verificar Site Publicado
- **URL**: `https://SEU_USUARIO.github.io/REPO/`
- Aguarde 2-5 minutos após deploy bem-sucedido

## 🐛 Troubleshooting

### ❌ Problema: "Pages build and deployment failed"

**Possíveis Causas:**
1. GitHub Pages não habilitado
2. Permissões insuficientes
3. Erro na configuração do workflow

**Soluções:**
```bash
# 1. Verificar configuração do Pages
# Settings > Pages > Source = "GitHub Actions"

# 2. Verificar permissões
# Settings > Actions > General > Workflow permissions = "Read and write"

# 3. Re-executar workflow
# Actions > Documentation Deployment > Re-run jobs
```

### ❌ Problema: "Site não atualiza após deploy"

**Soluções:**
```bash
# 1. Limpar cache do navegador (Ctrl+F5)

# 2. Aguardar propagação (até 10 minutos)

# 3. Verificar se o deploy foi bem-sucedido
# Actions > último workflow > verificar status
```

### ❌ Problema: "Workflow não executa automaticamente"

**Verificações:**
1. **Triggers corretos**: Mudanças em `docs/`, `mkdocs.yml`, `README.md`
2. **Branch correta**: Push deve ser na `main`
3. **Arquivos modificados**: Pelo menos um arquivo nos paths do trigger

**Solução:**
```bash
# Forçar execução manual
# GitHub > Actions > Documentation Deployment > Run workflow
```

### ❌ Problema: "MkDocs build falha"

**Logs Comuns:**
```
Config file 'mkdocs.yml' does not exist
```

**Solução:**
```bash
# Verificar se mkdocs.yml existe na raiz
ls -la mkdocs.yml

# Verificar sintaxe do YAML
python -c "import yaml; yaml.safe_load(open('mkdocs.yml'))"
```

### ❌ Problema: "Dependências Python falham"

**Solução:**
```bash
# Verificar requirements.txt
cat requirements.txt

# Testar instalação local
pip install -r requirements.txt
mkdocs build
```

## 📋 Checklist de Verificação

### ✅ Configuração Inicial
- [ ] GitHub Pages habilitado (Source = GitHub Actions)
- [ ] Workflow permissions = Read and write
- [ ] Arquivo `mkdocs.yml` existe na raiz
- [ ] Arquivo `requirements.txt` existe
- [ ] Diretório `docs/` existe com conteúdo

### ✅ Estrutura de Arquivos
```
projeto/
├── .github/workflows/docs-deploy.yml  ✅
├── docs/                              ✅
│   ├── index.md                       ✅
│   └── ...                            ✅
├── mkdocs.yml                         ✅
├── requirements.txt                   ✅
└── README.md                          ✅
```

### ✅ Teste Local
```bash
# Instalar dependências
pip install -r requirements.txt

# Testar build
mkdocs build

# Testar servidor local
mkdocs serve
# Acessar: http://localhost:8000
```

## 🔗 URLs Importantes

- **Repositório**: `https://github.com/SEU_USUARIO/REPO`
- **Actions**: `https://github.com/SEU_USUARIO/REPO/actions`
- **Settings**: `https://github.com/SEU_USUARIO/REPO/settings/pages`
- **Documentação**: `https://SEU_USUARIO.github.io/REPO/`

## 📞 Suporte Adicional

Se os problemas persistirem:

1. **Verificar logs detalhados** nos Actions
2. **Testar build local** com `mkdocs build`
3. **Comparar com repositório funcionando**
4. **Verificar status do GitHub Pages**: https://www.githubstatus.com/

---

**📅 Última atualização**: 2024-08-05  
**🔧 Versão**: 1.0.0  
**👤 Autor**: Ítalo Moura
