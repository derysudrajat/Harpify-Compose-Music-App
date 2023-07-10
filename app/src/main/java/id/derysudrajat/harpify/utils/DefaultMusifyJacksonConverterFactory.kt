package id.derysudrajat.harpify.utils

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import retrofit2.converter.jackson.JacksonConverterFactory

private val jacksonModule = kotlinModule {
    // warning: this feature has a significant performance impact.
    // Consider using it only in debug mode.
    configure(KotlinFeature.StrictNullChecks, true)
}

val defaultHarpifyJacksonConverterFactory: JacksonConverterFactory = JacksonConverterFactory.create(
    jsonMapper { addModule(jacksonModule) }
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
)