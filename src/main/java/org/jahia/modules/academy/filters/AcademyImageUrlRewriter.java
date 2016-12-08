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
			String srcValue = imgTag.getAttributeValue("src");
			String imgClass = imgTag.getAttributeValue("class");
			if (srcValue.startsWith(context + PATH_TOWARD_IMG)) {
				logger.debug("SRC value to rewrite : " + srcValue);
				StringBuilder sbSrcValue = new StringBuilder(srcValue);
				if (srcValue.contains("?")) {
					sbSrcValue.append("&");
				}
				else {
					sbSrcValue.append("?");
				}
				sbSrcValue.append("t=" + THUMBNAIL_NAME);
				logger.debug("New SRC value : " + sbSrcValue.toString());

				// Rewrite the whole IMG tag
				/*
				 <a href="http://www.youtube.com/watch?v=k6mFF3VmVAs" data-toggle="lightbox" data-gallery="youtubevideos" class="col-sm-4">
                     <img src="//i1.ytimg.com/vi/yP11r5n5RNg/mqdefault.jpg" class="img-responsive">
                 </a>
				*/
				StringBuilder newImgTag = new StringBuilder("<a href=\"").append(srcValue).append("\" data-toggle=\"lightbox\" data-gallery=\"doc-images\">");
				newImgTag.append("<img src=\"").append(sbSrcValue.toString()).append("\"");
				if (! "".equals(imgClass)){
					newImgTag.append(" class=\""+ imgClass + "\"");
				}
				newImgTag.append("></a>");
				/*
				StringBuilder newImgTag = new StringBuilder("<img");
				newImgTag.append(" alt=\"" + imgTag.getAttributeValue("alt") + "\" data-lity");
				newImgTag.append(" data-lity-target=\"" + srcValue + "\"");
				newImgTag.append(" src=\"" + sbSrcValue.toString() + "\"/>");
				 */

				logger.debug("New IMG tag : " + newImgTag.toString());
				out.replace(imgTag, newImgTag.toString());
			}
		}

		return out.toString();
	}
}
