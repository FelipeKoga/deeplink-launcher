package dev.koga.deeplinklauncher.shared.di

import androidx.lifecycle.DEFAULT_ARGS_KEY
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.SAVED_STATE_REGISTRY_OWNER_KEY
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.enableSavedStateHandles
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.savedState
import kotlin.reflect.KClass

internal class TestNavEntryOwner : SavedStateRegistryOwner, ViewModelStoreOwner {
    private val controller = SavedStateRegistryController.create(this)
    override val lifecycle = LifecycleRegistry.createUnsafe(this)
    override val savedStateRegistry: SavedStateRegistry get() = controller.savedStateRegistry
    override val viewModelStore = ViewModelStore()

    init {
        controller.performAttach()
        controller.performRestore(null)
        enableSavedStateHandles()
        lifecycle.currentState = Lifecycle.State.RESUMED
    }
}

internal fun AppGraph.createViewModels(keys: Set<KClass<out ViewModel>>): List<Pair<String?, String?>> {
    val owner = TestNavEntryOwner()
    val extras = MutableCreationExtras().apply {
        set(SAVED_STATE_REGISTRY_OWNER_KEY, owner)
        set(VIEW_MODEL_STORE_OWNER_KEY, owner)
        set(
            DEFAULT_ARGS_KEY,
            savedState {
                putString("id", "missing")
                putBoolean("showFolder", false)
                putString("folderId", "missing")
            },
        )
    }
    val provider = ViewModelProvider.create(owner.viewModelStore, metroViewModelFactory, extras)

    @Suppress("UNCHECKED_CAST")
    val created = keys.map { key -> key.simpleName to provider[key as KClass<ViewModel>]::class.simpleName }
    owner.viewModelStore.clear()
    return created
}
