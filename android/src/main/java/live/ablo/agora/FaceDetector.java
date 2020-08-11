package live.ablo.agora;

import android.graphics.Bitmap;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import live.ablo.agora.data.MediaDataObserverPlugin;
import live.ablo.agora.data.MediaDataVideoObserver;
import live.ablo.agora.data.MediaPreProcessing;

import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;
import static live.ablo.agora.AgoraConst.AGonFaceDetected;
import static live.ablo.agora.AgoraConst.AGonFacePositionChanged;

public class FaceDetector implements MediaDataVideoObserver, OnSuccessListener<List<Face>>, OnFailureListener {
	public static final String TAG = FaceDetector.class.getSimpleName();

	private static FaceDetector detector;
	private final com.google.mlkit.vision.face.FaceDetector mlDetector;
	private boolean blurOnNoFaceDetected;
	private boolean sendFaceDetectionDataEvent;
	private boolean sendFaceDetectionStatusEvent;
	private boolean isTimerRunning;
	private long lastTimeFaceSeen, lastTimeFaceSent;
	private Boolean lastFaceStatus = null;
	private RtcEventHandler eventHandler;
	private Timer timer;
	private TimerTask task;
	private boolean blur = false;
	private boolean processingFace;
	private MediaDataObserverPlugin mediaDataObserverPlugin;
	private boolean enabledFaceDetection;

	private FaceDetector() {
		mlDetector = FaceDetection.getClient(new FaceDetectorOptions.Builder()
				.setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
				.setContourMode(FaceDetectorOptions.CONTOUR_MODE_NONE)
				.setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
				.setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
				.enableTracking()
				.build());
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
		mediaDataObserverPlugin.addVideoObserver(this);
		// add decode buffer for local user
		mediaDataObserverPlugin.addDecodeBuffer(0);
	}

	public void destroy() {
		Log.v(TAG, "destroy face detector");
		isTimerRunning = false;
		if (task != null) {
			Log.v(TAG, "cancel timer task");
			task.cancel();
		}
		if (mediaDataObserverPlugin != null) {
			mediaDataObserverPlugin.removeVideoObserver(this);
			mediaDataObserverPlugin.removeAllBuffer();
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
			toggleBlurring(false);
		}
	}

	public boolean sendFaceDetectionDataEvents() {
		return sendFaceDetectionDataEvent;
	}

	public void setSendFaceDetectionStatusEvent(boolean sendFaceDetectionStatusEvent) {
		Log.v(TAG, "setSendFaceDetectionStatusEvent " + sendFaceDetectionStatusEvent);
		this.sendFaceDetectionStatusEvent = sendFaceDetectionStatusEvent;
		checkTimerLogic();
	}

	public void enableFaceDetection(boolean toggleFaceDetection) {
		enabledFaceDetection = toggleFaceDetection;
	}

	public void toggleBlurring(boolean enable) {
		blur = enable;
	}

	private TimerTask getNewTask() {
		return new TimerTask() {
			@Override
			public void run() {
				boolean noFaceDetected = lastTimeFaceSeen < System.currentTimeMillis() - 500;

				if (blurOnNoFaceDetected) {
					toggleBlurring(noFaceDetected);
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

	@Override
	public void onCaptureVideoFrame(byte[] data, int frameType, int width, int height, int bufferLength, int yStride, int uStride, int vStride, int rotation, long renderTimeMs) {
		Bitmap frame = YUVUtils.i420ToBitmap(width, height, rotation, bufferLength, data, yStride, uStride, vStride);
		if (!processingFace && enabledFaceDetection) {
			processingFace = true;
			InputImage image = InputImage.fromBitmap(Bitmap.createScaledBitmap(frame, frame.getWidth() / 2, frame.getHeight() / 2, true), 0);
			mlDetector.process(image).addOnSuccessListener(this).addOnFailureListener(this);
		}
		if (blur) {
			Bitmap bmp = YUVUtils.pixelate(frame, 10);
			System.arraycopy(YUVUtils.bitmapToI420(width, height, bmp), 0, data, 0, bufferLength);
		}
	}

	@Override
	public void onRenderVideoFrame(int uid, byte[] data, int frameType, int width, int height, int bufferLength, int yStride, int uStride, int vStride, int rotation, long renderTimeMs) {

	}

	@Override
	public void onSuccess(final List<Face> faces) {
		Log.v(TAG, "detected " + faces.size() + " faces");
		if (!faces.isEmpty()) {
			lastTimeFaceSeen = System.currentTimeMillis();
		}
		if (FaceDetector.getInstance().sendFaceDetectionDataEvents()) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					WritableMap map = Arguments.createMap();
					WritableArray list = Arguments.createArray();
					for (Face info : faces) {
						WritableMap face = Arguments.createMap();
						list.pushMap(face);
					}
					map.putArray("faces", list);
					RtcEventHandler.sendEvent(eventHandler.getReactApplicationContext(), AGonFacePositionChanged, map);
				}
			});
		}
		processingFace = false;
	}

	@Override
	public void onFailure(@NonNull Exception e) {
		Log.e(TAG, "Failed detecting face", e);
		processingFace = false;
	}

}
