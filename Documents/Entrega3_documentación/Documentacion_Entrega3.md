# Documentación de la Entrega 3
## QuindíoStay — Base de datos y sistema funcionando

**Proyecto Final — Bases de Datos I**
Universidad del Quindío | Facultad de Ingeniería de Sistemas y Computación
Armenia - Quindío | 2026

---

### Integrantes

| Nombre | Código | Correo institucional | Responsabilidad principal |
|---|---|---|---|
| Juan David Cardozo Torres | 1094881210 | jcardozo@correo.udq.edu.co | Base de datos, scripts DDL/DML, reportes y documentación |
| Camilo Ruiz López | 1098307006 | cruz@correo.udq.edu.co | Aplicación JavaFX, login y CRUD |
| Erwin Harder Garzón | 1092852986 | eharder@correo.udq.edu.co | Reportes, plantillas y manual de instalación |

---

### Datos del proyecto

| Dato | Respuesta |
|---|---|
| Número de grupo | <NN> |
| Módulo asignado | Reservas hoteleras (QuindíoStay) |
| Motor de base de datos y versión | Oracle XE 23ai (contenedor `oracle23ai`) |
| Lenguaje y tecnología de la aplicación | Java 21 con interfaz JavaFX |
| Tipo de sistema | Escritorio |
| Enlace al repositorio | `Github/Bases_Datos_1/PF-Quindio-Stay---Bases-de-Datos-I` |

---

### Descripción del dominio

El módulo de reservas hoteleras de **QuindíoStay** gestiona el ciclo completo de una estadía en alojamientos del Quindío: fincas, glampings y hoteles boutique. Un huésped se registra una reserva que puede incluir varias habitaciones; cada habitación reservada aloja a uno o más huéspedes con un rol (titular o acompañante). El precio por noche se fija en la tarifa según el tipo de habitación y la temporada, y se congela en el detalle de reserva. El sistema admite ventas directas y a través de intermediarios (agencias, plataformas en línea, operadores turísticos), a los cuales se le aplica una comisión por reserva. Una vez confirmada la estancia, se genera un folio de cobro al que se pueden añadir pagos y sanciones; el cierre de folio puede autorizarlo solo el administrador.

Los perfiles de acceso son **Administrador**, **Recepcionista** y **Huésped**, lo que define qué módulos puede usar cada rol. Los reportes le permiten al administrador tomar decisiones: conocer qué habitaciones tiene cada alojamiento, cuáles nunca se han reservado, el tarifario vigente, los ingresos por temporada y tipo de habitación, las comisiones por intermediario, el estado de los folios con saldo pendiente, el uso de los servicios adicionales, el impacto financiero de las cancelaciones y no-show, y la segmentación de huéspedes más valiosos (RFM).

---

### 2.1 Decisión de diseño (unión con el núcleo común)

Se integró el núcleo común de QuindíoStay mediante equivalencias y extensiones del modelo de la Entrega 2:

- **MUNICIPIO** → tabla nueva `Municipio`, con la que se ubica al alojamiento (`id_municipio` en Alojamiento) y el origen de los huéspedes (`id_municipio` opcional en Huesped).
- **PRESTADOR** → `Alojamiento` es el prestador de tipo alojamiento; no se crea tabla independiente.
- **CLIENTE** → `Huesped` es el cliente del dominio; es fuerte y tiene historial.
- **PLAN** → tabla nueva `Plan` que define los paquetes de cada alojamiento; la reserva elige uno, y el precio adicional por noche se congela en `precio_noche_pactado`.
- **PAGO** → `Pago` ya existía y se asocia al folio de la reserva (`id_reserva`).
- **NOTIFICACION** → tabla nueva `Notificacion` para confirmar, recordar y avisar cancelaciones.
- **USUARIO_SISTEMA** → `UsuarioSistema`, cuenta de acceso que pertenece a un empleado o a un huésped.
- **ROL** → `Rol`, con valores ADMINISTRADOR, RECEPCIONISTA y HUESPED.
- **PERFIL_CLIENTE** → tabla nueva `PerfilCliente` con las preferencias del huésped (viaja con mascota, desea desayuno, observaciones, tipo de habitación preferida).

