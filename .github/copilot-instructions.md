# Instrucciones para agentes AI en DaviviendaBank

Este documento guía a los agentes AI para trabajar de manera efectiva en el proyecto DaviviendaBank.

## Arquitectura general
- Proyecto Android/Kotlin con Gradle (archivos principales: `build.gradle.kts`, `settings.gradle.kts`, carpeta `app/`).
- El código fuente está en `app/src/main/java/` y los recursos en `app/src/main/res/`.
- El manifiesto de la app está en `app/src/main/AndroidManifest.xml`.
- Las pruebas se ubican en `app/src/test/java/` y `app/src/androidTest/java/`.

## Flujos de desarrollo
- **Compilación:** Usar `./gradlew build` (Linux/macOS) o `gradlew.bat build` (Windows).
- **Ejecución de pruebas:** `./gradlew test` para pruebas unitarias, `./gradlew connectedAndroidTest` para pruebas instrumentadas.
- **Limpieza:** `./gradlew clean` elimina artefactos de compilación.
- **Debug:** Utilizar Android Studio para depuración interactiva; los logs se encuentran en `app/build/outputs/logs/`.

## Convenciones y patrones
- Estructura estándar de Android, pero los scripts de Gradle usan Kotlin DSL (`.kts`).
- Los recursos generados y archivos intermedios se ubican en `app/build/intermediates/` y `app/build/generated/`.
- Los iconos y assets personalizados están en `app/src/main/`.
- Las reglas de ProGuard se definen en `app/proguard-rules.pro`.

## Integraciones y dependencias
- Las dependencias se gestionan en `gradle/libs.versions.toml` y los archivos `build.gradle.kts`.
- El wrapper de Gradle está en `gradle/wrapper/`.
- No se detectan integraciones externas automáticas (API, backend) en la estructura actual; revisar el código fuente para detalles.

## Ejemplo de flujo de trabajo
1. Modificar código en `app/src/main/java/`.
2. Ejecutar `gradlew.bat build` para compilar en Windows.
3. Ejecutar pruebas con `gradlew.bat test`.
4. Revisar logs en `app/build/outputs/logs/` si hay errores.

## Archivos clave
- `app/build.gradle.kts`: Configuración principal del módulo app.
- `app/proguard-rules.pro`: Reglas de ofuscación y optimización.
- `gradle/libs.versions.toml`: Versiones y dependencias centralizadas.
- `app/src/main/AndroidManifest.xml`: Configuración de la app.

---

Actualiza este documento si detectas nuevos patrones, flujos o integraciones relevantes.
