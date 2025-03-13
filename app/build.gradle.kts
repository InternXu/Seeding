plugins {
    alias(libs.plugins.android.application)
    kotlin("android")
    kotlin("kapt")
    id("com.google.devtools.ksp")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.seeding"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.seeding"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        
        // Room相关配置
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
            arg("room.incremental", "true")
            arg("room.expandProjection", "true")
        }
        
        // 矢量图兼容性
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    
    kotlinOptions {
        jvmTarget = "17"
    }
    
    buildFeatures {
        viewBinding = true
        compose = true
    }
    
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.6"
    }
    
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // 核心库
    implementation(libs.androidx.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    
    // Jetpack Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.vision.internal.vkp)
    debugImplementation(libs.androidx.compose.ui.tooling)
    
    // Lifecycle组件
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    
    // Navigation组件
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    implementation(libs.androidx.navigation.compose)
    
    // Room数据库
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    
    // Retrofit网络请求
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
    
    // WorkManager
    implementation(libs.androidx.work.runtime.ktx)
    
    // DataStore存储
    implementation(libs.androidx.datastore.preferences)
    
    // Hilt依赖注入
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    
    // Coil图片加载
    implementation(libs.coil.compose)
    
    // 测试相关
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    
    // 调试工具
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    implementation("androidx.compose.material:material-icons-extended:1.6.0")
}