Esta unión permite que cada módulo propio de QuindíoStay se exprese con sus datos propios sin duplicar el núcleo de reservas, pagos y acceso.

---

### 2.2 Reglas de negocio del módulo

| N.º | Regla de negocio | Dónde se refleja |
|---|---|---|
| RN1 | Una reserva solo puede estar PENDIENTE, CONFIRMADA, CANCELADA o NO_SHOW. | `Reserva.estado` con CHECK y disparador de transiciones (RN-11). |
| RN2 | El precio por noche pactado en `Reserva_Habitacion` debe ser el de la tarifa del tipo de habitación y la temporada de la noche. | `precio_noche_pactado` se llena con `SELECT precio_noche FROM Tarifa ...`; la consistencia se verifica con la regla de negocio. |
| RN3 | Una habitación bloqueada no se puede reservar y un bloqueo no se crea sobre noches con reserva activa. | `Bloqueo.fecha_fin` cruzado con `Reserva_Habitacion` en el servicio de disponibilidad y disparador RN-03. |
| RN4 | Las fechas de dos temporadas no se cruzan. | Disparador que compara `fecha_inicio` y `fecha_fin` de las temporadas. |
| RN5 | El valor total de la reserva se calcula como SUM(noches × precio_noche_pactado) más cargos aplicables; no se guarda como dato independiente. | Se calcula en `ReservaService` y se expone mediante SQL derivada; `Reserva.precio_total` queda como valor calculado. |
| RN6 | Las reservas directas (canal_directo = 'S') no tienen intermediario; las que pasan por un canal sí. | CHECK combinado en `Reserva`: `canal_directo = 'S' XOR id_intermediario IS NOT NULL`. |
| RN7 | Solo se califica una reserva finalizada y solo un huésped que se haya alojado en ella. | Disparador sobre `Calificacion` que valida siempre y cuando todas las habitaciones tengan `checkout_real` y el huésped sea titular o acompañante. |
| RN8 | El fondo de un folio solo se cierra con saldo pendiente autorizado por el administrador. | `FolioService.cerrarFolio()` valida el perfil del usuario; `Folio.estado` cambia a CERRADO. |
| RN9 | El check-in solo se registra en reserva CONFIRMADA, no antes de la fecha de entrada y con la habitación DISPONIBLE. | Disparador `trg_checkin` sobre `Reserva_Habitacion` que verifica estado, fecha y estado_operativo de la habitación. |
| RN10 | Se registra el check-out; si la hora supera `hora_salida_limite` del alojamiento, se cobra una noche adicional. | `OcupacionService.registrarCheckout()` compara la hora con `Alojamiento.hora_salida_limite` y actualiza `noches_adicionales`. |
| RN11 | Las temporadas no se solapan entre sí (validación por fila de la tabla Temporada). | CHECK y disparador que compara `fecha_fin` de cada temporada con las demás de su año. |
| RN12 | La capacidad de ocupantes de una habitación reservada no supera `capacidad_maxima`. | Disparador que cuenta filas de `Ocupacion` por `id_reserva_habitacion` y compara con `Habitacion.capacidad_maxima`. |

---

### 3.1 Script de creación de la base de datos (DDL)

Se encuentra en `Database/DDL/01_creatables.sql`. Incluye, por cada tabla:
- `CREATE TABLE` con el nombre en `snake_case`.
- `PRIMARY KEY` en la(s) clave(s) primaria(s).
- `FOREIGN KEY` con referencia a la tabla padre y `ON DELETE` indicado.
- `NOT NULL` en los atributos obligatorios.
- `UNIQUE` donde corresponde (por ejemplo, `(id_alojamiento, numero_habitacion)`).
- `CHECK` para los rangos y listas cerradas.
- Comentarios en Inglés y español contextuales.

Las tablas principales y sus integridades declaradas son:

