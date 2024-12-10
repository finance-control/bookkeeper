package com.marsofandrew.bookkeeper.auth.client

interface ClientIdProvider {

    fun getClientId(): String?
}