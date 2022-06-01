
# Academy templates v5

A few search and replace are mandatory to migrate from bootstrap 3 to bootstrap 5.
Here is the list of search replace to do on the repository.xml (default + live)

- bootstrap3nt:columns -> bootstrap5nt:grid
- bootstrap5mix:customColumns -> bootstrap5mix:customGrid
- bootstrap5mix:predefinedColumns -> bootstrap5mix:predefinedGrid
- bootstrap5mix:siteLogo / siteLogo ->  bootstrap5mix:siteBrand / brandImage
- bootstrap5mix:advancedModal / size -> bootstrap5mix:modal -> modalSize
- bootstrap3nt:modal -> bootstrap5nt:button + add mixin bootstrap5mix:modal
- columnsType -> typeOfGrid
- typeOfGrid="predefinedColumns" -> typeOfGrid="predefinedGrid"
- typeOfGrid="customColumns" -> typeOfGrid="customGrid"
- gridLayout -> containerType
- fixed-width -> container
- full-width -> container-fluid
- fadeEffect -> fade
- navigation="tab" -> type="tab"
- navigation="pill" -> type="pill"
- useSystemNameAsAnchor -> useListNameAsAnchor
- tabsPosition="top" -> align="justify-content-start"

Also a few remove are mandatory

- remove navJustified="false"
- remove navStacked="false"
- remove state="primary"

And a few things to check:

- For lines with bootstrap5mix:customGrid check if there is a bootstrap5mix:createRow
- For lines with bootstrap5mix:predefinedGrid check if there is a bootstrap5mix:createRow
- For blocks with typeOfGrid="nogrid" check if there is a bootstrap5mix:createRow
- On jnt:virtualsite node, remove modal property

## how to upgrade the repository.xml

Here is a few search / replace that do the job:

```
sed 's/bootstrap3mix/bootstrap5mix/g' repository.xml > new.xml && mv new.xml repository.xml
sed 's/bootstrap3nt:columns/bootstrap5nt:grid/g' repository.xml > new.xml && mv new.xml repository.xml
sed 's/bootstrap5mix:customColumns/bootstrap5mix:customGrid/g' repository.xml > new.xml && mv new.xml repository.xml
sed 's/bootstrap5mix:predefinedColumns/bootstrap5mix:predefinedGrid/g' repository.xml > new.xml && mv new.xml repository.xml
sed 's/columnsType/typeOfGrid/g' repository.xml > new.xml && mv new.xml repository.xml
sed 's/typeOfGrid="predefinedColumns"/typeOfGrid="predefinedGrid"/g' repository.xml > new.xml && mv new.xml repository.xml
sed 's/typeOfGrid="customColumns"/typeOfGrid="customGrid"/g' repository.xml > new.xml && mv new.xml repository.xml
sed 's/gridLayout/containerType/g' repository.xml > new.xml && mv new.xml repository.xml
sed 's/fixed-width/container/g' repository.xml > new.xml && mv new.xml repository.xml
sed 's/full-width/container-fluid/g' repository.xml > new.xml && mv new.xml repository.xml
sed 's/fadeEffect/fade/g' repository.xml > new.xml && mv new.xml repository.xml
sed 's/navigation="tab"/type="tab"/g' repository.xml > new.xml && mv new.xml repository.xml
sed 's/navigation="pill"/type="pill"/g' repository.xml > new.xml && mv new.xml repository.xml
sed 's/navJustified="false"//g' repository.xml > new.xml && mv new.xml repository.xml
sed 's/navStacked="false"//g' repository.xml > new.xml && mv new.xml repository.xml
sed 's/state="primary"//g' repository.xml > new.xml && mv new.xml repository.xml
sed 's/tabsPosition="top"/align="justify-content-start"/g' repository.xml > new.xml && mv new.xml repository.xml
sed 's/useSystemNameAsAnchor/useListNameAsAnchor/g' repository.xml > new.xml && mv new.xml repository.xml
sed 's/bootstrap5mix:siteLogo/bootstrap5mix:siteBrand/g' repository.xml > new.xml && mv new.xml repository.xml
sed 's/siteLogo/brandImage/g' repository.xml > new.xml && mv new.xml repository.xml
sed 's/bootstrap5mix:advancedModal/bootstrap5mix:modal/g' repository.xml > new.xml && mv new.xml repository.xml
sed 's/size="lg"/modalSize="lg"/g' repository.xml > new.xml && mv new.xml repository.xml
sed 's/size="default"/modalSize="default"/g' repository.xml > new.xml && mv new.xml repository.xml
sed 's/bootstrap3nt:modal/bootstrap5nt:button/g' repository.xml > new.xml && mv new.xml repository.xml
sed 's/bootstrap3nt/bootstrap5nt/g' repository.xml > new.xml && mv new.xml repository.xml
sed 's/bootstrap5mix:createContainer/bootstrap5mix:createContainer bootstrap5mix:createRow/g' repository.xml > new.xml && mv new.xml repository.xml
sed 's/bootstrap5mix:createContainer bootstrap5mix:createRow bootstrap5mix:createRow/bootstrap5mix:createContainer bootstrap5mix:createRow/g' repository.xml > new.xml && mv new.xml repository.xml
sed 's/jacademix:hideNavbuttons//g' repository.xml > new.xml && mv new.xml repository.xml
sed 's/jacademix:isMultiplePageDoc//g' repository.xml > new.xml && mv new.xml repository.xml
sed 's/bootstrap5nt:textBox/jacademy:textBox/g' repository.xml > new.xml && mv new.xml repository.xml
```

