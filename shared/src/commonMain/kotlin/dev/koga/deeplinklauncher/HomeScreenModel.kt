package dev.koga.deeplinklauncher

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.repository.DeepLinkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

class HomeScreenModel(
    private val repository: DeepLinkRepository,
) : ScreenModel {

    val deeplinks = repository.getAllDeepLinks().stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )


    fun insertDeepLink(link: String) {
        screenModelScope.launch {
            repository.insertDeeplink(
                DeepLink(
                    id = Random.nextInt().toString(),
                    link = link,
                    name = null,
                    description = null,
                    createdAt = 0,
                    updatedAt = null,
                    folder = null
                )
            )
        }
    }

}