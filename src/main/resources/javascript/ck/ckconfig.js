CKEDITOR.stylesSet.add('text', [
    {name: 'H2', element: 'h2'},
    {name: 'H3', element: 'h3'},
    {name: 'H4', element: 'h4'},
    {name: 'Text', element: 'p'},
    {name: 'Blue text', element: 'strong', attributes: {"class": "blue"}},
    {name: 'Code', element: 'code'},
    {name: 'Formated text', element: 'pre'},
    {name: 'Box success', element: 'div', attributes: {"class": "alert alert-success"}},
    {name: 'Box info', element: 'div', attributes: {"class": "alert alert-info"}},
    {name: 'Box warning', element: 'div', attributes: {"class": "alert alert-warning"}},
    {name: 'Box danger', element: 'div', attributes: {"class": "alert alert-danger"}},
    {name: 'Box grey', element: 'div', attributes: {"class": "well"}}
]);
CKEDITOR.plugins.addExternal('fontawesome', ((typeof contextJsParameters != 'undefined') ? contextJsParameters.contextPath : '') + '/modules/jahia-academy-template/javascript/ck/fontawesome/plugin.js');
CKEDITOR.plugins.addExternal('maximize', ((typeof contextJsParameters != 'undefined') ? contextJsParameters.contextPath : '') + '/modules/jahia-academy-template/javascript/ck/maximize/plugin.js');

CKEDITOR.editorConfig = function (config) {
    config.siteKey = (typeof contextJsParameters != 'undefined') ? contextJsParameters.siteKey : '';
    config.workspace = (typeof contextJsParameters != 'undefined') ? contextJsParameters.workspace : '';

    config.contentsCss = [((typeof contextJsParameters != 'undefined') ? contextJsParameters.contextPath : '') + '/modules/jahia-academy-template/css/bootstrap.min.css', ((typeof contextJsParameters != 'undefined') ? contextJsParameters.contextPath : '') + '/modules/highlightjs/css/foundation.css', ((typeof contextJsParameters != 'undefined') ? contextJsParameters.contextPath : '') + '/modules/highlightjs/css/clipboard.css', ((typeof contextJsParameters != 'undefined') ? contextJsParameters.contextPath : '') + '/modules/jahia-academy-template/css/academy.css', ((typeof contextJsParameters != 'undefined') ? contextJsParameters.contextPath : '') + '/modules/font-awesome/css/all.min.css', ((typeof contextJsParameters != 'undefined') ? contextJsParameters.contextPath : '') + '/modules/jahia-academy-template/css/academy.ck.css'  ];
    config.templates_files = [((typeof contextJsParameters != 'undefined') ? contextJsParameters.contextPath : '') + '/modules/jahia-academy-template/javascript/ck/cktemplates.js'];

    config.stylesSet = 'text';

    config.justifyClasses = ['text-left', 'text-center', 'text-right', 'text-justify'];
    config.templates_replaceContent = false;

    config.toolbar_Tiny = [
        ['Source', '-', 'Templates', 'PasteText', 'wsc', 'Scayt', 'ACheck', 'Styles'],
        ['Bold', 'Italic'],
        ['NumberedList', 'BulletedList', 'Outdent', 'Indent', 'Blockquote'],
        ['JustifyLeft', 'JustifyCenter', 'JustifyRight'],
        ['Link', 'Unlink', 'Anchor', 'Image','FontAwesome'],
        ['RemoveFormat', 'HorizontalRule', 'CodeSnippet'],
        ['Maximize']
    ];
    config.extraPlugins = 'acheck,wsc,scayt,macrosdropdown,codesnippet,fontawesome,maximize,autogrow';
    config.codeSnippet_theme = 'googlecode';
    config.codeSnippet_languages = {
        apache: 'Apache conf',
        bash: 'Bash',
        css: 'CSS',
        groovy: 'Groovy',
        html: 'HTML',
        ini: 'ini/properties',
        java: 'Java',
        javascript: 'JavaScript',
        json: 'JSON',
        php: 'PHP',
        sql:'SQL',
        xml: 'XML'

    };

};
CKEDITOR.config.toolbar = 'Tiny';
CKEDITOR.config.autoGrow_minHeight = 200;
CKEDITOR.config.autoGrow_maxHeight = 768;
CKEDITOR.config.autoGrow_bottomSpace = 50;
CKEDITOR.dtd.$removeEmpty['i'] = 0;
CKEDITOR.dtd.$removeEmpty['span'] = 0;
CKEDITOR.dtd.$removeEmpty['th'] = 0;
