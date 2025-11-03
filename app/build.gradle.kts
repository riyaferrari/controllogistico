// Asegúrate de que tienes esto en la sección 'dependencies'

dependencies {
    // Dependencias básicas de Compose
    implementation(platform("androidx.compose:compose-bom:2024.04.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // Dependencias de Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // Dependencias de ViewModel y LiveData (para gestión de estados)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    // Iconos extendidos para una mejor UI
    implementation("androidx.compose.material:material-icons-extended:1.6.6")

    // Dependencias para Coroutines (gestión de asincronía)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    // Para las pruebas, puedes dejarlas por defecto
    // testImplementation(...)
    // androidTestImplementation(...)