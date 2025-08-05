#!/bin/bash

# Script para popular o banco usando os endpoints específicos de menu e itens
# Certifique-se de que a aplicação está rodando em http://localhost:8081

BASE_URL="http://localhost:8081/api"

echo "🍽️  Populando banco usando endpoints específicos..."

# Função para extrair ID da resposta JSON
extract_id() {
    echo "$1" | jq -r '.id'
}

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

# Criar categoria Bebida
echo "📋 Criando categoria Bebida..."
BEBIDA_CATEGORY_RESPONSE=$(curl -s -X POST "$BASE_URL/restaurants/$RESTAURANT_ID/menu" \
  -H "Content-Type: application/json" \
  -d '{
    "type": "Bebida"
  }')

BEBIDA_CATEGORY_ID=$(extract_id "$BEBIDA_CATEGORY_RESPONSE")
echo "✅ Categoria Bebida criada com ID: $BEBIDA_CATEGORY_ID"

# Adicionar itens à categoria Bebida
echo "🍶 Adicionando Sake Tradicional..."
curl -s -X POST "$BASE_URL/restaurants/$RESTAURANT_ID/menu/$BEBIDA_CATEGORY_ID/item" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Sake Tradicional",
    "description": "Sake japonês tradicional 300ml",
    "price": 25.00,
    "onlyForLocalConsumption": true,
    "imagePath": "/images/sake.jpg",
    "isActive": true
  }' > /dev/null

echo "🍵 Adicionando Chá Verde..."
curl -s -X POST "$BASE_URL/restaurants/$RESTAURANT_ID/menu/$BEBIDA_CATEGORY_ID/item" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Chá Verde",
    "description": "Chá verde japonês",
    "price": 8.50,
    "onlyForLocalConsumption": false,
    "imagePath": "/images/cha-verde.jpg",
    "isActive": true
  }' > /dev/null

echo ""
echo "📝 Criando Pizzaria..."
PIZZARIA_RESPONSE=$(curl -s -X POST "$BASE_URL/restaurants" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Pizzaria Bella Napoli",
    "address": "Rua Augusta, 456",
    "kitchenType": {
      "name": "Italiana",
      "description": "Cozinha Italiana Autêntica"
    },
    "daysOperation": [
      {
        "day": "TUESDAY",
        "openingHours": "18:00",
        "closingHours": "23:30"
      },
      {
        "day": "WEDNESDAY",
        "openingHours": "18:00",
        "closingHours": "23:30"
      },
      {
        "day": "THURSDAY",
        "openingHours": "18:00",
        "closingHours": "23:30"
      },
      {
        "day": "FRIDAY",
        "openingHours": "18:00",
        "closingHours": "00:30"
      },
      {
        "day": "SATURDAY",
        "openingHours": "18:00",
        "closingHours": "00:30"
      },
      {
        "day": "SUNDAY",
        "openingHours": "18:00",
        "closingHours": "23:00"
      }
    ],
    "ownerId": "550e8400-e29b-41d4-a716-446655440002",
    "isActive": true
  }')

PIZZARIA_ID=$(extract_id "$PIZZARIA_RESPONSE")
echo "✅ Pizzaria criada com ID: $PIZZARIA_ID"

# Criar categoria Pizza
echo "📋 Criando categoria Pizza..."
PIZZA_CATEGORY_RESPONSE=$(curl -s -X POST "$BASE_URL/restaurants/$PIZZARIA_ID/menu" \
  -H "Content-Type: application/json" \
  -d '{
    "type": "Pizza"
  }')

PIZZA_CATEGORY_ID=$(extract_id "$PIZZA_CATEGORY_RESPONSE")
echo "✅ Categoria Pizza criada com ID: $PIZZA_CATEGORY_ID"

# Adicionar pizzas
echo "🍕 Adicionando Pizza Margherita..."
curl -s -X POST "$BASE_URL/restaurants/$PIZZARIA_ID/menu/$PIZZA_CATEGORY_ID/item" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Pizza Margherita",
    "description": "Molho de tomate, mussarela, manjericão fresco",
    "price": 35.90,
    "onlyForLocalConsumption": false,
    "imagePath": "/images/pizza-margherita.jpg",
    "isActive": true
  }' > /dev/null

echo "🍕 Adicionando Pizza Quattro Stagioni..."
curl -s -X POST "$BASE_URL/restaurants/$PIZZARIA_ID/menu/$PIZZA_CATEGORY_ID/item" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Pizza Quattro Stagioni",
    "description": "Quatro sabores: presunto, cogumelos, alcachofra e azeitonas",
    "price": 42.90,
    "onlyForLocalConsumption": false,
    "imagePath": "/images/pizza-quattro.jpg",
    "isActive": true
  }' > /dev/null

# Criar categoria Massa
echo "📋 Criando categoria Massa..."
MASSA_CATEGORY_RESPONSE=$(curl -s -X POST "$BASE_URL/restaurants/$PIZZARIA_ID/menu" \
  -H "Content-Type: application/json" \
  -d '{
    "type": "Massa"
  }')

MASSA_CATEGORY_ID=$(extract_id "$MASSA_CATEGORY_RESPONSE")
echo "✅ Categoria Massa criada com ID: $MASSA_CATEGORY_ID"

# Adicionar massa
echo "🍝 Adicionando Spaghetti Carbonara..."
curl -s -X POST "$BASE_URL/restaurants/$PIZZARIA_ID/menu/$MASSA_CATEGORY_ID/item" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Spaghetti Carbonara",
    "description": "Massa com bacon, ovos, queijo parmesão",
    "price": 28.90,
    "onlyForLocalConsumption": true,
    "imagePath": "/images/carbonara.jpg",
    "isActive": true
  }' > /dev/null

