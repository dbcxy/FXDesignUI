package model;

public class OverlayItem {
	
	String Title;
	double X;
	double Y;
	
	public OverlayItem(final Double x, final Double y) {
		this(null,x,y);
	}
	
	public OverlayItem(final String name, final Double x, final Double y) {
		this.Title = name;
		if(x!=null)
			this.X = x;
		if(y!=null)
			this.Y = y;
	}

	public double getX() {
		return X;
	}

	public void setX(final double x) {
		X = x;
	}

	public double getY() {
		return Y;
	}

	public void setY(final double y) {
		Y = y;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(final String title) {
		Title = title;
	}
	
	
	
	

}
