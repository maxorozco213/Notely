package com.example.notely.models

import com.example.notely.common.FileType
import java.io.File

data class FileModel(
    val path: String,
    val fileType: FileType,
    val name: String,
    val sizeInMB: Double,
    val extension: String = "",
    val subFiles: Int = 0
)