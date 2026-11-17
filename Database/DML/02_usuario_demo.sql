-- =====================================================================
--  DML - USUARIO DEMO PARA EL INICIO DE SESIÓN
--  Ejecutar sobre una base de datos QuindíoStay ya creada.
--  Usuario: admin_demo | Contraseña: Admin123
-- =====================================================================

MERGE INTO Usuario_Sistema u
USING (SELECT 'admin_demo' AS nombre_usuario FROM dual) s
ON (u.nombre_usuario = s.nombre_usuario)
WHEN MATCHED THEN UPDATE SET
    u.contrasena_hash = '3b612c75a7b5048a435fb6ec81e52ff92d6d795a8b5a9c17070f6a63c97a53b2',
    u.estado = 'ACTIVO',
    u.id_rol = 1,
    u.id_personal = NULL
WHEN NOT MATCHED THEN INSERT
    (id_usuario, nombre_usuario, contrasena_hash, estado, id_rol, id_personal)
VALUES
    (7, s.nombre_usuario, '3b612c75a7b5048a435fb6ec81e52ff92d6d795a8b5a9c17070f6a63c97a53b2', 'ACTIVO', 1, NULL);

COMMIT;
