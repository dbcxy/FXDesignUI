package model;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.effect.Light.Point;

import org.apache.log4j.Logger;

import utils.Constance;
import utils.ModelDrawing;
import messages.radar.AzimuthPlaneDetectionPlotMsg;
import messages.radar.AzimuthPlanePlotsPerCPIMsg;
import messages.radar.AzimuthPlaneTrackMsg;
import messages.radar.ElevationPlaneDetectionPlotMsg;
import messages.radar.ElevationPlanePlotsPerCPIMsg;
import messages.radar.ElevationPlaneTrackMsg;
import messages.radar.PlaneRAWVideoMsg;
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
	
	private SketchItemizedOverlay mAzEachBeamVideoList;
	private SketchItemizedOverlay mElEachBeamVideoList;
	
	private List<SketchItemizedOverlay> mAzVideoList;
	private List<SketchItemizedOverlay> mElVideoList;
	
	public DataObserver() {
		mAzTrackList = new SketchItemizedOverlay();
		mAzPlotList = new SketchItemizedOverlay();
		mElTrackList = new SketchItemizedOverlay();
		mElPlotList = new SketchItemizedOverlay();
		
		mAzEachBeamVideoList = new SketchItemizedOverlay();
		mElEachBeamVideoList = new SketchItemizedOverlay();
		
//		mAzVideoList = new ArrayList<SketchItemizedOverlay>();
//		mElVideoList = new ArrayList<SketchItemizedOverlay>();
//		
//		//Init Video List
//		for(int i=0;i<Constance.RAW_AZ_BEAMS;i++)
//			mAzVideoList.add(i, new SketchItemizedOverlay());
//		
//		for(int i=0;i<Constance.RAW_EL_BEAMS;i++)
//			mElVideoList.add(i, new SketchItemizedOverlay());
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
	
	public SketchItemizedOverlay getAzVideoDataList() {
		return mAzEachBeamVideoList;
	}
	
	public SketchItemizedOverlay getElVideoDataList() {
		return mElEachBeamVideoList;
	}
	
//	public List<SketchItemizedOverlay> getAzVideoDataList() {
//		return mAzVideoList;
//	}
//	
//	public List<SketchItemizedOverlay> getElVideoDataList() {
//		return mElVideoList;
//	}
	
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
//			track.extractGraphAER();
			
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
//		track.extractGraphAER();
		mElTrackList.addOverlayItem(track);
	}
	
	public void addRAWVideo(PlaneRAWVideoMsg vidMsg) {

		MatrixRef matrixRef = MatrixRef.getInstance();
		if(vidMsg.getAzBPN()!=0 && vidMsg.getAzBPN()<=Constance.RAW_AZ_BEAMS){

//			SketchItemizedOverlay mAzEachBeamVideoList = new SketchItemizedOverlay();
			//(20% overlap of actual beam per angle) & 0.6degree beam width
			double azAngle = (Constance.AZIMUTH_MAX - (vidMsg.getAzBPN()*0.6*0.8));//In degress 
			double rangeDisp = (matrixRef.getMaxRange()/vidMsg.getNoRangeCells());//In KM
			double range = rangeDisp;// In KM
			for(int i=0;i<vidMsg.getNoRangeCells();i++)  {
				Video video = new Video();
		    	video.setVal(vidMsg.getRangeCellList(i));

		    	//need to put in range & Az
		    	video.setAz(true);
		    	video.setAzimuth(azAngle);
		    	video.setRange(range*1000);//In meters
		    	
				mAzEachBeamVideoList.addOverlayItem(video);
				range+=rangeDisp;
			}
//			mAzVideoList.set(vidMsg.getAzBPN()-1, mAzEachBeamVideoList);//Current index
//			mAzVideoList.get(vidMsg.getAzBPN()%Constance.RAW_AZ_BEAMS).clear();//Next index
		} 
		
		if(vidMsg.getElBPN()!=0 && vidMsg.getElBPN()<=Constance.RAW_EL_BEAMS) {
			
//			SketchItemizedOverlay mElEachBeamVideoList = new SketchItemizedOverlay();
			double elAngle = (vidMsg.getElBPN()*0.6*0.8);//In degrees			
	        double rangeDisp = (matrixRef.getMaxRange()/vidMsg.getNoRangeCells());//In KM
			double range = rangeDisp;// In KM
			for(int i=0;i<vidMsg.getNoRangeCells();i++)  {
				Video video = new Video();
				video.setVal(vidMsg.getRangeCellList(i));
				
				//need to put in range & El
				video.setEl(true);
				video.setElevation(elAngle);
		    	video.setRange(range*1000);//In Meters
		    	
				mElEachBeamVideoList.addOverlayItem(video);
				range+=rangeDisp;
			}
//			mElVideoList.set(vidMsg.getElBPN()-1, mElEachBeamVideoList);
//			mElVideoList.get(vidMsg.getElBPN()%Constance.RAW_EL_BEAMS).clear();
		}		
	}
	
}