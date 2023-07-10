package id.derysudrajat.harpify.utils

import androidx.compose.ui.Modifier

fun Modifier.conditional(
    condition: Boolean,
    block: Modifier.() -> Modifier
): Modifier = if (condition) this.then(block()) else this