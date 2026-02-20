package org.jahia.modules.academy.filters;

import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;
import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.filter.AbstractFilter;
import org.jahia.services.render.filter.RenderChain;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Find HTML img tags with src pointing to internal images (/files/*),
 * and rewrite the src so it points toward the thumbnail.
 */
@Component(service = AbstractFilter.class, immediate = true)
public class AcademyImageUrlRewriter extends AbstractFilter {

    private static final Logger logger = LoggerFactory.getLogger(AcademyImageUrlRewriter.class);

    private static final String PATH_TOWARD_IMG = "/files";
    private static final String THUMBNAIL_NAME = "community750";

    @Activate
    public void activate() {
        // Equivalent of Spring properties
        setPriority(22);
        setDescription("Find URL toward /files images and rewrite them in order to use the community750 thumbnail");
        setApplyOnNodeTypes("jacademix:document,jacademix:content");
        setApplyOnTemplates("default");
    }

    @Override
    public String execute(String previousOut, RenderContext renderContext, Resource resource, RenderChain chain) throws Exception {
        if (previousOut == null || previousOut.isEmpty()) {
            return previousOut;
        }

        Source source = new Source(previousOut);
        OutputDocument out = new OutputDocument(source);

        List<StartTag> imgTags = source.getAllStartTags("img");
        if (imgTags == null || imgTags.isEmpty()) {
            return previousOut;
        }

        String context = renderContext.getURLGenerator().getContext();

        for (StartTag imgTag : imgTags) {
            String href = imgTag.getAttributeValue("data-href");
            String gallery = imgTag.getAttributeValue("data-gallery");
            String src = imgTag.getAttributeValue("src");
            String imgAlt = imgTag.getAttributeValue("alt");

            String disableLightboxValue = imgTag.getAttributeValue("data-disable-lightbox");
            boolean disableLightbox = "true".equals(disableLightboxValue);

            if (imgAlt == null) {
                imgAlt = "";
            }

            // Prevent NPE
            if (src == null) {
                continue;
            }

            if (src.startsWith(context + PATH_TOWARD_IMG)) {
                StringBuilder sbSrcValue = new StringBuilder(src);
                sbSrcValue.append(src.contains("?") ? "&" : "?");
                sbSrcValue.append("t=").append(THUMBNAIL_NAME);

                // Keep previous behavior: only wrap/replace when lightbox is enabled
                if (!disableLightbox) {
                    if (href != null) {
                        src = href;
                    }
                    if (gallery == null) {
                        gallery = "doc-images";
                    }

                    StringBuilder newImgTag = new StringBuilder();
                    newImgTag.append("<figure class=\"figure\">");
                    newImgTag.append("<a href=\"").append(src).append("\" data-toggle=\"lightbox\" data-gallery=\"").append(gallery).append("\">");
                    newImgTag.append("<img src=\"").append(sbSrcValue).append("\"");
                    newImgTag.append(" alt=\"").append(imgAlt).append("\"");
                    newImgTag.append(" class=\"figure-img img-fluid rounded shadow\"");
                    newImgTag.append(" />");
                    newImgTag.append("</a>");
                    newImgTag.append("</figure>");

                    out.replace(imgTag, newImgTag.toString());
                } else {
                    // If lightbox is disabled, we still rewrite the src attribute only (more consistent)
                    // Replace full start tag by updating src attribute is harder with Jericho.
                    // Minimal approach: replace the tag with a rebuilt <img ...> keeping class/alt.
                    String imgClass = imgTag.getAttributeValue("class");
                    String icon = imgTag.getAttributeValue("data-icon");

                    StringBuilder newImgTag = new StringBuilder();
                    newImgTag.append("<img src=\"").append(sbSrcValue).append("\"");
                    newImgTag.append(" alt=\"").append(imgAlt).append("\"");
                    if (imgClass != null) {
                        newImgTag.append(" class=\"").append(imgClass).append("\"");
                    }
                    if (icon != null) {
                        newImgTag.append(" data-icon=\"").append(icon).append("\"");
                    }
                    newImgTag.append(" data-disable-lightbox=\"true\"");
                    newImgTag.append(" />");

                    out.replace(imgTag, newImgTag.toString());
                }
            }
        }

        return out.toString();
    }
}
