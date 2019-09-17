package com.example.constants

class Preferences {

    companion object {
        private const val KILO_BYTE = 1024L
        private const val MEGA_BYTE = 1024 * KILO_BYTE

        const val OK_HTTP_CACHE_SIZE = 10 * MEGA_BYTE // 10MB
        const val CONNECT_TIME_OUT_SECONDS = 30L
        const val ERROR_MESSAGE_TIME_LENGTH = 2000L
    }
}