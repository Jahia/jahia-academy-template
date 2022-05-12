<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>

<%--@elvariable id="currentNode" type="org.jahia.services.content.JCRNodeWrapper"--%>
<%--@elvariable id="renderContext" type="org.jahia.services.render.RenderContext"--%>


<aside class="sticky-top bg-light d-none d-sm-block vh-100">
    <nav class="collapse bd-links pt-4 sticky-top " id="bd-docs-nav" aria-label="Docs navigation">
        <%--<ul class="mb-0 py-2 pt-md-1">
            <li>
                <a class="d-inline-flex align-items-center rounded" href="#">
                    Getting started
                </a>
                <ul>
                    <li>
                        <a class="d-inline-flex align-items-center rounded" data-bs-toggle="collapse"
                           data-bs-target="#customize-collapse" aria-expanded="false">
                            Customize
                        </a>
                    </li>
                    <li>
                        <a class="d-inline-flex align-items-center rounded collapsed"
                                data-bs-toggle="collapse" data-bs-target="#layout-collapse"
                                aria-expanded="false" href="#">
                            Layout
                        </a>

                        <div class="collapse" id="layout-collapse">
                            <ul class="fw-normal pb-1 small">
                                <li><a href="/docs/5.1/layout/breakpoints/"
                                       class="d-inline-flex align-items-center rounded">Breakpoints</a>
                                </li>
                                <li><a href="/docs/5.1/layout/containers/"
                                       class="d-inline-flex align-items-center rounded">Containers</a>
                                </li>
                                <li><a href="/docs/5.1/layout/grid/"
                                       class="d-inline-flex align-items-center rounded">Grid</a></li>
                                <li><a href="/docs/5.1/layout/columns/"
                                       class="d-inline-flex align-items-center rounded">Columns</a></li>
                                <li><a href="/docs/5.1/layout/gutters/"
                                       class="d-inline-flex align-items-center rounded">Gutters</a></li>
                                <li><a href="/docs/5.1/layout/utilities/"
                                       class="d-inline-flex align-items-center rounded">Utilities</a>
                                </li>
                                <li><a href="/docs/5.1/layout/z-index/"
                                       class="d-inline-flex align-items-center rounded">Z-index</a></li>
                                <li><a href="/docs/5.1/layout/css-grid/"
                                       class="d-inline-flex align-items-center rounded">CSS Grid</a>
                                </li>
                            </ul>
                        </div>
                    </li>
                    <li>
                        <a class="btn d-inline-flex align-items-center rounded"
                                data-bs-toggle="collapse" data-bs-target="#content-collapse"
                                aria-expanded="true" aria-current="true" href="#">
                            Content
                        </a>

                        <div class="collapse show" id="content-collapse">
                            <ul class="fw-normal pb-1 small">
                                <li><a href="/docs/5.1/content/reboot/"
                                       class="d-inline-flex align-items-center rounded active"
                                       aria-current="page">Reboot</a></li>
                                <li><a href="/docs/5.1/content/typography/"
                                       class="d-inline-flex align-items-center rounded">Typography</a>
                                </li>
                                <li><a href="/docs/5.1/content/images/"
                                       class="d-inline-flex align-items-center rounded">Images</a></li>
                                <li><a href="/docs/5.1/content/tables/"
                                       class="d-inline-flex align-items-center rounded">Tables</a></li>
                                <li><a href="/docs/5.1/content/figures/"
                                       class="d-inline-flex align-items-center rounded">Figures</a></li>
                            </ul>
                        </div>
                    </li>
                    <li>
                        <a class="btn d-inline-flex align-items-center rounded collapsed"
                                data-bs-toggle="collapse" data-bs-target="#forms-collapse"
                                aria-expanded="false" href="#">
                            Forms
                        </a>

                        <div class="collapse" id="forms-collapse">
                            <ul class="fw-normal pb-1 small">
                                <li><a href="/docs/5.1/forms/overview/"
                                       class="d-inline-flex align-items-center rounded">Overview</a>
                                </li>
                                <li><a href="/docs/5.1/forms/form-control/"
                                       class="d-inline-flex align-items-center rounded">Form control</a>
                                </li>
                                <li><a href="/docs/5.1/forms/select/"
                                       class="d-inline-flex align-items-center rounded">Select</a></li>
                                <li><a href="/docs/5.1/forms/checks-radios/"
                                       class="d-inline-flex align-items-center rounded">Checks &amp;
                                    radios</a></li>
                                <li><a href="/docs/5.1/forms/range/"
                                       class="d-inline-flex align-items-center rounded">Range</a></li>
                                <li><a href="/docs/5.1/forms/input-group/"
                                       class="d-inline-flex align-items-center rounded">Input group</a>
                                </li>
                                <li><a href="/docs/5.1/forms/floating-labels/"
                                       class="d-inline-flex align-items-center rounded">Floating
                                    labels</a>
                                </li>
                                <li><a href="/docs/5.1/forms/layout/"
                                       class="d-inline-flex align-items-center rounded">Layout</a></li>
                                <li><a href="/docs/5.1/forms/validation/"
                                       class="d-inline-flex align-items-center rounded">Validation</a>
                                </li>
                            </ul>
                        </div>
                    </li>
                </ul>
            </li>
            <li>
                <a class="d-inline-flex align-items-center rounded collapsed" href="#">
                    Getting started 2
                </a>
                <ul>
                    <li>
                        <a class="btn d-inline-flex align-items-center rounded collapsed"
                                data-bs-toggle="collapse" data-bs-target="#components-collapse"
                                aria-expanded="false" href="#">
                            Components
                        </a>

                        <div class="collapse" id="components-collapse">
                            <ul class="fw-normal pb-1 small">
                                <li><a href="/docs/5.1/components/accordion/"
                                       class="d-inline-flex align-items-center rounded">Accordion</a>
                                </li>
                                <li><a href="/docs/5.1/components/alerts/"
                                       class="d-inline-flex align-items-center rounded">Alerts</a></li>
                                <li><a href="/docs/5.1/components/badge/"
                                       class="d-inline-flex align-items-center rounded">Badge</a></li>
                                <li><a href="/docs/5.1/components/breadcrumb/"
                                       class="d-inline-flex align-items-center rounded">Breadcrumb</a>
                                </li>
                                <li><a href="/docs/5.1/components/buttons/"
                                       class="d-inline-flex align-items-center rounded">Buttons</a></li>
                                <li><a href="/docs/5.1/components/button-group/"
                                       class="d-inline-flex align-items-center rounded">Button group</a>
                                </li>
                                <li><a href="/docs/5.1/components/card/"
                                       class="d-inline-flex align-items-center rounded">Card</a></li>
                                <li><a href="/docs/5.1/components/carousel/"
                                       class="d-inline-flex align-items-center rounded">Carousel</a>
                                </li>
                                <li><a href="/docs/5.1/components/close-button/"
                                       class="d-inline-flex align-items-center rounded">Close button</a>
                                </li>
                                <li><a href="/docs/5.1/components/collapse/"
                                       class="d-inline-flex align-items-center rounded">Collapse</a>
                                </li>
                                <li><a href="/docs/5.1/components/dropdowns/"
                                       class="d-inline-flex align-items-center rounded">Dropdowns</a>
                                </li>
                                <li><a href="/docs/5.1/components/list-group/"
                                       class="d-inline-flex align-items-center rounded">List group</a>
                                </li>
                                <li><a href="/docs/5.1/components/modal/"
                                       class="d-inline-flex align-items-center rounded">Modal</a></li>
                                <li><a href="/docs/5.1/components/navs-tabs/"
                                       class="d-inline-flex align-items-center rounded">Navs &amp;
                                    tabs</a>
                                </li>
                                <li><a href="/docs/5.1/components/navbar/"
                                       class="d-inline-flex align-items-center rounded">Navbar</a></li>
                                <li><a href="/docs/5.1/components/offcanvas/"
                                       class="d-inline-flex align-items-center rounded">Offcanvas</a>
                                </li>
                                <li><a href="/docs/5.1/components/pagination/"
                                       class="d-inline-flex align-items-center rounded">Pagination</a>
                                </li>
                                <li><a href="/docs/5.1/components/placeholders/"
                                       class="d-inline-flex align-items-center rounded">Placeholders</a>
                                </li>
                                <li><a href="/docs/5.1/components/popovers/"
                                       class="d-inline-flex align-items-center rounded">Popovers</a>
                                </li>
                                <li><a href="/docs/5.1/components/progress/"
                                       class="d-inline-flex align-items-center rounded">Progress</a>
                                </li>
                                <li><a href="/docs/5.1/components/scrollspy/"
                                       class="d-inline-flex align-items-center rounded">Scrollspy</a>
                                </li>
                                <li><a href="/docs/5.1/components/spinners/"
                                       class="d-inline-flex align-items-center rounded">Spinners</a>
                                </li>
                                <li><a href="/docs/5.1/components/toasts/"
                                       class="d-inline-flex align-items-center rounded">Toasts</a></li>
                                <li><a href="/docs/5.1/components/tooltips/"
                                       class="d-inline-flex align-items-center rounded">Tooltips</a>
                                </li>
                            </ul>
                        </div>
                    </li>
                    <li>
                        <a class="btn d-inline-flex align-items-center rounded collapsed"
                                data-bs-toggle="collapse" data-bs-target="#helpers-collapse"
                                aria-expanded="false" href="#">
                            Helpers
                        </a>

                        <div class="collapse" id="helpers-collapse">
                            <ul class="fw-normal pb-1 small">
                                <li><a href="/docs/5.1/helpers/clearfix/"
                                       class="d-inline-flex align-items-center rounded">Clearfix</a>
                                </li>
                                <li><a href="/docs/5.1/helpers/colored-links/"
                                       class="d-inline-flex align-items-center rounded">Colored
                                    links</a>
                                </li>
                                <li><a href="/docs/5.1/helpers/ratio/"
                                       class="d-inline-flex align-items-center rounded">Ratio</a></li>
                                <li><a href="/docs/5.1/helpers/position/"
                                       class="d-inline-flex align-items-center rounded">Position</a>
                                </li>
                                <li><a href="/docs/5.1/helpers/stacks/"
                                       class="d-inline-flex align-items-center rounded">Stacks</a></li>
                                <li><a href="/docs/5.1/helpers/visually-hidden/"
                                       class="d-inline-flex align-items-center rounded">Visually
                                    hidden</a>
                                </li>
                                <li><a href="/docs/5.1/helpers/stretched-link/"
                                       class="d-inline-flex align-items-center rounded">Stretched
                                    link</a>
                                </li>
                                <li><a href="/docs/5.1/helpers/text-truncation/"
                                       class="d-inline-flex align-items-center rounded">Text
                                    truncation</a>
                                </li>
                                <li><a href="/docs/5.1/helpers/vertical-rule/"
                                       class="d-inline-flex align-items-center rounded">Vertical
                                    rule</a>
                                </li>
                            </ul>
                        </div>
                    </li>
                    <li>
                        <a class="btn d-inline-flex align-items-center rounded collapsed"
                                data-bs-toggle="collapse" data-bs-target="#utilities-collapse"
                                aria-expanded="false" href="#">
                            Utilities
                        </a>

                        <div class="collapse" id="utilities-collapse">
                            <ul class="fw-normal pb-1 small">
                                <li><a href="/docs/5.1/utilities/api/"
                                       class="d-inline-flex align-items-center rounded">API</a></li>
                                <li><a href="/docs/5.1/utilities/background/"
                                       class="d-inline-flex align-items-center rounded">Background</a>
                                </li>
                                <li><a href="/docs/5.1/utilities/borders/"
                                       class="d-inline-flex align-items-center rounded">Borders</a></li>
                                <li><a href="/docs/5.1/utilities/colors/"
                                       class="d-inline-flex align-items-center rounded">Colors</a></li>
                                <li><a href="/docs/5.1/utilities/display/"
                                       class="d-inline-flex align-items-center rounded">Display</a></li>
                                <li><a href="/docs/5.1/utilities/flex/"
                                       class="d-inline-flex align-items-center rounded">Flex</a></li>
                                <li><a href="/docs/5.1/utilities/float/"
                                       class="d-inline-flex align-items-center rounded">Float</a></li>
                                <li><a href="/docs/5.1/utilities/interactions/"
                                       class="d-inline-flex align-items-center rounded">Interactions</a>
                                </li>
                                <li><a href="/docs/5.1/utilities/opacity/"
                                       class="d-inline-flex align-items-center rounded">Opacity</a></li>
                                <li><a href="/docs/5.1/utilities/overflow/"
                                       class="d-inline-flex align-items-center rounded">Overflow</a>
                                </li>
                                <li><a href="/docs/5.1/utilities/position/"
                                       class="d-inline-flex align-items-center rounded">Position</a>
                                </li>
                                <li><a href="/docs/5.1/utilities/shadows/"
                                       class="d-inline-flex align-items-center rounded">Shadows</a></li>
                                <li><a href="/docs/5.1/utilities/sizing/"
                                       class="d-inline-flex align-items-center rounded">Sizing</a></li>
                                <li><a href="/docs/5.1/utilities/spacing/"
                                       class="d-inline-flex align-items-center rounded">Spacing</a></li>
                                <li><a href="/docs/5.1/utilities/text/"
                                       class="d-inline-flex align-items-center rounded">Text</a></li>
                                <li><a href="/docs/5.1/utilities/vertical-align/"
                                       class="d-inline-flex align-items-center rounded">Vertical
                                    align</a>
                                </li>
                                <li><a href="/docs/5.1/utilities/visibility/"
                                       class="d-inline-flex align-items-center rounded">Visibility</a>
                                </li>
                            </ul>
                        </div>
                    </li>
                    <li>
                        <a class="btn d-inline-flex align-items-center rounded collapsed"
                                data-bs-toggle="collapse" data-bs-target="#extend-collapse"
                                aria-expanded="false" href="#">
                            Extend
                        </a>

                        <div class="collapse" id="extend-collapse">
                            <ul class="fw-normal pb-1 small">
                                <li><a href="/docs/5.1/extend/approach/"
                                       class="d-inline-flex align-items-center rounded">Approach</a>
                                </li>
                                <li><a href="/docs/5.1/extend/icons/"
                                       class="d-inline-flex align-items-center rounded">Icons</a></li>
                            </ul>
                        </div>
                    </li>
                    <li>
                        <a class="btn d-inline-flex align-items-center rounded collapsed"
                                data-bs-toggle="collapse" data-bs-target="#about-collapse"
                                aria-expanded="false" href="#">
                            About
                        </a>

                        <div class="collapse" id="about-collapse">
                            <ul class="fw-normal pb-1 small">
                                <li><a href="/docs/5.1/about/overview/"
                                       class="d-inline-flex align-items-center rounded">Overview</a>
                                </li>
                                <li><a href="/docs/5.1/about/team/"
                                       class="d-inline-flex align-items-center rounded">Team</a></li>
                                <li><a href="/docs/5.1/about/brand/"
                                       class="d-inline-flex align-items-center rounded">Brand</a></li>
                                <li><a href="/docs/5.1/about/license/"
                                       class="d-inline-flex align-items-center rounded">License</a></li>
                                <li><a href="/docs/5.1/about/translations/"
                                       class="d-inline-flex align-items-center rounded">Translations</a>
                                </li>
                            </ul>
                        </div>
                    </li>
                </ul>
            </li>

        </ul>
        --%>
    </nav>

</aside>
