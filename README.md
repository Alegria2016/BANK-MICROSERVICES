# Microservicios Bancarios

Este proyecto consiste en una arquitectura de microservicios para un sistema bancario, que incluye los servicios de `account-service` y `client-service`, junto con sus respectivas bases de datos MySQL.

## Descripción General

El objetivo de este proyecto es demostrar una implementación de microservicios utilizando Docker y Compose para facilitar el despliegue y la gestión de los servicios.

## Servicios

### Account Service

*   **Descripción:** Servicio encargado de la gestión de cuentas bancarias. Permite crear, actualizar, eliminar y consultar cuentas.
*   **Tecnologías:**
    *   Java
    *   Spring Boot
    *   MySQL
    *   Maven
    *   Docker
*   **Puerto:** 8082 (ver `health-check.sh`)
*   **Dependencias:**
    *   MySQL (`mysql-account`)

### Client Service

*   **Descripción:** Servicio encargado de la gestión de clientes. Permite registrar, actualizar y consultar información de los clientes.
*   **Tecnologías:**
    *   Java
    *   Spring Boot
    *   MySQL
    *   Maven
    *   Docker
*   **Puerto:** 8081 (ver `health-check.sh`)
*   **Dependencias:**
    *   MySQL (`mysql-client`)

## Requisitos

*   Docker
*   Docker Compose

## Despliegue

El despliegue se realiza mediante Docker Compose. Siga estos pasos:

1.  Clonar el repositorio:

    ```bash
    git clone <URL_DEL_REPOSITORIO>
    cd bank-microservices
    ```

2.  Ejecutar el script de despliegue:

    ```bash
    ./deploy.sh
    ```

    El script `deploy.sh` realiza las siguientes acciones:

    *   Detiene y elimina contenedores y volúmenes existentes.
    *   Construye las imágenes de Docker para `mysql-account`, `mysql-client`, `account-service` y `client-service`.
    *   Inicia los contenedores de las bases de datos MySQL.
    *   Espera a que las bases de datos estén listas.
    *   Inicia los servicios `account-service` y `client-service`.
    *   Verifica el estado final de los servicios.

## Configuración

La configuración de las bases de datos MySQL se encuentra en el archivo [docker-compose.yml](docker-compose.yml). Las variables de entorno para la conexión a las bases de datos se definen en este mismo archivo.

*   **mysql-account:**
    *   Puerto: 3307
    *   Base de datos: account\_db
    *   Usuario: app\_user
    *   Contraseña: userpassword
*   **mysql-client:**
    *   Puerto: 3308
    *   Base de datos: client\_db
    *   Usuario: app\_user
    *   Contraseña: userpassword

## Verificación de Salud

El script [health-check.sh](health-check.sh) verifica la salud de los servicios.  Ejecútelo para confirmar que todos los servicios están en funcionamiento:

```bash
./health-check.sh