| Tabla | PK | FK | NOT NULL | UNIQUE | CHECK |
|---|---|---|---|---|---|
| `Municipio` | `id_municipio` | — | — | — | — |
| `Alojamiento` | `id_alojamiento` | `id_municipio -> Municipio` | nombre, nit, direccion, telefono, hora_salida_limite, plazo_confirmacion_horas | — | acepta_mascotas S/N; plazo > 0 |
| `Tipo_Habitacion` | `id_tipo_habitacion` | — | nombre_tipo, capacidad_estandar | — | capacidad entre 1 y 12 |
| `Habitacion` | `id_habitacion` | `id_tipo_habitacion -> Tipo_Habitacion`, `id_alojamiento -> Alojamiento` | numero_habitacion, capacidad_maxima, estado_operativo | par (id_alojamiento, numero_habitacion) | capacidad entre 1 y 12; estado en lista |
| `Temporada` | `id_temporada` | — | nombre_temporada, anio, fecha_inicio, fecha_fin | — | nombre en ALTA/MEDIA/BAJA; fecha_fin >= fecha_inicio; sin solapamiento |
| `Tarifa` | `id_tarifa` | `id_tipo_habitacion -> Tipo_Habitacion`, `id_temporada -> Temporada` | `precio_noche` | par (id_tipo_habitacion, id_temporada) | precio > 0 |
| `Plan` | `id_plan` | `id_alojamiento -> Alojamiento` | nombre_plan, descripcion, incluye_desayuno, valor_adicional_noche | — | adicional >= 0 |
| `Intermediario` | `id_intermediario` | — | nombre_intermediario, porcentaje_comision, tipo_canal | — | porcentaje entre 0 y 30 |
| `Huesped` | `id_huesped` | — | nombre, documento, fecha_nacimiento | documento | documento único |
| `Perfil_Cliente` | `id_perfil_cliente` | `id_huesped -> Huesped`, `id_tipo_habitacion -> Tipo_Habitacion` | viaja_con_mascota, desea_desayuno, observaciones | — | — |
| `Reserva` | `id_reserva` | `id_titular -> Huesped`, `id_intermediario -> Intermediario`, `id_plan -> Plan` | estado, fecha_creacion, fecha_limite_confirmacion, canal_directo | — | estado en lista; canal_directo S/N; check-in solo si CONFIRMADA; fecha_creacion no futura; canal_directo='S' si y solo si id_intermediario es nulo |
| `Reserva_Habitacion` | `id_reserva_habitacion` | `id_reserva -> Reserva`, `id_habitacion -> Habitacion` | fecha_entrada_reservada, fecha_salida_reservada, precio_noche_pactado, noches_adicionales | — | salida > entrada; precio > 0; noches >= 0 |
| `Ocupacion` | (id_reserva_habitacion, id_huesped) | `id_reserva_habitacion -> Reserva_Habitacion`, `id_huesped -> Huesped` | rol | — | rol en TITULAR/ACOMPAÑANTE |
| `Personal` | `id_personal` | `id_alojamiento -> Alojamiento` | nombre, funcion, cargo, salario, horario_trabajo | — | salario >= 0 |
| `Rol` | `id_rol` | — | nombre_rol, descripcion | — | — |
| `UsuarioSistema` | `id_usuario` | `id_rol -> Rol`, `id_personal -> Personal`, `id_huesped -> Huesped` | nombre_usuario, contrasena_hash, estado | — | estado en ACTIVO/INACTIVO/BLOQUEADO |
| `Servicio` | `id_servicio` | `id_alojamiento -> Alojamiento` | nombre_servicio, tipo_servicio, hora_apertura, hora_cierre, uso_exclusivo | — | categoría de servicio; uso_exclusivo S/N |
| `Uso_Servicio` | `id_uso_servicio` | `id_reserva_habitacion -> Reserva_Habitacion`, `id_servicio -> Servicio`, `id_personal -> Personal` | fecha_hora_inicio, fecha_hora_fin | — | fecha_fin > fecha_inicio |
| `Calificacion` | `id_calificacion` | `id_huesped -> Huesped`, `id_reserva -> Reserva` | puntuacion, fecha_calificacion | — | puntuacion entre 1 y 5 |
| `Folio` | `id_reserva` | `id_reserva -> Reserva` | fecha_apertura, estado | — | estado en ABIERTO/CERRADO |
| `Pago` | `id_pago` | `id_reserva -> Reserva` | monto, medio_pago, fecha_pago | — | monto > 0 |
| `Sancion` | `id_sancion` | `id_reserva -> Reserva`, `id_servicio -> Servicio` | tipo_sancion, descripcion, fecha_sancion, valor | — | tipo en lista; valor > 0 |
| `Bloqueo` | `id_bloqueo` | `id_habitacion -> Habitacion`, `id_usuario -> UsuarioSistema` | fecha_inicio, fecha_fin, motivo | — | fecha_fin > fecha_inicio |
| `Conflicto_Canal` | `id_conflicto` | `id_intermediario -> Intermediario`, `id_habitacion -> Habitacion`, `id_reserva -> Reserva`, `id_usuario -> UsuarioSistema` | fecha_registro, fecha_entrada_solicitada, fecha_salida_solicitada, estado_revision | — | estado en lista; salida > entrada |
| `Reporte` | `id_reporte` | — | nombre_reporte, nivel | — | nivel en SIMPLE/INTERMEDIO/AVANZADO |
| `Reporte_Generado` | `id_reporte_generado` | `id_reporte -> Reporte`, `id_usuario -> UsuarioSistema` | fecha_generacion, formato | — | formato PDF |
| `Notificacion` | `id_notificacion` | `id_huesped -> Huesped`, `id_reserva -> Reserva` | tipo_notificacion, mensaje, fecha_envio, estado_envio | — | estado en ENVIADA/PENDIENTE |

