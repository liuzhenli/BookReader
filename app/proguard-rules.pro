# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

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

#---------glide--------
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
# for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule
#---------glide--------


#---------retrofit------
# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>
#---------retrofit------

#---------okhttp------
# JSR 305 annotations are for embedding nullability information.
-dontwarn javax.annotation.**

# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*

# OkHttp platform used only on JVM and when Conscrypt dependency is available.
-dontwarn okhttp3.internal.platform.ConscryptPlatform
#---------okhttp------

-keep class **_FragmentFinder { *; }
-keep class com.qmuiteam.qmui.arch.record.** { *; }
-keep class androidx.fragment.app.* { *; }

-keep class com.lljjcoder.**{
	*;
}

-dontwarn demo.**
-keep class demo.**{*;}
-dontwarn net.sourceforge.pinyin4j.**
-keep class net.sourceforge.pinyin4j.**{*;}
-keep class net.sourceforge.pinyin4j.format.**{*;}
-keep class net.sourceforge.pinyin4j.format.exception.**{*;}

-keep class com.alibaba.sdk.android.oss.** { *; }
-dontwarn okio.**
-dontwarn org.apache.commons.codec.binary.**

#umeng
-keep class com.umeng.** {*;}

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep public class cn.solist.level.R$*{
 public static final int *;
 }

 #所有native的方法不能去混淆.
 -keepclasseswithmembernames class * {
     native <methods>;
 }
 -keepclassmembers class * {
 *** *(android.content.Context);
 }

 -keep class org.greenrobot.eventbus.**{*;}
 #eventBus
 -keepattributes *Annotation*
 -keepclassmembers class ** {
     @org.greenrobot.eventbus.Subscribe <methods>;
 }
 -keep enum org.greenrobot.eventbus.ThreadMode { *; }
 -keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
     <init>(java.lang.Throwable);
 }

 #==================gson && protobuf==========================
 -dontwarn com.google.**
 -keep class com.google.gson.** {*;}
 -keep class com.google.protobuf.** {*;}


 ## butterknife start
 -keep class butterknife.** { *; }
 -dontwarn butterknife.internal.**
 -keep class **$$ViewBinder { *; }
 -keepclasseswithmembernames class * {
     @butterknife.* <fields>;
 }
 -keepclasseswithmembernames class * {
     @butterknife.* <methods>;
 }
 ## butterknife end

 # 保留androidx下的所有类及其内部类
 -keep class androidx.** {*;}
 # 保留继承的
 -keep public class * extends androidx.**
 -keep class androidx.core.app.CoreComponentFactory

 #glide
 -keep public class * implements com.bumptech.glide.module.GlideModule
 -keep public class * extends com.bumptech.glide.module.AppGlideModule
 -keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
   **[] $VALUES;
   public *;
 }


 #rxjava
 -dontwarn rx.**
 -keep class rx.** { *; }

 -dontwarn sun.misc.**
 -keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
  long producerIndex;
  long consumerIndex;
 }
 -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
  rx.internal.util.atomic.LinkedQueueNode producerNode;
 }
 -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
  rx.internal.util.atomic.LinkedQueueNode consumerNode;
 }

 -keep class rx.android.** { *; }
 -keep class com.jakewharton.rxbinding.** { *; }
 -keep class javax.annotation.** { *; }
 -keep class javax.inject.** { *; }
 -keep class io.**

 -keep class okhttp3.internal.publicsuffix.PublicSuffixDatabase
 -keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
 -keep class okhttp3.**{*;}

 -keepclassmembers class * extends android.app.Activity {
 public void *(android.view.View);
 }
 -keep class * implements java.io.Serializable

 -assumenosideeffects class com.orhanobut.logger.Logger {
     public static *** v(...);
     public static *** i(...);
     public static *** d(...);
     public static *** w(...);
     public static *** e(...);
 }


-keep public class android.net.http.SslError
-keep public class android.webkit.WebViewClient

-dontwarn android.webkit.WebView
-dontwarn android.net.http.SslError
-dontwarn android.webkit.WebViewClient

-dontwarn android.support.**

-dontwarn org.apache.**
-keep class org.apache.** { *;}



-dontwarn org.jivesoftware.smack.**
-keep class org.jivesoftware.smack.** { *;}

