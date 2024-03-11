package com.marsofandrew.bookkeeper.properties.email

data class Email(
    val value: String
) {
    init {
        check(emailRegex.matches(value)) { "provided value does not match email pattern" }
    }

    companion object {
        private val emailRegex =
            Regex(
                "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                        + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$"
            )
    }
}
