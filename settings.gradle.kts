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
    "spendings:spendings-fixtures",
    "spendings:adapters:spendings-data",
    "spendings:adapters:spendings-rest",
)

include(
    "transfers:transfers-domain",
    "transfers:transfers-usecases",
    "transfers:transfers-application",
    "transfers:transfers-fixtures",
    "transfers:adapters:transfers-data",
    "transfers:adapters:transfers-rest",
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
    "auth:adapters:auth-spring"
)

include(
    "accounts:accounts-domain",
    "accounts:accounts-application",
    "accounts:accounts-usecases",
    "accounts:accounts-fixtures",
    "accounts:adapters:accounts-data",
    "accounts:adapters:accounts-rest",
    "accounts:adapters:accounts-events",
)

include(
    "reports:reports-domain",
    "reports:reports-application",
    "reports:reports-usecases",
    "reports:reports-fixtures",
    "reports:adapters:reports-data",
    "reports:adapters:reports-events",
)

include(
    "spring-events",
    "spring-transactions"
)
