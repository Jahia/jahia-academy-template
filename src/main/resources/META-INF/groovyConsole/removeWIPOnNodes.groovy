import org.jahia.api.Constants
import org.jahia.services.content.*

import javax.jcr.ItemNotFoundException
import javax.jcr.RepositoryException

boolean doIt = false;
def logger = log;

Set<String> nodesToAutoPublish = new HashSet<String>();
Set<String> nodesToManuallyPublish = new HashSet<String>();

HashMap<String, JCRNodeWrapper> documentTypeToNode = new HashMap<String, String>();
documentTypeToNode.put("9680ccea-4d05-437b-a8e7-72b1e7903038","j:workInProgressLanguages");
documentTypeToNode.put("3675eb65-2df3-45e6-994e-e4d51ab3509f","j:workInProgressLanguages");
documentTypeToNode.put("d38dddca-1c02-48cf-8dcf-acc268e443b0","j:workInProgressLanguages");
documentTypeToNode.put("93422783-8e70-40ba-8497-dafbdd546eb4","j:workInProgressLanguages");
documentTypeToNode.put("be8a1aff-4dcd-47ee-b911-a99f5cbe46c5","j:workInProgressLanguages");
documentTypeToNode.put("72d22ce5-7b60-4024-823c-ea004206c393","j:workInProgressLanguages");
documentTypeToNode.put("08246ef2-840f-4e72-9a24-03d3f4c109e4","j:workInProgressLanguages");
documentTypeToNode.put("ff133cc6-8e40-4e1e-bed4-2b5aa57443e3","j:workInProgressLanguages");
documentTypeToNode.put("c7d27853-a715-4436-829c-e20d88749fee","j:workInProgressLanguages");
documentTypeToNode.put("8799c833-e171-47f5-be0f-ac13c2c6a195","j:workInProgressLanguages");
documentTypeToNode.put("befe1862-4d95-4965-a508-60ff487e85d7","j:workInProgressLanguages");
documentTypeToNode.put("ee786ee5-8f60-40a0-a420-a469ffe03129","j:workInProgressLanguages");
documentTypeToNode.put("651179a9-81e1-43d7-a98d-d319619bee3d","j:workInProgressLanguages");
documentTypeToNode.put("259d15f0-9467-4329-bad3-85d3de886c38","j:workInProgressLanguages");
documentTypeToNode.put("50444c45-6ad8-45f9-813d-48907777ed56","j:workInProgressLanguages");
documentTypeToNode.put("e5350886-0458-4d33-a9de-d140beef473f","j:workInProgressLanguages");
documentTypeToNode.put("717157b3-7075-495c-b49c-d431ef3257b4","j:workInProgressLanguages");
documentTypeToNode.put("7c5e35c2-a1f8-43b1-bf45-6c3934ac4e82","j:workInProgressLanguages");
documentTypeToNode.put("dc043068-9e0d-4122-93e1-a5f0623217cc","j:workInProgressLanguages");
documentTypeToNode.put("fad84a7d-69a3-4269-b161-dbcc53863846","j:workInProgressLanguages");
documentTypeToNode.put("21b3aef4-1984-41a1-ba18-488d00f21fd4","j:workInProgressLanguages");
documentTypeToNode.put("c4fa0b31-b0c0-4bfb-813d-135e6810eac5","j:workInProgressLanguages");
documentTypeToNode.put("af23ff65-ccab-4a83-903d-aff52229836f","j:workInProgressLanguages");
documentTypeToNode.put("0ab55adc-bed4-40a7-9afe-e528d738c5f9","j:workInProgressLanguages");
documentTypeToNode.put("8f924481-8bca-4f86-b1bf-da66f4e3db4a","j:workInProgressLanguages");
documentTypeToNode.put("c57c58bb-40e8-4016-b20b-1ea0d34dd21e","j:workInProgressLanguages");
documentTypeToNode.put("7806025f-9143-4544-90b4-1421180dfde4","j:workInProgressLanguages");
documentTypeToNode.put("4527e15f-2fae-4b61-98e3-38b5f832440a","j:workInProgressLanguages");
documentTypeToNode.put("0fb86846-f2a9-4483-bb7e-a68e0ba3fce7","j:workInProgressLanguages");
documentTypeToNode.put("02e3b53e-8f9f-4f77-a94f-b330a793932b","j:workInProgressLanguages");
documentTypeToNode.put("6dce457d-c12e-4b0b-86c6-4f86b3bc5c0a","j:workInProgressLanguages");
documentTypeToNode.put("e4c7e1f6-fa15-4942-ba67-4ce2c5bc8b80","j:workInProgressLanguages");
documentTypeToNode.put("077329f6-c9ea-47b6-b084-86dc62c64b78","j:workInProgressLanguages");
documentTypeToNode.put("196064d8-11ff-4205-a063-b3661280679f","j:workInProgressLanguages");
documentTypeToNode.put("95b271cd-fe63-4d84-bd1b-5f8617ed66ac","j:workInProgressLanguages");
documentTypeToNode.put("b934189b-0159-4b63-af85-dd4e128b1f0b","j:workInProgressLanguages");
documentTypeToNode.put("0d78bf7f-88c4-4157-b88c-70f6cdb33b43","j:workInProgressLanguages");
documentTypeToNode.put("120de647-4084-4593-b909-ee149af736a4","j:workInProgressLanguages");
documentTypeToNode.put("7cbcc44b-5da7-4474-8697-4c093e9ca2a8","j:workInProgressLanguages");
documentTypeToNode.put("27a2be32-e68f-47f8-81ed-6da5108f4db9","j:workInProgressLanguages");
documentTypeToNode.put("58aa5a4d-e5ac-47d8-a7a0-ce1e5561684a","j:workInProgressLanguages");
documentTypeToNode.put("5800aab6-95d5-4cad-ab1c-497a8b097a60","j:workInProgressLanguages");
documentTypeToNode.put("b00f3933-4678-42cd-9cc1-de757b04d61c","j:workInProgressLanguages");
documentTypeToNode.put("50a3bde1-7d13-4a1f-b1d7-5a70cff46a17","j:workInProgressLanguages");
documentTypeToNode.put("54fbf304-6c86-4f3e-9f03-902041d0d997","j:workInProgressLanguages");
documentTypeToNode.put("ed0c46b2-1d2a-41f7-9f7c-47d8ad53e534","j:workInProgressLanguages");
documentTypeToNode.put("34b70058-d40d-474b-b38b-2b3f883f8eee","j:workInProgressLanguages");
documentTypeToNode.put("c1688cf3-a7e0-495e-af54-205841bd28a3","j:workInProgressLanguages");
documentTypeToNode.put("9b36cfcf-2ac2-45a7-a8cb-a45691680be5","j:workInProgressLanguages");
documentTypeToNode.put("7f1fce22-df93-43e3-9820-3e950c5df09b","j:workInProgressLanguages");
documentTypeToNode.put("92f28870-6ed7-4cbd-a100-97c94734fc0f","j:workInProgressLanguages");
documentTypeToNode.put("becbb136-6c3b-44e4-a13c-808b6bde12ef","j:workInProgressLanguages");
documentTypeToNode.put("8c955efb-6aef-4dd5-a0ea-ae41821ab1ee","j:workInProgressLanguages");
documentTypeToNode.put("afa1225d-d8af-46e5-90b6-ac4b59dd4227","j:workInProgressLanguages");
documentTypeToNode.put("810f0659-f16b-43c6-8eec-71d83353d76c","j:workInProgressLanguages");
documentTypeToNode.put("645e7051-fa4b-4168-81d1-448f895f7210","j:workInProgressLanguages");
documentTypeToNode.put("e76ecf5b-0e93-4bf6-8366-27f0b60caf50","j:workInProgressLanguages");
documentTypeToNode.put("a0d98940-ceca-4f13-9780-8f08572b1c88","j:workInProgressLanguages");
documentTypeToNode.put("955c15a7-4df8-4610-afd3-2c4bbade87aa","j:workInProgressLanguages");
documentTypeToNode.put("5c9e369b-688f-4af7-bdaf-510c6ab2bc9b","j:workInProgressLanguages");
documentTypeToNode.put("5a516329-e379-46a0-97d3-6ae557ed4190","j:workInProgressLanguages");
documentTypeToNode.put("425375eb-fd75-47d9-bedd-ad40d90e6b12","j:workInProgressLanguages");
documentTypeToNode.put("767971ba-7fd8-41c0-842c-ba003e1ac5c1","j:workInProgressLanguages");
documentTypeToNode.put("4e2cd84e-0b48-4b48-98ff-f7bbe3e5e679","j:workInProgressLanguages");
documentTypeToNode.put("535c8326-3bed-4e21-bb41-9b9bfbf24e12","j:workInProgressLanguages");
documentTypeToNode.put("9953287b-1a82-4676-b169-55ca90c259c0","j:workInProgressLanguages");
documentTypeToNode.put("96e40fbf-3c65-4a26-942c-a6250568973e","j:workInProgressLanguages");
documentTypeToNode.put("7639d72a-0c25-492b-8f1a-068638076d09","j:workInProgressLanguages");
documentTypeToNode.put("81f050df-aa72-469b-acbb-80a013f17d09","j:workInProgressLanguages");


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
                    } catch (RepositoryException e) {
                        logger.warn(e.getMessage() + " on " + node.getPath() + " " + uuid);
                    }
                    try {
                        node.getProperty("j:workInProgressStatus").remove();
                    } catch (RepositoryException e) {
                        logger.warn(e.getMessage() + " on " + node.getPath() + " " + uuid);
                    }
                    node.getSession().save();
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



