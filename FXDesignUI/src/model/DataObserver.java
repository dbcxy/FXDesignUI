package model;

import org.apache.log4j.Logger;

import messages.radar.AzimuthPlaneDetectionPlotMsg;
import messages.radar.AzimuthPlanePlotsPerCPIMsg;
import messages.radar.AzimuthPlaneTrackMsg;
import messages.radar.ElevationPlaneDetectionPlotMsg;
import messages.radar.ElevationPlanePlotsPerCPIMsg;
import messages.radar.ElevationPlaneTrackMsg;
import model.drawable.Plot;
import model.drawable.SketchItemizedOverlay;
import model.drawable.Track;
import model.drawable.Video;

public class DataObserver {
	
	private static final Logger logger = Logger.getLogger(DataObserver.class);

	private SketchItemizedOverlay mAzTrackList;
	private SketchItemizedOverlay mAzPlotList;
	private SketchItemizedOverlay mElTrackList;
	private SketchItemizedOverlay mElPlotList;
	private SketchItemizedOverlay mVideoList;
	
	public DataObserver() {
		mAzTrackList = new SketchItemizedOverlay();
		mAzPlotList = new SketchItemizedOverlay();
		mElTrackList = new SketchItemizedOverlay();
		mElPlotList = new SketchItemizedOverlay();
		mVideoList = new SketchItemizedOverlay();
	}
	
	public SketchItemizedOverlay getAzTrackDataList() {
		return mAzTrackList;
	}
	
	public SketchItemizedOverlay getAzPlotDataList() {
		return mAzPlotList;
	}
	
	public SketchItemizedOverlay getElTrackDataList() {
		return mElTrackList;
	}
	
	public SketchItemizedOverlay getElPlotDataList() {
		return mElPlotList;
	}
	
	public SketchItemizedOverlay getVideoDataList() {
		return mVideoList;
	}
	
	public void addAzPlots(AzimuthPlanePlotsPerCPIMsg aPlotsPerCPIMsg) {
		for(AzimuthPlaneDetectionPlotMsg aPlotMsg : aPlotsPerCPIMsg.getAzimuthPlaneDetectionPlotMsg()) {
			Plot plot = new Plot();
			plot.setAzimuth((aPlotMsg.getAzimuth()*0.0001));
			plot.setRange(aPlotMsg.getRange());
			plot.setAz(true);
			plot.extractGraphAER();
			
			mAzPlotList.addOverlayItem(plot);
			
//			plot.getX();
//			plot.getY();
//			if(mPlotList.size() >= RING_BUFFER_LENGTH)
//				mPlotList.setOverlayItem(mPlotIndex,plot);
//			else 
//				mAzPlotList.addOverlayItem(mPlotIndex,plot);
//			mPlotIndex++;
//			mPlotIndex = mPlotIndex % RING_BUFFER_LENGTH;
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
	    	Track track = new Track();
	    	track.setTrackNumber(aTrackMsg.getTrackName());
			track.setY(aTrackMsg.getY());
			track.setX(aTrackMsg.getX());
			track.setAz(true);
			track.extractGraphAER();
			
			mAzTrackList.addOverlayItem(track);
			
//			double x = track.getX();
//			double y = track.getY();
//			mAzTrackList.addOverlayItem(mTrackIndex,track);
//			mapTracks.put(aTrackMsg.getTrackName(), mTrackIndex);
//			mTrackIndex++;
//			mTrackIndex = mTrackIndex % RING_BUFFER_LENGTH;
//	    }
	}
	
	public void addElPlots(ElevationPlanePlotsPerCPIMsg ePlotsPerCPIMsg) {
		for(ElevationPlaneDetectionPlotMsg aPlotMsg : ePlotsPerCPIMsg.getElevationPlaneDetectionPlotMsg()) {
			Plot plot = new Plot();
			plot.setElevation((aPlotMsg.getElevation()*0.0001));
			plot.setRange(aPlotMsg.getRange());
			plot.setEl(true);
			plot.extractGraphAER();	
			mElPlotList.addOverlayItem(plot);
		}
	}
	
	public void addElTracks(ElevationPlaneTrackMsg eTrackMsg) {
    	Track track = new Track();
    	track.setTrackNumber(eTrackMsg.getTrackName());
		track.setZ(eTrackMsg.getZ());
		track.setX(eTrackMsg.getX());
		track.setEl(true);
		track.extractGraphAER();
		mElTrackList.addOverlayItem(track);
		logger.info("ElRng: "+track.getRange());
		logger.info("ElEl: "+track.getElevation());
	}
	
}