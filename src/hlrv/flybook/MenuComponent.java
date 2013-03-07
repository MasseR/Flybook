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

            ((FlybookUI) UI.getCurrent()).getAuth().logout();
            UI.getCurrent().setContent(new MainView());
        }
    }

    /*
     * This command opens a modal window for user modification
     */
    private class UserCommand implements Command {

        @Override
        public void menuSelected(MenuItem selectedItem) {

            UI.getCurrent().getUI().addWindow(new UserInformationView(false));
        }
    }

    /*
     * This command opens the AircraftView-class
     */
    private class AircraftCommand implements Command {

        @Override
        public void menuSelected(MenuItem selectedItem) {

            // Aircrafts

        }
    }

    private final boolean admin;
    private final MenuBar menubar;

    /**
     * Create a menu with the following menu objects: User settings, logout and
     * aircraft settings for the admin
     */
    public MenuComponent() {

        menubar = new MenuBar();
        menubar.setWidth("100%");
        initializeMenu();
        admin = ((FlybookUI) UI.getCurrent()).getUser().getBean().isAdmin();
        this.setCompositionRoot(menubar);

    }

    /*
     * Initialize menu items
     */
    private void initializeMenu() {

        final MenuBar.MenuItem flybook = menubar.addItem("Flybook", null);
        final MenuBar.MenuItem settings = menubar.addItem("Settings", null);

        Command logoutCommand = new LogoutCommand();
        Command userCommand = new UserCommand();
        Command adminCommand = new AircraftCommand();

        flybook.addItem("Logout", logoutCommand);

        if (admin) {
            settings.addItem("Aircraft", adminCommand);
        }
        settings.addItem("User", userCommand);
    }
}
