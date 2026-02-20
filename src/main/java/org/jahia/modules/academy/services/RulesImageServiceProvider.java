package org.jahia.modules.academy.services;

import org.jahia.services.content.rules.ImageService;
import org.jahia.services.image.JahiaImageService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * OSGi component that builds and configures the Jahia Rules ImageService singleton,
 * and exposes it via a provider service (this component).
 *
 * We do NOT publish ImageService itself as an OSGi service, because DS can only publish
 * the component instance, not an arbitrary field instance. Consumers should reference
 * this provider with a target filter.
 */
@Component(
        service = RulesImageServiceProvider.class,
        immediate = true,
        property = {
                "academy.rulesImageService=true"
        }
)
public class RulesImageServiceProvider {

    private volatile ImageService rulesImageService;

    @Activate
    public void activate() {
        // Equivalent of Spring factory-method="getInstance"
        rulesImageService = ImageService.getInstance();
    }

    @Reference
    public void bindJahiaImageService(JahiaImageService jahiaImageService) {
        // Equivalent of Spring <property name="imageService" ref="imageService"/>
        // Guard just in case activate order changes
        if (rulesImageService == null) {
            rulesImageService = ImageService.getInstance();
        }
        rulesImageService.setImageService(jahiaImageService);
    }

    public void unbindJahiaImageService(JahiaImageService jahiaImageService) {
        // Optional cleanup:
        // if (rulesImageService != null) rulesImageService.setImageService(null);
    }

    public ImageService getRulesImageService() {
        return rulesImageService;
    }
}
