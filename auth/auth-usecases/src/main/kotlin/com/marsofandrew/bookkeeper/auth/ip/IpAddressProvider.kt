package com.marsofandrew.bookkeeper.auth.ip

interface IpAddressProvider {

    fun getIpAddress(): String
}