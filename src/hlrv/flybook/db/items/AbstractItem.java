package hlrv.flybook.db.items;

import com.vaadin.data.Item;

/**
 * Wrapper class for Item.
 */
public abstract class AbstractItem {

    protected Item item;

    public AbstractItem(Item source) {
        this.item = source;
    }

    public Item getItem() {
        return item;
    }

    public boolean isNull() {
        return item == null;
    }

    protected Integer getInteger(String pid) {
        if (isNull()) {
            return null;
        } else {
            return (Integer) item.getItemProperty(pid).getValue();
        }
    }

    protected String getString(String pid) {
        if (isNull()) {
            return null;
        } else {
            return (String) item.getItemProperty(pid).getValue();
        }
    }

    protected void setValue(String pid, Object value) {
        item.getItemProperty(pid).setValue(value);
    }
}
