#!/bin/bash

echo "🐳 Docker Version: $(docker --version)"

echo ""
echo "🐳 Paso 1: Limpiando recursos anteriores..."
docker compose down
docker system prune -f
docker volume prune -f

echo ""
echo "🐳 Paso 2: Construyendo imágenes secuencialmente..."
docker compose build mysql-account
docker compose build mysql-client
docker compose build account-service
docker compose build client-service

echo ""
echo "🐳 Paso 3: Iniciando base de datos..."
docker compose up -d mysql-account mysql-client

echo "🐳 Esperando a que las bases de datos estén listas..."
sleep 45

echo ""
echo "🐳 Verificando estado de las bases de datos..."
docker compose ps

echo ""
echo "🐳 Mostrando logs de mysql-account..."
docker compose logs mysql-account --tail=20

echo ""
echo "🐳 Mostrando logs de mysql-client..."
docker compose logs mysql-client --tail=20

echo ""
echo "🐳 Paso 4: Iniciando servicios de aplicación..."
docker compose up -d account-service client-service

echo "🐳 Esperando a que los servicios estén listos..."
sleep 30

echo ""
echo "🐳 Paso 5: Verificando estado final..."
docker compose ps

echo ""
echo "📊 Logs de los servicios:"
docker compose logs --tail=10

echo ""
echo "✅ Despliegue completado. Verificando salud de los servicios..."
curl -s http://localhost:8081/api/swagger-ui/index.html || echo "❌ account-service no responde"
curl -s http://localhost:8082/api/swagger-ui/index.html || echo "❌ client-service no responde"