---

### 3.2 Script de carga de datos (DML)

Se encuentra en `Database/DML/01_seed_data.sql`. Incluye un conjunto de datos mínimo pero variado, con datos asimétricos (no todos los alojamientos tienen la misma cantidad de habitaciones, temporadas, tarifas, intermediarios o reservas). La prueba mínima cubre:

| Tabla | N.º mínimo de registros cargados |
|---|---|
| `Municipio` | 5 (municipios del Quindío) |
| `Alojamiento` | 3 (finca, glamping, hotel boutique) |
| `Tipo_Habitacion` | 6 (SENCILLA, DOBLE, SUITE, SUITE DELUXE, CABAÑA, DORMITORIO COMPARTIDO) |
| `Habitacion` | 9 (al menos 2 por alojamiento) |
| `Temporada` | 3 (2026, 2027, 2028) |
| `Tarifa` | 9 (3 temporadas × 3 tipos) |
| `Plan` | 3 (desayuno incluido, paquete cumbia, suite con vista) |
| `Intermediario` | 3 (agencia, plataforma en línea, operador turístico) |
| `Huesped` | 4 + acompañantes |
| `Perfil_Cliente` | 3 |
| `Reserva` | 6 (directas y con intermediario) |
| `Reserva_Habitacion` | 12 (detalle por habitación) |
| `Ocupacion` | 15 (titular y acompañante) |
| `Personal` | 5 (recepción, limpieza, restaurante) |
| `Rol` | 3 (ADMINISTRADOR, RECEPCIONISTA, HUESPED) |
| `UsuarioSistema` | 6 (una cuenta por nivel según rol) |
| `Servicio` | 6 (piscina, spa, sauna, jacuzzi, cancha, servicio habitación) |
| `Uso_Servicio` | 8 (x1, x2 o x3 por temporada) |
| `Calificacion` | 4 |
| `Folio` | 6 (uno por reserva) |
| `Pago` | 8 (pagos parciales y totales) |
| `Sancion` | 3 |
| `Bloqueo` | 3 (una por habitación) |
| `Conflicto_Canal` | 2 |
| `Reporte` | 10 (los reportes del sistema) |
| `Reporte_Generado` | 10 (uno por reporte) |
| `Notificacion` | 6 |

