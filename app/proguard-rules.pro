# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/agrahari/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-optimizationpasses 5


##-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-verbose
######### retrofit
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions


#################### picasso
-dontwarn com.squareup.okhttp.**

-dontwarn okio.**


#################butterknife
# Retain generated class which implement Unbinder.

-keep public class * implements butterknife.Unbinder { public <init>(**, android.view.View); }



# Prevent obfuscation of types which use ButterKnife annotations since the simple name

# is used to reflectively look up the generated ViewBinding.

-keep class butterknife.*

-keepclasseswithmembernames class * { @butterknife.* <methods>; }

-keepclasseswithmembernames class * { @butterknife.* <fields>; }



################timber
-dontwarn org.jetbrains.annotations.**

-dontwarn io.branch.**

############# razorpay-android-sample-app
-keepclassmembers class * {@android.webkit.JavascriptInterface <methods>;}
-keepattributes JavascriptInterface
-keepattributes *Annotation*
-dontwarn com.razorpay.**
-keep class com.razorpay.** {*;}
-optimizations !method/inlining/*
-keepclasseswithmembers class * {
  public void onPayment*(...);
}


########## Gson files

-keep class com.braingroom.user.model.** {*;}

-dontwarn com.crashlytics.android.answers.shim.**

-keep class com.google.firebase.**{*;}


-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
#####


-keep class com.facebook.applinks.** { *; }
-keepclassmembers class com.facebook.applinks.** { *; }
-keep class com.facebook.FacebookSdk { *; }
-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}
-keep class com.google.android.gms.ads.identifier.** { *; }
-printmapping mapping.txt
