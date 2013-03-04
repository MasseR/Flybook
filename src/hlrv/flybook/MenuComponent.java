package hlrv.flybook;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;

public class MenuComponent extends CustomComponent {

    private class LogoutCommand implements Command {

        @Override
        public void menuSelected(MenuItem selectedItem) {

            // Open user window

        }

    }

    private class UserCommand implements Command {

        @Override
        public void menuSelected(MenuItem selectedItem) {

            UI.getCurrent().getUI().addWindow(new RegisterView(false));

        }

    }

    private final MenuBar menubar;

    /**
     * Create a menu with the following menu objects:
     * 
     */
    public MenuComponent() {

        menubar = new MenuBar();
        initializeMenu();

    }

    /*
     * Initialize menu items
     */
    private void initializeMenu() {

        final MenuBar.MenuItem flybook = menubar.addItem("Flybook", null);
        final MenuBar.MenuItem settings = menubar.addItem("Settings", null);

        Command logoutCommand = null;
        Command userCommand = null;

        flybook.addItem("Logout", logoutCommand);
        settings.addItem("User", userCommand);

    }

}
