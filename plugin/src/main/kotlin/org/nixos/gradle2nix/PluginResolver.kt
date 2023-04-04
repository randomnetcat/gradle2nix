package org.nixos.gradle2nix

import org.gradle.api.Project
import org.gradle.api.internal.artifacts.DependencyResolutionServices
import org.gradle.api.internal.artifacts.repositories.ResolutionAwareRepository
import org.gradle.plugin.management.PluginRequest
import org.gradle.plugin.use.internal.PluginDependencyResolutionServices
import javax.inject.Inject

private val handlerMethod = PluginDependencyResolutionServices::class.java.getDeclaredMethod("getDependencyResolutionServices").also {
    it.isAccessible = true
}

private fun accessRawServices(pluginServices: PluginDependencyResolutionServices): DependencyResolutionServices {
    val result = handlerMethod.invoke(pluginServices)
    return result as DependencyResolutionServices
}

internal open class PluginResolver @Inject constructor(
    project: Project,
    pluginDependencyResolutionServices: PluginDependencyResolutionServices
) {
    private val configurations = accessRawServices(pluginDependencyResolutionServices).configurationContainer

    private val resolver = ConfigurationResolverFactory(
        project,
        ConfigurationScope.PLUGIN,
        pluginDependencyResolutionServices.pluginRepositoryHandler.filterIsInstance<ResolutionAwareRepository>()
    ).create(accessRawServices(pluginDependencyResolutionServices).dependencyHandler)

    fun resolve(pluginRequests: List<PluginRequest>): List<DefaultArtifact> {
        val markerDependencies = pluginRequests.map { request ->
            request.module?.let { module ->
                ApiHack.defaultExternalModuleDependency(module.group, module.name, module.version)
            } ?: request.id.id.let { id ->
                ApiHack.defaultExternalModuleDependency(id, "$id.gradle.plugin", request.version)
            }
        }
        return resolver.resolve(configurations.detachedConfiguration(*markerDependencies.toTypedArray()))
    }
}
