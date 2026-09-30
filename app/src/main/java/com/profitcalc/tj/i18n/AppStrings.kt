package com.profitcalc.tj.i18n

/**
 * All user-facing text in the app, typed and centralized.
 *
 * Design note: Android's resource-qualifier locale system (values-ru/strings.xml)
 * requires an Activity recreation (or the per-app-language API, which only exists
 * from API 33) to switch languages at runtime. Since this app must switch language
 * instantly from Settings on minSdk 24 devices without a jarring recreate, UI text
 * is driven from this in-memory model instead, selected by [AppLanguage]. The
 * values-ru/strings.xml file still exists for the OS-level app label.
 */
data class AppStrings(
    // Navigation / screen titles
    val navDashboard: String,
    val navCalculator: String,
    val navBreakEven: String,
    val navDiscount: String,
    val navMaxDiscount: String,
    val navRoas: String,
    val navQuickCalc: String,
    val navHistory: String,
    val navSavedProducts: String,
    val navSettings: String,

    // App
    val appNameTj: String,
    val appTagline: String,

    // Dashboard
    val dashboardTodayProfit: String,
    val dashboardTodayCalculations: String,
    val dashboardSavedProducts: String,
    val dashboardAverageMargin: String,
    val dashboardNoData: String,
    val dashboardQuickActions: String,

    // Calculator inputs
    val inputProductName: String,
    val inputPurchasePrice: String,
    val inputSalePrice: String,
    val inputQuantity: String,
    val inputCommission: String,
    val inputLogistics: String,
    val inputPackaging: String,
    val inputAdvertising: String,
    val inputDiscount: String,
    val inputTax: String,
    val inputOtherCosts: String,
    val adModeTotal: String,
    val adModePerItem: String,

    // Results
    val resultProfitPerItem: String,
    val resultTotalProfit: String,
    val resultRevenue: String,
    val resultTotalCost: String,
    val resultTotalCommission: String,
    val resultMargin: String,
    val resultRoi: String,
    val resultFinalSalePrice: String,
    val resultDiscountAmount: String,
    val sectionResults: String,
    val sectionBreakdown: String,

    // Break-even
    val breakEvenTitle: String,
    val breakEvenMinPrice: String,
    val breakEvenProfitAtCurrentPrice: String,
    val breakEvenNotAchievable: String,

    // Target profit
    val targetProfitTitle: String,
    val targetProfitInput: String,
    val targetProfitRequiredPriceLabel: String,
    val targetProfitResultTemplate: String, // "%s" placeholder for price

    // Discount calculator
    val discountTitle: String,
    val discountOriginalPrice: String,
    val discountPercent: String,
    val discountFinalPrice: String,

    // Max discount
    val maxDiscountTitle: String,
    val maxDiscountMinProfit: String,
    val maxDiscountResultLabel: String,
    val maxDiscountNotAchievable: String,

    // ROAS
    val roasTitle: String,
    val roasSpend: String,
    val roasRevenue: String,
    val roasResultLabel: String,
    val roasExplanationTemplate: String, // "%s" placeholder for multiplier

    // Quick calculator
    val quickCalcTitle: String,
    val quickCalcClear: String,

    // History
    val historyTitle: String,
    val historyEmpty: String,
    val historyClearAll: String,
    val historyClearConfirmTitle: String,
    val historyClearConfirmMessage: String,
    val historyDelete: String,
    val historyDuplicate: String,
    val historyEdit: String,
    val historyRecalculate: String,
    val historyDetailsTitle: String,

    // Saved products
    val savedProductsTitle: String,
    val savedProductsEmpty: String,
    val savedProductsAdd: String,
    val savedProductsSave: String,
    val savedProductsDelete: String,
    val savedProductsLoad: String,

    // Settings
    val settingsTitle: String,
    val settingsLanguage: String,
    val settingsTheme: String,
    val settingsThemeLight: String,
    val settingsThemeDark: String,
    val settingsThemeSystem: String,
    val settingsCurrency: String,
    val settingsDecimalPlaces: String,
    val settingsClearHistory: String,
    val settingsAbout: String,
    val settingsAboutBody: String,
    val settingsVersion: String,

    // Export / share
    val exportCsv: String,
    val exportTxt: String,
    val shareResult: String,
    val exportSuccess: String,

    // Validation
    val validationNegative: String,
    val validationCommissionRange: String,
    val validationDiscountRange: String,
    val validationQuantityMin: String,
    val validationRequired: String,

    // Common buttons
    val actionCalculate: String,
    val actionSave: String,
    val actionCancel: String,
    val actionDelete: String,
    val actionConfirm: String,
    val actionClose: String,
    val currencySuffix: String,
)

