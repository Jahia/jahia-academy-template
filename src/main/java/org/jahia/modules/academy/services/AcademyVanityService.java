package org.jahia.modules.academy.services;

import org.drools.core.spi.KnowledgeHelper;
import org.jahia.services.SpringContextSingleton;
import org.jahia.services.content.*;
import org.jahia.services.content.decorator.JCRSiteNode;
import org.jahia.services.content.rules.AddedNodeFact;

import org.jahia.services.content.rules.MovedNodeFact;
import org.jahia.services.seo.VanityUrl;
import org.jahia.services.seo.jcr.NonUniqueUrlMappingException;
import org.jahia.services.seo.jcr.VanityUrlManager;
import org.jahia.taglibs.jcr.node.JCRTagUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.NodeIterator;
import javax.jcr.query.Query;
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
     * @param pageNode the AddedNodeFact which called the rule
     * @param drools   drools helper
     * @throws Exception
     */
    public void addVanity(AddedNodeFact pageNode, KnowledgeHelper drools) throws
            Exception {
            createVanity(pageNode.getNode(),pageNode.getLanguage());
    }

    private void createVanity(JCRNodeWrapper page, String lang) throws Exception {
        JCRSiteNode site = page.getResolveSite();
        String siteKey = site.getSiteKey();
        List<JCRNodeWrapper> parentNodes = JCRTagUtils.getParentsOfType(page, "jmix:navMenuItem");
        String url = "/" + slug(page.getDisplayableName());
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
                urlMgr.saveVanityUrlMapping(page, vanityUrl, session);
                logger.debug("addVanity " + url + " for page " + page.getPath());
            } else {
                logger.debug("could not add vanity " + url + " for page " + page.getPath() + " -> already exist");
            }
        } catch (NonUniqueUrlMappingException nonUniqueUrlMappingException) {
            logger.error(nonUniqueUrlMappingException.getMessage(), nonUniqueUrlMappingException);
        }
    }

    private String slug(final String str) {
        if (str == null) {
            return "";
        }
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").replaceAll("[^ \\w|.]", "").trim().replaceAll("\\s+", "-").toLowerCase(Locale.ENGLISH);
    }

}
