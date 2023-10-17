document.addEventListener("DOMContentLoaded", function() {

    // Cache frequently accessed DOM elements for efficiency.
    let navTabs = document.querySelector('.nav-tabs');
    let dropdownToggleBtn;
    let dropdownDiv;

    // Create a dropdown menu for tabs.
    function createDropdownMenu() {
        // If dropdown is already created, don't recreate it.
        if (dropdownDiv) return;

        // Create the dropdown container.
        dropdownDiv = document.createElement('div');
        dropdownDiv.classList.add('dropdown', 'tab-dropdown');

        // Create and configure the dropdown button.
        const button = document.createElement('button');
        button.className = 'btn btn-secondary dropdown-toggle';
        button.setAttribute('type', 'button');
        button.setAttribute('data-bs-toggle', 'dropdown');
        button.setAttribute('aria-expanded', 'false');

        // Set the dropdown button's text to match the currently active tab.
        const activeTabLabel = navTabs.querySelector('.active').textContent;
        button.textContent = activeTabLabel || 'Select Tab';

        // Create the dropdown menu list.
        const dropdownMenu = document.createElement('ul');
        dropdownMenu.className = 'dropdown-menu';

        // Clone each tab as an item into the dropdown menu.
        navTabs.querySelectorAll('button').forEach(tab => {
            const menuItem = document.createElement('li');
            const clonedTab = tab.cloneNode(true);
            clonedTab.classList.add('dropdown-item');
            clonedTab.classList.remove('nav-link');
            menuItem.appendChild(clonedTab);
            dropdownMenu.appendChild(menuItem);
        });

        // Append button and menu list to dropdown container.
        dropdownDiv.appendChild(button);
        dropdownDiv.appendChild(dropdownMenu);

        // Insert the dropdown after the tabs and hide the tabs.
        navTabs.insertAdjacentElement('afterend', dropdownDiv);
        navTabs.style.display = 'none';

        // Cache the dropdown toggle button for later use.
        dropdownToggleBtn = button;
    }

    // Throttle variable for resize event optimization.
    let resizeTimeout;

    // Adjust the display based on window width.
    function adjustTabsDisplay() {
        // Clear existing timeout to prevent rapid triggering of resize events.
        if (resizeTimeout) clearTimeout(resizeTimeout);

        // Throttle the resize event handling to optimize performance.
        resizeTimeout = setTimeout(() => {
            if (window.innerWidth <= 768) {
                createDropdownMenu();
            } else {
                // Remove the dropdown and display the tabs if width is greater than 768px.
                if (dropdownDiv) {
                    dropdownDiv.remove();
                    dropdownDiv = null;
                }
                navTabs.style.display = 'flex';
            }
        }, 100);
    }

    // Initial adjustment based on current window width.
    adjustTabsDisplay();

    // Add resize event listener to handle window resizing.
    window.addEventListener('resize', adjustTabsDisplay);

    // Handle tab selection from the dropdown menu.
    document.addEventListener('click', function(event) {
        const clickedDropdownItem = event.target.closest('.tab-dropdown .dropdown-menu button');

        // Exit early if clicked element is not a dropdown item.
        if (!clickedDropdownItem) return;

        // Find the matching tab based on data-bs-target attribute.
        const matchingTab = navTabs.querySelector('button[data-bs-target="' + clickedDropdownItem.getAttribute('data-bs-target') + '"]');
        if (!matchingTab) return;

        // Activate the clicked tab and update the dropdown button's text.
        new bootstrap.Tab(matchingTab).show();
        dropdownToggleBtn.textContent = clickedDropdownItem.textContent;
    });

});

