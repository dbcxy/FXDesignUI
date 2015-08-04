package model;

import java.util.HashMap;
import java.util.Map;

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
	private static final int RING_BUFFER_LENGTH = 500;

	private SketchItemizedOverlay mTrackList;
	private SketchItemizedOverlay mPlotList;
	private SketchItemizedOverlay mVideoList;
	
	private Track track = new Track();
	private Plot plot = new Plot();
	private Video video = new Video();
	
	private int mTrackIndex;
	private int mPlotIndex;
	private int mVideoIndex;
	
	private Map<Integer, Integer> mapTracks = new HashMap<Integer, Integer>();
	
	public DataObserver() {
		mTrackList = new SketchItemizedOverlay();
		mPlotList = new SketchItemizedOverlay();
		mVideoList = new SketchItemizedOverlay();
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

	public void setPlot(Plot plot) {
		this.plot = plot;
	}

	public Video getVideo() {
		return video;
	}

	public void setVideo(Video video) {
		this.video = video;
	}
	
	public void addAzPlots(AzimuthPlanePlotsPerCPIMsg aPlotsPerCPIMsg) {
		for(AzimuthPlaneDetectionPlotMsg aPlotMsg : aPlotsPerCPIMsg.getAzimuthPlaneDetectionPlotMsg()) {
			plot = new Plot();
			plot.setAzimuth((aPlotMsg.getAzimuth()*0.0001));
			plot.setRange(aPlotMsg.getRange());
			plot.getX();
			plot.getY();
//			if(mPlotList.size() >= RING_BUFFER_LENGTH)
//				mPlotList.setOverlayItem(mPlotIndex,plot);
//			else 
				mPlotList.addOverlayItem(mPlotIndex,plot);
			mPlotIndex++;
			mPlotIndex = mPlotIndex % RING_BUFFER_LENGTH;
		}
	}

	public void addAzTracks(AzimuthPlaneTrackMsg aTrackMsg) {
		
//		if(mapTracks.containsKey(aTrackMsg.getTrackName())) {
//			int trackIndex = mapTracks.get(aTrackMsg.getTrackName());
//			Track track = (Track)mTrackList.getItem(trackIndex);
//    		track.setX(aTrackMsg.getX());
//			track.setY(aTrackMsg.getY());
//			double x = track.getX();
//			double y = track.getY();
//    		mTrackList.setOverlayItem(trackIndex, track);	    
//		} else {
	    	track = new Track();
	    	track.setTrackNumber(aTrackMsg.getTrackName());
			track.setY(aTrackMsg.getY());
			track.setX(aTrackMsg.getX());
			track.setAz(true);
			double x = track.getX();
			double y = track.getY();
			mTrackList.addOverlayItem(mTrackIndex,track);
			mapTracks.put(aTrackMsg.getTrackName(), mTrackIndex);
			mTrackIndex++;
			mTrackIndex = mTrackIndex % RING_BUFFER_LENGTH;
//	    }
	}
	
}
