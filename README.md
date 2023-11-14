# Jahia Academy Templates Repository

This repository holds the specialized template set used for the [academy.jahia.com](https://academy.jahia.com) website. These templates are tailored specifically for our needs, with some code sections containing hard-coded content related to certain Jahia websites.

This template set not only shapes the aesthetic design but also includes detailed content definitions for displaying the site's information effectively.

Please be aware that we incorporate certain external bundles in the live website due to historical reasons.

## License

The repository is available under two licenses: GPL and JSEL.

- GPL: If you opt for the GPL license, please adhere to the terms of the GNU General Public License as published by the Free Software Foundation.
- JSEL: If you choose the JSEL license, the commercial and supported versions of the program, also known as Enterprise Distributions, must be used according to the terms and conditions detailed in a separate written agreement between you and Jahia Solutions Group SA.

For more information, please visit the [Jahia License Page](http://www.jahia.com).

## Installation

### Prerequisites
Before compiling and building this template set, ensure Yarn is installed on your system.

Ensure you have Jahia v8.1.0.0 or a later version installed. Clone the repository and navigate to the project's root directory. Install the necessary dependencies with the following commands:

```bash
git clone git@github.com:Jahia/jahia-academy-template.git
cd jahia-academy-template
mvn clean install
```
## Dependencies

The module relies on the following Jahia modules:

- bootstrap5-components
- default
- font-awesome
- jquery
- animate
- jahiacom-templates

## Yarn Dependencies

This project uses:

- [Bootstrap 5](https://getbootstrap.com/): A popular HTML, CSS, and JS framework for developing responsive, mobile-first projects.
- [@popperjs/core](https://popper.js.org/): A positioning engine to manage the positioning of tooltips, popovers, dropdowns, etc.

The project relies on the following development dependencies:

- [Sass](https://sass-lang.com/): A mature extension to standard CSS, offering nested rules, variables, mixins, selector inheritance, etc.
- [Stylelint](https://stylelint.io/): A powerful, modern CSS linter that enforces consistent conventions and averts errors in stylesheets.
- [PostCSS](https://postcss.org/): A tool for transforming styles with JS plugins, useful for automating routine operations, supporting CSS features, and fixing CSS bugs.
- [Stylelint-config-standard-scss](https://www.npmjs.com/package/stylelint-config-standard-scss): The standard shareable SCSS configuration for Stylelint.
