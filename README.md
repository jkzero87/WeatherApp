# 🌤️ WeatherApp

Aplicación móvil Android que permite consultar el clima actual de cualquier ciudad del mundo, almacenar las ubicaciones consultadas localmente y gestionarlas desde una lista intuitiva.

Desarrollada como actividad académica del curso de **Desarrollo de Software Móvil**, integrando el consumo de una API REST externa (OpenWeatherMap) con almacenamiento local mediante SQLite (Room).

---

## ✨ Funcionalidades

- 🔍 Consulta del clima en tiempo real desde la API de OpenWeatherMap.
- 💾 Almacenamiento local de ubicaciones consultadas usando SQLite a través de Room.
- ➕ Agregar y eliminar ubicaciones de la lista.
- 📋 Visualización de las ubicaciones guardadas con sus datos meteorológicos.
- 🌐 Soporte multilenguaje (descripciones del clima en español).
- 📱 Interfaz construida con Material Design.

---

## 🛠️ Tecnologías utilizadas

| Tecnología | Para qué |
|---|---|
| **Kotlin** | Lenguaje principal |
| **Android Studio** | IDE de desarrollo |
| **Retrofit 2** | Cliente HTTP para consumir la API |
| **Gson** | Serialización/deserialización de JSON |
| **OkHttp Logging** | Logs de peticiones HTTP para depuración |
| **Room** | ORM sobre SQLite para persistencia local |
| **Coroutines** | Manejo de concurrencia y operaciones asíncronas |
| **ViewModel + LiveData** | Arquitectura MVVM y observación de datos |
| **ViewBinding** | Acceso seguro a las vistas del XML |
| **Glide** | Carga de íconos del clima desde URL |
| **Material Components** | Componentes visuales modernos |

---

## 🏛️ Arquitectura

El proyecto sigue el patrón **MVVM (Model-View-ViewModel)** con un **Repository** que coordina las fuentes de datos.
┌──────────────────────────────┐
│      View (Activity + XML)   │
└─────────────┬────────────────┘
▼
┌──────────────────────────────┐
│         ViewModel            │
└─────────────┬────────────────┘
▼
┌──────────────────────────────┐
│         Repository           │
└──────┬──────────────┬────────┘
▼              ▼
┌────────────┐  ┌────────────┐
│ Retrofit   │  │   Room     │
│ (API)      │  │  (SQLite)  │
└────────────┘  └────────────┘

Cada capa tiene una responsabilidad clara, lo que facilita pruebas, mantenimiento y reemplazo de componentes.

---

## 🌐 API utilizada

[OpenWeatherMap](https://openweathermap.org/api) — plan gratuito.

- **Endpoint:** `GET https://api.openweathermap.org/data/2.5/weather`
- **Parámetros usados:** `q` (ciudad), `appid` (API key), `units=metric`, `lang=es`

---

## ⚙️ Configuración del proyecto

### Requisitos

- Android Studio Otter o superior
- JDK 17+
- API key gratuita de [OpenWeatherMap](https://home.openweathermap.org/api_keys)
- Dispositivo físico o emulador con Android 7.0 (API 24) o superior

### Pasos

1. **Clonar el repositorio:**
```bash
   git clone https://github.com/jkzero87/WeatherApp.git
```

2. **Obtener una API key gratuita:**
   - Crear cuenta en [OpenWeatherMap](https://home.openweathermap.org/users/sign_up).
   - Ir a [API keys](https://home.openweathermap.org/api_keys) y copiar la key.
   - Esperar a que se active (puede tardar hasta 2 horas).

3. **Configurar la API key:**
   - Abrir el archivo `local.properties` en la raíz del proyecto.
   - Agregar al final la línea:
```properties
     OPENWEATHER_API_KEY=tu_api_key_aqui
```

4. **Sincronizar Gradle** desde Android Studio.

5. **Ejecutar la app** con ▶ Run.

> 🔒 **Nota de seguridad:** la API key se gestiona vía `local.properties`, archivo que está excluido del repositorio mediante `.gitignore`. La key nunca se sube a GitHub.

---

## 📁 Estructura del proyecto
app/src/main/
├── java/com/example/weatherapp/
│   ├── api/
│   │   ├── WeatherResponse.kt
│   │   ├── WeatherApiService.kt
│   │   └── RetrofitClient.kt
│   └── MainActivity.kt
└── res/
├── layout/
│   └── activity_main.xml
└── values/
└── strings.xml

---

## 📚 Conceptos aplicados

- Consumo de **API REST** con autenticación por API key.
- **Persistencia local** con SQLite mediante Room.
- **Programación asíncrona** con Coroutines y `lifecycleScope`.
- **Arquitectura MVVM** con separación de responsabilidades.
- **ViewBinding** para acceso a vistas sin `findViewById`.
- **Buenas prácticas de seguridad**: gestión de secretos vía `local.properties` + `BuildConfig`.

---

## 👤 Autor

**Juan Bejarano**

Proyecto académico — 2026.

---

## 📄 Licencia

Este proyecto se distribuye con fines educativos.