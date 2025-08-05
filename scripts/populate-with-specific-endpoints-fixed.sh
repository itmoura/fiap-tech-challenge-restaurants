#!/bin/bash

# Script para popular o banco usando os endpoints específicos de menu e itens
# Certifique-se de que a aplicação está rodando em http://localhost:8081
# Este script não depende do jq

BASE_URL="http://localhost:8081/api"

echo "🍽️  Populando banco usando endpoints específicos..."

# Função para extrair ID da resposta JSON sem jq
extract_id() {
    echo "$1" | grep -o '"id":"[^"]*"' | head -1 | cut -d'"' -f4
}

# Primeiro, vamos criar alguns tipos de cozinha
echo "🍳 Criando tipos de cozinha..."

echo "📝 Criando tipo de cozinha: Japonesa..."
curl -s -X POST "$BASE_URL/kitchen_types" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Japonesa",
    "description": "Cozinha japonesa tradicional com sushi, sashimi e pratos quentes"
  }' > /dev/null

echo "📝 Criando tipo de cozinha: Italiana..."
curl -s -X POST "$BASE_URL/kitchen_types" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Italiana",
    "description": "Cozinha italiana autêntica com massas, pizzas e risotos"
  }' > /dev/null

echo "📝 Criando tipo de cozinha: Americana..."
curl -s -X POST "$BASE_URL/kitchen_types" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Americana",
    "description": "Cozinha americana contemporânea com hambúrgueres e grelhados"
  }' > /dev/null

echo "📝 Criando tipo de cozinha: Brasileira..."
curl -s -X POST "$BASE_URL/kitchen_types" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Brasileira",
    "description": "Cozinha brasileira tradicional com pratos regionais"
  }' > /dev/null

# Criar primeiro restaurante
echo "📝 Criando Restaurante Japonês..."
RESTAURANT_RESPONSE=$(curl -s -X POST "$BASE_URL/restaurants" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Sushi Zen",
    "address": "Rua da Liberdade, 123",
    "kitchenType": {
      "name": "Japonesa",
      "description": "Cozinha Japonesa Tradicional"
    },
    "daysOperation": [
      {
        "day": "MONDAY",
        "openingHours": "18:00",
        "closingHours": "23:00"
      },
      {
        "day": "TUESDAY",
        "openingHours": "18:00",
        "closingHours": "23:00"
      },
      {
        "day": "WEDNESDAY",
        "openingHours": "18:00",
        "closingHours": "23:00"
      },
      {
        "day": "THURSDAY",
        "openingHours": "18:00",
        "closingHours": "23:00"
      },
      {
        "day": "FRIDAY",
        "openingHours": "18:00",
        "closingHours": "00:00"
      },
      {
        "day": "SATURDAY",
        "openingHours": "18:00",
        "closingHours": "00:00"
      }
    ],
    "ownerId": "550e8400-e29b-41d4-a716-446655440001",
    "isActive": true
  }')

RESTAURANT_ID=$(extract_id "$RESTAURANT_RESPONSE")
echo "✅ Restaurante criado com ID: $RESTAURANT_ID"

# Criar categoria Sushi
echo "📋 Criando categoria Sushi..."
SUSHI_CATEGORY_RESPONSE=$(curl -s -X POST "$BASE_URL/restaurants/$RESTAURANT_ID/menu" \
  -H "Content-Type: application/json" \
  -d '{
    "type": "Sushi"
  }')

SUSHI_CATEGORY_ID=$(extract_id "$SUSHI_CATEGORY_RESPONSE")
echo "✅ Categoria Sushi criada com ID: $SUSHI_CATEGORY_ID"

# Adicionar itens à categoria Sushi
echo "🍣 Adicionando Combo Salmão..."
curl -s -X POST "$BASE_URL/restaurants/$RESTAURANT_ID/menu/$SUSHI_CATEGORY_ID/item" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Combo Salmão",
    "description": "10 peças de sushi de salmão fresco",
    "price": 45.90,
    "onlyForLocalConsumption": false,
    "imagePath": "/images/combo-salmao.jpg",
    "isActive": true
  }' > /dev/null

echo "🍣 Adicionando Combo Atum..."
curl -s -X POST "$BASE_URL/restaurants/$RESTAURANT_ID/menu/$SUSHI_CATEGORY_ID/item" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Combo Atum",
    "description": "10 peças de sushi de atum fresco",
    "price": 52.90,
    "onlyForLocalConsumption": false,
    "imagePath": "/images/combo-atum.jpg",
    "isActive": true
  }' > /dev/null

# Criar categoria Temaki
echo "📋 Criando categoria Temaki..."
TEMAKI_CATEGORY_RESPONSE=$(curl -s -X POST "$BASE_URL/restaurants/$RESTAURANT_ID/menu" \
  -H "Content-Type: application/json" \
  -d '{
    "type": "Temaki"
  }')

TEMAKI_CATEGORY_ID=$(extract_id "$TEMAKI_CATEGORY_RESPONSE")
echo "✅ Categoria Temaki criada com ID: $TEMAKI_CATEGORY_ID"

# Adicionar item à categoria Temaki
echo "🍣 Adicionando Temaki Salmão..."
curl -s -X POST "$BASE_URL/restaurants/$RESTAURANT_ID/menu/$TEMAKI_CATEGORY_ID/item" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Temaki Salmão",
    "description": "Temaki de salmão com cream cheese",
    "price": 18.90,
    "onlyForLocalConsumption": false,
    "imagePath": "/images/temaki-salmao.jpg",
    "isActive": true
  }' > /dev/null

echo ""
echo "✅ Dados de exemplo criados com sucesso usando endpoints específicos!"
echo ""
echo "🔍 Para testar os endpoints:"
echo "   GET $BASE_URL/kitchen_types - Lista tipos de cozinha"
echo "   GET $BASE_URL/restaurants - Lista restaurantes (sem menu)"
echo "   GET $BASE_URL/restaurants/full - Lista restaurantes (com menu)"
echo "   GET $BASE_URL/restaurants/$RESTAURANT_ID - Restaurante específico"
echo "   GET $BASE_URL/restaurants/$RESTAURANT_ID/menu/$SUSHI_CATEGORY_ID - Categoria específica"
echo "   GET $BASE_URL/swagger-ui.html - Documentação da API"
echo ""
echo "📊 Resumo criado:"
echo "   🍳 4 Tipos de Cozinha"
echo "   🏪 1 Restaurante"
echo "   📋 2 Categorias de Menu"
echo "   🍽️ 3 Itens de Menu"
echo ""
