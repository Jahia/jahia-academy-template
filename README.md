# jahia-academy-template
This templateset will contain the layout and styles for Jahia Academy

## Post installation

Add this into your `<INSTALL_DIR>/digital-factory-config/jahia/applicationcontext-custom.xml` file to support Lity (Lightweight, accessible and responsive lightbox. http://sorgalla.com/lity/
)

    <bean id="org.jahia.services.render.filter.URLTraverser" class="org.jahia.services.render.filter.HtmlTagAttributeTraverser">
        <constructor-arg>
            <map>
                <entry key="a">
                    <set>
                        <value>href</value>
                    </set>
                </entry>
                <entry key="area">
                    <set>
                        <value>href</value>
                    </set>
                </entry>
                <entry key="embed">
                    <set>
                        <value>src</value>
                    </set>
                </entry>
                <entry key="form">
                    <set>
                        <value>action</value>
                    </set>
                </entry>
                <entry key="img">
                    <set>
                        <value>src</value>
                        <value>data-lity-target</value>
                    </set>
                </entry>
                <entry key="link">
                    <set>
                        <value>href</value>
                    </set>
                </entry>
                <entry key="param">
                    <set>
                        <value>value</value>
                    </set>
                </entry>
                <entry key="div">
                    <set>
                        <value>data-href</value>
                    </set>
                </entry>
            </map>
        </constructor-arg>
    </bean>