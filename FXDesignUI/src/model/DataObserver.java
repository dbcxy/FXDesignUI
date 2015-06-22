package model;

import model.drawable.Plot;
import model.drawable.SketchItemizedOverlay;
import model.drawable.Track;
import model.drawable.Video;

public class DataObserver {

	private String mData;
	private SketchItemizedOverlay mTrackList;
	private SketchItemizedOverlay mPlotList;
	private SketchItemizedOverlay mVideoList;
	
	public DataObserver() {
		mTrackList = new SketchItemizedOverlay(SketchItemizedOverlay.TRACK);
		mPlotList = new SketchItemizedOverlay(SketchItemizedOverlay.PLOT);
		mVideoList = new SketchItemizedOverlay(SketchItemizedOverlay.VIDEO);
	}
	
	public void analyseData(String data) {
		mData = data;
//		validateData();
//		classifyData();		
		assignData(1);
		assignData(2);
	}
	
	public SketchItemizedOverlay getTrackDataList() {
		return mTrackList;
	}
	
	public SketchItemizedOverlay getPlotDataList() {
		return mPlotList;
	}
	
	public SketchItemizedOverlay getVideoDataList() {
		return mVideoList;
	}

	private void assignData(int id) {
		
		switch (id) {
		case 1:
			Track track = new Track();
			track.setXY(200, 200);
			track.setText("AA10", "3200/100");        	
			mTrackList.addOverlayItem(track);
			Track track1 = new Track();
			track1.setXY(300, 300);
			track1.setText("AA11", "32/10");
			mTrackList.addOverlayItem(track1);
			break;
			
		case 2:
			Plot plot = new Plot();
			plot.setXY(250, 250);
			plot.setTitle("PLOT1"); 
			mPlotList.addOverlayItem(plot);
			Plot plot1 = new Plot();
			plot1.setXY(350, 350);
			plot1.setTitle("PLOT2"); 
			mPlotList.addOverlayItem(plot1);
			break;
			
		case 3:
			
			break;

		default:
			break;
		}
		
	}

	private void classifyData() {
		// TODO Auto-generated method stub
		
	}

	private void validateData() {
		// TODO Auto-generated method stub
		
	}
	
	
}
