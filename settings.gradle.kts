plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "bookkeeper"

include("app")

include(
    "common:common-data",
    "common:common-properties",
    "common:user-context",
    "common:common-rest",
    "common:common-events",
    "common:common-base",
)

include(
    "spendings:spendings-domain",
    "spendings:spendings-usecases",
    "spendings:spendings-application",
    "spendings:adapters:spendings-data",
    "spendings:adapters:spendings-rest",
    "spendings:adapters:spendings-category",
    "spendings:adapters:spendings-account",
)

include(
    "transfers:transfers-domain",
    "transfers:transfers-usecases",
    "transfers:transfers-application",
    "transfers:adapters:transfers-data",
    "transfers:adapters:transfers-rest",
    "transfers:adapters:transfers-account",
    "transfers:adapters:transfers-category",
)

include(
    "categories:categories-usecases",
    "categories:categories-domain",
    "categories:categories-application",
    "categories:adapters:categories-data",
    "categories:adapters:categories-rest",
)

include(
    "auth:auth-usecases",
    "auth:auth-application",
    "auth:adapters:auth-spring",
    "auth:adapters:auth-credentials",
    "auth:adapters:auth-tokens",
)

include(
    "accounts:accounts-domain",
    "accounts:accounts-application",
    "accounts:accounts-usecases",
    "accounts:adapters:accounts-data",
    "accounts:adapters:accounts-rest",
    "accounts:adapters:accounts-events",
)

include(
    "reports:reports-domain",
    "reports:reports-application",
    "reports:reports-usecases",
    "reports:adapters:reports-data",
    "reports:adapters:reports-events",
    "reports:adapters:reports-rest",
)

include(
    "credentials:credentials-domain",
    "credentials:credentials-usecases",
    "credentials:credentials-application",
    "credentials:adapters:credentials-data",
    "credentials:adapters:credentials-encryption",
    "credentials:adapters:credentials-rest",
)

include(
    "tokens:tokens-domain",
    "tokens:tokens-usecases",
    "tokens:tokens-application",
    "tokens:adapters:tokens-data",
    "tokens:adapters:tokens-hashing",
)

include(
    "users:users-domain",
    "users:users-usecases",
    "users:users-application",
    "users:adapters:users-data",
    "users:adapters:users-credentials",
    "users:adapters:users-rest",
)

include(
    "assets:assets-usecases",
    "assets:assets-application",
    "assets:assets-domain",
    "assets:adapters:assets-rest",
)

include("logging")

include(
    "spring-events",
    "spring-transactions",
)
