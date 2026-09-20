# Guía de Git para el Proyecto QuindíoStay

## ¿Qué es Git?

Git es un **sistema de control de versiones** que permite:
- Registrar cambios en el código fuente
- Trabajar en equipo sin conflictos
- Retroceder a versiones anteriores
- Desarrollar en paralelo mediante ramas (branches)

---

## Configuración Inicial

```bash
# Configurar nombre y email (solo una vez)
git config --global user.name "Tu Nombre"
git config --global user.email "tu@email.com"

# Verificar configuración
git config --list
```

---

## Flujo de Trabajo Básico

```
Working Directory → Staging Area → Repositorio Local → Repositorio Remoto
     (modificar)      (git add)        (git commit)         (git push)
```

---

## Comandos Esenciales

### 1. Ver el estado del repositorio

```bash
# Qué archivos han sido modificados, agregados o eliminados
git status
```

**¿Cuándo usarlo?** Siempre antes de hacer un commit para verificar qué cambios van a quedar registrados.

---

### 2. Agregar archivos al área de preparación (Staging)

```bash
# Agregar un archivo específico
git add archivo.sql

# Agregar todos los cambios
git add .

# Agregar varios archivos específicos
git add archivo1.sql archivo2.sql
```

**¿Cuándo usarlo?** Después de modificar un archivo y antes de hacer commit. Permite seleccionar qué cambios incluir.

---

### 3. Confirmar cambios (Commit)

```bash
# Crear un commit con mensaje descriptivo
git commit -m "Descripción clara del cambio"

# Ejemplo
git commit -m "Agrega script DDL para tabla HABITACIONES"
```

**Reglas para buenos mensajes:**
- Usar imperativo: "Agrega", "Corrige", "Actualiza"
- Máximo 50 caracteres en el título
- Explicar QUÉ se hizo y POR QUÉ (no CÓMO)

**¿Cuándo usarlo?** Cuando termines una tarea lógica completa (no por cada línea de código).

---

### 4. Subir cambios al repositorio remoto (Push)

```bash
# Subir commits de la rama actual
git push

# Subir a una rama específica
git push origin nombre-rama
```

**¿Cuándo usarlo?** Después de hacer commit localmente y quieres que tu equipo vea los cambios.

---

### 5. Descargar cambios del repositorio remoto (Pull)

```bash
# Descargar y fusionar cambios remotos
git pull

# Solo descargar sin fusionar (para revisar primero)
git fetch
```

**¿Cuándo usarlo?** Antes de empezar a trabajar y después de que un compañero haga push. Siempre trabajar con la versión más actualizada.

---

### 6. Crear y gestionar ramas (Branches)

```bash
# Ver ramas existentes (* = rama actual)
git branch

# Crear nueva rama
git branch nombre-rama

# Cambiar a otra rama
git checkout nombre-rama

# Crear y cambiar en un solo comando
git checkout -b nombre-rama

# Eliminar rama (ya fusionada)
git branch -d nombre-rama
```

**Convención de nombres:**
| Tipo | Ejemplo |
|------|---------|
| Nueva función | `feature/reservas-crud` |
| Corrección de bug | `fix/error-fechas` |
| Base de datos | `database/tabla-habitaciones` |
| Documentación | `docs/guia-git` |

**¿Cuándo usarlo?** Siempre que trabajes en una tarea que no es urgente. Nunca trabajar directamente en `main`.

---

### 7. Fusionar ramas (Merge)

```bash
# Cambiar a la rama destino
git checkout main

# Fusionar la rama origen
git merge nombre-rama
```

**¿Cuándo usarlo?** Cuando terminaste tu tarea en tu rama y quieres integrarla al código principal.

---

### 8. Ver historial de cambios

```bash
# Historial básico
git log

# Historial en una línea
git log --oneline

# Historial gráfico
git log --graph --oneline
```

---

### 9. Deshacer cambios

```bash
# Quitar archivo del staging (perder cambios)
git reset HEAD archivo.sql

# Deshacer último commit (conservar cambios)
git reset --soft HEAD~1

# Deshacer commit y cambios (CUIDADO: borra todo)
git reset --hard HEAD~1
```

---

## Flujo de Trabajo en Equipo (3 personas)

```
main (producción)
  │
  ├── feature/analisis-requisitos    ← Persona 1
  │
  ├── database/tabla-reservas        ← Persona 2
  │
  └── docs/guia-normalizacion        ← Persona 3
```

### Pasos para trabajar en equipo:

1. **Antes de empezar:**
   ```bash
   git pull origin main
   ```

2. **Crear tu rama:**
   ```bash
   git checkout -b feature/mi-tarea
   ```

3. **Trabajar y hacer commits frecuentes:**
   ```bash
   git add .
   git commit -m "Avance en análisis de reservas"
   ```

4. **Subir tu rama:**
   ```bash
   git push origin feature/mi-tarea
   ```

5. **Crear Pull Request en GitHub** para que revisen tu código

6. **Después de merge, actualizar local:**
   ```bash
   git checkout main
   git pull
   ```

---

## Resumen Rápido

| Comando | Función | Frecuencia |
|---------|---------|------------|
| `git status` | Ver cambios pendientes | Constante |
| `git add` | Preparar archivos | Antes de commit |
| `git commit` | Guardar cambios localmente | Varias veces al día |
| `git push` | Subir al servidor | Después de commit |
| `git pull` | Descargar cambios | Al empezar a trabajar |
| `git branch` | Crear/cambiar rama | Al iniciar nueva tarea |
| `git merge` | Unir ramas | Al terminar tarea |

---

## Errores Comunes y Cómo Evitarlos

| Error | Solución |
|-------|----------|
| "Conflicto de merge" | Editar archivos marcados, elegir qué mantener, luego `git add` y `git commit` |
| Olvidar hacer pull antes de trabajar | Siempre ejecutar `git pull` al iniciar jornada |
| Hacer commit en `main` | Crear rama antes de trabajar |
| Mensajes de commit confusos | Seguir la convención de mensajes |

---

## Comandos Útiles Adicionales

```bash
# Ver diferencias sin agregar
git diff

# Ver diferencias de un archivo específico
git diff archivo.sql

# Ver historial de un archivo
git log --follow archivo.sql

# Stash: guardar cambios temporalmente
git stash
git stash pop
```
