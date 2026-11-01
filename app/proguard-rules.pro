# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# ---------------------------------------------------------------------------
# General Android & Debugging Stacktrace Rules
# ---------------------------------------------------------------------------
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# ---------------------------------------------------------------------------
# Retrofit & OkHttp
# ---------------------------------------------------------------------------
-keepattributes Exceptions
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

# ---------------------------------------------------------------------------
# Gson & Network DTOs / Domain Models
# ---------------------------------------------------------------------------
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
    @com.google.gson.annotations.Expose <fields>;
}
-keep class com.google.gson.** { *; }
-dontwarn com.google.gson.**

# Prevent obfuscation of DTOs and Domain Models used in Gson serialization
-keep class com.turkoglu.moviecomposeapp.data.remote.dto.** { *; }
-keep class com.turkoglu.moviecomposeapp.domain.model.** { *; }

# ---------------------------------------------------------------------------
# Room Database
# ---------------------------------------------------------------------------
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao interface * { *; }
-keepclassmembers class * {
    @androidx.room.TypeConverter *;
}
-dontwarn androidx.room.paging.**

# ---------------------------------------------------------------------------
# Dagger & Hilt
# ---------------------------------------------------------------------------
-dontwarn dagger.hilt.**
-keep class dagger.hilt.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper
-keep class * extends androidx.hilt.work.HiltWorker
-keepclassmembers class * {
    @javax.inject.Inject <init>(...);
    @javax.inject.Inject <fields>;
    @javax.inject.Inject <methods>;
}
-keep class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}

# ---------------------------------------------------------------------------
# Kotlin Coroutines
# ---------------------------------------------------------------------------
-dontwarn kotlinx.coroutines.**
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# ---------------------------------------------------------------------------
# Coil Image Loader
# ---------------------------------------------------------------------------
-dontwarn coil.**
-keep class coil.** { *; }
