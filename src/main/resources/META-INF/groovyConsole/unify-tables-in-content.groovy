    import org.jahia.api.Constants
    import org.jahia.services.content.*
    import org.jahia.services.sites.JahiaSite
    import org.jahia.taglibs.jcr.node.JCRTagUtils

    import javax.jcr.NodeIterator
    import javax.jcr.RepositoryException
    import javax.jcr.query.Query

    /* set to true to save the result */
    boolean doIt = false;

    /* siteKey */
    String siteKey = "academy";

    /* limit the search/replace to a certain path */
    String descendentnode = "/sites/academy";

    /* propertiesToLookAt is the list of nodeTypes/properties to search in */
    def propertiesToLookAt = new HashMap<String, Object>();
    propertiesToLookAt.put("jacademix:textContent",["textContent"]);
    propertiesToLookAt.put("jacademix:kbQa",["textContent","answer"]);
    propertiesToLookAt.put("jacademix:kbUseCase",["textContent","answer","cause"]);
    propertiesToLookAt.put("jacademy:textBox",["text"]);

    /* only search/replace in path that contains a pathRestriction */
    Set<String> pathRestriction = new HashSet<String>();
    pathRestriction.add("/");

    /* list of search / replace text */
    def searchReplace = new LinkedHashMap<String, String>();
    searchReplace.put("<table([^>]*)\\sclass=\"table\\s[^\"]*\"([^>]*)>", "<table class=\"table table-borderless table-striped\">");
    searchReplace.put("<table\\s+class=\"table\">","<table class=\"table table-borderless table-striped\">");

    def JahiaSite site = org.jahia.services.sites.JahiaSitesService.getInstance().getSiteByKey(siteKey);
    for (Locale locale : site.getLanguagesAsLocales()) {
        JCRTemplate.getInstance().doExecuteWithSystemSession(null, Constants.EDIT_WORKSPACE, locale, new JCRCallback() {
            @Override
            Object doInJCR(JCRSessionWrapper session) throws RepositoryException {

                for (String nt : propertiesToLookAt.keySet()) {
                    def props = propertiesToLookAt.get(nt);
                    for (String prop : props) {
                        def q = "select * from [" + nt + "] where isdescendantnode('" + descendentnode + "')";

                        NodeIterator iterator = session.getWorkspace().getQueryManager().createQuery(q, Query.JCR_SQL2).execute().getNodes();
                        while (iterator.hasNext()) {
                            final JCRNodeWrapper node = (JCRNodeWrapper) iterator.nextNode();
                            String nodePath = node.getPath();
                            if (isInPathRestriction(nodePath,pathRestriction)) {
                                if (updateNode(node, prop, searchReplace)) {
                                    if (doIt) {
                                        // publish
                                        List<String> uuidsToPublish = getAllSubIds(node, session);
                                        ServicesRegistry.getInstance().getJCRPublicationService().publish(uuidsToPublish, Constants.EDIT_WORKSPACE, Constants.LIVE_WORKSPACE, null);
                                    }
                                    //String URL = JCRTagUtils.getMeAndParentsOfType(node, "jnt:page")[0].getUrl();
                                    log.info("update [" + nt + "  " + prop + "] in path " + node.getPath() + " " );
                                }
                            }
                        }
                    }
                }
                if (doIt) {
                    session.save();
                }
                return null;
            }
        });
    }

    public boolean isInPathRestriction(String nodePath, Set<String> pathToCheck) {
        Iterator<Integer> iterator = pathToCheck.iterator();
        while(iterator.hasNext()) {
            if (nodePath.contains(iterator.next())) {
                return true;
            }
        }
        return false;
    }

    public boolean updateNode(JCRNodeWrapper node, String property, LinkedHashMap searchReplace) {
        String prop = node.getPropertyAsString(property);
        boolean needToBeUpdated = false;
        if (prop != null) {
            String tmp = updateContent(prop,searchReplace);
            if (!tmp.equals(prop)) {
                needToBeUpdated = true;
                node.setProperty(property, tmp);
            }
        }
        return needToBeUpdated;
    }

    public String updateContent(String str,LinkedHashMap searchReplace) {
        if (str == null) {
            return "";
        }
        for (String searchFrom : searchReplace.keySet()) {
            def searchTo = searchReplace.get(searchFrom);
            str = str.replaceAll(searchFrom, searchTo);
        }
        return str;
    }
    public List<String> getAllSubIds(JCRNodeWrapper node, JCRSessionWrapper session) throws RepositoryException {
        List<String> uuids = new ArrayList<String>();
        if (!node.isNodeType("j:acl")) {
            uuids.add(node.getIdentifier());

            JCRNodeIteratorWrapper nodes = node.getNodes();
            while (nodes.hasNext()) {
                JCRNodeWrapper subNode = (JCRNodeWrapper)nodes.next();
                uuids.addAll(getAllSubIds(subNode, session));
            }
        }
        return uuids;
    }
