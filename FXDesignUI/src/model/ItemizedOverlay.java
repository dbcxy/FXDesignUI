package model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import javafx.scene.canvas.Canvas;

public abstract class ItemizedOverlay<Item extends OverlayItem> {
	
    protected final Deque<Item> mInternalItemList;

    public ItemizedOverlay() {
    	mInternalItemList = new ArrayDeque<Item>();    	
	}
    
    public boolean addOverlayItem(Item item) {
        return mInternalItemList.add(item);
	}
    
//    public void setOverlayItem(int location, Item item) {
//    	mInternalItemList.set(location, item);
//    }
	
	public void addOverlayItem(int location, Item item) {
		mInternalItemList.add(item);
	}
	
	public boolean removeOverlayItem(Item item) {
		return mInternalItemList.remove(item);
	}
	
	public Item removeOverlayItem() {
        return mInternalItemList.remove();
	}
	
   	protected final int size() {
   		return mInternalItemList.size();
   	}
}
