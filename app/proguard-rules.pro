# ProfitCalc TJ — ProGuard rules
# The app is fully offline: no reflection-heavy networking libraries are used,
# so default Android/Compose/Room consumer rules are sufficient.

-keep class com.profitcalc.tj.data.local.** { *; }
-keepattributes *Annotation*
