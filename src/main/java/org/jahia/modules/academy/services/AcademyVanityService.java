package org.jahia.modules.academy.services;

import org.drools.core.spi.KnowledgeHelper;
import org.jahia.services.SpringContextSingleton;
import org.jahia.services.content.JCRContentUtils;
import org.jahia.services.content.JCRNodeWrapper;
import org.jahia.services.content.JCRSessionFactory;
import org.jahia.services.content.JCRSessionWrapper;
import org.jahia.services.content.decorator.JCRSiteNode;
import org.jahia.services.content.rules.AddedNodeFact;

import org.jahia.services.render.RenderContext;
import org.jahia.services.seo.VanityUrl;
import org.jahia.services.seo.jcr.NonUniqueUrlMappingException;
import org.jahia.services.seo.jcr.VanityUrlManager;
import org.jahia.taglibs.jcr.node.JCRTagUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.Normalizer;
import java.util.*;

/**
 * Service allowing to create a VanityUrl on page creation page
 */
public class AcademyVanityService {

    private static final Logger logger = LoggerFactory.getLogger(AcademyVanityService.class);
    /**
     * Will create a thumbnail for the giving image and the specified thumbnailSize. If the original image's width is
     * inferior than the thumbnail size, the created thumbnail will have the original width of the image : it will be
     * a duplicate
     *
     * @param node the AddedNodeFact which called the rule
     * @param drools   drools helper
     * @throws Exception
     */
    public void addVanity(AddedNodeFact node, KnowledgeHelper drools) throws
            Exception {
            createVanity(node.getNode(),node.getLanguage());
    }

    private void createVanity(JCRNodeWrapper node, String lang) throws Exception {
        JCRSiteNode site = node.getResolveSite();
        String siteKey = site.getSiteKey();
        RenderContext context = new org.jahia.services.render.RenderContext(null,null,null);
        context.setSite(site);
        if (JCRContentUtils.isADisplayableNode(node,context)) {
            logger.debug("Try to create a vanity for node " + node.getPath());
            List<JCRNodeWrapper> parentNodes = JCRTagUtils.getParentsOfType(node, "jmix:navMenuItem");
            String url = "/" + slug(node.getDisplayableName());
            Iterator<JCRNodeWrapper> parentNodesIterator = parentNodes.iterator();
            JCRSessionWrapper session = JCRSessionFactory.getInstance().getCurrentUserSession();

            while (parentNodesIterator.hasNext()) {
                JCRNodeWrapper parentPage = parentNodesIterator.next();
                // skip the home (the last one)
                if (parentNodesIterator.hasNext()) {
                    String pageTitle = parentPage.getDisplayableName();
                    String slugTitle = slug(pageTitle);
                    url = "/" + slugTitle + url;
                }
            }
            if (!"en".equals(lang)) {
                url = "/" + lang + url;
            }

            VanityUrl vanityUrl = new VanityUrl(url, siteKey, lang, true, true);
            try {
                VanityUrlManager urlMgr = SpringContextSingleton.getInstance().getContext().getBean(VanityUrlManager.class);
                if (urlMgr.findExistingVanityUrls(url, siteKey, session).isEmpty()) {
                    urlMgr.saveVanityUrlMapping(node, vanityUrl, session);
                    logger.debug("addVanity " + url + " for page " + node.getPath());
                } else {
                    logger.debug("could not add vanity " + url + " for page " + node.getPath() + " -> already exist");
                }
            } catch (NonUniqueUrlMappingException nonUniqueUrlMappingException) {
                logger.error(nonUniqueUrlMappingException.getMessage(), nonUniqueUrlMappingException);
            }
        } else {
            logger.debug("Could not create a vanity for node " + node.getPath() + " (not a displayableNode)");
        }


    }

    private String slug(final String str) {
        if (str == null) {
            return "";
        }
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").replaceAll("[^ \\w|.]", "").trim().replaceAll("\\s+", "-").toLowerCase(Locale.ENGLISH);
    }

}
