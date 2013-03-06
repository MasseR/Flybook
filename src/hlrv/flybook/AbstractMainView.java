package hlrv.flybook;

import com.vaadin.ui.CustomComponent;

public abstract class AbstractMainView extends CustomComponent {

    public AbstractMainView() {
        setSizeFull();
    }

    public abstract void tabSelected();

}
