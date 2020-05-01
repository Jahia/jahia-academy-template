CKEDITOR.addTemplates('default',
    {
        // The name of the subfolder that contains the preview images of the templates.
        imagesPath: ((typeof contextJsParameters != 'undefined') ? contextJsParameters.contextPath : '') + '/modules/jahia-academy-template/icons/templates/',
        // Template definitions.
        templates: [
            {
                title: 'Sample document',
                image: 'jacademix_component.png',
                description: 'This template can be used to start a document with some styles already set',
                html: '<h2>Title</h2><p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam eget urna orci. Ut condimentum sapien sit amet elit dictum ultrices. Proin finibus eros at arcu auctor sollicitudin. Integer nec quam egestas, malesuada ante non, euismod felis. Nullam non eros volutpat, luctus justo venenatis, molestie purus. In molestie purus ante, et viverra risus efficitur sit amet. In aliquam, nibh eget interdum luctus, eros quam facilisis justo, nec ultricies ex dolor vitae justo. Vivamus ac massa in diam sodales commodo.</p><h3>Subtitle</h3><p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam eget urna orci. Ut condimentum sapien sit amet elit dictum ultrices. Proin finibus eros at arcu auctor sollicitudin. Integer nec quam egestas, malesuada ante non, euismod felis. Nullam non eros volutpat, luctus justo venenatis, molestie purus. In molestie purus ante, et viverra risus efficitur sit amet. In aliquam, nibh eget interdum luctus, eros quam facilisis justo, nec ultricies ex dolor vitae justo. Vivamus ac massa in diam sodales commodo.</p><ol>	<li>Ordered</li>	<li>List</li></ol><ul>	<li>Unordered</li>	<li>List</li></ul><div class="alert alert-danger"><strong><span class="fas fa-exclamation-triangle fa-fw fa-fw "></span>&nbsp;Important</strong><br />Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam eget urna orci. Ut condimentum sapien sit amet elit dictum ultrices. Proin finibus eros at arcu auctor sollicitudin. Integer nec quam egestas, malesuada ante non, euismod felis. Nullam non eros volutpat, luctus justo venenatis, molestie purus. In molestie purus ante, et viverra risus efficitur sit amet. In aliquam, nibh eget interdum luctus, eros quam facilisis justo, nec ultricies ex dolor vitae justo. Vivamus ac massa in diam sodales commodo.</div><div class="alert alert-success"><strong><span class="fas fa-bookmark fa-fw fa-fw "></span>&nbsp;Reference</strong><br />Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam eget urna orci. Ut condimentum sapien sit amet elit dictum ultrices. Proin finibus eros at arcu auctor sollicitudin. Integer nec quam egestas, malesuada ante non, euismod felis. Nullam non eros volutpat, luctus justo venenatis, molestie purus. In molestie purus ante, et viverra risus efficitur sit amet. In aliquam, nibh eget interdum luctus, eros quam facilisis justo, nec ultricies ex dolor vitae justo. Vivamus ac massa in diam sodales commodo.</div><div class="alert alert-warning"><strong><span class="fas fa-eye  fa-fw fa-fw "></span>&nbsp;Caution</strong><br />Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam eget urna orci. Ut condimentum sapien sit amet elit dictum ultrices. Proin finibus eros at arcu auctor sollicitudin. Integer nec quam egestas, malesuada ante non, euismod felis. Nullam non eros volutpat, luctus justo venenatis, molestie purus. In molestie purus ante, et viverra risus efficitur sit amet. In aliquam, nibh eget interdum luctus, eros quam facilisis justo, nec ultricies ex dolor vitae justo. Vivamus ac massa in diam sodales commodo.</div><div class="alert alert-info"><strong><span class="fas fa-file-alt fa-fw fa-fw "></span>&nbsp;Info</strong><br />Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam eget urna orci. Ut condimentum sapien sit amet elit dictum ultrices. Proin finibus eros at arcu auctor sollicitudin. Integer nec quam egestas, malesuada ante non, euismod felis. Nullam non eros volutpat, luctus justo venenatis, molestie purus. In molestie purus ante, et viverra risus efficitur sit amet. In aliquam, nibh eget interdum luctus, eros quam facilisis justo, nec ultricies ex dolor vitae justo. Vivamus ac massa in diam sodales commodo.</div>'
            },
            {
                title: 'Button',
                image: 'jacademix_component.png',
                description: 'This template can be used to add a button',
                html: '<p><a class="btn btn-primary" href="#" title="Continue reading">Continue reading&nbsp;<span class="fas fa-arrow-right"></span></a></p>'
            },
            {
                title: 'Stack table',
                image: 'jacademix_component.png',
                description: 'This template can be used to add a stackable table',
                html: '<table class="table stack">\n' +
                '    <thead>\n' +
                '        <tr>\n' +
                '            <th>Col 1</th>\n' +
                '            <th>Col 2</th>\n' +
                '            <th>Col 3</th>\n' +
                '        </tr>\n' +
                '    </thead>\n' +
                '    <tbody>\n' +
                '        <tr>\n' +
                '            <td><strong>Value 1.1</strong></td>\n' +
                '            <td>Value 1.2</td>\n' +
                '            <td>Value 1.3</td>\n' +
                '        </tr>\n' +
                '        <tr>\n' +
                '            <td><strong>Value 2.1</strong></td>\n' +
                '            <td>Value 2.2</td>\n' +
                '            <td>Value 2.3</td>\n' +
                '        </tr>\n' +
                '        <tr>\n' +
                '            <td><strong>Value 3.1</strong></td>\n' +
                '            <td>Value 3.2</td>\n' +
                '            <td>Value 3.3</td>\n' +
                '        </tr>\n' +
                '    </tbody>\n' +
                '</table>'
            },
            {
                title: '2 Columns',
                image: 'jacademix_component.png',
                description: 'This template generate 2 bootstrap columns',
                html: '<div class="row">\n' +
                    '    <div class="col-md-4">Column 1</div>\n' +
                    '    <div class="col-md-4">Column 2</div>\n' +
                    '</div>'
            },
            {
                title: '3 Columns',
                image: 'jacademix_component.png',
                description: 'This template generate 3 bootstrap columns',
                html: '<div class="row">\n' +
                    '    <div class="col-md-4">Column 1</div>\n' +
                    '    <div class="col-md-4">Column 2</div>\n' +
                    '    <div class="col-md-4">Column 3</div>\n' +
                    '</div>'
            }
        ]
    });
