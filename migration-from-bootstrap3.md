# Bootstrap 3 to 5 Migration Guide

This document details the process of migrating the Academy's content from Bootstrap 3 to Bootstrap 5, which was previously a part of the main README file. The archival purposes necessitated its separate existence.

## Migration Steps

Migrating from Bootstrap 3 to Bootstrap 5 involves specific 'search and replace' actions in the repository.xml (default + live). Here's the list:

- `bootstrap3nt:columns` -> `bootstrap5nt:grid`
- `bootstrap5mix:customColumns` -> `bootstrap5mix:customGrid`
- `bootstrap5mix:predefinedColumns` -> `bootstrap5mix:predefinedGrid`
- `bootstrap5mix:siteLogo / siteLogo` ->  `bootstrap5mix:siteBrand / brandImage`
- `bootstrap5mix:advancedModal / size` -> `bootstrap5mix:modal -> modalSize`
- `bootstrap3nt:modal` -> `bootstrap5nt:button` + add mixin `bootstrap5mix:modal`
- `columnsType` -> `typeOfGrid`
- `typeOfGrid="predefinedColumns"` -> `typeOfGrid="predefinedGrid"`
- `typeOfGrid="customColumns"` -> `typeOfGrid="customGrid"`
- `gridLayout` -> `containerType`
- `fixed-width` -> `container`
- `full-width` -> `container-fluid`
- `fadeEffect` -> `fade`
- `navigation="tab"` -> `type="tab"`
- `navigation="pill"` -> `type="pill"`
- `useSystemNameAsAnchor` -> `useListNameAsAnchor`
- `tabsPosition="top"` -> `align="justify-content-start"`

Additionally, there are some mandatory removals:

- Remove `navJustified="false"`
- Remove `navStacked="false"`
- Remove `state="primary"`

Ensure to verify the following:

- Lines with `bootstrap5mix:customGrid` should have a `bootstrap5mix:createRow`
- Lines with `bootstrap5mix:predefinedGrid` should have a `bootstrap5mix:createRow`
- Blocks with `typeOfGrid="nogrid"` should have a `bootstrap5mix:createRow`
- On `jnt:virtualsite` node, remove the modal property

## Upgrading the Repository.xml

Follow these search/replace operations to upgrade your `repository.xml`. These commands employ 'sed' for the modifications:

```bash
Here is a few search / replace that do the job:

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
sed 's/jcr:mixinTypes="bootstrap5mix:predefinedGrid"/jcr:mixinTypes="bootstrap5mix:predefinedGrid bootstrap5mix:createRow" /g' repository.xml > new.xml && mv new.xml repository.xml
sed 's/bootstrap3-basic/bootstrap5-basic/g' repository.xml > new.xml && mv new.xml repository.xml

```

In lines with `bootstrap5mix:predefinedGrid`, verify the existence of `bootstrap5mix:createRow`. For example:

```bash
cat -n repository.xml|grep bootstrap5mix:predefinedGrid|grep -v bootstrap5mix:createRow
```
(Note that this should be resolved using the last 'sed' command.)

## Import Process

Before importing, modify `site.properties` by changing `installedModules.16=bootstrap3-components` to `installedModules.16=bootstrap5-components` (Note: ID might not be 16).

Next, proceed with the import process.

Post-import, you need to execute the following scripts until error-free:

- `grid-refactoring.groovy`
- `grid-rename.groovy`
- `tooltips.groovy`
- `search-for-row-eq-height.groovy`
