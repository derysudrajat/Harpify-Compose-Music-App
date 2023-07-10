package id.derysudrajat.harpify.ui.dynamicTheme.dynamicbackgroundmodifier

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import id.derysudrajat.harpify.ui.dynamicTheme.manager.DynamicThemeManager
import id.derysudrajat.harpify.ui.dynamicTheme.manager.HarpifyDynamicThemeManager
import id.derysudrajat.harpify.usecases.downloadDrawableFromUrlUseCase.HarpifyDownloadDrawableFromUrlUseCase
import kotlinx.coroutines.Dispatchers

val LocalDynamicThemeManager: ProvidableCompositionLocal<DynamicThemeManager> =
    staticCompositionLocalOf {
        HarpifyDynamicThemeManager(
            downloadDrawableFromUrlUseCase = HarpifyDownloadDrawableFromUrlUseCase(Dispatchers.IO),
            defaultDispatcher = Dispatchers.IO
        )
    }