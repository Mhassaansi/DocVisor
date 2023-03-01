package com.fictivestudios.docsvisor.enums

enum class FileType {
    IMAGE, VIDEO, DOCUMENT;

    fun canonicalForm(): String {
        return name.toLowerCase()
    }

}