#!/bin/bash

# Script para testar o salvamento no MongoDB

echo "🧪 Testando salvamento no MongoDB"
echo "=================================="

BASE_URL="http://localhost:8081"

echo "1. Testando endpoint normal..."
curl -X POST "$BASE_URL/api/kitchen-types" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Teste Normal",
    "description": "Descrição do teste normal"
  }' | jq .

echo -e "\n2. Testando endpoint de debug direto..."
curl -X POST "$BASE_URL/api/debug/kitchen-type-direct" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Teste Debug Direto",
    "description": "Descrição do teste debug direto"
  }' | jq .

echo -e "\n3. Testando endpoint de debug com template..."
curl -X POST "$BASE_URL/api/debug/kitchen-type-template" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Teste Debug Template",
    "description": "Descrição do teste debug template"
  }' | jq .

echo -e "\n4. Listando todos os kitchen types (raw)..."
curl -X GET "$BASE_URL/api/debug/kitchen-types-raw" | jq .

echo -e "\n5. Buscando por nome específico..."
curl -X GET "$BASE_URL/api/debug/kitchen-types-by-name" | jq .

echo -e "\n6. Listando via endpoint normal..."
curl -X GET "$BASE_URL/api/kitchen-types" | jq .

echo -e "\n✅ Testes concluídos!"
