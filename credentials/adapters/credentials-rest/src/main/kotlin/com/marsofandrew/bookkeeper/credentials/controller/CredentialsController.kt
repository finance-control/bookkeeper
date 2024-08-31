package com.marsofandrew.bookkeeper.credentials.controller

import com.marsofandrew.bookkeeper.credentials.CredentialsModification
import com.marsofandrew.bookkeeper.credentials.controller.dto.CredentialsModificationDto
import com.marsofandrew.bookkeeper.properties.id.asId
import com.marsofandrew.bookkeeper.userContext.UserId
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/credentials")
internal class CredentialsController(
    private val credentialsModification: CredentialsModification
) {

    @PatchMapping("/current")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun patch(
        @Parameter(hidden = true) @UserId userId: Long,
        @RequestBody modification: CredentialsModificationDto
    ) {
        credentialsModification.modify(userId.asId(), modification.password)
    }
}