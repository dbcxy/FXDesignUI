package model;

import org.apache.log4j.Logger;

import messages.radar.AzimuthPlaneDetectionPlotMsg;
import messages.radar.AzimuthPlanePlotsPerCPIMsg;
import messages.radar.AzimuthPlaneTrackMsg;
import model.drawable.Plot;
import model.drawable.SketchItemizedOverlay;
import model.drawable.Track;
import model.drawable.Video;

public class DataObserver {
	
	private static final Logger logger = Logger.getLogger(DataObserver.class);

	private SketchItemizedOverlay mTrackList;
	private SketchItemizedOverlay mPlotList;
	private SketchItemizedOverlay mVideoList;
	
	private Track track;
	private Plot plot;
	private Video video;
	
	public DataObserver() {
		mTrackList = new SketchItemizedOverlay(SketchItemizedOverlay.TRACK);
		mPlotList = new SketchItemizedOverlay(SketchItemizedOverlay.PLOT);
		mVideoList = new SketchItemizedOverlay(SketchItemizedOverlay.VIDEO);
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

	public void addAzPlots(AzimuthPlanePlotsPerCPIMsg aPlotsPerCPIMsg) {
		for(AzimuthPlaneDetectionPlotMsg aPlotMsg : aPlotsPerCPIMsg.getAzimuthPlaneDetectionPlotMsg()) {
			plot = new Plot();
			plot.setAzimuth(Math.toDegrees(aPlotMsg.getAzimuth()));
			plot.setRange(aPlotMsg.getRange());
			mPlotList.addOverlayItem(plot);
		}
	}
	
	public void addAzTracks(AzimuthPlaneTrackMsg aTrackMsg) {
		track = new Track();
		track.setY(aTrackMsg.getX());
		track.setX(aTrackMsg.getY());
		mTrackList.addOverlayItem(track);
		
	}
	
	
}
