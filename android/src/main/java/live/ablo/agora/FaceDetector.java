package live.ablo.agora;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;

import java.util.Timer;
import java.util.TimerTask;

import io.agora.rtc.IRtcEngineEventHandler;

import static live.ablo.agora.AgoraConst.AGonFaceDetected;

public class FaceDetector {
	private static FaceDetector detector;

	private boolean blurOnNoFaceDetected;
	private boolean sendFaceDetectionDataEvent;
	private boolean sendFaceDetectionStatusEvent;
	private boolean isTimerRunning;
	private long lastTimeFaceSeen;
	private boolean lastFaceStatus;
	private RtcEventHandler eventHandler;
	private Timer timer;
	private TimerTask task;

	private FaceDetector() {

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
		this.eventHandler = eventHandler;
		timer = new Timer("face-detector");
	}

	public void destroy() {
		isTimerRunning = false;
		if (task != null) {
			task.cancel();
		}
	}

	public void faceDataChanged(IRtcEngineEventHandler.AgoraFacePositionInfo[] faces) {
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
		this.sendFaceDetectionDataEvent = sendFaceDetectionEvents;
	}

	public void setBlurOnNoFaceDetected(boolean blurOnNoFaceDetected) {
		this.blurOnNoFaceDetected = blurOnNoFaceDetected;
		checkTimerLogic();
	}

	public boolean sendFaceDetectionDataEvents() {
		return sendFaceDetectionDataEvent;
	}

	public void setSendFaceDetectionStatusEvent(boolean sendFaceDetectionStatusEvent) {
		this.sendFaceDetectionStatusEvent = sendFaceDetectionStatusEvent;
		checkTimerLogic();
	}

	private TimerTask getNewTask() {
		return new TimerTask() {
			@Override
			public void run() {
				boolean noFaceDetected = lastTimeFaceSeen < System.currentTimeMillis() - 500;

				if (blurOnNoFaceDetected) {
					AgoraManager.getInstance().toggleBlurring(noFaceDetected);
				}
				if (sendFaceDetectionStatusEvent && lastFaceStatus != noFaceDetected) {
					WritableMap map = Arguments.createMap();
					map.putBoolean("faceDetected", !noFaceDetected);
					RtcEventHandler.sendEvent(eventHandler.getReactApplicationContext(), AGonFaceDetected, map);
					lastFaceStatus = noFaceDetected;
				}
			}
		};
	}

}
