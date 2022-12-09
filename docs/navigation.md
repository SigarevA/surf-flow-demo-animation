# Навигация в приложении

Приложение построено с использованием подхода Single Activity Architecture. Все ui-компоненты реализованы с помощью `@Composable`-функций (кроме [снекбара](alerter.md)).
Навигация реализована с помощью библиотеки [compose-destinations][lib], которая основна на стандартной навигации от Google из [jetpack][jetpack],
при этом библиотека добавляет удобство использования и type-safety при парсинге аргументов с помощью кодогенерации.

Библиотека имеет исчерпывающую документацию и примеры, здесь перечислим основные концепции, применимые в нашем приложении.

## Настройка

Каждый модуль, использующий навигацию, должен иметь следующие настройки:

```
// build.gradle.kts
plugins {
    alias(libs.plugins.ksp.plugin)
}

val kspKotlinConfig: Any.() -> Unit by project.extra

android {
    ksp {
        arg("compose-destinations.mode", "destinations")
        arg("compose-destinations.moduleName", "yourModuleName")
    }
}

kotlin {
    // make sure the IDE looks at the generated folder
    this.kspKotlinConfig()
}

dependencies {
    implementation(libs.compose.destinations.core)
    ksp(libs.compose.destinations.ksp)
}
```

Далее можно описывать экран:
```
@Destination
@Composable
fun MyScreen() {
}
```

Собрать приложение или выполнить `./gradlew kspDebugKotlin` для кодогенерации.
Станет доступен полученный `MyScreenDestination`, который используется для навигации к экрану.

В конфигурации используется `compose-destinations.mode` со значением `destinations` без генерации графов, которые настраиваются вручную.

`ru.surfstudio.android.demo.NavGraphs` определяет вложенные графы каждого таба приложения при использовании `BottomNavigation`.

## Использование

**Важно**: для корректной навигации на новый экран важно добавить его в граф, показав тем самым, из какого вложенного графа он будет достижим,
иначе будет ошибка в рантайме. Например, если `MyScreen` можно открыть с таба корзины:
```
  val basket = object : NavGraphSpec {
        override val route: String = "basket"
        override val startRoute: Route = BasketScreenDestination routedIn this
        override val destinationsByRoute: Map<String, DestinationSpec<*>> =
            listOf(
                BasketScreenDestination,
                MyScreenDestination // new screen
            )
                .routedIn(this)
                .associateBy { it.route }
    }
```

`ru.surfstudio.android.demo.MainScreen` описывает `DestinationsNavHost` всего приложения.

В большинстве случаев `MyScreen` не нужно добавлять в `DestinationsNavHost` - если он уже добавлен в `NavGraphs`, то навигация к нему будет работать.
Однако если `MyScreen` имеет параметры, которые библиотека не сможет сгенерировать, их нужно описать вручную:
```
@Destination
@Composable
fun MyScreenWithParams(onClick: () -> Unit) {
}
// MainScreen
DestinationsNavHost(
    engine = rememberNavHostEngine(),
    navController = navController,
    navGraph = NavGraphs.root
) {
    composable(MyScreenWithParamsDestination) {
        MyScreenWithParams(onClick = {})
    }
}
```

Таким образом, всю навигацию с помощью callbacks можно определить в MainScreen, не передавая `navController` по иерархии.

Можно предоставить зависимости в `Destination` с помощью `dependenciesContainerBuilder` (см `AppNavHost`),
таким образом поставляется интерфейс навигации `AppNavigation`, который является общим интерфейсом навигации всех модулей приложения.
Каждый экран предоставляет его собственный интерфейс навигации, например `FeedScreenNavigation`,
который станет частью интерфейса навигации модуля, например `FeedNavigation`.
Реализацию навигации следует делать в модуле `app`, так как он подключает все модули приложения и имеет доступ к любому объекту `Destination`, куда требуется навигация.

### Навигация без навбара

Для экранов, при которых навбар не показывается, реализован фильтр с помощью их роутов:

```
@Destination(
    route = "${fullScreenNavigationKey}Category"
)
@Composable
fun FullCategoryScreen(name: String) {
    CategoryBody(name = name)
}

@Destination
@Composable
fun CategoryScreen(name: String) {
    CategoryBody(name = name)
}

// MainState
val shouldShowBottomBar: Boolean
    @Composable get() {
        val currentRoute = navController
            .currentBackStackEntryAsState().value?.destination?.route.orEmpty()
        return !currentRoute.contains(fullScreenNavigationKey)
    }
```

Таким образом, экран категории имеет две конфигурации (полноэкранную или в рамках навбара), каждую можно вызывать при необходимости, описав в графе.

### Возврат результата

В библиотеке есть `ResultRecipient` и `ResultBackNavigator` для подписки на результат экрана.

Также реализован подход с `SharedResultBus` для возврата любого результата (подписка в middleware).

Начиная с версии `1.5.9-beta`, библиотека реализует `OpenResultRecipient`, позволяющий подписываться на результаты экранов разных модулей
([описание][issue])

### Tips

* `destinationsNavigator` можно использовать наравне со стандартным `navController` (он является оберткой),
однако у `destinationsNavigator` есть преимущество - флаг `onlyIfResumed`, который решает проблему множественных
нажатий на кнпоку и открытия лишних экранов, пример в `MainScreen`.
* `RootBackHandler` используется для каждого корневого экрана вложенного `NavGraph` для того,
чтобы со стартовой страницы каждого таба `BottomBar` можно было закрыть приложение (здесь же может быть добавлена фича
press again to exit app). Здесь же решается [проблема](https://issuetracker.google.com/issues/201900574)
(по умолчанию происходит переход на первый таб, при этом при возврате на отдельный таб он пересоздается и запросы на нем выполняются заново).
* `RootBackHandler(navController)` используется для корневого экрана, где показывается bottom sheet,
чтобы для `onBackPressed` можно было закрыть bottomSheet (его роут должен содержать `bottomSheetNavigationKey`)

[lib]: https://github.com/raamcosta/compose-destinations
[jetpack]: https://developer.android.com/jetpack/compose/navigation
[issue]: https://github.com/raamcosta/compose-destinations/issues/70