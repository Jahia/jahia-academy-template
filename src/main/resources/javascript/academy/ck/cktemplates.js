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
                html: '<h2 class="doc-child title scrollspy-offset-fix" id="sec1" style="box-sizing: border-box; color: rgb(45, 60, 87); font-size: 24px; margin-bottom: 20px; font-family: Lato;">Title</h2>' +
                '<p class="doc-child" style="box-sizing: border-box; margin: 0px 0px 15px; color: rgb(45, 60, 87); font-size: 18px; font-family: Lato;">Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam eget urna orci. Ut condimentum sapien sit amet elit dictum ultrices. Proin finibus eros at arcu auctor sollicitudin. Integer nec quam egestas, malesuada ante non, euismod felis. Nullam non eros volutpat, luctus justo venenatis, molestie purus. In molestie purus ante, et viverra risus efficitur sit amet. In aliquam, nibh eget interdum luctus, eros quam facilisis justo, nec ultricies ex dolor vitae justo. Vivamus ac massa in diam sodales commodo.</p>' +
                '<h3 class="doc-child subtitle scrollspy-offset-fix" id="sec1_1" style="font-weight: normal; box-sizing: border-box; padding-top: 180px; margin-top: -180px; color: rgb(0, 160, 227); font-size: 20px; margin-bottom: 10px;">Subtitle</h3>' +
                '<p class="doc-child" style="box-sizing: border-box; margin: 0px 0px 15px; color: rgb(45, 60, 87); font-size: 18px; font-family: Lato;">Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam eget urna orci. Ut condimentum sapien sit amet elit dictum ultrices. Proin finibus eros at arcu auctor sollicitudin. Integer nec quam egestas, malesuada ante non, euismod felis. Nullam non eros volutpat, luctus justo venenatis, molestie purus. In molestie purus ante, et viverra risus efficitur sit amet. In aliquam, nibh eget interdum luctus, eros quam facilisis justo, nec ultricies ex dolor vitae justo. Vivamus ac massa in diam sodales commodo.</p>' +
                '<ol class="doc-child" style="box-sizing: border-box; margin-top: 0px; margin-bottom: 30px; padding-right: 0px; padding-left: 0px; list-style: none; font-family: Lato; font-size: 14px;">' +
                '<li style="box-sizing: border-box; color: rgb(45, 60, 87); font-size: 18px; margin: 10px 0px 10px 50px; counter-increment: step-counter 1; text-indent: -50px;">Ordered</li>' +
                '<li style="box-sizing: border-box; color: rgb(45, 60, 87); font-size: 18px; margin: 10px 0px 10px 50px; counter-increment: step-counter 1; text-indent: -50px;">List</li>' +
                '</ol>' +
                '<ul class="doc-child" style="box-sizing: border-box; margin-top: 0px; margin-bottom: 30px; list-style: none; padding-right: 0px; padding-left: 0px; font-family: Lato; font-size: 14px;">' +
                '<li style="box-sizing: border-box; color: rgb(45, 60, 87); font-size: 18px; margin: 10px 0px 10px 15px; text-indent: -14px;">Unordered</li>' +
                '<li style="box-sizing: border-box; color: rgb(45, 60, 87); font-size: 18px; margin: 10px 0px 10px 15px; text-indent: -14px;">List</li>' +
                '</ul>' +
                '<div class="doc-child block-important" style="box-sizing: border-box; width: 750px; background: rgb(245, 255, 248); border: 1px dashed rgb(23, 159, 36); padding: 15px 20px; margin: 20px 0px; font-family: Lato; font-size: 14px;">' +
                '<h3 class="title" style="box-sizing: border-box; color: rgb(23, 159, 36); font-weight: 700; font-size: 18px; text-transform: uppercase;">Important</h2>' +
                '<p style="box-sizing: border-box; margin: 0px 0px 10px; color: rgb(23, 159, 36); font-size: 16px;">Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam eget urna orci. Ut condimentum sapien sit amet elit dictum ultrices. Proin finibus eros at arcu auctor sollicitudin. Integer nec quam egestas, malesuada ante non, euismod felis. Nullam non eros volutpat, luctus justo venenatis, molestie purus. In molestie purus ante, et viverra risus efficitur sit amet. In aliquam, nibh eget interdum luctus, eros quam facilisis justo, nec ultricies ex dolor vitae justo. Vivamus ac massa in diam sodales commodo.</p>' +
                '</div>' +
                '<div class="doc-child block-reference" style="box-sizing: border-box; width: 750px; background: rgb(245, 253, 255); border: 1px dashed rgb(0, 160, 227); padding: 15px 20px; margin: 20px 0px; font-family: Lato; font-size: 14px;">' +
                '<h3 class="title" style="box-sizing: border-box; color: rgb(0, 160, 227); font-weight: 700; font-size: 18px; text-transform: uppercase;">Reference</h2>' +
                '<p style="box-sizing: border-box; margin: 0px 0px 10px; color: rgb(0, 160, 227); font-size: 16px;">Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam eget urna orci. Ut condimentum sapien sit amet elit dictum ultrices. Proin finibus eros at arcu auctor sollicitudin. Integer nec quam egestas, malesuada ante non, euismod felis. Nullam non eros volutpat, luctus justo venenatis, molestie purus. In molestie purus ante, et viverra risus efficitur sit amet. In aliquam, nibh eget interdum luctus, eros quam facilisis justo, nec ultricies ex dolor vitae justo. Vivamus ac massa in diam sodales commodo.</p>' +
                '</div>' +
                '<div class="doc-child block-caution" style="box-sizing: border-box; width: 750px; background: rgb(255, 246, 245); border: 1px dashed rgb(205, 63, 47); padding: 15px 20px; margin: 20px 0px; font-family: Lato; font-size: 14px;">' +
                '<h3 class="title" style="box-sizing: border-box; color: rgb(205, 63, 47); font-weight: 700; font-size: 18px; text-transform: uppercase;">Caution</h2>' +
                '<p style="box-sizing: border-box; margin: 0px 0px 10px; color: rgb(205, 63, 47); font-size: 16px;">Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam eget urna orci. Ut condimentum sapien sit amet elit dictum ultrices. Proin finibus eros at arcu auctor sollicitudin. Integer nec quam egestas, malesuada ante non, euismod felis. Nullam non eros volutpat, luctus justo venenatis, molestie purus. In molestie purus ante, et viverra risus efficitur sit amet. In aliquam, nibh eget interdum luctus, eros quam facilisis justo, nec ultricies ex dolor vitae justo. Vivamus ac massa in diam sodales commodo.</p>' +
                '</div>' +
                '<div class="doc-child block-info" style="box-sizing: border-box; width: 750px; background: rgb(255, 252, 245); border: 1px dashed rgb(218, 146, 21); padding: 15px 20px; margin: 20px 0px; font-family: Lato; font-size: 14px;">' +
                '<h3 class="title" style="box-sizing: border-box; color: rgb(218, 146, 21); font-weight: 700; font-size: 18px; text-transform: uppercase;">Info</h2>' +
                '<p style="box-sizing: border-box; margin: 0px 0px 10px; color: rgb(218, 146, 21); font-size: 16px;">Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam eget urna orci. Ut condimentum sapien sit amet elit dictum ultrices. Proin finibus eros at arcu auctor sollicitudin. Integer nec quam egestas, malesuada ante non, euismod felis. Nullam non eros volutpat, luctus justo venenatis, molestie purus. In molestie purus ante, et viverra risus efficitur sit amet. In aliquam, nibh eget interdum luctus, eros quam facilisis justo, nec ultricies ex dolor vitae justo. Vivamus ac massa in diam sodales commodo.</p>' +
                '</div>'
            },
            {
                title: 'Important Block',
                image: 'jacademix_component.png',
                description: 'This template can be used to add important blocks to your document',
                html: '<div class="doc-child block-important" style="box-sizing: border-box; width: 750px; background: rgb(245, 255, 248); border: 1px dashed rgb(23, 159, 36); padding: 15px 20px; margin: 20px 0px; font-family: Lato; font-size: 14px;">' +
                '<h3 class="title" style="box-sizing: border-box; color: rgb(23, 159, 36); font-weight: 700; font-size: 18px; text-transform: uppercase;">Important</h3>' +
                '<p style="box-sizing: border-box; margin: 0px 0px 10px; color: rgb(23, 159, 36); font-size: 16px;">Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam eget urna orci. Ut condimentum sapien sit amet elit dictum ultrices. Proin finibus eros at arcu auctor sollicitudin. Integer nec quam egestas, malesuada ante non, euismod felis. Nullam non eros volutpat, luctus justo venenatis, molestie purus. In molestie purus ante, et viverra risus efficitur sit amet. In aliquam, nibh eget interdum luctus, eros quam facilisis justo, nec ultricies ex dolor vitae justo. Vivamus ac massa in diam sodales commodo.</p>' +
                '</div>'
            },
            {
                title: 'Reference Block',
                image: 'jacademix_component.png',
                description: 'This template can be used to add reference blocks to your document',
                html: '<div class="doc-child block-reference" style="box-sizing: border-box; width: 750px; background: rgb(245, 253, 255); border: 1px dashed rgb(0, 160, 227); padding: 15px 20px; margin: 20px 0px; font-family: Lato; font-size: 14px;">' +
                '<h3 class="title" style="box-sizing: border-box; color: rgb(0, 160, 227); font-weight: 700; font-size: 18px; text-transform: uppercase;">Reference</h3>' +
                '<p style="box-sizing: border-box; margin: 0px 0px 10px; color: rgb(0, 160, 227); font-size: 16px;">Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam eget urna orci. Ut condimentum sapien sit amet elit dictum ultrices. Proin finibus eros at arcu auctor sollicitudin. Integer nec quam egestas, malesuada ante non, euismod felis. Nullam non eros volutpat, luctus justo venenatis, molestie purus. In molestie purus ante, et viverra risus efficitur sit amet. In aliquam, nibh eget interdum luctus, eros quam facilisis justo, nec ultricies ex dolor vitae justo. Vivamus ac massa in diam sodales commodo.</p>' +
                '</div>'
            },
            {
                title: 'Caution Block',
                image: 'jacademix_component.png',
                description: 'This template can be used to add caution blocks to your document',
                html: '<div class="doc-child block-caution" style="box-sizing: border-box; width: 750px; background: rgb(255, 246, 245); border: 1px dashed rgb(205, 63, 47); padding: 15px 20px; margin: 20px 0px; font-family: Lato; font-size: 14px;">' +
                '<h3 class="title" style="box-sizing: border-box; color: rgb(205, 63, 47); font-weight: 700; font-size: 18px; text-transform: uppercase;">Caution</h3>' +
                '<p style="box-sizing: border-box; margin: 0px 0px 10px; color: rgb(205, 63, 47); font-size: 16px;">Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam eget urna orci. Ut condimentum sapien sit amet elit dictum ultrices. Proin finibus eros at arcu auctor sollicitudin. Integer nec quam egestas, malesuada ante non, euismod felis. Nullam non eros volutpat, luctus justo venenatis, molestie purus. In molestie purus ante, et viverra risus efficitur sit amet. In aliquam, nibh eget interdum luctus, eros quam facilisis justo, nec ultricies ex dolor vitae justo. Vivamus ac massa in diam sodales commodo.</p>' +
                '</div>'
            },
            {
                title: 'Info Block',
                image: 'jacademix_component.png',
                description: 'This template can be used to add information blocks to your document',
                html: '<div class="doc-child block-info" style="box-sizing: border-box; width: 750px; background: rgb(255, 252, 245); border: 1px dashed rgb(218, 146, 21); padding: 15px 20px; margin: 20px 0px; font-family: Lato; font-size: 14px;">' +
                '<h3 class="title" style="box-sizing: border-box; color: rgb(218, 146, 21); font-weight: 700; font-size: 18px; text-transform: uppercase;">Info</h3>' +
                '<p style="box-sizing: border-box; margin: 0px 0px 10px; color: rgb(218, 146, 21); font-size: 16px;">Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam eget urna orci. Ut condimentum sapien sit amet elit dictum ultrices. Proin finibus eros at arcu auctor sollicitudin. Integer nec quam egestas, malesuada ante non, euismod felis. Nullam non eros volutpat, luctus justo venenatis, molestie purus. In molestie purus ante, et viverra risus efficitur sit amet. In aliquam, nibh eget interdum luctus, eros quam facilisis justo, nec ultricies ex dolor vitae justo. Vivamus ac massa in diam sodales commodo.</p>' +
                '</div>'
            },
            {
                title: 'List',
                image: 'jacademix_component.png',
                description: 'This template can be used to add unordered lists to your document',
                html: '<ul class="doc-child" style="box-sizing: border-box; margin-top: 0px; margin-bottom: 30px; list-style: none; padding-right: 0px; padding-left: 0px; font-family: Lato; font-size: 14px;">' +
                '<li style="box-sizing: border-box; color: rgb(45, 60, 87); font-size: 18px; margin: 10px 0px 10px 15px; text-indent: -14px;">Unordered</li>' +
                '<li style="box-sizing: border-box; color: rgb(45, 60, 87); font-size: 18px; margin: 10px 0px 10px 15px; text-indent: -14px;">List</li>' +
                '</ul>'
            },
            {
                title: 'Ordered List',
                image: 'jacademix_component.png',
                description: 'This template can be used to add ordered lists to your document',
                html: '<ol class="doc-child" style="box-sizing: border-box; margin-top: 0px; margin-bottom: 30px; padding-right: 0px; padding-left: 0px; list-style: none; font-family: Lato; font-size: 14px;">' +
                '<li style="box-sizing: border-box; color: rgb(45, 60, 87); font-size: 18px; margin: 10px 0px 10px 50px; counter-increment: step-counter 1; text-indent: -50px;">Ordered</li>' +
                '<li style="box-sizing: border-box; color: rgb(45, 60, 87); font-size: 18px; margin: 10px 0px 10px 50px; counter-increment: step-counter 1; text-indent: -50px;">List</li>' +
                '</ol>'
            },
            {
                title: 'Button',
                image: 'jacademix_component.png',
                description: 'This template can be used to add a button',
                html: '<p><a class="btn btn-primary" href="#" title="Continue reading">Continue reading&nbsp;<span class="fa fa-arrow-right"></span></a></p>'
            }
        ]
    });
