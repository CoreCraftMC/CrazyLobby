package me.corecraft.paper.api.runtime;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import me.corecraft.paper.api.runtime.enums.Plugins;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

public class DependencyLoader implements PluginLoader {

    @Override
    public void classloader(@NotNull final PluginClasspathBuilder classpathBuilder) {
        final MavenLibraryResolver resolver = new MavenLibraryResolver();

        resolver.addRepository(new RemoteRepository.Builder("crazycrewReleases", "default", "https://repo.crazycrew.us/releases/").build());

        resolver.addRepository(new RemoteRepository.Builder("paper", "default", "https://repo.papermc.io/repository/maven-public/").build());

        // Add configme
        resolver.addDependency(Plugins.config_me.asDependency());

        // Add fusion paper api
        resolver.addDependency(Plugins.fusion_paper.asDependency());

        // Add cloud api
        resolver.addDependency(Plugins.cloud_annotations.asDependency());
        resolver.addDependency(Plugins.cloud_extras.asDependency());
        resolver.addDependency(Plugins.cloud_paper.asDependency());

        // Populate resolvers
        classpathBuilder.addLibrary(resolver);
    }
}