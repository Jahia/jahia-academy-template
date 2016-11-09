CKEDITOR.stylesSet.add('text', [
    { name:'Title', element:'h2', attributes:{"class":"doc-child title scrollspy-offset-fix", style:"box-sizing: border-box; color: rgb(45, 60, 87); font-size: 24px; margin-top: -172px;margin-bottom: 20px; font-family: Lato;" }},
    { name:'Subtitle', element:'h2', attributes:{"class":"doc-child subtitle scrollspy-offset-fix", style:"font-weight: normal; box-sizing: border-box; padding-top: 180px; margin-top: -180px; color: rgb(0, 160, 227); font-size: 20px; margin-bottom: 10px;" }},
    { name:'Text', element:'p', attributes:{"class":"doc-child", style:"box-sizing: border-box; margin: 0px 0px 15px; color: rgb(45, 60, 87); font-size: 18px; font-family: Lato;" }},
    { name:'Inline blue text', element:'span', attributes:{"class":"blue", style:"box-sizing: border-box; font-weight: 700; color: rgb(0, 160, 227);" }},
    { name:'Code', element:'code'}
]);

CKEDITOR.editorConfig = function( config ) {
    config.siteKey = (typeof contextJsParameters != 'undefined') ? contextJsParameters.siteKey: '';
    config.workspace = (typeof contextJsParameters != 'undefined') ? contextJsParameters.workspace: '';

    config.contentsCss = [ ((typeof contextJsParameters != 'undefined') ? contextJsParameters.contextPath : '') + '/files/' + config.workspace + '/sites/' + config.siteKey + '/files/bootstrap/css/bootstrap.css',((typeof contextJsParameters != 'undefined') ? contextJsParameters.contextPath : '') + '/modules/jahia-academy-template/css/academy.css'];
    config.templates_files = [ ((typeof contextJsParameters != 'undefined') ? contextJsParameters.contextPath : '') + '/modules/jahia-academy-template/javascript/academy/ck/cktemplates.js' ];

    config.stylesSet = 'text';

    config.justifyClasses = [ 'text-left', 'text-center', 'text-right', 'text-justify' ];
    config.templates_replaceContent = false;

    config.toolbar_Tiny = [
        ['Source', '-', 'Templates', 'PasteText','SpellChecker','Styles'],
        ['Bold','Italic'],
        ['NumberedList', 'BulletedList', 'Outdent','Indent', 'Blockquote'],
        ['JustifyLeft','JustifyCenter','JustifyRight'],
        ['Link', 'Unlink','Anchor','Image'],
        ['RemoveFormat','HorizontalRule','ShowBlocks']
    ];
    config.extraPlugins='codesnippet';
    config.codeSnippet_theme = 'googlecode';
    config.codeSnippet_languages = {
        javascript: 'JavaScript',
        php: 'PHP',
        java: 'Java',
        xml: 'XML',
        html: 'HTML'
    };
};
CKEDITOR.config.toolbar = 'Tiny';

CKEDITOR.dtd.$removeEmpty['i'] = 0;