package dev.koga.deeplinklauncher.shared.spike

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import dev.koga.deeplinklauncher.analytics.api.AnalyticsTracker
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ViewModelAssistedFactoryKey

@AssistedInject
class SpikeViewModel(
    @Assisted val savedStateHandle: SavedStateHandle,
    val analyticsTracker: AnalyticsTracker,
) : ViewModel() {

    @AssistedFactory
    @ViewModelAssistedFactoryKey(SpikeViewModel::class)
    @ContributesIntoMap(AppScope::class)
    fun interface Factory : ViewModelAssistedFactory {
        override fun create(extras: CreationExtras): SpikeViewModel = create(extras.createSavedStateHandle())

        fun create(@Assisted savedStateHandle: SavedStateHandle): SpikeViewModel
    }
}
