# AniLib

## 1) Multimodule App:
  - app
  - core
    - db 
    - designsystem
    - navigation
    - network
    - utils
    - widget
  - feature
    - home
      - api
      - impl  
    - favorites
      - api
      - impl
    - profile
      - api
      - impl
    - auth
      - api
      - impl
    
## 2) API: [ShikimoriAPI](https://shikimori.one/api/doc)

## 3) Authentication via [Shikimori OAuth2](https://shikimori.one/oauth)

## 4) Presentation Layer
  - JetpackCompose
  - MVI
  - Navigation: [Voyager](https://voyager.adriel.cafe/)
  - Coil

## 5) DI: Koin

## 6) DB: Room

## 7) Network: OkHttp + Retrofit + Kotlin Serialization 

## 8) CI/CD: GitHub Actions
  - detekt
  - unit-tests
  - app distribution via Firebase App Distribution

## 9) Firebase:
  - Analytics
  - Crashlytics
  - Perfomance
  - Notifications

## 10) Unit-tests on usecases

## 11) Dependency versioning via .toml file

## 12) Additional:
  - Paging 3
  - Chrome Custom Tabs
  - Charts
  - Dark and light themes
  - Datastore Preferences

## PS: возможные проблемы/недочёты
  - нет @Preview (не приучил себя писать @Preview функции)
  - возможны краши из-за null полей в response'ах API, не уверен, что все обработал, т.к. в документации нет никакой информации о том, какого типа поля у response'ов
  - возможны проблемы с OAuth2, а именно с обновлением access токена (трудно тестировать, т.к. токен становится невалидным только через сутки)
  - так и не пофиксил "проблему" с TabNavigator'ом (выход из приложения при onBackPress)