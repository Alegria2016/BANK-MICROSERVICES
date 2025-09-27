#!/bin/bash

echo "🧪 Probando despliegue..."

# Crear un cliente de prueba
echo "1. Creando cliente de prueba..."
response=$(curl -s -w "%{http_code}" -X POST http://localhost:8081/api/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Jose Lema",
    "genero": "Masculino",
    "edad": 35,
    "identificacion": "1234567890",
    "direccion": "Otavalo sn y principal",
    "telefono": "098254785",
    "password": "password123"
  }')

http_code=${response: -3}
response_body=${response%???}

if [ "$http_code" -eq 201 ]; then
    echo "✅ Cliente creado exitosamente"
    echo "Response: $response_body"
else
    echo "❌ Error creando cliente: HTTP $http_code"
    echo "Response: $response_body"
    exit 1
fi

# Verificar clientes
echo "2. Listando clientes..."
curl -s http://localhost:8081/api/clientes | jq .

# Verificar cuentas
echo "3. Listando cuentas..."
curl -s http://localhost:8082/api/cuentas | jq .

# Verificar logs de eventos
echo "4. Verificando eventos..."
docker-compose logs client-service | grep -i "evento" | tail -5
docker-compose logs account-service | grep -i "evento" | tail -5

echo "✅ Prueba completada exitosamente!"