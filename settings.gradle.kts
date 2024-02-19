plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "bookkeeper"

include("app")

include(
    "common:common-data",
    "common:common-properties",
    "common:user-context",
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
)

include(
    "auth:auth-usecases",
    "auth:adapters:auth-spring"
)
