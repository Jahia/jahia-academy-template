package org.jahia.modules.academy.filters;

import org.ahocorasick.trie.Token;
import org.ahocorasick.trie.Trie;
import org.apache.taglibs.standard.tag.common.core.Util;
import org.jahia.services.content.JCRNodeIteratorWrapper;
import org.jahia.services.content.JCRNodeWrapper;
import org.jahia.services.content.JCRSessionWrapper;
import org.jahia.services.query.QueryResultWrapper;
import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.filter.AbstractFilter;
import org.jahia.services.render.filter.RenderChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import java.util.Collection;
import java.util.HashMap;

public class GlossaryFilter extends AbstractFilter {

    private static final Logger logger = LoggerFactory.getLogger(GlossaryFilter.class);


    public String execute(String previousOut, RenderContext renderContext, Resource resource, RenderChain chain) throws Exception {
        logger.debug("Parse " + resource.getPath());


        //String statement = "//element(*,jnt:glossaryTerm)";

        //logger.debug("statement is : " + statement);

        JCRSessionWrapper session = resource.getNode().getSession();
        QueryManager queryManager = session.getWorkspace().getQueryManager();
        String siteKey = resource.getNode().getResolveSite().getSiteKey();

        Query query = query = queryManager.createQuery("SELECT * FROM [jacademy:glossaryTerm] as news WHERE ISDESCENDANTNODE('/sites/" + siteKey + "')", Query.JCR_SQL2);

        //query = queryManager.createQuery(statement, Query.XPATH);

        QueryResultWrapper queryResult = (QueryResultWrapper) query.execute();
        JCRNodeIteratorWrapper nodes = queryResult.getNodes();

        Trie.TrieBuilder trieBuilder = Trie.builder().ignoreOverlaps().onlyWholeWords().ignoreCase();
        HashMap glossary = new HashMap<String,String>();

        while (nodes.hasNext()) {
            JCRNodeWrapper node = (JCRNodeWrapper) nodes.next();
            String term = node.getDisplayableName();
            String definition = node.getPropertyAsString("glossaryDefinition");
            glossary.put(term.toLowerCase(), definition);
            logger.debug("Add term [" + term.toLowerCase() + "] to the trie");
            trieBuilder.addKeyword(term);
        }
        Trie trie = trieBuilder.build();

        Collection<Token> tokens = trie.tokenize(previousOut);

        StringBuffer html = new StringBuffer();

        for (Token token : tokens) {
            if (token.isMatch()) {
                logger.debug(("Found term [" + token.getFragment() + "] that match [" + token.getEmit().getKeyword() + "]"));
                html.append("<acronym class=\"glossTerm\" title=\"" + Util.escapeXml(token.getFragment()) + "\" data-content=\"" + Util.escapeXml((String)glossary.get(token.getEmit().getKeyword())).replaceAll("(\\r|\\n)", "")+ "\">");
            }
            html.append(token.getFragment());
            if (token.isMatch()) {
                html.append("</acronym>");
            }
        }

        return html.toString();
    }
}
