package live.ablo.agora;

import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;

import java.util.Timer;
import java.util.TimerTask;

import io.agora.rtc.IRtcEngineEventHandler;
import live.ablo.agora.data.MediaDataObserverPlugin;
import live.ablo.agora.data.MediaPreProcessing;

import static live.ablo.agora.AgoraConst.AGonFaceDetected;

public class FaceDetector {
	public static final String TAG = FaceDetector.class.getSimpleName();

	private static FaceDetector detector;

	private boolean blurOnNoFaceDetected;
	private boolean sendFaceDetectionDataEvent;
	private boolean sendFaceDetectionStatusEvent;
	private boolean isTimerRunning;
	private long lastTimeFaceSeen, lastTimeFaceSent;
	private Boolean lastFaceStatus = null;
	private RtcEventHandler eventHandler;
	private Timer timer;
	private TimerTask task;
	private MediaDataObserverPlugin mediaDataObserverPlugin;
	private VideoFrameObserver videoFrameObserver;

	private FaceDetector() {
		this.videoFrameObserver = new VideoFrameObserver();
	}

	public static FaceDetector getInstance() {
		if (detector == null) {
			synchronized (FaceDetector.class) {
				if (detector == null) {
					detector = new FaceDetector();
				}
			}
		}
		return detector;
	}

	public void init(RtcEventHandler eventHandler) {
		Log.v(TAG, "Init face detector");
		this.eventHandler = eventHandler;
		timer = new Timer("face-detector");
		mediaDataObserverPlugin = MediaDataObserverPlugin.the();
		MediaPreProcessing.setCallback(mediaDataObserverPlugin);
		MediaPreProcessing.setVideoCaptureByteBuffer(mediaDataObserverPlugin.byteBufferCapture);
		mediaDataObserverPlugin.addVideoObserver(videoFrameObserver);
		// add decode buffer for local user
		mediaDataObserverPlugin.addDecodeBuffer(0);
	}

	public void takeScreenshot(String filePath) {
		Log.v(TAG, "Take screenshot and save in " + filePath);
		mediaDataObserverPlugin.saveCaptureVideoSnapshot(filePath);
	}

	public void destroy() {
		Log.v(TAG, "destroy face detector");
		isTimerRunning = false;
		if (task != null) {
			Log.v(TAG, "cancel timer task");
			task.cancel();
		}
		if (mediaDataObserverPlugin != null) {
			mediaDataObserverPlugin.removeVideoObserver(videoFrameObserver);
			mediaDataObserverPlugin.removeAllBuffer();
		}
	}

	public void faceDataChanged(IRtcEngineEventHandler.AgoraFacePositionInfo[] faces) {
		Log.v(TAG, "detected " + faces.length + " faces");
		if (faces.length > 0) {
			lastTimeFaceSeen = System.currentTimeMillis();
		}
	}

	private void checkTimerLogic() {
		if (!isTimerRunning && (sendFaceDetectionStatusEvent || blurOnNoFaceDetected)) {
			isTimerRunning = true;
			task = getNewTask();
			timer.scheduleAtFixedRate(task, 0, 100);
		} else if (isTimerRunning && (!sendFaceDetectionStatusEvent && !blurOnNoFaceDetected)) {
			isTimerRunning = false;
			task.cancel();
		}
	}

	public void setSendFaceDetectionDataEvents(boolean sendFaceDetectionEvents) {
		Log.v(TAG, "setSendFaceDetectionDataEvents " + sendFaceDetectionEvents);
		this.sendFaceDetectionDataEvent = sendFaceDetectionEvents;
	}

	public void setBlurOnNoFaceDetected(boolean blurOnNoFaceDetected) {
		Log.v(TAG, "setBlurOnNoFaceDetected " + blurOnNoFaceDetected);
		this.blurOnNoFaceDetected = blurOnNoFaceDetected;
		lastFaceStatus = null;
		checkTimerLogic();
		if (!blurOnNoFaceDetected) {
			videoFrameObserver.toggleBlurring(false);
		}
	}

	public void setBlurring(boolean enabled) {
		videoFrameObserver.toggleBlurring(enabled);
	}

	public boolean sendFaceDetectionDataEvents() {
		return sendFaceDetectionDataEvent;
	}

	public void setSendFaceDetectionStatusEvent(boolean sendFaceDetectionStatusEvent) {
		Log.v(TAG, "setSendFaceDetectionStatusEvent " + sendFaceDetectionStatusEvent);
		this.sendFaceDetectionStatusEvent = sendFaceDetectionStatusEvent;
		checkTimerLogic();
	}

	private TimerTask getNewTask() {
		return new TimerTask() {
			@Override
			public void run() {
				boolean noFaceDetected = lastTimeFaceSeen < System.currentTimeMillis() - 1500;

				if (blurOnNoFaceDetected) {
					videoFrameObserver.toggleBlurring(noFaceDetected);
				}
				if (sendFaceDetectionStatusEvent && (lastFaceStatus == null || lastFaceStatus != noFaceDetected || lastTimeFaceSent < System.currentTimeMillis() - 1000)) {
					Log.v(TAG, "Sending face status event to react, " + (noFaceDetected ? "no face detected." : "face detected."));
					WritableMap map = Arguments.createMap();
					map.putBoolean("faceDetected", !noFaceDetected);
					RtcEventHandler.sendEvent(eventHandler.getReactApplicationContext(), AGonFaceDetected, map);
					lastFaceStatus = noFaceDetected;
					lastTimeFaceSent = System.currentTimeMillis();
				}
			}
		};
	}

}
