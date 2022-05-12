package org.jahia.modules.academy.filters;

import com.ctc.wstx.util.StringUtil;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;
import org.apache.commons.lang.StringUtils;
import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.filter.AbstractFilter;
import org.jahia.services.render.filter.RenderChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Find HTML img tag with src towards internal images (/files/*), and rewrite the src so it points toward the thumbnail
 */
public class AcademyImageUrlRewriter extends AbstractFilter {

    private static final Logger logger = LoggerFactory.getLogger(AcademyImageUrlRewriter.class);

    private static final String PATH_TOWARD_IMG = "/files";

    private static final String THUMBNAIL_NAME = "community750";

    @Override
    public String execute(String previousOut, RenderContext renderContext, Resource resource, RenderChain chain) throws Exception {
        Source source = new Source(previousOut);
        OutputDocument out = new OutputDocument(source);

        List<StartTag> imgTags = source.getAllStartTags("img");

        String context = renderContext.getURLGenerator().getContext();
        logger.debug("Context : " + context);
        for (StartTag imgTag : imgTags) {
            String href = imgTag.getAttributeValue("data-href");
            String gallery = imgTag.getAttributeValue("data-gallery");
            String src = imgTag.getAttributeValue("src");
            String imgClass = imgTag.getAttributeValue("class");
            String imgAlt = imgTag.getAttributeValue("alt");
            String icon = imgTag.getAttributeValue("data-icon");
            String disableLightboxValue = imgTag.getAttributeValue("data-disable-lightbox");
            boolean disableLightbox = false;
            if (disableLightboxValue != null && "true".equals(disableLightboxValue)) {
                disableLightbox = true;
            }
            logger.debug("disableLightbox is " + disableLightbox);
            if (imgAlt == null) {
                imgAlt = "";
            }
            if (src.startsWith(context + PATH_TOWARD_IMG)) {
                logger.debug("SRC value to rewrite : " + src);
                StringBuilder sbSrcValue = new StringBuilder(src);
                if (src.contains("?")) {
                    sbSrcValue.append("&");
                } else {
                    sbSrcValue.append("?");
                }
                sbSrcValue.append("t=" + THUMBNAIL_NAME);
                logger.debug("New SRC value : " + sbSrcValue.toString());

                // Rewrite the whole IMG tag
                /*
                <figure class="figure">
                    <a href="https://unsplash.it/1200/768.jpg?image=254" data-toggle="lightbox"
                       data-gallery="example-gallery">
                        <img src="https://unsplash.it/500/300.jpg?image=254"
                             class="figure-img img-fluid rounded shadow" alt="...">
                    </a>
                </figure>

				*/
                StringBuilder newImgTag = new StringBuilder();
                if (!disableLightbox) {
                    if (href != null) {
                        src = href;
                    }
                    if (gallery == null) {
                        gallery="doc-images";
                    }
                    newImgTag.append("<figure class=\"figure\">");
                    newImgTag.append("    <a href=\"").append(src).append("\" data-toggle=\"lightbox\" data-gallery=\"" + gallery + "\">");

                    newImgTag.append("        <img src=\"").append(sbSrcValue.toString()).append("\"");
                    newImgTag.append("           alt=\"" + imgAlt + "\"");
                    newImgTag.append("           class=\"figure-img img-fluid rounded shadow\"");
                    newImgTag.append("         />");
                    newImgTag.append("    </a>");
                    newImgTag.append("</figure>");
                    logger.debug("New IMG tag : " + newImgTag.toString());
                    out.replace(imgTag, newImgTag.toString());
                }

            }
        }

        return out.toString();
    }
}
