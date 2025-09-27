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
    *   RabbitMQ
    *   Maven
    *   OpenApi (Swagger)
    *   Docker
*   **Puerto:** 8081 (ver `health-check.sh`)
*   **Dependencias:**
    *   MySQL (`mysql-account`)

### Client Service

*   **Descripción:** Servicio encargado de la gestión de clientes. Permite registrar, actualizar y consultar información de los clientes.
*   **Tecnologías:**
    *   Java
    *   Spring Boot
    *   MySQL
    *   RabbitMQ
    *   Maven
    *   OpenApi (Swagger)
    *   Docker
*   **Puerto:** 8082 (ver `health-check.sh`)
*   **Dependencias:**
    *   MySQL (`mysql-client`)


## Funcionalidades

### F1:
   
    *  Generecion de CRUD (Crear, leer, actualizar y eliminar) en Entidades: Cliente.
    *  Generacion de CRUD (Crear, leer, actualizar y eliminar) Endidades: Cuenta y Movimiento.

### F2:
   
    *  Registro de movimientos: al registrar un movimiento en la cuenta debe tener en cuenta lo siguiente:
	   Para un movimiento se puede tener valores positivos o negativos.
	*  Al realizar un movimiento se debe actualizar el saldo disponible.
	*  Se debe llevar el registro de las transacciones realizadas.

### F1:
   
   Generecion de CRUD (Crear, leer, actualizar y eliminar) en Entidades: Cliente.
	Generacion de CRUD (Crear, leer, actualizar y eliminar) Endidades: Cuenta y Movimiento.



## Diagramas de la Base de Datos

### Estructura de la Base de Datos


### Modelo Entidad Relación (ERD)

Aunque los proyectos están separados y no tiene una relación directa entre las tablas de los dos proyectos, se muestra a continuación la relación lógica da la tabla clientes del proyecto client-service y las dos tablas cuentas y movimientos del servicio account-service, la cual consiste en que un cliente puede tener muchas cuentas y una cuenta puede tener muchos movimientos.

<img width="839" height="449" alt="image" src="https://github.com/user-attachments/assets/a4d67f32-9327-4c17-ae14-b2fb23cbcf71" />




## Requisitos

*   Docker
*   Docker Compose
*   RabbitMQ
*   MySQL

## Despliegue

El despliegue se realiza mediante Docker Compose. Siga estos pasos:

1.  Clonar el repositorio:

    ```bash
    git clone https://github.com/Alegria2016/BANK-MICROSERVICES
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

Una vez realizado el despliegue verifica documentacion técnica de OpenApi de los servicios en: http://localhost:8081/api/swagger-ui/index.html y http://localhost:8082/api/swagger-ui/index.html o ejecútelo el comando a continuación para confirmar que todos los servicios están en funcionamiento:

```bash
./health-check.sh
