import org.jahia.api.Constants
import org.jahia.services.content.*

import javax.jcr.ItemNotFoundException
import javax.jcr.RepositoryException

boolean doIt = false;
def logger = log;

Set<String> nodesToAutoPublish = new HashSet<String>();
Set<String> nodesToManuallyPublish = new HashSet<String>();

HashMap<String, JCRNodeWrapper> documentTypeToNode = new HashMap<String, String>();
documentTypeToNode.put("0982a155-6e27-4081-92b8-38a2c20ec82e","jcr:title");
documentTypeToNode.put("d14b0fe2-057d-4e4e-9cc0-ddfbcd29c1b1","jcr:title");
documentTypeToNode.put("3851b926-7916-4b62-86a6-5512cac5cdc6","jcr:title");
documentTypeToNode.put("f2f572e2-bf36-4dae-9163-900d071f20fe","jcr:title");
documentTypeToNode.put("84a348ed-efef-407b-baa4-d67d5edb5163","jcr:title");
documentTypeToNode.put("e2130007-43ad-4f67-bf10-d83188462745","jcr:title");
documentTypeToNode.put("770f2e18-dc67-43af-92a9-0262972ebb1d","jcr:title");
documentTypeToNode.put("4422b6e2-f1d2-4499-921d-b969a2d4ba1c","jcr:title");
documentTypeToNode.put("fddd1b12-fb36-477a-a952-edb24b61d00d","jcr:title");
documentTypeToNode.put("846fdfac-311b-4eb9-819f-d8e44fe6b2f9","jcr:title");
documentTypeToNode.put("aa9a76d9-df37-45be-9fc2-4f41776f6b98","jcr:title");
documentTypeToNode.put("01612eef-3e2e-4ffe-85c4-0a62bd9447fc","game4:webappTheme");
documentTypeToNode.put("01612eef-3e2e-4ffe-85c4-0a62bd9447fc","game4:transitionLabel");
documentTypeToNode.put("01612eef-3e2e-4ffe-85c4-0a62bd9447fc","game4:transition");
documentTypeToNode.put("01612eef-3e2e-4ffe-85c4-0a62bd9447fc","game4:browsing");
documentTypeToNode.put("01612eef-3e2e-4ffe-85c4-0a62bd9447fc","game4:reset");
documentTypeToNode.put("1031abc9-8782-4fe4-b96d-752996d1fc61","title");
documentTypeToNode.put("cef01ed8-7888-403e-a6d6-555fe0462887","title");
documentTypeToNode.put("d729a66e-1a51-4c2e-acfb-b4ea1e84870a","title");
documentTypeToNode.put("56c16051-1591-4ac0-9c01-c22d85cff399","title");
documentTypeToNode.put("7d54e464-8087-45c0-83b6-f729ce4c0f80","title");


JCRTemplate.getInstance().doExecuteWithSystemSession(null, Constants.EDIT_WORKSPACE, new JCRCallback() {
    @Override
    Object doInJCR(JCRSessionWrapper session) throws RepositoryException {

        for (String uuid : documentTypeToNode.keySet()) {
            try {
                JCRNodeWrapper node = session.getNodeByIdentifier(uuid);
                String property = documentTypeToNode.get(uuid);
                logger.info("Remove property [" + property + "] on node " + uuid + " " + node.getPath());
                int status = JCRPublicationService.getInstance().getStatus(node, session, null);
                /*
                    public static final int PUBLISHED = 1;
                    public static final int MODIFIED = 3;
                    public static final int NOT_PUBLISHED = 4;
                    public static final int UNPUBLISHED = 5;
                    public static final int MANDATORY_LANGUAGE_UNPUBLISHABLE = 6;
                    public static final int LIVE_MODIFIED = 7;
                    public static final int LIVE_ONLY = 8;
                    public static final int CONFLICT = 9;
                    public static final int MANDATORY_LANGUAGE_VALID = 10;
                    public static final int DELETED = 11;
                    public static final int MARKED_FOR_DELETION = 12;

                 */
                if (status == null) {
                    status = PublicationInfo.UNPUBLISHED;
                }

                if (status == PublicationInfo.PUBLISHED) {
                    nodesToAutoPublish.add(node.getIdentifier());
                } else {
                    nodesToManuallyPublish.add(node.getIdentifier());
                }
                if (doIt) {
                    try {
                        node.getProperty(property).remove();
                        node.getSession().save();
                    } catch (RepositoryException e) {
                        logger.warn(e.getMessage() + " on " + node.getPath() + " " + uuid);
                    }
                }

            } catch (ItemNotFoundException e) {
                logger.warn("Error: Could not find node " + uuid);
            }
        }
        if (CollectionUtils.isNotEmpty(nodesToAutoPublish)) {
            if (doIt) {
                JCRPublicationService.getInstance().publish(nodesToAutoPublish.asList(), Constants.EDIT_WORKSPACE, Constants.LIVE_WORKSPACE, null);
            };
            logger.info("");
            logger.info("Nodes re-published:")
            for (String identifier : nodesToAutoPublish) {
                logger.warn("   " + session.getNodeByIdentifier(identifier).getPath());
            }

            logger.info("");
            logger.info("Nodes to publish manually:")
            for (String identifier : nodesToManuallyPublish) {
                logger.warn("   " + session.getNodeByIdentifier(identifier).getPath());
            }

        }

        return null;
    }
});