val TjStrings = AppStrings(
    navDashboard = "Асосӣ",
    navCalculator = "Ҳисобкунак",
    navBreakEven = "Бе зарар",
    navDiscount = "Скидка",
    navMaxDiscount = "Макс. скидка",
    navRoas = "ROAS",
    navQuickCalc = "Калькулятор",
    navHistory = "Таърих",
    navSavedProducts = "Товарҳо",
    navSettings = "Танзимот",

    appNameTj = "Ҳисобкунаки Фоида",
    appTagline = "Офлайн ҳисобкунаки фоидаи фурӯш",

    dashboardTodayProfit = "Фоидаи имрӯз",
    dashboardTodayCalculations = "Ҳисобҳои имрӯз",
    dashboardSavedProducts = "Товарҳои сабтшуда",
    dashboardAverageMargin = "Марҷи миёна",
    dashboardNoData = "Ҳоло маълумот нест",
    dashboardQuickActions = "Амалҳои зуд",

    inputProductName = "Номи товар",
    inputPurchasePrice = "Нарх / себестоимость",
    inputSalePrice = "Нархи фурӯш",
    inputQuantity = "Миқдор",
    inputCommission = "Комиссия (%)",
    inputLogistics = "Логистика",
    inputPackaging = "Упаковка",
    inputAdvertising = "Реклама",
    inputDiscount = "Скидка (%)",
    inputTax = "Андоз / дигар хароҷот (%)",
    inputOtherCosts = "Хароҷоти иловагӣ",
    adModeTotal = "Маблағи умумӣ",
    adModePerItem = "Ба як дона",

    resultProfitPerItem = "Фоида барои як дона",
    resultTotalProfit = "Фоидаи умумӣ",
    resultRevenue = "Даромад",
    resultTotalCost = "Хароҷоти умумӣ",
    resultTotalCommission = "Комиссияи умумӣ",
    resultMargin = "Марҷ",
    resultRoi = "ROI",
    resultFinalSalePrice = "Нархи ниҳоӣ",
    resultDiscountAmount = "Маблағи скидка",
    sectionResults = "Натиҷаҳо",
    sectionBreakdown = "Тафсилот",

    breakEvenTitle = "Нуқтаи бе зарар",
    breakEvenMinPrice = "Ҳадди ақали нархи фурӯш",
    breakEvenProfitAtCurrentPrice = "Фоида дар нархи ҷорӣ",
    breakEvenNotAchievable = "Бо ин хароҷот бе зарар расидан ғайриимкон аст",

    targetProfitTitle = "Фоидаи мақсаднок",
    targetProfitInput = "Фоидаи мақсаднок (умумӣ)",
    targetProfitRequiredPriceLabel = "Нархи зарурии фурӯш",
    targetProfitResultTemplate = "Барои гирифтани фоидаи мақсаднок, нархи фурӯш бояд %s бошад.",

    discountTitle = "Скидка",
    discountOriginalPrice = "Нархи аслӣ",
    discountPercent = "Скидка (%)",
    discountFinalPrice = "Нархи баъди скидка",

    maxDiscountTitle = "Максималии скидка",
    maxDiscountMinProfit = "Фоидаи ҳадди ақали дилхоҳ",
    maxDiscountResultLabel = "Скидкаи максималӣ",
    maxDiscountNotAchievable = "Бо ин параметрҳо скидка имконнопазир аст",

    roasTitle = "ROAS",
    roasSpend = "Хароҷоти реклама",
    roasRevenue = "Даромад",
    roasResultLabel = "ROAS",
    roasExplanationTemplate = "ROAS %s яъне барои ҳар 1 сомонии реклама %s сомонӣ фурӯш.",

    quickCalcTitle = "Калькулятори оддӣ",
    quickCalcClear = "Тоза кардан",

    historyTitle = "Таърих",
    historyEmpty = "Таърих холист",
    historyClearAll = "Пок кардани ҳама",
    historyClearConfirmTitle = "Пок кардани таърих?",
    historyClearConfirmMessage = "Ҳама ҳисобҳои сабтшуда бебозгашт нест мешаванд.",
    historyDelete = "Нест кардан",
    historyDuplicate = "Нусхабардорӣ",
    historyEdit = "Таҳрир",
    historyRecalculate = "Аз нав ҳисоб кардан",
    historyDetailsTitle = "Тафсилоти ҳисоб",

    savedProductsTitle = "Товарҳои сабтшуда",
    savedProductsEmpty = "Ҳоло товаре сабт нашудааст",
    savedProductsAdd = "Илова кардани товар",
    savedProductsSave = "Сабт кардан",
    savedProductsDelete = "Нест кардан",
    savedProductsLoad = "Истифода бурдан",

    settingsTitle = "Танзимот",
    settingsLanguage = "Забон",
    settingsTheme = "Мавзӯъ",
    settingsThemeLight = "Равшан",
    settingsThemeDark = "Торик",
    settingsThemeSystem = "Системавӣ",
    settingsCurrency = "Асъор",
    settingsDecimalPlaces = "Рақамҳои даҳдарҷа",
    settingsClearHistory = "Пок кардани таърих",
    settingsAbout = "Дар бораи барнома",
    settingsAboutBody = "Ҳисобкунаки офлайнии фоида барои фурӯшандаҳо ва менеджерони маркетплейс. Ҳеҷ маълумот ба сервер фиристода намешавад.",
    settingsVersion = "Версия",

    exportCsv = "Содирот CSV",
    exportTxt = "Содирот TXT",
    shareResult = "Мубодилаи натиҷа",
    exportSuccess = "Файл захира шуд",

    validationNegative = "Рақами манфӣ иҷозат дода намешавад",
    validationCommissionRange = "Комиссия бояд аз 0 то 100% бошад",
    validationDiscountRange = "Скидка бояд аз 0 то 100% бошад",
    validationQuantityMin = "Миқдор бояд ҳадди ақал 1 бошад",
    validationRequired = "Ин майдон ҳатмист",

    actionCalculate = "Ҳисоб кардан",
    actionSave = "Сабт",
    actionCancel = "Бекор кардан",
    actionDelete = "Нест кардан",
    actionConfirm = "Тасдиқ",
    actionClose = "Пӯшидан",
    currencySuffix = "сомонӣ",
)

