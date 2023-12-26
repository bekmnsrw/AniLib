pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "AniLib"

/* App Module */
include(":app")


/* Core Module */
include(":core:designsystem")
include(":core:utils")
include(":core:widget")
include(":core:network")
include(":core:db")
include(":core:navigation")


/* Feature Module */

/* Feature Module: Auth */
include(":feature:auth:api")
include(":feature:auth:impl")

/* Feature Module: Home */
include(":feature:home:api")
include(":feature:home:impl")

/* Feature Module: Profile */
include(":feature:profile:api")
include(":feature:profile:impl")

/* Feature Module: Favorites */
include(":feature:favorites:api")
include(":feature:favorites:impl")
