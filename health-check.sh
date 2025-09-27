#!/bin/bash

echo "🔍 Verificando salud de los servicios..."

# Función para verificar servicio
check_service() {
    local name=$1
    local url=$2
    if curl -s --connect-timeout 10 "$url" > /dev/null; then
        echo "✅ $name: HEALTHY"
        return 0
    else
        echo "❌ $name: UNHEALTHY"
        return 1
    fi
}

# Verificar servicios
check_service "Client Service" "http://localhost:8081/api/actuator/health"
check_service "Account Service" "http://localhost:8082/api/actuator/health"

# Verificar RabbitMQ
if docker exec rabbitmq rabbitmqctl status > /dev/null 2>&1; then
    echo "✅ RabbitMQ: HEALTHY"
else
    echo "❌ RabbitMQ: UNHEALTHY"
fi

# Verificar MySQL Client
if docker exec mysql-client mysql -uroot -ppassword -e "SELECT 1;" > /dev/null 2>&1; then
    echo "✅ MySQL Client: HEALTHY"
else
    echo "❌ MySQL Client: UNHEALTHY"
fi

# Verificar MySQL Account
if docker exec mysql-account mysql -uroot -ppassword -e "SELECT 1;" > /dev/null 2>&1; then
    echo "✅ MySQL Account: HEALTHY"
else
    echo "❌ MySQL Account: UNHEALTHY"
fi

echo "📊 Resumen de salud completado"