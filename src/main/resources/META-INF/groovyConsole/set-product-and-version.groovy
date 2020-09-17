import org.jahia.api.Constants
import org.jahia.services.content.*
import org.jahia.services.sites.JahiaSite
import org.jahia.taglibs.jcr.node.JCRTagUtils

import javax.jcr.NodeIterator
import javax.jcr.PathNotFoundException
import javax.jcr.RepositoryException
import javax.jcr.query.Query

def logger = log;

def JahiaSite site = org.jahia.services.sites.JahiaSitesService.getInstance().getSiteByKey("academy");

Set<String> nodesToAutoPublish = new HashSet<String>();
// list of path to ignore for any reason...
Set<String> pathToIgnore = new HashSet<String>();
pathToIgnore.add("/sites/academy/home/training--kb");
boolean doIt = true;

HashMap<String, String> productToUUID = new HashMap<String, String>();
HashMap<String, String> versionToUUID = new HashMap<String, String>();
HashMap<String, String> personaToUUID = new HashMap<String, String>();


for (Locale locale : site.getLanguagesAsLocales()) {
    JCRTemplate.getInstance().doExecuteWithSystemSession(null, Constants.EDIT_WORKSPACE, locale, new JCRCallback() {
        @Override
        Object doInJCR(JCRSessionWrapper session) throws RepositoryException {
            JCRNodeWrapper productRootCategoryNode = null;
            try {
                productRootCategoryNode = session.getNode("/sites/systemsite/categories/products");
            } catch (javax.jcr.PathNotFoundException e) {
                JCRNodeWrapper rootCategoryNode = session.getNode("/sites/systemsite/categories");
                productRootCategoryNode = rootCategoryNode.addNode("products", "jnt:category");
                productRootCategoryNode.setProperty("jcr:title", "Products");
                session.save();
            }
            JCRNodeWrapper versionRootCategoryNode = null;
            try {
                versionRootCategoryNode = session.getNode("/sites/systemsite/categories/versions");
            } catch (javax.jcr.PathNotFoundException e) {
                JCRNodeWrapper rootCategoryNode = session.getNode("/sites/systemsite/categories");
                versionRootCategoryNode = rootCategoryNode.addNode("versions", "jnt:category");
                versionRootCategoryNode.setProperty("jcr:title", "Versions");
                session.save();
            }

            JCRNodeWrapper personasRootCategoryNode = null;
            try {
                personasRootCategoryNode = session.getNode("/sites/systemsite/categories/personas");
            } catch (javax.jcr.PathNotFoundException e) {
                JCRNodeWrapper rootCategoryNode = session.getNode("/sites/systemsite/categories");
                personasRootCategoryNode = rootCategoryNode.addNode("personas", "jnt:category");
                personasRootCategoryNode.setProperty("jcr:title", "Personas");
                session.save();
            }

            JCRNodeWrapper endUserCategoryNode = null;
            try {
                endUserCategoryNode = session.getNode("/sites/systemsite/categories/personas/end-user");
            } catch (javax.jcr.PathNotFoundException e) {
                endUserCategoryNode = personasRootCategoryNode.addNode("end-user", "jnt:category");
                endUserCategoryNode.setProperty("jcr:title", "End user");
                session.save();
            }
            personaToUUID.put("end-user",endUserCategoryNode.getIdentifier());

            JCRNodeWrapper systemAdministratorCategoryNode = null;
            try {
                systemAdministratorCategoryNode = session.getNode("/sites/systemsite/categories/personas/system-administrator");
            } catch (javax.jcr.PathNotFoundException e) {
                systemAdministratorCategoryNode = personasRootCategoryNode.addNode("system-administrator", "jnt:category");
                systemAdministratorCategoryNode.setProperty("jcr:title", "System Administrator");
                session.save();
            }
            personaToUUID.put("system-administrator",systemAdministratorCategoryNode.getIdentifier());

            JCRNodeWrapper developerCategoryNode = null;
            try {
                developerCategoryNode = session.getNode("/sites/systemsite/categories/personas/developer");
            } catch (javax.jcr.PathNotFoundException e) {
                developerCategoryNode = personasRootCategoryNode.addNode("developer", "jnt:category");
                developerCategoryNode.setProperty("jcr:title", "Developer");
                session.save();
            }
            personaToUUID.put("developer",developerCategoryNode.getIdentifier());

            // check category tree
            if (productRootCategoryNode != null && versionRootCategoryNode != null) {
                def q = "SELECT * FROM [jacademix:isVersionPage]";

                NodeIterator iterator = session.getWorkspace().getQueryManager().createQuery(q, Query.JCR_SQL2).execute().getNodes();
                while (iterator.hasNext()) {
                    final JCRNodeWrapper versionNode = (JCRNodeWrapper) iterator.nextNode();

                    Iterator<String> it = pathToIgnore.iterator();
                    boolean ignoreThisNode = false;
                    while (it.hasNext()) {
                        String ignorePath = it.next();
                        if (versionNode.getPath().contains(ignorePath)) {
                            ignoreThisNode = true;
                            break
                        }
                    }
                    if (!ignoreThisNode) {
                        String version = versionNode.getDisplayableName();
                        String product = versionNode.getParent().getDisplayableName();
                        String productName = product.replace(" ", "-").toLowerCase();
                        String versionName = productName + "-" + version.replace(".", "-").toLowerCase(); // marketing-factory-8-0

                        // try to find a category for this product
                        try {
                            JCRNodeWrapper productCategoryNode = session.getNode(productRootCategoryNode.getPath() + "/" + productName);
                            productToUUID.put(product, productCategoryNode.getIdentifier());
                        } catch (javax.jcr.PathNotFoundException e) {
                            logger.info("Create new category " + productRootCategoryNode.getPath() + "/" + productName + " with title [" + product + "]");
                            productRootCategoryNode.addNode(productName, "jnt:category");
                            JCRNodeWrapper productCategoryNode = session.getNode(productRootCategoryNode.getPath() + "/" + productName);
                            productCategoryNode.setProperty("jcr:title", product);
                            productToUUID.put(product, productCategoryNode.getIdentifier());
                            session.save();
                        }
                        try {
                            JCRNodeWrapper versionCategoryNode = session.getNode(versionRootCategoryNode.getPath() + "/" + versionName);
                            versionToUUID.put(product + version, versionCategoryNode.getIdentifier());
                        } catch (javax.jcr.PathNotFoundException e) {
                            logger.info("Create new category " + versionRootCategoryNode.getPath() + "/" + versionName + " with title [" + product + " " + version + "]");
                            versionRootCategoryNode.addNode(versionName, "jnt:category");
                            JCRNodeWrapper versionCategoryNode = session.getNode(versionRootCategoryNode.getPath() + "/" + versionName);
                            versionCategoryNode.setProperty("jcr:title", product + " " + version);
                            versionToUUID.put(product + version, versionCategoryNode.getIdentifier());
                            session.save();
                        }
                    }
                }
            }


/*
			JCRPropertyWrapper permissions = role.getProperty("j:permissionNames");
			JCRValueWrapper[] permissionNames = permissions.getValues();
			boolean permissionAlreadySet = false;
			for (JCRValueWrapper permName : permissionNames) {
				if(permName.getString().equals(permissionName)) {
					permissionAlreadySet = true;
				}
			}
			if (!permissionAlreadySet) {
			     permissions.addValue(permissionName);
			     role.setProperty("j:permissionNames", permissions.getValues());
			     role.saveSession();
			     log.info("Permission " + permissionName + " set for role " + roleName);
			} else {
				log.info("Role " + roleName + " has already the permission " + permissionName);
			}

 */

            // add version + product + persona to nodes
            if (true && productRootCategoryNode != null && versionRootCategoryNode != null) {

                def q = "SELECT * FROM [jacademix:isVersionPage]";

                NodeIterator iterator = session.getWorkspace().getQueryManager().createQuery(q, Query.JCR_SQL2).execute().getNodes();
                while (iterator.hasNext()) {
                    final JCRNodeWrapper versionNode = (JCRNodeWrapper) iterator.nextNode();

                    Iterator<String> it = pathToIgnore.iterator();
                    boolean ignoreThisNode = false;
                    while (it.hasNext()) {
                        String ignorePath = it.next();
                        if (versionNode.getPath().contains(ignorePath)) {
                            ignoreThisNode = true;
                            break
                        }
                    }
                    if (!ignoreThisNode) {
                        String product = versionNode.getParent().getDisplayableName();
                        String version = versionNode.getDisplayableName();
                        boolean isLatestVersion = "current".equals(versionNode.getPropertyAsString("version"));
                        String productUUID = productToUUID.get(product);
                        String versionUUID = versionToUUID.get(product + version);
                        logger.info("Check subpages of " + versionNode.getPath() +  "[" + product + "] [" + version + "]");
                        if (true) {
                            // now we get sub jacademix:document and set the version persona and product
                            def q2 = "SELECT * FROM [jacademix:document] WHERE ISDESCENDANTNODE ('" + versionNode.getPath() + "')";
                            NodeIterator iterator2 = session.getWorkspace().getQueryManager().createQuery(q2, Query.JCR_SQL2).execute().getNodes();
                            while (iterator2.hasNext()) {
                                final JCRNodeWrapper documentNode = (JCRNodeWrapper) iterator2.nextNode();
                                JCRNodeWrapper parentPage = documentNode.getParent();
                                if (!parentPage.isNodeType("jnt:page")) {
                                    parentPage = parentPage.getParent();
                                }
                                if (!parentPage.isNodeType("jnt:page")) {
                                    parentPage = parentPage.getParent();
                                }
                                if (parentPage.isNodeType("jnt:page")) {
                                    String parentPagePath = parentPage.getPath()
                                    String persona = null;
                                    if (parentPagePath.contains("end-user")) {
                                        persona = "end-user";
                                    } else if (parentPagePath.contains("system-administrator")) {
                                        persona = "system-administrator";
                                    } else if (parentPagePath.contains("developer")) {
                                        persona = "developer";
                                    } else {
                                        persona = "unknown";
                                    }

                                    logger.info("  " +parentPage.getPath());
                                    if (!parentPage.isNodeType("jacademix:metadatas")) {
                                        logger.info("    Add mixin jacademix:metadatas on " + parentPage.getPath());
                                        parentPage.addMixin("jacademix:metadatas");
                                        parentPage.saveSession();
                                    }
                                    logger.info("     - Set latestversion to " + isLatestVersion);
                                    parentPage.setProperty("latestVersion",isLatestVersion);
                                    boolean productAlreadySet = false;
                                    JCRPropertyWrapper productProperty = null;
                                    try {
                                        productProperty = parentPage.getProperty("products");
                                        // bypass loop

                                        JCRValueWrapper[] productValues = productProperty.getValues()
                                        for (JCRValueWrapper productValue : productValues) {
                                            //logger.info("Found productValue " + productValue);
                                            if (productValue.getString().equals(productUUID)) {
                                                productAlreadySet = true;
                                            }
                                        }

                                    } catch (PathNotFoundException e) {
                                        // prop do not exists... create it
                                        parentPage.setProperty("products", new JCRValueWrapper[0]);
                                        productProperty = parentPage.getProperty("products");
                                    }
                                    if (!productAlreadySet) {
                                        try {
                                            productProperty.addValue(productUUID);
                                        } catch (javax.jcr.ValueFormatException e) {
                                            logger.info("    Error when trying to set value " + productUUID + " (" + product + ") on " + parentPagePath);
                                        }

                                        parentPage.setProperty("products", productProperty.getValues());
                                        if (doIt) {
                                            parentPage.saveSession();
                                        }
                                        logger.info("     - Add product [" + product + "] -> done");
                                    } else {
                                        logger.info("     - Add product [" + product + "] -> already set");
                                    }

                                    boolean  versionAlreadySet = false;
                                    JCRPropertyWrapper  versionProperty = null;
                                    try {
                                        versionProperty = parentPage.getProperty("versions");
                                        // bypass loop

                                        JCRValueWrapper[]  versionValues =  versionProperty.getValues()
                                        for (JCRValueWrapper  versionValue : versionValues) {
                                            //logger.info("Found  versionValue " +  versionValue);
                                            if ( versionValue.getString().equals(versionUUID)) {
                                                versionAlreadySet = true;
                                            }
                                        }

                                    } catch (PathNotFoundException e) {
                                        // prop do not exists... create it
                                        parentPage.setProperty("versions", new JCRValueWrapper[0]);
                                        versionProperty = parentPage.getProperty("versions");
                                    }
                                    if (! versionAlreadySet) {
                                        try {
                                            versionProperty.addValue( versionUUID);
                                        } catch (javax.jcr.ValueFormatException e) {
                                            logger.info("Error when trying to set value " + versionUUID + " (" + version + ") on " + parentPagePath);
                                        }

                                        parentPage.setProperty("versions", versionProperty.getValues());
                                        if (doIt) {
                                            parentPage.saveSession();
                                        }
                                        logger.info("     - Add version [" + version + "] -> done");
                                    } else {
                                        logger.info("     - Add version [" + version + "] -> already set");
                                    }


                                    if (! "unknown".equals(persona)) {

                                        String personaUUID = personaToUUID.get(persona);

                                        boolean  personaAlreadySet = false;
                                        JCRPropertyWrapper  personaProperty = null;
                                        try {
                                            personaProperty = parentPage.getProperty("personas");

                                            JCRValueWrapper[]  personaValues =  personaProperty.getValues()
                                            for (JCRValueWrapper  personaValue : personaValues) {
                                                //logger.info("Found  personaValue " +  personaValue);
                                                if ( personaValue.getString().equals(personaUUID)) {
                                                    personaAlreadySet = true;
                                                }
                                            }

                                        } catch (PathNotFoundException e) {
                                            parentPage.setProperty("personas", new JCRValueWrapper[0]);
                                            personaProperty = parentPage.getProperty("personas");
                                        }
                                        if (! personaAlreadySet) {
                                            try {
                                                personaProperty.addValue( personaUUID);
                                            } catch (javax.jcr.ValueFormatException e) {
                                                logger.info("Error when trying to set value " + personaUUID + " (" + persona + ") on " + parentPagePath);
                                            }

                                            parentPage.setProperty("personas", personaProperty.getValues());
                                            if (doIt) {
                                                parentPage.saveSession();
                                            }
                                            logger.info("     - Add persona [" + persona + "] -> done");
                                        } else {
                                            logger.info("     - Add persona [" + persona + "] -> already set");
                                        }
                                    }




                                } else {
                                    logger.info("Could not find parent page for " + documentNode.getPath());
                                }

                            }
                        }

                    }
                }


/*
			JCRPropertyWrapper permissions = role.getProperty("j:permissionNames");
			JCRValueWrapper[] permissionNames = permissions.getValues();
			boolean permissionAlreadySet = false;
			for (JCRValueWrapper permName : permissionNames) {
				if(permName.getString().equals(permissionName)) {
					permissionAlreadySet = true;
				}
			}
			if (!permissionAlreadySet) {
			     permissions.addValue(permissionName);
			     role.setProperty("j:permissionNames", permissions.getValues());
			     role.saveSession();
			     log.info("Permission " + permissionName + " set for role " + roleName);
			} else {
				log.info("Role " + roleName + " has already the permission " + permissionName);
			}

 */
            }

            if (doIt) {
                session.save();
            }
            if (CollectionUtils.isNotEmpty(nodesToAutoPublish)) {
                if (doIt) {
                    JCRPublicationService.getInstance().publish(nodesToAutoPublish.asList(), Constants.EDIT_WORKSPACE, Constants.LIVE_WORKSPACE, null);
                };
                logger.info("");
                logger.info("Nodes which where republished:")
                for (String identifier : nodesToAutoPublish) {
                    logger.warn("   " + session.getNodeByIdentifier(identifier).getPath());
                }
            }

            return null;
        }
    }
    );
}


public boolean hasPendingModification(final JCRNodeWrapper node) throws RepositoryException {
    try {
        if (!Constants.EDIT_WORKSPACE.equals(node.getSession().getWorkspace().getName())) {
            throw new IllegalArgumentException("The node has to be accessed through a session opened on the default workspace");
        }
        return JCRTemplate.getInstance().doExecuteWithSystemSession(null, Constants.LIVE_WORKSPACE, null, new JCRCallback<Boolean>() {
            public Boolean doInJCR(JCRSessionWrapper session) throws RepositoryException {
                int status = JCRPublicationService.getInstance().getStatus(node, session, null);
                if (status == null) {
                    status = PublicationInfo.UNPUBLISHED;
                }
                return PublicationInfo.PUBLISHED != status;
            }
        });
    } catch (Exception e) {
        return false;
    }
}