val RuStrings = AppStrings(
    navDashboard = "Главная",
    navCalculator = "Калькулятор",
    navBreakEven = "Безубыточность",
    navDiscount = "Скидка",
    navMaxDiscount = "Макс. скидка",
    navRoas = "ROAS",
    navQuickCalc = "Калькулятор",
    navHistory = "История",
    navSavedProducts = "Товары",
    navSettings = "Настройки",

    appNameTj = "Калькулятор Прибыли",
    appTagline = "Офлайн калькулятор прибыли от продаж",

    dashboardTodayProfit = "Прибыль сегодня",
    dashboardTodayCalculations = "Расчётов сегодня",
    dashboardSavedProducts = "Сохранённые товары",
    dashboardAverageMargin = "Средняя маржа",
    dashboardNoData = "Пока нет данных",
    dashboardQuickActions = "Быстрые действия",

    inputProductName = "Название товара",
    inputPurchasePrice = "Цена / себестоимость",
    inputSalePrice = "Цена продажи",
    inputQuantity = "Количество",
    inputCommission = "Комиссия (%)",
    inputLogistics = "Логистика",
    inputPackaging = "Упаковка",
    inputAdvertising = "Реклама",
    inputDiscount = "Скидка (%)",
    inputTax = "Налог / прочие расходы (%)",
    inputOtherCosts = "Доп. расходы",
    adModeTotal = "Общая сумма",
    adModePerItem = "За единицу",

    resultProfitPerItem = "Прибыль за единицу",
    resultTotalProfit = "Общая прибыль",
    resultRevenue = "Выручка",
    resultTotalCost = "Общие расходы",
    resultTotalCommission = "Общая комиссия",
    resultMargin = "Маржа",
    resultRoi = "ROI",
    resultFinalSalePrice = "Итоговая цена",
    resultDiscountAmount = "Сумма скидки",
    sectionResults = "Результаты",
    sectionBreakdown = "Детализация",

    breakEvenTitle = "Точка безубыточности",
    breakEvenMinPrice = "Минимальная цена продажи",
    breakEvenProfitAtCurrentPrice = "Прибыль при текущей цене",
    breakEvenNotAchievable = "При таких расходах выйти в безубыток невозможно",

    targetProfitTitle = "Целевая прибыль",
    targetProfitInput = "Целевая прибыль (общая)",
    targetProfitRequiredPriceLabel = "Необходимая цена продажи",
    targetProfitResultTemplate = "Для получения целевой прибыли цена продажи должна быть %s.",

    discountTitle = "Скидка",
    discountOriginalPrice = "Исходная цена",
    discountPercent = "Скидка (%)",
    discountFinalPrice = "Цена после скидки",

    maxDiscountTitle = "Максимальная скидка",
    maxDiscountMinProfit = "Желаемая минимальная прибыль",
    maxDiscountResultLabel = "Максимальная скидка",
    maxDiscountNotAchievable = "При этих параметрах скидка недостижима",

    roasTitle = "ROAS",
    roasSpend = "Расходы на рекламу",
    roasRevenue = "Выручка",
    roasResultLabel = "ROAS",
    roasExplanationTemplate = "ROAS %s значит на каждый 1 сомони рекламы приходится %s сомони продаж.",

    quickCalcTitle = "Простой калькулятор",
    quickCalcClear = "Очистить",

    historyTitle = "История",
    historyEmpty = "История пуста",
    historyClearAll = "Очистить всё",
    historyClearConfirmTitle = "Очистить историю?",
    historyClearConfirmMessage = "Все сохранённые расчёты будут безвозвратно удалены.",
    historyDelete = "Удалить",
    historyDuplicate = "Дублировать",
    historyEdit = "Изменить",
    historyRecalculate = "Пересчитать",
    historyDetailsTitle = "Детали расчёта",

    savedProductsTitle = "Сохранённые товары",
    savedProductsEmpty = "Пока нет сохранённых товаров",
    savedProductsAdd = "Добавить товар",
    savedProductsSave = "Сохранить",
    savedProductsDelete = "Удалить",
    savedProductsLoad = "Использовать",

    settingsTitle = "Настройки",
    settingsLanguage = "Язык",
    settingsTheme = "Тема",
    settingsThemeLight = "Светлая",
    settingsThemeDark = "Тёмная",
    settingsThemeSystem = "Системная",
    settingsCurrency = "Валюта",
    settingsDecimalPlaces = "Знаков после запятой",
    settingsClearHistory = "Очистить историю",
    settingsAbout = "О приложении",
    settingsAboutBody = "Офлайн калькулятор прибыли для продавцов и менеджеров маркетплейсов. Данные никуда не отправляются.",
    settingsVersion = "Версия",

    exportCsv = "Экспорт CSV",
    exportTxt = "Экспорт TXT",
    shareResult = "Поделиться результатом",
    exportSuccess = "Файл сохранён",

    validationNegative = "Отрицательное число не допускается",
    validationCommissionRange = "Комиссия должна быть от 0 до 100%",
    validationDiscountRange = "Скидка должна быть от 0 до 100%",
    validationQuantityMin = "Количество должно быть не менее 1",
    validationRequired = "Это поле обязательно",

    actionCalculate = "Рассчитать",
    actionSave = "Сохранить",
    actionCancel = "Отмена",
    actionDelete = "Удалить",
    actionConfirm = "Подтвердить",
    actionClose = "Закрыть",
    currencySuffix = "сомони",
)
