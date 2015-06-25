package model;

public class MatrixRef {
	
	private double startX;
	private double startY;
	private double endX;
	private double endY;
	
	private double zoomStartX;
	private double zoomStartY;
	private double zoomEndX;
	private double zoomEndY;
	
	private int zoomLevel;
	
	public MatrixRef() {
	
	}
	
	public MatrixRef(double sY, double eY) {
		this.startY = sY;
		this.endY = eY;
	}
	
	public MatrixRef(double sX,double sY, double eX, double eY) {
		this.startX = sX;
		this.startY = sY;
		this.endX = eX;
		this.endY = eY;
	}
	
	private void reCalibrateScale() {
		zoomStartX = startX;
		zoomStartY = startY;
		zoomEndX = endX;
		zoomEndY = endY;
		
		
	}
	
	public boolean checkPointInZoomedRegion(double X, double Y) {
		if((zoomStartX <= X) && (X <= zoomEndX))
			if((zoomStartY <= Y) && (Y <= zoomEndY))
				return true;
		return false;
	}
	
	public void setZoomlevel(int Scale) {
		zoomLevel = Scale;
		reCalibrateScale();
	}

	public double getOriginalStartX() {
		return startX;
	}

	public void setStartX(double startX) {
		this.startX = startX;
	}

	public double getOriginalStartY() {
		return startY;
	}

	public void setStartY(double startY) {
		this.startY = startY;
	}

	public double getOriginalEndX() {
		return endX;
	}

	public void setEndX(double endX) {
		this.endX = endX;
	}

	public double getOriginalEndY() {
		return endY;
	}

	public void setEndY(double endY) {
		this.endY = endY;
	}
	
	

}