For lines with bootstrap5mix:predefinedGrid check if there is a bootstrap5mix:createRow (found 4)

```    cat -n repository.xml|grep bootstrap5mix:predefinedGrid|grep -v bootstrap5mix:createRow```


## Import
Befor importing, in `site.properties`, change `installedModules.16=bootstrap3-components` to `installedModules.16=bootstrap5-components`(maybe the id is not 16)

THen you can import

After that, you need to play 2 scripts (play it until there are no more errors):

- grid-refactoring.groovy
- grid-rename.groovy
- tooltips.groovy
- search-for-row-eq-height.groovy

This may be usefull if we remove the custom hidePage

Search for jacademix:hidePage
--> add j:displayInMenuName property with value hide-this-page

This can be done with this script

```
import org.jahia.api.Constants
import org.jahia.services.content.*
import org.jahia.services.sites.JahiaSite

import javax.jcr.NodeIterator
import javax.jcr.PathNotFoundException
import javax.jcr.RepositoryException
import javax.jcr.query.Query

String mixin = "jacademix:hidePage";
def JahiaSite site = org.jahia.services.sites.JahiaSitesService.getInstance().getSiteByKey("academy");

for (Locale locale : site.getLanguagesAsLocales()) {
    JCRTemplate.getInstance().doExecuteWithSystemSession(null, Constants.EDIT_WORKSPACE, locale, new JCRCallback() {
        @Override
        Object doInJCR(JCRSessionWrapper session) throws RepositoryException {
            def q = "SELECT * FROM [" + mixin + "]";

            NodeIterator iterator = session.getWorkspace().getQueryManager().createQuery(q, Query.JCR_SQL2).execute().getNodes();
            while (iterator.hasNext()) {
                final JCRNodeWrapper node = (JCRNodeWrapper) iterator.nextNode();
                node.addMixin("jmix:navMenu");
                JCRPropertyWrapper platformVersionsProperty = null;
                try {
                    platformVersionsProperty = node.getProperty("j:displayInMenuName");
                    try {
                        platformVersionsProperty.addValue("hide-this-page");
                    } catch (javax.jcr.ValueFormatException e) {
                    }
                    node.setProperty("j:displayInMenuName", platformVersionsProperty.getValues());
                    logger.info(node.path);
                    session.save();
                } catch (PathNotFoundException pnf) {

                }

            }
            return null;
        }
    }
    );
}

```










