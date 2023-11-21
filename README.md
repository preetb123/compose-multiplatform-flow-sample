This is a Kotlin Multiplatform project targeting Android, iOS.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CryptoKit for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform, 
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.


Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…


This app demonstrates use of Kotlin Coroutines & flow for asynchronous updates processing and list mutations.
App works on both Android & iOS with single codebase
Code can be found here composeApp/src/commonMain/kotlin/App.kt

https://github.com/preetb123/compose-multiplatform-flow-sample/assets/4496555/db3cf645-2b09-48ae-a88e-1be3d7c12475

