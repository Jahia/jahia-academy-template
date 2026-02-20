package org.jahia.modules.academy.rules;

import org.jahia.modules.academy.services.AcademyImageService;
import org.jahia.services.content.rules.ModuleGlobalObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * Registers Academy globals into the Drools rules context.
 */
@Component(service = ModuleGlobalObject.class)
public class AcademyRulesGlobals extends ModuleGlobalObject {

    @Reference
    public void bindAcademyImageService(AcademyImageService academyImageService) {
        // MUST match the name used in rules: "global AcademyImageService academyImageService"
        getGlobalRulesObject().put("academyImageService", academyImageService);
    }

    public void unbindAcademyImageService(AcademyImageService academyImageService) {
        getGlobalRulesObject().remove("academyImageService");
    }
}
