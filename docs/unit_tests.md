# Unit tests

[Старая инструкция](https://github.com/surfstudio/SurfAndroidStandard/blob/main/docs/common/unit_testing.md)

Модуль `test-utils` содержит основные утилиты для написания unit-тестов.

`BaseMviScreenTest` - базовый класс тестирования экранов на mvi, позволяющий тестировать `Reducer` и `Middleware` как вместе, так и по отдельности,
таким образом позволяя покрыть основные сценарии бизнес-логики экрана. Тестируя экран, мы можем проверять его `state` или текущий ожидаемый `event` последовательно.

`ViewModel` экрана - основной компонент, участвующий в тестировании, с помощью которого можно получить доступ к `reducer` и `middleware` экрана для гибкого тестирования.

## Основные правила

* Использовать интерфейсы `Producer` всех сущностей при тестировании, чтобы они создавались заново для каждого теста, обеспечивая их независимость.
* Использовать моки для сущностей, необходимых при создании middleware или reducer, при этом моки можно гибко настраивать для того, что требуется в конкретном тесте:

```
// Мок BasketInteractor, где getBasketCount возвращает ожидаемое значение, остальные функции не используются
private val succeedBasketInteractor = mockk<BasketInteractor> {
    coEvery { getBasketCount() } returns testBasketCount
}

mockkConstructor(SessionChangedInteractor::class)
every { anyConstructed<SessionChangedInteractor>().insertTokens(any()) } returns Unit
// Вызов реального конструктора с созданными ранее параметрами, однако нужный нам метод замокан ранее
sessionChangedInteractor = SessionChangedInteractor(db, testDispatcher)
```

* Использовать `runTimeoutTest` вместо `runTest` для того, чтобы `dispatchTimeoutMs` был одинаков для всего тестового окружения.
* В конструкторах использовать интерфейсы вместо конкретных реализаций для удобной замены в тестовом окружении:

```
class MainMiddleware @Inject constructor(
    private val messageController: IconMessageController
)

// main app
@Provides
fun provideMessageController(): IconMessageController {
    return TopSnackIconMessageController(AppStatics.activityHolder)
}

// tests
private val middlewareProducer = MiddlewareProducer {
    MainMiddleware(TestMessageController())
}
```

* `Scope` и `CoroutineDispatcher` тоже должны быть предоставлены в конструкторе для удобной замены в тестах.
* `@ScreenLoadOnStart` - хак, который используется для удобного тестирования экранов, где запрос делается при их старте:

```
class MainMiddleware @Inject constructor(
    // true for main app
    @ScreenLoadOnStart private val loadOnStart: Boolean
) : MapperFlowMiddleware<MainEvent> {

    override fun transform(eventStream: Flow<MainEvent>): Flow<MainEvent> {
        return eventStream.transformations {
            addAll(
                if (loadOnStart) getBasketCount() else skip()
            )
        }
    }
}

// tests
private val middlewareProducer = CustomMiddlewareProducer { loadOnStart ->
    MainMiddleware(loadOnStart)
}
```

Таким образом можно гибко настроить тестовое окружение экрана, чтобы при создании middleware не было лишних эмитов, и стейт оставался неизменным.
Если в тестовом окружении вызвать `MainMiddleware(loadOnStart = true)`, то полностью протестировать флоу первого события не получится, так как конструктор и эмит события вызовутся раньше
подписки на результат `runTimeoutTest`, таким образом, при подписке поведение может отличаться (например, observe сразу вернет результат запроса без промежуточного loading-стейта).
TODO предложить более удачное решение проблемы тестирования запроса при старте экрана