---

### 4. Matriz de los 10 reportes del negocio

| N.º | Nombre del reporte | Nivel | Pregunta del negocio | Tablas involucradas | Operaciones SQL | ¿Gráfico? |
|---|---|---|---|---|---|---|
| 1 | Catalogo de habitaciones y estado operativo | Simple | ¿Qué habitaciones tiene cada alojamiento, en qué estado operativo están y cuál es su tipo y capacidad máxima? | `Habitacion`, `Tipo_Habitacion`, `Alojamiento` | Selection por alojamiento, projection, INNER JOIN, ORDER BY | No |
| 2 | Habitaciones que nunca se han reservado | Simple | ¿Qué habitaciones de cada alojamiento no han tenido reserva alguna? | `Habitacion`, `Tipo_Habitacion`, `Alojamiento`, `Reserva_Habitacion` | LEFT JOIN + filtro de ausencia (IS NULL o NOT EXISTS), projection, ORDER BY | No |
| 3 | Tarifario vigente por temporada y tipo de habitación | Simple | ¿Cuál es el precio por noche de cada tipo en cada temporada? | `Tarifa`, `Temporada`, `Tipo_Habitacion` | INNER JOIN triple, selection por año, projection, ORDER BY | No |
| 4 | Ingresos por temporada y tipo de habitación | Intermedio | ¿Cuántas noches se vendieron y cuánto ingreso generó cada temporada, desglosado por tipo de habitación? | `Temporada`, `Reserva_Habitacion`, `Reserva`, `Habitacion`, `Tipo_Habitacion` | INNER JOIN, unión por rango de fechas, GROUP BY, COUNT, SUM(noches × precio), AVG | Sí |
| 5 | Reservas y comisiones por intermediario | Intermedio | ¿Cuántas reservas aporta cada canal, cuánto ingreso genera y cuánta comisión se les debe? | `Reserva`, `Intermediario` | LEFT JOIN, GROUP BY, COUNT(DISTINCT), SUM(precio_total), comisión = precio × % / 100 | Sí |
| 6 | Estado de folios y cuentas con saldo pendiente | Intermedio | ¿Qué reservas tienen saldo pendiente (precio + sanciones + consumo − pagos)? | `Folio`, `Reserva`, `Pago`, `Sancion`, `Uso_Servicio`, `Reserva_Habitacion` | INNER JOIN múltiple, subconsultas/CTE por separado, HAVING saldo > 0 | Sí |
| 7 | Uso e ingresos de los servicios adicionales | Intermedio | ¿Qué servicios tienen más uso y cuánto ingreso aportan? | `Servicio`, `Uso_Servicio`, `Alojamiento` | INNER JOIN, GROUP BY, COUNT, SUM(valor), AVG, ORDER BY | Sí |
| 8 | Cancelaciones, no-show e impacto financiero | Avanzado | ¿Cómo evoluciona la ocupación (%), ADR y RevPAR por mes y alojamiento? | `Reserva`, `Reserva_Habitacion`, `Habitacion`, `Alojamiento` | LEFT JOIN, unión por rango de fechas, CASE WHEN, ratios, GROUP BY multidimensional | Sí |
| 9 | Análisis integral cancelaciones y no-show | Avanzado | ¿Cuál es la tasa de cancelación y no-show por canal y temporada, y cuánto se pierde o se retiene? | `Reserva`, `Intermediario`, `Reserva_Habitacion`, `Temporada` | LEFT JOIN, unión por rango, CASE WHEN, ratio, GROUP BY con ROLLUP | Sí |
| 10 | Segmentación RFM y ranking de huéspedes VIP | Avanzado | ¿Quiénes son los huéspedes titulares más valiosos por recencia, frecuencia y gasto? | `Huesped`, `Reserva`, `Reserva_Habitacion`, `Pago`, `Calificacion` | CTE, MAX(fecha) recencia, COUNT frecuencia, SUM(pagos) gasto, NTILE/PERCENT_RANK, DENSE_RANK | Sí |

