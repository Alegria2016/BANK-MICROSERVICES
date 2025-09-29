# Microservicios Bancarios

Este proyecto consiste en una arquitectura de microservicios para un sistema bancario, que incluye los servicios de `account-service` y `client-service`, junto con sus respectivas bases de datos MySQL.

## Descripción General

El objetivo de este proyecto es demostrar una implementación de microservicios utilizando Docker y Compose para facilitar el despliegue y la gestión de los servicios.

## Diagramas de la Base de Datos

### Estructura de la Base de Datos


### Modelo Entidad Relación (ERD)

Aunque los proyectos están separados y no tiene una relación directa entre las tablas de los dos proyectos, se muestra a continuación la relación lógica de la tabla clientes del proyecto client-service y las dos tablas cuentas y movimientos del servicio account-service, la cual consiste en que un cliente puede tener muchas cuentas y una cuenta puede tener muchos movimientos.

<img width="839" height="449" alt="image" src="https://github.com/user-attachments/assets/a4d67f32-9327-4c17-ae14-b2fb23cbcf71" />

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
   
    *  Genereción de CRUD (Crear, leer, actualizar y eliminar) en Entidades: Cliente.
    *  Generación de CRUD (Crear, leer, actualizar y eliminar) Entidades: Cuenta y Movimiento.

### F2:
   
    *  Registro de movimientos: al registrar un movimiento en la cuenta debe tener en cuenta lo siguiente:
	   Para un movimiento se puede tener valores positivos o negativos.
	*  Al realizar un movimiento se debe actualizar el saldo disponible.
	*  Se debe llevar el registro de las transacciones realizadas.

### F3:
   
    *  Registro de movimientos: Al realizar un movimiento el cual no cuente con saldo, debe alertar mediante el siguiente 
	   mensaje "Saldo no disponible"
	*  Defina según su expertise, la mejor manera de capturar y mostrar el error.

### F4:

	*  Reportes: Generar un reporte de "Estado de cuenta" especificando un rango de fechas y cliente.
	   Este reporte debe contener:
	   Cuenta asociada con su respectivos saldos.
	   Detalle de movimientos de las cuentas.
	   el endpoint que se debe utilizar para esto debe ser el siguiente:
	   /reportes?fecha=rango fechas & cliente
	   El servicio del reporte debe retornar la informacion en formato JSON.
	   Defina, segun su expertise, la mejor manera de solictar retornar esta informacion.

### F5:

	*  Pruebas unitarias: Implementar 1 prueba unitaria para la entidad de dominio Cliente.

### F6:

	*  Pruebas de integración: Implementar 1 prueba de integración.

### F7:

	*  Despliegue la solución en contenedores Docker.


### Formas para probras las funcionalidades.
Se agrega colección de Postmas (bank-microservices.postman_collection.json) de cada una de los servicios para validar las funcionalidades. Adicional a esta opción también se puede hacer en la página de inicio (client-service: http://localhost:8082/api/swagger-ui/index.html y account-service:http://localhost:8081/api/swagger-ui/index.html) una vez carga la aplicación directamente con la documentación de OpenApi:

<img width="1340" height="672" alt="image" src="https://github.com/user-attachments/assets/a52f551a-3bc8-47bc-b0e0-d59c4c306047" />





## Requisitos

*   Docker
*   Docker Compose
*   RabbitMQ
*   MySQL

## Despliegue

El despliegue se realiza mediante Docker Compose. Siga estos pasos:

Asegurarse de tener Docker corriendo en la maquina donde se realizará el despliegue.

1.  Clonar el repositorio:

    ```bash
    git clone https://github.com/Alegria2016/BANK-MICROSERVICES
    cd bank-microservices
    ```

2.  Ejecutar el script de despliegue:
    Abrir consola de Powershell ubicarse en la raíz del proyecto bank-microservices y ejecutar el siguiente comando para realizar el despliegue.

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
En este punto estar atento a la consola para ver cada proceso, una ver termina el despliegue muestra el estado de los servicios ver imagen
<img width="516" height="144" alt="image" src="https://github.com/user-attachments/assets/078db1e0-d5f7-4d6f-9b66-7a2b0f6ada9c" />
<img width="747" height="246" alt="image" src="https://github.com/user-attachments/assets/c62a7248-c08a-4535-a5e8-974198f12d74" />

Una vez se vea lo que muestra la imagen anterior ya se puede acceder a la aplicación para ver la documentación y probar.

URLs de los servicios corriendo:
http://localhost:8082/api/swagger-ui/index.html

http://localhost:8081/api/swagger-ui/index.html



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

Una vez realizado el despliegue verifica documentación técnica de OpenApi de los servicios en: http://localhost:8081/api/swagger-ui/index.html y http://localhost:8082/api/swagger-ui/index.html o ejecútelo el comando a continuación para confirmar que todos los servicios están en funcionamiento:

```bash
./health-check.sh
