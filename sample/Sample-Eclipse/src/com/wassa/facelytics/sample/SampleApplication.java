package com.wassa.facelytics.sample;

import java.util.Map;

import android.app.Application;
import android.content.Context;

import com.wassa.facelytics.all.FacelyticsBitmapServiceNative;
import com.wassa.facelytics.all.FacelyticsCameraServiceNative;
import com.wassa.facelytics.all.FacelyticsUtils;
import com.wassa.facelytics.common.FacelyticsServiceNative.SessionEndedListener;
import com.wassa.facelytics.events.OnEventListener;
import com.wassa.facelytics.wrapper.WData.DrawingQuality;
import com.wassa.noyau.capture.input.KInputCamId;
import com.wassa.noyau.debug.KLog;

public class SampleApplication extends Application {

	/** You may have to set a LICENCE_KEY to use this SDK */
	public static final String LICENCE_KEY = "<YOUR_OWN_LICENCE>";
	
	/** Current CAM_ID used by the SDK */
	public static KInputCamId CAM_ID = KInputCamId.CAMERA_ID_ANY;
	
	private static boolean LOADED = false;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		if (!LOADED) 
		{

			/** Set true/false to turn on/off logs */
			KLog.EASY_LOG = true;
			
			/** Will load plugin(s) ; armeabi-v7a (*.so) files */
			FacelyticsUtils.loadPlugin(this);
			
			LOADED = true;
			
			if (KLog.IF()) 
			{
				
				try {
					
					/** Debug: Print available camId && capture size */
					Map<KInputCamId, Integer> located = KInputCamId.locateAvailableCam();
					for (KInputCamId k : located.keySet())
						KLog.d(KLog.DEFAULT_TAG, "Cam: " + k.name() + " available at location " + located.get(k));
					
				} catch (RuntimeException exc) 
				{
					KLog.e(KLog.DEFAULT_TAG, "err: unable to locate cam", exc);
				}
				
				/** Debug: Print available heapsize memory */
				KLog.heapMem();
				
			}
		}
	}
	
	public static FacelyticsCameraServiceNative initService_4Camera_SampleActivity(Context _ctx, OnEventListener<?> _onEvent, SessionEndedListener _onEnded) {
		FacelyticsCameraServiceNative newService = initCameraService(
				_ctx,  
				"config", /** Config file name submited to Facelytics-SDK */ 
				_onEvent,
				_onEnded);
		
		newService.setRenderToMat(true);
		
		newService.setDrawingQuality(DrawingQuality.LOW);
		newService.getInputConfig().timer = 1;
		newService.getInputConfig().maxWidth = 550;
		newService.getInputConfig().maxHeight = 550;
	
		return newService;
	}
	
	public static FacelyticsBitmapServiceNative initService_4Bitmap_SampleActivity(Context _ctx, OnEventListener<?> _onEvent, SessionEndedListener _onEnded) {
		FacelyticsBitmapServiceNative newService = initBitmapService(
				_ctx,  
				"config_light", /** Config file name submited to Facelytics-SDK */ 
				_onEvent,
				_onEnded);
		
		newService.setRenderToMat(true);
		
		newService.setDrawingQuality(DrawingQuality.LOW);
		newService.getInputConfig().timer = 1000;
	
		return newService;
	}
	
	/**
	 * Easy & quick way to init Facelytics-SDK service.
	 * 
	 * @param _ctx
	 * @param _configFile , config file name submited to Facelytics-SDK (Do not add the file ext)
	 * @param _onEvent , event detection listener
	 */
	private static FacelyticsCameraServiceNative initCameraService(final Context _ctx, String _configFile, OnEventListener<?> _onEvent, SessionEndedListener _onEnded) {
		if (!FacelyticsUtils.isInit()) 
		{
			FacelyticsCameraServiceNative newService = (FacelyticsCameraServiceNative) FacelyticsUtils.createInstance(
				new FacelyticsCameraServiceNative(_ctx, _configFile, SampleApplication.LICENCE_KEY)
			);
			
			newService.load(SampleApplication.CAM_ID);
			
			newService.addOnEventListener(_onEvent);
			newService.addSessionEndedListener(_onEnded);
		}
		return (FacelyticsCameraServiceNative) FacelyticsUtils.getInstance();
	}
	
	/**
	 * Easy & quick way to init Facelytics-SDK service.
	 * 
	 * @param _ctx
	 * @param _configFile , config file name submited to Facelytics-SDK (Do not add the file ext)
	 * @param _onEvent , event detection listener
	 */
	private static FacelyticsBitmapServiceNative initBitmapService(final Context _ctx, String _configFile, OnEventListener<?> _onEvent, SessionEndedListener _onEnded) {
		if (!FacelyticsUtils.isInit()) 
		{
			FacelyticsBitmapServiceNative newService = (FacelyticsBitmapServiceNative) FacelyticsUtils.createInstance(
				new FacelyticsBitmapServiceNative(_ctx, _configFile, SampleApplication.LICENCE_KEY)
			);
			newService.load();
			newService.addOnEventListener(_onEvent);
			newService.addSessionEndedListener(_onEnded);
		}
		return (FacelyticsBitmapServiceNative) FacelyticsUtils.getInstance();
	}
	
}
