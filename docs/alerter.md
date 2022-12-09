# Alerter

`ru.surfstudio.android.demo.core.snackbar`

Вынесен [старый подход](https://github.com/surfstudio/SurfAndroidStandard/tree/dev/G-0.5.0/template/v-message-controller-top) для показа снекбара в приложении,
так как с помощью него можно инжектить `TopSnackIconMessageController` в `Activity` или `ViewModel`, при этом логика показа снека описана централизовано.

Показанный снек привязан к текущей активити, при подходе Single Activity Architecture это позволяет нам показать снек без привязки к конкретному экрану, основанному на `@Composable`,
а также можно будет вынести общую логику показа снеков для всего приложения при серверных ошибках, когда их обработка стандартна.