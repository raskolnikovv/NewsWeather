# NewsWeather
Aplicativo Android que combina manchetes de notícias em tempo real com informações meteorológicas locais baseadas na localização atual do usuário.

https://github.com/user-attachments/assets/3133d47a-1721-4603-927e-75d0f18ff30f

## Tech Stack
- **UI:** Jetpack Compose com Material 3.
- **Arquitetura:** MVVM + Princípios de Clean Architecture.
- **DI:** Hilt (Dagger).
- **Rede:** Retrofit + GSON.
- **Localização:** Google Play Services Location (FusedLocationProvider).
- **Assincronia:** Kotlin Coroutines & Flow.

## Principais Funcionalidades
- **Notícias Dinâmicas:** Busca as últimas manchetes através da News API.
- **Clima Baseado em Localização:** Detecta automaticamente a cidade do usuário via Geocodificação Reversa e busca os dados climáticos na Open-Meteo API.
- **Favoritos:** Persistência de dados para salvar artigos de notícias (DataStore/Room).
- **UI Responsiva:** Implementação de *pull-to-refresh* e estados de carregamento com *shimmer*.

## Como funciona
1. Ao iniciar, o aplicativo solicita as permissões de localização.
2. Uma vez concedidas, o app recupera as coordenadas atuais e as converte no nome da cidade correspondente.
3. A interface é atualizada simultaneamente com as notícias recentes e as condições climáticas específicas da região.
