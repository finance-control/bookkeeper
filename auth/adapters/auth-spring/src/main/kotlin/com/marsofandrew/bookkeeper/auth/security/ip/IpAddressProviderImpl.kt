package com.marsofandrew.bookkeeper.auth.security.ip

import com.marsofandrew.bookkeeper.auth.ip.IpAddressProvider
import com.marsofandrew.bookkeeper.userContext.getRequestIpAddress
import org.springframework.stereotype.Service

@Service
internal class IpAddressProviderImpl : IpAddressProvider {

    override fun getIpAddress(): String = getRequestIpAddress()
}