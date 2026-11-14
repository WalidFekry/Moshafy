# ProGuard / R8 Rules for Moshafy App

# Preserve line numbers for Crashlytics stack traces
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Gson & Data Models
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
-keep class com.appwalied.quran.notifications_messages.model.** { *; }
-keep class com.appwalied.quran.quran_listening.model.** { *; }
-keep class com.appwalied.quran.quran.db.models.** { *; }
-keep class com.appwalied.quran.quran.model.** { *; }
-keep class com.appwalied.quran.quran.room.** { *; }
-keep class com.appwalied.quran.azkar.** { *; }

# Retrofit
-keepclassmembers,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }

# Room Database
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**

# Media3 & ExoPlayer
-keep class androidx.media3.** { *; }
-dontwarn androidx.media3.**

# SQLite Asset Helper
-keep class com.readystatesoftware.sqliteasset.** { *; }

# ViewBinding
-keep class * implements androidx.viewbinding.ViewBinding { *; }