echo ""
echo "📝 Criando Hamburgueria..."
BURGER_RESPONSE=$(curl -s -X POST "$BASE_URL/restaurants" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Burger House",
    "address": "Rua dos Pinheiros, 789",
    "kitchenType": {
      "name": "Americana",
      "description": "Cozinha Americana Contemporânea"
    },
    "daysOperation": [
      {
        "day": "MONDAY",
        "openingHours": "11:00",
        "closingHours": "23:00"
      },
      {
        "day": "TUESDAY",
        "openingHours": "11:00",
        "closingHours": "23:00"
      },
      {
        "day": "WEDNESDAY",
        "openingHours": "11:00",
        "closingHours": "23:00"
      },
      {
        "day": "THURSDAY",
        "openingHours": "11:00",
        "closingHours": "23:00"
      },
      {
        "day": "FRIDAY",
        "openingHours": "11:00",
        "closingHours": "00:00"
      },
      {
        "day": "SATURDAY",
        "openingHours": "11:00",
        "closingHours": "00:00"
      },
      {
        "day": "SUNDAY",
        "openingHours": "11:00",
        "closingHours": "22:00"
      }
    ],
    "ownerId": "550e8400-e29b-41d4-a716-446655440003",
    "isActive": true
  }')

BURGER_ID=$(extract_id "$BURGER_RESPONSE")
echo "✅ Hamburgueria criada com ID: $BURGER_ID"

# Criar categoria Hambúrguer
echo "📋 Criando categoria Hambúrguer..."
HAMBURGUER_CATEGORY_RESPONSE=$(curl -s -X POST "$BASE_URL/restaurants/$BURGER_ID/menu" \
  -H "Content-Type: application/json" \
  -d '{
    "type": "Hambúrguer"
  }')

HAMBURGUER_CATEGORY_ID=$(extract_id "$HAMBURGUER_CATEGORY_RESPONSE")
echo "✅ Categoria Hambúrguer criada com ID: $HAMBURGUER_CATEGORY_ID"

# Adicionar hambúrgueres
echo "🍔 Adicionando Classic Burger..."
curl -s -X POST "$BASE_URL/restaurants/$BURGER_ID/menu/$HAMBURGUER_CATEGORY_ID/item" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Classic Burger",
    "description": "Hambúrguer artesanal 180g, queijo, alface, tomate, cebola",
    "price": 24.90,
    "onlyForLocalConsumption": false,
    "imagePath": "/images/classic-burger.jpg",
    "isActive": true
  }' > /dev/null

echo "🍔 Adicionando Bacon Cheeseburger..."
BACON_BURGER_RESPONSE=$(curl -s -X POST "$BASE_URL/restaurants/$BURGER_ID/menu/$HAMBURGUER_CATEGORY_ID/item" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Bacon Cheeseburger",
    "description": "Hambúrguer 180g, bacon crocante, queijo cheddar duplo",
    "price": 29.90,
    "onlyForLocalConsumption": false,
    "imagePath": "/images/bacon-cheese.jpg",
    "isActive": true
  }')

BACON_BURGER_ITEM_ID=$(extract_id "$BACON_BURGER_RESPONSE")
echo "✅ Bacon Cheeseburger criado com ID: $BACON_BURGER_ITEM_ID"

# Criar categoria Acompanhamento
echo "📋 Criando categoria Acompanhamento..."
ACOMP_CATEGORY_RESPONSE=$(curl -s -X POST "$BASE_URL/restaurants/$BURGER_ID/menu" \
  -H "Content-Type: application/json" \
  -d '{
    "type": "Acompanhamento"
  }')

ACOMP_CATEGORY_ID=$(extract_id "$ACOMP_CATEGORY_RESPONSE")
echo "✅ Categoria Acompanhamento criada com ID: $ACOMP_CATEGORY_ID"

# Adicionar acompanhamentos
echo "🍟 Adicionando Batata Frita..."
curl -s -X POST "$BASE_URL/restaurants/$BURGER_ID/menu/$ACOMP_CATEGORY_ID/item" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Batata Frita",
    "description": "Batatas fritas crocantes com sal especial",
    "price": 12.90,
    "onlyForLocalConsumption": false,
    "imagePath": "/images/batata-frita.jpg",
    "isActive": true
  }' > /dev/null

echo ""
echo "✅ Dados de exemplo criados com sucesso usando endpoints específicos!"
echo ""
echo "🔍 Para testar os endpoints:"
echo "   GET $BASE_URL/restaurants - Lista restaurantes (sem menu)"
echo "   GET $BASE_URL/restaurants/full - Lista restaurantes (com menu)"
echo "   GET $BASE_URL/restaurants/$RESTAURANT_ID - Restaurante específico"
echo "   GET $BASE_URL/restaurants/$RESTAURANT_ID/menu/$SUSHI_CATEGORY_ID - Categoria específica"
echo "   GET $BASE_URL/restaurants/menu/item/$BACON_BURGER_ITEM_ID - Item com contexto"
echo "   GET $BASE_URL/swagger-ui.html - Documentação da API"
echo ""
echo "📊 Resumo criado:"
echo "   🏪 3 Restaurantes"
echo "   📋 7 Categorias de Menu"
echo "   🍽️ 10+ Itens de Menu"
echo ""
