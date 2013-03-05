package hlrv.flybook.db.items;

import com.vaadin.data.Item;

/**
 * Wrapper class for Item.
 */
public abstract class AbstractItem {

    /**
     * Container item
     */
    protected Item item;

    /**
     * Optional stored itemid.
     */
    protected Object itemId;

    public AbstractItem(Item item) {
        this.item = item;
    }

    public AbstractItem(Item item, Object iid) {
        this.item = item;
        this.itemId = iid;
    }

    public Item getItem() {
        return item;
    }

    public Object getItemId() {
        return itemId;
    }

    public void setItemId(Object iid) {
        this.itemId = iid;
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
