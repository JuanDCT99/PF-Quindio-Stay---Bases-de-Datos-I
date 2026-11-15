-- =====================================================================
--  DML - USUARIO DEMO PARA EL INICIO DE SESIÓN
--  Ejecutar sobre una base de datos QuindíoStay ya creada.
--  Usuario: admin_demo | Contraseña: Admin123
-- =====================================================================

INSERT INTO Usuario_Sistema (id_usuario, nombre_usuario, contrasena_hash, estado, id_rol, id_personal)
VALUES (7, 'admin_demo', '3b612c75a7b5048a435fb6ec81e52ff92d6d795a8b5a9c17070f6a63c97a53b2', 'ACTIVO', 1, NULL);

COMMIT;
