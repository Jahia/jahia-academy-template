package  scripts;
org.jahia.services.content.JCRTemplate.getInstance().doExecuteWithSystemSession(null, org.jahia.api.Constants.EDIT_WORKSPACE, new org.jahia.services.content.JCRCallback() {
    @Override
    Object doInJCR(org.jahia.services.content.JCRSessionWrapper session) throws javax.jcr.RepositoryException {

        List<org.jahia.services.content.nodetypes.initializers.ChoiceListValue> listValues = new ArrayList<org.jahia.services.content.nodetypes.initializers.ChoiceListValue>();

        String stmt = "SELECT * FROM [jacademix:isVersionPage]";
        javax.jcr.query.Query query = session.getWorkspace().getQueryManager().createQuery(stmt, javax.jcr.query.Query.JCR_SQL2);
        javax.jcr.NodeIterator nodeIterator = query.execute().getNodes();

        while (nodeIterator.hasNext()) {
            org.jahia.services.content.JCRNodeWrapper nodeWrapper = (org.jahia.services.content.JCRNodeWrapper) nodeIterator.next();

            String version = nodeWrapper.getDisplayableName();
            String product = nodeWrapper.getParent().getDisplayableName();


            //println("add  [" + product + " " + version + "] to listValues with value " + nodeWrapper.getIdentifier());
            listValues.add(new org.jahia.services.content.nodetypes.initializers.ChoiceListValue(product + " " + version, nodeWrapper.getIdentifier()));
        }
        return listValues;
    }
})