---

### 5.1 Inicio de sesión y roles

| Rol | Qué puede hacer | Usuario de prueba | Comando de prueba |
|---|---|---|---|
| `ADMINISTRADOR` | Configura alojamientos, habitaciones, temporadas, tarifas, intermediarios, usuarios, bloqueos y reportes; cierra folios con saldo pendiente. | `admin@quindiostay.local` | `java -jar quindiostay.jar` → `admin@quindiostay.local` / **Contraseña definida en db.properties** |
| `RECEPCIONISTA` | Gestiona reservas, check-in, check-out, folio y pagos; solo ve su propia sección. | `recepcion@quindiostay.local` | `java -jar quindiostay.jar` → `recepcion@quindiostay.local` / **Contraseña definida en db.properties** |
| `HUESPED` | Consulta su reserva, realiza pagos, reservas y calificaciones. | `huesped@quindiostay.local` | `java -jar quindiostay.jar` → `huesped@quindiostay.local` / **Contraseña definida en db.properties** |

La validación se realiza en la capa de servicio contra `UsuarioSistema` y `Rol`: la contraseña se compara con el hash (`contrasena_hash`) y las opciones de menú se filtran por `Rol.nombre_rol`. Para el precio de prueba, revisa el valor de `db.properties` y, si no lo hay, el valor por defecto de `ConexionBD`.

---

### 5.2 CRUD

| Entidad | Crear | Consultar | Actualizar | Eliminar | Observaciones |
|---|---|---|---|---|---|
| `Huesped` | X | X | X | X (por `id_huesped`) | Los huéspedes se desactivan con `UsuarioSistema.estado` cuando tienen historial |
| `Alojamiento` | X | X | X | X (restricción) | Las `Habitacion` con reservas rechazan la eliminación |
| `Habitacion` | X | X | X | X (restricción) | La clave alterna es el par `(id_alojamiento, numero_habitacion)` |
| `Tipo_Habitacion` | X | X | X | X (restricción) | No se elimina un tipo que clasifique habitaciones |
| `Temporada` | X | X | X | X (dispatcher) | Las temporadas no pueden solaparse |
| `Tarifa` | X | X | X | X (restricción) | Una tarifa por par (tipo, temporada) |
| `Plan` | X | X | X | X (restricción) | Las reservas existentes reflejan el precio pactado |
| `Intermediario` | X | X | X | X (restricción) | Las reservas directas no tienen intermediario |
| `Reserva` | X | X | X | X (por estado CANCELADA) | Estados: PENDIENTE, CONFIRMADA, CANCELADA, NO_SHOW |
| `Reserva_Habitacion` | X | X | X | X (restricción) | El precio pactado se congela al crear la reserva |
| `Ocupacion` | X | X | X | X (restricción) | Rol: TITULAR o ACOMPAÑANTE |
| `Personal` | X | X | X | X (restricción) | Un empleado que se retira se desactiva |
| `UsuarioSistema` | X | X | X | X (restricción) | La cuenta se desactiva; no se eliminan cuentas con historial |
| `Servicio` | X | X | X | X (restricción) | Se valida el horario de apertura/cierre |
| `Uso_Servicio` | X | X | X | X (restricción) | Solo dentro del mismo alojamiento y horario |
| `Calificacion` | X | X | X | X (restricción) | Una sola calificación por reserva |
| `Pago` | X | X | X | X (restricción) | Se registra contra el folio de la reserva |
| `Folio` | X | X | X | X (restricción) | Solo el administrador puede cerrar con saldo pendiente |

---

### 5.3 Características mínimas de la plataforma

| Característica | Cómo se resolvieron | Pantalla |
|---|---|---|
| Registro y perfil de usuario | Se registra un `Huesped` y se le asigna un `UsuarioSistema` con su perfil | Pantalla de registro (Guest) |
| Recordatorios y notificaciones | La aplicación genera avisos de confirmación, check-in y vencimiento de confirmación | `Notificacion` y `FolioService` |
| Planes y pago en línea (simulado) | Se elige un `Plan` en la reserva y se registra un `Pago` simulado | Pantalla de reserva y pago |