-dontwarn com.loopj.**
-keep class com.loopj.** { *;}

-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *;}
-keep interface com.squareup.okhttp.** { *; }

#okhttp3
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-keep class okhttp3.** { *;}
-dontwarn okio.**
-dontwarn okhttp3.**

#retrofit2
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
#dragger
-keep class dagger.** { *; }

-dontwarn org.xbill.**
-keep class org.xbill.** { *;}

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

-dontwarn InnerClasses
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,LocalVariable*Table,*Annotation*,Synthetic,EnclosingMethod
-dontoptimize

#-optimizations optimization_filter
# 指定混淆是采用的算法，后面的参数是一个过滤器
# 这个过滤器是谷歌推荐的算法，一般不做更改
-optimizations !code/simplification/cast,!field/*,!class/merging/*


-keep class com.umeng.scrshot.**
-keep public class com.tencent.** {*;}
-keep class com.umeng.socialize.sensor.**
-keep class com.umeng.socialize.handler.**
-keep class com.umeng.socialize.handler.*
-keep class com.umeng.weixin.handler.**
-keep class com.umeng.weixin.handler.*
-keep class com.umeng.qq.handler.**
-keep class com.umeng.qq.handler.*
-keep class UMMoreHandler{*;}
-keep class com.tencent.mm.opensdk.modelmsg.WXMediaMessage {*;}
-keep class com.tencent.mm.opensdk.modelmsg.** implements   com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
-keep class com.tencent.mm.opensdk.** {
 *;
}

-keep class com.tencent.** {*;}
-keep class com.iflytek.** {*;}
-keep class com.liuzhenli.app.bean.** {*;}
-keep class com.liuzhenli.app.bean.AppConfigManager.{*;}
-keep class com.liuzhenli.app.utils.mananger.** {*;}
-keep class com.liuzhenli.app.utils.** {*;}
-keep class com.liuzhenli.app.network.** {*;}
-keep class com.tencent.mm.opensdk.** {
   *;
}
-keep class com.chineseall.reader.api.**{*;}

-keep class com.tencent.wxop.** {
   *;
}
-keep class com.tencent.mm.sdk.** {
   *;
}


#广点通混淆
-keep class com.qq.e.** {
    public protected *;
}

-dontwarn com.alimama.mobile.**
-dontwarn android.app.**
-dontwarn android.support.v4.**

-keep class com.taobao.** {*; }
-keep class com.alimama.**{*; }
-keep class com.alimama.mobile.**{*; }
-keep class android.taobao.** {*; }
-keep class android.support.v4.** { *; }
-keep class android.app.**{*;}
-keep class **.R$* {*;}

#360 混淆
-keep class com.ak.** {*;}
-keep class android.support.v4.**{
      public *;
}
-keep class android.support.v7.**{
        public *;
}

#vivoSDK
-keep class com.vivo.** { *; }
-dontwarn com.androidquery.auth.**



-keep class com.huawei.gamebox.plugin.gameservice.**{*;}

#OPPO AD SDK
-keep class com.oppo.** {
    public protected *;
}

-keep class com.oppo.market.** {
    public protected *;
}
#oppo支付
-keep class com.nearme.** { *; }
-keep class okio.**{ *; }
-keep class com.squareup.wire.**{ *; }
-keep public class * extends com.squareup.wire.**{ *; }
# Keep methods with Wire annotations (e.g. @ProtoField)
-keepclassmembers class ** {
    @com.squareup.wire.ProtoField public *;
    @com.squareup.wire.ProtoEnum public *;
}
-keep public class com.oppo.oaps.base.**{ *; }
-keep class com.nearme.instant.router.**{
    *;
}
-keepattributes *Annotation*
-keepattributes *JavascriptInterface*
#support-v4


# QQ
-keep class com.qq.e.** {
    public protected *;
}
-keep class android.support.v4.app.NotificationCompat**{
    public *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-printmapping build/outputs/mapping/release/mapping.txt

-keep class com.avos.avoscloud.im.v2.*{*;}

#腾讯x5内核-----
-dontwarn dalvik.**
-dontwarn com.tencent.smtt.**
-keep class com.tencent.smtt.** {
    *;
}
-keep class com.tencent.tbs.** {
    *;
}
#腾讯x5内核-----
