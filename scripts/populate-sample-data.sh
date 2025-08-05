#!/bin/bash

# Script para popular o banco com dados de exemplo
# Certifique-se de que a aplicação está rodando em http://localhost:8081

BASE_URL="http://localhost:8081/api"

echo "🍽️  Populando banco com dados de exemplo..."

# Criar primeiro restaurante
echo "📝 Criando Restaurante Japonês..."
curl -X POST "$BASE_URL/restaurants" \
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
    "isActive": true,
    "menu": [
      {
        "type": "Sushi",
        "items": [
          {
            "name": "Combo Salmão",
            "description": "10 peças de sushi de salmão fresco",
            "price": 45.90,
            "onlyForLocalConsumption": false,
            "imagePath": "/images/combo-salmao.jpg",
            "isActive": true
          },
          {
            "name": "Combo Atum",
            "description": "10 peças de sushi de atum fresco",
            "price": 52.90,
            "onlyForLocalConsumption": false,
            "imagePath": "/images/combo-atum.jpg",
            "isActive": true
          }
        ]
      },
      {
        "type": "Temaki",
        "items": [
          {
            "name": "Temaki Salmão",
            "description": "Temaki de salmão com cream cheese",
            "price": 18.90,
            "onlyForLocalConsumption": false,
            "imagePath": "/images/temaki-salmao.jpg",
            "isActive": true
          }
        ]
      },
      {
        "type": "Bebida",
        "items": [
          {
            "name": "Sake Tradicional",
            "description": "Sake japonês tradicional 300ml",
            "price": 25.00,
            "onlyForLocalConsumption": true,
            "imagePath": "/images/sake.jpg",
            "isActive": true
          },
          {
            "name": "Chá Verde",
            "description": "Chá verde japonês",
            "price": 8.50,
            "onlyForLocalConsumption": false,
            "imagePath": "/images/cha-verde.jpg",
            "isActive": true
          }
        ]
      }
    ]
  }' | jq '.'

echo ""
echo "📝 Criando Pizzaria..."
curl -X POST "$BASE_URL/restaurants" \
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
    "isActive": true,
    "menu": [
      {
        "type": "Pizza",
        "items": [
          {
            "name": "Pizza Margherita",
            "description": "Molho de tomate, mussarela, manjericão fresco",
            "price": 35.90,
            "onlyForLocalConsumption": false,
            "imagePath": "/images/pizza-margherita.jpg",
            "isActive": true
          },
          {
            "name": "Pizza Quattro Stagioni",
            "description": "Quatro sabores em uma pizza: presunto, cogumelos, alcachofra e azeitonas",
            "price": 42.90,
            "onlyForLocalConsumption": false,
            "imagePath": "/images/pizza-quattro.jpg",
            "isActive": true
          }
        ]
      },
      {
        "type": "Massa",
        "items": [
          {
            "name": "Spaghetti Carbonara",
            "description": "Massa com bacon, ovos, queijo parmesão",
            "price": 28.90,
            "onlyForLocalConsumption": true,
            "imagePath": "/images/carbonara.jpg",
            "isActive": true
          }
        ]
      },
      {
        "type": "Bebida",
        "items": [
          {
            "name": "Vinho Chianti",
            "description": "Vinho tinto italiano 750ml",
            "price": 65.00,
            "onlyForLocalConsumption": true,
            "imagePath": "/images/chianti.jpg",
            "isActive": true
          }
        ]
      }
    ]
  }' | jq '.'

echo ""
echo "📝 Criando Hamburgueria..."
curl -X POST "$BASE_URL/restaurants" \
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
    "isActive": true,
    "menu": [
      {
        "type": "Hambúrguer",
        "items": [
          {
            "name": "Classic Burger",
            "description": "Hambúrguer artesanal 180g, queijo, alface, tomate, cebola",
            "price": 24.90,
            "onlyForLocalConsumption": false,
            "imagePath": "/images/classic-burger.jpg",
            "isActive": true
          },
          {
            "name": "Bacon Cheeseburger",
            "description": "Hambúrguer 180g, bacon crocante, queijo cheddar duplo",
            "price": 29.90,
            "onlyForLocalConsumption": false,
            "imagePath": "/images/bacon-cheese.jpg",
            "isActive": true
          }
        ]
      },
      {
        "type": "Acompanhamento",
        "items": [
          {
            "name": "Batata Frita",
            "description": "Batatas fritas crocantes com sal especial",
            "price": 12.90,
            "onlyForLocalConsumption": false,
            "imagePath": "/images/batata-frita.jpg",
            "isActive": true
          },
          {
            "name": "Onion Rings",
            "description": "Anéis de cebola empanados e fritos",
            "price": 15.90,
            "onlyForLocalConsumption": false,
            "imagePath": "/images/onion-rings.jpg",
            "isActive": true
          }
        ]
      },
      {
        "type": "Bebida",
        "items": [
          {
            "name": "Milkshake Chocolate",
            "description": "Milkshake cremoso de chocolate",
            "price": 16.90,
            "onlyForLocalConsumption": true,
            "imagePath": "/images/milkshake-chocolate.jpg",
            "isActive": true
          }
        ]
      }
    ]
  }' | jq '.'

echo ""
echo "✅ Dados de exemplo criados com sucesso!"
echo ""
echo "🔍 Para testar os endpoints:"
echo "   GET $BASE_URL/restaurants - Lista restaurantes (sem menu)"
echo "   GET $BASE_URL/restaurants/full - Lista restaurantes (com menu)"
echo "   GET $BASE_URL/swagger-ui.html - Documentação da API"
echo ""
