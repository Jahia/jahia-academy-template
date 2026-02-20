package org.jahia.modules.academy.services;

import org.drools.core.spi.KnowledgeHelper;
import org.jahia.services.content.JCRNodeWrapper;
import org.jahia.services.content.rules.AddedNodeFact;
import org.jahia.services.content.rules.ImageService;
import org.jahia.services.image.Image;
import org.jahia.services.image.JahiaImageService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import java.io.IOException;

/**
 * Service allowing to addThumbnail for an image.
 * Exposed as a Drools global through AcademyRulesGlobals.
 */
@Component(service = AcademyImageService.class)
public class AcademyImageService {

    private static final Logger logger = LoggerFactory.getLogger(AcademyImageService.class);

    private volatile JahiaImageService jahiaImageService;
    private volatile ImageService rulesImageService;

    @Reference
    public void bindJahiaImageService(JahiaImageService jahiaImageService) {
        this.jahiaImageService = jahiaImageService;
    }

    public void unbindJahiaImageService(JahiaImageService jahiaImageService) {
        if (this.jahiaImageService == jahiaImageService) {
            this.jahiaImageService = null;
        }
    }

    /**
     * Bind the Rules ImageService through our dedicated provider to avoid ambiguity.
     */
    @Reference(target = "(academy.rulesImageService=true)")
    public void bindRulesImageServiceProvider(RulesImageServiceProvider provider) {
        this.rulesImageService = provider.getRulesImageService();
    }

    public void unbindRulesImageServiceProvider(RulesImageServiceProvider provider) {
        // Clear only if it still matches
        if (this.rulesImageService == provider.getRulesImageService()) {
            this.rulesImageService = null;
        }
    }

    /**
     * Will create a thumbnail for the given image and the specified thumbnailSize.
     * If the original image's width is smaller than the thumbnail size, the created thumbnail
     * will have the original width of the image (duplicate).
     */
    public void addThumbnail(AddedNodeFact imageNode, String name, int thumbnailSize, KnowledgeHelper drools) throws Exception {
        if (imageNode == null) {
            return;
        }

        final String path = imageNode.getPath();
        if (path != null && path.endsWith(".svg")) {
            return;
        }

        final JahiaImageService jis = this.jahiaImageService;
        final ImageService ris = this.rulesImageService;
        if (jis == null || ris == null) {
            logger.warn("AcademyImageService not ready (missing JahiaImageService or Rules ImageService).");
            return;
        }

        logger.debug("addThumbnail called for name '{}' and size '{}'", name, thumbnailSize);

        try {
            JCRNodeWrapper theNode = imageNode.getNode();
            if (theNode == null) {
                logger.debug("Image node for '{}' is null", path);
                return;
            }

            Image image = jis.getImage(theNode);
            int originalWidth = jis.getWidth(image);
            logger.debug("Original width: {}", originalWidth);

            if (originalWidth >= thumbnailSize) {
                logger.info("Creating thumbnail image with size: {}", thumbnailSize);
                ris.addThumbnail(imageNode, name, thumbnailSize, drools);
            } else {
                logger.info("Creating thumbnail image with original size: {}", originalWidth);
                ris.addThumbnail(imageNode, name, originalWidth, drools);
            }
        } catch (RepositoryException | IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
