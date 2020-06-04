package live.ablo.agora;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;

import java.util.Timer;
import java.util.TimerTask;

import io.agora.rtc.IRtcEngineEventHandler;

import static live.ablo.agora.AgoraConst.AGonFaceDetected;
import static live.ablo.agora.AgoraConst.AGonFacePositionChanged;

public class FaceDetector {
	private static FaceDetector detector;

	private boolean blurOnNoFaceDetected;
	private boolean sendFaceDetectionDataEvent;
	private boolean sendFaceDetectionStatusEvent;
	private boolean isTimerRunning;
	private long lastTimeFaceSeen;
	private int currentAmountOfFaces;
	private RtcEventHandler eventHandler;
	private Timer timer;
	private TimerTask task = new TimerTask() {
		@Override
		public void run() {
			if (blurOnNoFaceDetected) {
				AgoraManager.getInstance().toggleBlurring(lastTimeFaceSeen < System.currentTimeMillis() - 500);
			}
			if (sendFaceDetectionStatusEvent) {
				WritableMap map = Arguments.createMap();
				map.putBoolean("faceDetected", currentAmountOfFaces > 0);
				RtcEventHandler.sendEvent(eventHandler.getReactApplicationContext(), AGonFaceDetected, map);
			}
		}
	};

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
		timer.cancel();
	}

	public void faceDataChanged(IRtcEngineEventHandler.AgoraFacePositionInfo[] faces) {
		currentAmountOfFaces = faces.length;
		if (currentAmountOfFaces > 0) {
			lastTimeFaceSeen = System.currentTimeMillis();
		}
	}

	private void checkTimerLogic() {
		if (!isTimerRunning && (sendFaceDetectionStatusEvent || blurOnNoFaceDetected)) {
			isTimerRunning = true;
			timer.scheduleAtFixedRate(task, 0, 100);
		} else if (isTimerRunning && (!sendFaceDetectionStatusEvent && !blurOnNoFaceDetected)) {
			isTimerRunning = false;
			timer.cancel();
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

	public boolean blurOnNoFaceDetected() {
		return blurOnNoFaceDetected;
	}


}
