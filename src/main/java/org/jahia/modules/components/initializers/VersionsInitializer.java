package org.jahia.modules.components.initializers;

import java.util.*;
import javax.jcr.NodeIterator;
import javax.jcr.PropertyType;
import javax.jcr.Value;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.RepositoryException;
import org.jahia.services.content.JCRNodeWrapper;
import org.jahia.services.content.JCRPropertyWrapper;
import org.jahia.services.content.JCRSessionFactory;
import org.jahia.services.content.nodetypes.ExtendedPropertyDefinition;
import org.jahia.services.content.nodetypes.initializers.ChoiceListValue;
import org.jahia.services.content.nodetypes.initializers.ModuleChoiceListInitializer;
import org.jahia.services.content.nodetypes.renderer.AbstractChoiceListRenderer;
import org.jahia.services.content.nodetypes.renderer.ModuleChoiceListRenderer;
import org.jahia.services.content.nodetypes.ValueImpl;
import org.jahia.services.render.RenderContext;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(name = "versionsInitializer", service = ModuleChoiceListInitializer.class, immediate = true)
public class VersionsInitializer extends AbstractChoiceListRenderer implements ModuleChoiceListInitializer, ModuleChoiceListRenderer {
    private String key = "versionsInitializer";
    private static final Logger logger = LoggerFactory.getLogger(VersionsInitializer.class);

    /**
     * {@inheritDoc}
     */
    public List<ChoiceListValue> getChoiceListValues(ExtendedPropertyDefinition epd, String param, List<ChoiceListValue> values,
                                                     Locale locale, Map<String, Object> context) {
        List<ChoiceListValue> listValues = new ArrayList<ChoiceListValue>();

        try {
            String stmt = "SELECT * FROM [jnt:category] where isdescendantnode('/sites/systemsite/categories/versions') order by ['j:nodename']";

            JCRSessionFactory session = JCRSessionFactory.getInstance();
            QueryManager qm = session.getCurrentUserSession().getWorkspace().getQueryManager();
            Query query = qm.createQuery(stmt, Query.JCR_SQL2);
            NodeIterator nodeIterator = query.execute().getNodes();

            while (nodeIterator.hasNext()) {
                JCRNodeWrapper nodeWrapper = (JCRNodeWrapper) nodeIterator.next();
                String version = nodeWrapper.getDisplayableName();
                logger.debug("Add new version in choicelist " + version);
                listValues.add(new ChoiceListValue(version, null, new ValueImpl(version, PropertyType.STRING, false)));
            }
        } catch (RepositoryException e) {
            logger.error(e.getMessage(), e);
        }
        return listValues;

    }

    /**
     * {@inheritDoc}
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * {@inheritDoc}
     */
    public String getKey() {
        return key;
    }

    /**
     * {@inheritDoc}
     */
    public String getStringRendering(RenderContext context, JCRPropertyWrapper propertyWrapper) throws RepositoryException {
        final StringBuilder sb = new StringBuilder();

        if (propertyWrapper.isMultiple()) {
            sb.append('{');
            final Value[] values = propertyWrapper.getValues();
            for (Value value : values) {
                sb.append('[').append(value.getString()).append(']');
            }
            sb.append('}');
        } else {
            sb.append('[').append(propertyWrapper.getValue().getString()).append(']');
        }

        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    public String getStringRendering(Locale locale, ExtendedPropertyDefinition propDef, Object propertyValue) throws RepositoryException {
        return "[" + propertyValue.toString() + "]";
    }
}

