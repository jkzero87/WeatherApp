# WeatherApp

App Android en Kotlin que consulta el clima actual de cualquier ciudad con la
API de OpenWeatherMap y guarda las ciudades consultadas en una lista local
(SQLite con Room).

Actividad académica del curso de Desarrollo de Software Móvil (2026).

## Funcionalidades

- Consulta del clima actual por nombre de ciudad, en grados Celsius y con la
  descripción en español (`units=metric`, `lang=es`).
- Lista de ubicaciones guardadas en SQLite (Room), con agregar y eliminar.
- Ícono del clima cargado desde la URL de la API con Glide.
- Tema claro y oscuro (Material Components).

## Arquitectura

MVVM con un repositorio que coordina la API y la base de datos local:

```
View (MainActivity + XML, ViewBinding)
        │
        ▼
WeatherViewModel (LiveData, coroutines)
        │
        ▼
WeatherRepository
   ├──► Retrofit + Gson ──► OpenWeatherMap
   └──► Room (LocationDao) ──► SQLite
```

| Paquete | Contenido |
|---|---|
| `api/` | `WeatherApiService` (Retrofit), `RetrofitClient` (con logging de OkHttp), `WeatherResponse` |
| `data/` | `AppDatabase`, `LocationDao`, `LocationEntity`, `WeatherRepository` |
| `viewmodel/` | `WeatherViewModel` |
| `ui/` | `MainActivity`, `LocationAdapter` |

## API

[OpenWeatherMap](https://openweathermap.org/api), plan gratuito:
`GET https://api.openweathermap.org/data/2.5/weather` con `q` (ciudad),
`appid`, `units=metric` y `lang=es`.

## Cómo correrla

Requisitos: Android Studio Otter o superior, JDK 17+, y un dispositivo o
emulador con Android 7.0 (API 24) o superior.

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/jkzero87/WeatherApp.git
   ```
2. Crear una API key gratuita en
   [OpenWeatherMap](https://home.openweathermap.org/api_keys). Puede tardar
   hasta 2 horas en activarse.
3. Agregar la key al final de `local.properties`, en la raíz del proyecto:
   ```properties
   OPENWEATHER_API_KEY=tu_api_key_aqui
   ```
4. Sincronizar Gradle y ejecutar con Run.

La key se lee de `local.properties` (excluido por `.gitignore`) y llega al
código como `BuildConfig.OPENWEATHER_API_KEY`, así que nunca se sube al
repositorio.

## Autor

Juan Bejarano, 2026. Proyecto con fines educativos.
