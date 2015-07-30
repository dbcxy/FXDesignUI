package model;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.Canvas;

public abstract class ItemizedOverlay<Item extends OverlayItem> {
	
    protected final ArrayList<Item> mInternalItemList;

    public ItemizedOverlay() {
    	mInternalItemList = new ArrayList<Item>();    	
	}
    
    public boolean addOverlayItem(Item item) {
        return mInternalItemList.add(item);
	}
    
    public void setOverlayItem(int location, Item item) {
    	mInternalItemList.set(location, item);
    }
	
	public void addOverlayItem(int location, Item item) {
		mInternalItemList.add(location, item);
	}
	
	public boolean removeOverlayItem(Item item) {
		return mInternalItemList.remove(item);
	}
	
	public Item removeOverlayItem(int position) {
        return mInternalItemList.remove(position);
	}
    
   	public final Item getItem(final int position) {
	        return mInternalItemList.get(position);
	}
   	
   	protected final int size() {
   		return mInternalItemList.size();
   	}
   	
   	public List<Item> getOverlayItemList() {
   		return mInternalItemList;
   	}

}
