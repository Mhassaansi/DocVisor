package com.fictivestudios.docsvisor.enums

enum class BaseURLTypes {
    BASE_URL, XML_URL, PRICE_BASE_URL;

    fun canonicalForm(): String {
        return name.toLowerCase()
    }

}