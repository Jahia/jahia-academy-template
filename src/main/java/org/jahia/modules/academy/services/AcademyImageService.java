package org.jahia.modules.academy.services;

import org.drools.core.spi.KnowledgeHelper;
import org.jahia.services.content.JCRNodeWrapper;
import org.jahia.services.content.rules.AddedNodeFact;
import org.jahia.services.content.rules.ImageService;
import org.jahia.services.image.Image;
import org.jahia.services.image.JahiaImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import java.io.IOException;

/**
 * Service allowing to addThumbnail for an image
 */
public class AcademyImageService {

	private static final Logger logger = LoggerFactory.getLogger(AcademyImageService.class);

	private JahiaImageService jahiaImageService;

	private ImageService rulesImageService;

	public void setJahiaImageService(JahiaImageService jahiaImageService) {
		this.jahiaImageService = jahiaImageService;
	}

	public void setRulesImageService(ImageService rulesImageService) {
		this.rulesImageService = rulesImageService;
	}

	/**
	 * Will create a thumbnail for the giving image and the specified thumbnailSize. If the original image's width is
	 * inferior than the thumbnail size, the created thumbnail will have the original width of the image : it will be
	 * a duplicate
	 *
	 * @param imageNode the AddedNodeFact which called the rule
	 * @param name the name to use for the new thumbnail
	 * @param thumbnailSize the size to use for the new thumbnail
	 * @param drools drools helper
	 * @throws Exception
	 */
	public void addThumbnail(AddedNodeFact imageNode, String name, int thumbnailSize, KnowledgeHelper drools) throws
			Exception {

		String path = imageNode.getPath();
		if (! path.endsWith(".svg")) {
			logger.debug("addThumbnail called for the name '" + name + "' and the size '" + thumbnailSize);

			Image image;
			try {
				JCRNodeWrapper theNode = imageNode.getNode();
				if (theNode != null) {
					image = jahiaImageService.getImage(theNode);

					int originalWidth = jahiaImageService.getWidth(image);
					logger.debug("Original width : " + originalWidth);

					// We create the thumbnail with the expected size
					if (originalWidth >= thumbnailSize) {
						logger.info("Creating thumbnail image with size : " + thumbnailSize);
						rulesImageService.addThumbnail(imageNode, name, thumbnailSize, drools);
					}
					// We create a thumbnail with the original size : it is a duplicate of the original image
					else {
						logger.info("Creating thumbnail image with original size : " + originalWidth);
						rulesImageService.addThumbnail(imageNode, name, originalWidth, drools);
					}
				} else {
					logger.debug("Image node for '" + path + " is null");
				}
			} catch (RepositoryException e) {
				logger.error(e.getMessage(),e);
			} catch (IOException e) {
				logger.error(e.getMessage(),e);
			}
		}

	}

}