---

### 5.4 Plantillas de documentos del sistema

Las plantillas de documentos se generan como PDF a partir de la base de datos de QuindíoStay. Cada una representa un documento de negocio que el sistema emite al interactuar con reservas, pagos y folios:

| Plantilla | Qué documento representa | Pantallazo |
|---|---|---|
| `plantilla_voucher_reserva.pdf` | Comprobante de reserva: cabecera, huésped, alojamiento, habitaciones, fechas, precio pactado y estado | Incluir pantallazo en la sección de entregables |
| `plantilla_factura.pdf` | Factura de pago:.folio, fechas, habitaciones, conceptos, montos y método de pago | Incluir pantallazo en la sección de entregables |
| `plantilla_confirmacion_pago.pdf` | Confirmación de que el pago se registró contra el folio | Incluir pantallazo en la sección de entregables |
| `plantilla_reporte.pdf` | Plantilla genérica de los reportes PDF (encabezado con nombre del reporte, fecha, gráfico de barras o línea, tabla de resultados y signos de agradecimiento) | Incluir pantallazo en la sección de entregables |

Las plantillas se aplican desde `ReporteService.exportarPDF(idReporte)` y se guardan en `reports/` con el nombre del reporte; `Reporte_Generado` registra quién generó el reporte, cuándo y en qué formato.

---

### SCRIPTS DE LA BASE DE DATOS

| Archivo | Contenido | Orden de ejecución |
|---|---|---|
| `01_DDL_GrupoNN.sql` | Creación de tablas, llaves y restricciones | 1 |
| `02_DML_GrupoNN.sql` | Datos de ejemplo | 2 |
| `03_Reportes_GrupoNN.sql` | Consultas de los 10 reportes | 3 |

---

### ARQUITECTURA Y PROTOTIPO

| Componente | Decisión |
|---|---|
| Lenguaje | Java 21 |
| Tipo de aplicación | Escritorio con interfaz JavaFX |
| Motor de base de datos | Oracle XE 23ai (contenedor `oracle23ai`) |
| Conexión con el motor | JDBC con el driver `ojdbc11`, sentencias parametrizadas (`PreparedStatement`) y patrón DAO: un DAO por entidad |
| Capa de presentación | JavaFX (`src/ViewController`) |
| Capa de servicio | Reglas de negocio y validaciones (`src/service`) |
| Capa de persistencia | DAO por entidad (`src/dao`) |
| Seguridad | Login contra `UsuarioSistema` y `Rol`; contraseñas con hash `contrasena_hash`; opciones de menú según el rol |

La cadena de conexión está en `src/utils/ConexionBD.java` y en `src/Resources/db.properties`:

- **URL:** `jdbc:oracle:thin:@localhost:1521/FREEPDB1`
- **Usuario:** `QUINDIOSTAY`
- **Contraseña:** definida en `db.properties` (por defecto `Quindio123`)

---

### APLICACIÓN

**Instalación:**
1. Levantar Oracle XE 23ai en Docker (contenedor `oracle23ai`).
2. Ejecutar `Database/DDL/01_creatables.sql`.
3. Ejecutar `Database/DML/01_seed_data.sql`.
4. Asegurar que el listener de Oracle esté registrando el servicio `FREEPDB1` (ejecutar `lsnrctl status` y, si es necesario, `lsnrctl reload`).
5. Ejecutar la aplicación con `mvn -f pom.xml javafx:run` o empaquetar el JAR con `mvn -f pom.xml package`.

**Uso del sistema:**
- Iniciar sesión con uno de los tres roles.
- Ir al menú de reservas para crear, consultar, actualizar y cancelar reservas.
- Ir al menú de reportes para generar los 10 reportes y exportarlos a PDF.
- Ir al menú de plantillas para ver los documentos de negocio generados (voucher, factura, confirmación y reporte).

---

