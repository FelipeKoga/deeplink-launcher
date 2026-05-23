package dev.koga.deeplinklauncher.deeplink.uicomponent.preview

import dev.koga.deeplinklauncher.deeplink.api.domain.model.Folder
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
internal val previewFolder: Folder = Folder(
    id = Uuid.random().toString(),
    name = "Folder name",
    description = "Folder description",
)
