package hlrv.flybook;

import com.vaadin.ui.Panel;

public abstract class AbstractMainViewTab extends Panel {

    public AbstractMainViewTab() {
        setSizeFull();
    }

    public abstract void tabSelected();

}