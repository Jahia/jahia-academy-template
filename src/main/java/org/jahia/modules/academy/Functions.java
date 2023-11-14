package org.jahia.modules.academy;

import org.jahia.services.content.JCRContentUtils;
import org.jahia.services.content.JCRNodeWrapper;

import javax.jcr.RepositoryException;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public final class Functions {

    private Functions() {
        // Private constructor to prevent instantiation
    }

    /**
     * Finds the URL of the first sub-page under the given node.
     *
     * @param node The starting node to search for sub-pages.
     * @return The URL of the first sub-page found, or "#" if none found.
     * @throws RepositoryException If there's an error accessing the repository.
     */
    public static String findFirstSubPageUrl(JCRNodeWrapper node) throws RepositoryException {
        if (node == null) {
            return null;
        }

        Stack<JCRNodeWrapper> stack = new Stack<>();
        stack.push(node);

        while (!stack.isEmpty()) {
            JCRNodeWrapper current = stack.pop();

            // Check if the current node is a page
            if (current.isNodeType("jnt:page")) {
                return current.getUrl();
            }

            // Add child nodes of the specified type to the stack
            List<JCRNodeWrapper> children = JCRContentUtils.getChildrenOfType(current, "jmix:navMenuItem");
            Collections.reverse(children); // Reverse the list to maintain depth-first order
            stack.addAll(children);
        }

        return "#"; // Return "#" if no sub-page URL found
    }

}