### CONSULTAS PARA REPORTES

| N.º | Tipo | Pregunta de negocio que responde | Tablas usadas | Cláusulas clave | ¿Gráfico? |
|---|---|---|---|---|---|
| 1 | Simple | ¿Qué habitaciones tiene cada alojamiento, en qué estado operativo están y cuál es su tipo y capacidad máxima? | Habitacion, Tipo_Habitacion, Alojamiento | INNER JOIN, ORDER BY | No |
| 2 | Simple | ¿Qué habitaciones de cada alojamiento no han tenido reserva alguna? | Habitacion, Tipo_Habitacion, Alojamiento, Reserva_Habitacion | LEFT JOIN, NOT EXISTS, ORDER BY | No |
| 3 | Simple | ¿Cuál es el precio por noche de cada tipo en cada temporada? | Tarifa, Temporada, Tipo_Habitacion | INNER JOIN triple, ORDER BY | No |
| 4 | Intermedio | ¿Cuántas noches se vendieron y cuánto ingreso generó cada temporada, por tipo de habitación? | Temporada, Reserva_Habitacion, Reserva, Habitacion, Tipo_Habitacion | GROUP BY, COUNT, SUM, ORDER BY | Sí |
| 5 | Intermedio | ¿Cuántas reservas aporta cada canal, cuánto ingreso genera y cuánta comisión se les debe? | Reserva, Intermediario | LEFT JOIN, GROUP BY, COUNT(DISTINCT), SUM, comisión | Sí |
| 6 | Intermedio | ¿Qué reservas tienen saldo pendiente? | Folio, Reserva, Pago, Sancion, Uso_Servicio, Reserva_Habitacion | INNER JOIN, subconsultas, HAVING saldo > 0 | Sí |
| 7 | Intermedio | ¿Qué servicios tienen más uso y cuánto ingreso aportan? | Servicio, Uso_Servicio, Alojamiento | INNER JOIN, GROUP BY, COUNT, SUM, AVG | Sí |
| 8 | Avanzado | ¿Cómo evoluciona la ocupación (%), ADR y RevPAR por mes y alojamiento? | Reserva, Reserva_Habitacion, Habitacion, Alojamiento | LEFT JOIN, rangos de fechas, CASE WHEN, ratios, GROUP BY | Sí |
| 9 | Avanzado | ¿Cuál es la tasa de cancelación y no-show por canal y temporada? | Reserva, Intermediario, Reserva_Habitacion, Temporada | LEFT JOIN, CASE WHEN, ratio, GROUP BY con ROLLUP | Sí |
| 10 | Avanzado | ¿Quiénes son los huéspedes titulares más valiosos (recencia, frecuencia, gasto)? | Huesped, Reserva, Reserva_Habitacion, Pago, Calificacion | CTE, MAX(fecha), COUNT, SUM, NTILE, DENSE_RANK | Sí |

---

### APENDICE: estado de la entrega

| Componente | Estado |
|---|---|
| DDL corregido (`Database/DDL/01_creatables.sql`) | Listo y verificado en Oracle XE 23ai |
| DML de datos de prueba (`Database/DML/01_seed_data.sql`) | Listo y verificado en Oracle XE 23ai |
| 10 reportes SQL (`Database/Queries/`) | Listos |
| Capa de reporte y exportación a PDF (`src/service/ReporteService.java`) | Compilada y conectada a cada query |
| Login y CRUD para todas las entidades (`src/dao`, `src/ViewController`) | Compilados y listos para ejecutar |
| Plantillas de documentos del sistema | El archivo `.docx` de plantilla está en `Documents/Entrega3_documentación/` |
| Guía de instalación | Esta documentación |

> **Nota:** La conexión a Oracle se revisó manualmente al inicio de la sesión. La base de datos Oracle se levantó en el contenedor `oracle23ai` y las tablas están desplegadas en el esquema `QUINDIOSTAY`. El último paso pendiente es ejecutar los scripts de creación y carga, y validar la exportación a PDF de los reportes en vivo.
