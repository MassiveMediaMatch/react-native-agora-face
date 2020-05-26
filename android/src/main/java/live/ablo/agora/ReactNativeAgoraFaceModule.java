package live.ablo.agora;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;

import java.nio.charset.Charset;
import java.util.ArrayList;

import io.agora.rtc.IAudioEffectManager;
import io.agora.rtc.IMetadataObserver;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.internal.LastmileProbeConfig;
import io.agora.rtc.live.LiveInjectStreamConfig;
import io.agora.rtc.live.LiveTranscoding;
import io.agora.rtc.video.AgoraImage;
import io.agora.rtc.video.BeautyOptions;
import io.agora.rtc.video.CameraCapturerConfiguration;

public class ReactNativeAgoraFaceModule extends ReactContextBaseJavaModule {

	private final RtcEngineEventHandler engineEventHandler;
	private MediaObserver mediaObserver;

	public ReactNativeAgoraFaceModule(ReactApplicationContext reactContext) {
		super(reactContext);
		engineEventHandler = new RtcEngineEventHandler(reactContext);
	}

	@Override
	public String getName() {
		return "ReactNativeAgoraFace";
	}

	@ReactMethod
	public void init(ReadableMap options) {
		RtcEngineDelegate.INSTANCE.init(getReactApplicationContext(), options, engineEventHandler);
	}

	@ReactMethod
	public void renewToken(String token,
						   Promise promise) {
		int res = RtcEngineDelegate.INSTANCE.renewToken(token);
		resolvePromiseFromResolve(res, promise, "renew token");
	}

	@ReactMethod
	public void enableWebSdkInteroperability(boolean enabled, Promise promise) {
		int res = RtcEngineDelegate.INSTANCE.enableWebSdkInteroperability(enabled);
		resolvePromiseFromResolve(res, promise, "enableWebSdkInteroperability Failed");
	}

	@ReactMethod
	public void getConnectionState(Promise promise) {
		int res = RtcEngineDelegate.INSTANCE.getConnectionState();
		resolvePromiseFromResolve(res, promise, "getConnectionState Failed");
	}

	@ReactMethod
	public void setClientRole(int role) {
		int res = RtcEngineDelegate.INSTANCE.setClientRole(role);
		if (res != 0) {
			sendError(getReactApplicationContext(), res, "setClientRole Failed");
		}
	}

	@ReactMethod
	public void joinChannel(ReadableMap options) {
		int res = RtcEngineDelegate.INSTANCE.joinChannel(options);
		if (res != 0) {
			sendError(getReactApplicationContext(), res, "joinChannel Failed");
		}
	}

	@ReactMethod
	public void leaveChannel(Promise promise) {
		int res = RtcEngineDelegate.INSTANCE.leaveChannel();
		resolvePromiseFromResolve(res, promise);
	}

	@ReactMethod
	public void destroy() {
		RtcEngine.destroy();
	}

	@ReactMethod
	public void startPreview() {
		RtcEngineDelegate.INSTANCE.startPreview();
	}

	@ReactMethod
	public void stopPreview() {
		RtcEngineDelegate.INSTANCE.stopPreview();
	}

	@ReactMethod
	public void setEnableSpeakerphone(boolean enabled) {
		int res = RtcEngineDelegate.INSTANCE.setEnableSpeakerphone(enabled);
		if (res != 0) {
			sendError(getReactApplicationContext(), res, "setEnableSpeakerphone Failed");
		}
	}

	@ReactMethod
	public void setDefaultAudioRouteToSpeakerphone(boolean enabled) {
		int res = RtcEngineDelegate.INSTANCE.setDefaultAudioRouteToSpeakerphone(enabled);
		if (res != 0) {
			sendError(getReactApplicationContext(), res, "$1");
		}
	}

	@ReactMethod
	public void enableVideo() {
		RtcEngineDelegate.INSTANCE.enableVideo();
	}

	@ReactMethod
	public void disableVideo() {
		RtcEngineDelegate.INSTANCE.disableVideo();
	}

	@ReactMethod
	public void enableLocalVideo(boolean enabled) {
		RtcEngineDelegate.INSTANCE.enableLocalVideo(enabled);
	}

	@ReactMethod
	public void muteLocalVideoStream(boolean muted) {
		RtcEngineDelegate.INSTANCE.muteLocalVideoStream(muted);
	}

	@ReactMethod
	public void muteAllRemoteVideoStreams(boolean muted) {
		RtcEngineDelegate.INSTANCE.muteAllRemoteVideoStreams(muted);
	}

	@ReactMethod
	public void muteRemoteVideoStream(int uid, boolean muted) {
		RtcEngineDelegate.INSTANCE.muteRemoteVideoStream(uid, muted);
	}

	@ReactMethod
	public void setDefaultMuteAllRemoteVideoStreams(boolean muted) {
		RtcEngineDelegate.INSTANCE.setDefaultMuteAllRemoteVideoStreams(muted);
	}

	@ReactMethod
	public void switchCamera(Promise promise) {
		int res = RtcEngineDelegate.INSTANCE.switchCamera();
		resolvePromiseFromResolve(res, promise, "switchCamera Failed");
	}

	@ReactMethod
	public void isCameraZoomSupported(Promise promise) {
		boolean res = RtcEngineDelegate.INSTANCE.isCameraZoomSupported();
		resolvePromiseFromResolve(res, promise);
	}


	@ReactMethod
	public void isCameraTorchSupported(Promise promise) {
		boolean res = RtcEngineDelegate.INSTANCE.isCameraTorchSupported();
		resolvePromiseFromResolve(res, promise);
	}

	@ReactMethod
	public void isCameraFocusSupported(Promise promise) {
		boolean res = RtcEngineDelegate.INSTANCE.isCameraFocusSupported();
		resolvePromiseFromResolve(res, promise);
	}

	@ReactMethod
	public void isCameraExposurePositionSupported(Promise promise) {
		boolean res = RtcEngineDelegate.INSTANCE.isCameraExposurePositionSupported();
		resolvePromiseFromResolve(res, promise);
	}


	@ReactMethod
	public void isCameraAutoFocusFaceModeSupported(Promise promise) {
		boolean res = RtcEngineDelegate.INSTANCE.isCameraAutoFocusFaceModeSupported();
		resolvePromiseFromResolve(res, promise);

	}

	@ReactMethod
	public void setCameraZoomFactor(float factor, Promise promise) {
		int res = RtcEngineDelegate.INSTANCE.setCameraZoomFactor(factor);
		resolvePromiseFromResolve(res, promise, "setCameraZoomFactor Failed");
	}

	@ReactMethod
	public void getCameraMaxZoomFactor(Promise promise) {
		double res = RtcEngineDelegate.INSTANCE.getCameraMaxZoomFactor();
		resolvePromiseFromResolve(res, promise);
	}

	@ReactMethod
	public void setCameraFocusPositionInPreview(ReadableMap options, Promise promise) {
		int res = RtcEngineDelegate.INSTANCE.setCameraFocusPositionInPreview(
				(float) options.getDouble("x"),
				(float) options.getDouble("y")
		);
		resolvePromiseFromResolve(res, promise, "setCameraFocusPositionInPreview Failed");

	}

	@ReactMethod
	public void setCameraExposurePosition(ReadableMap options, Promise promise) {
		int res = RtcEngineDelegate.INSTANCE.setCameraExposurePosition(
				(float) options.getDouble("x"),
				(float) options.getDouble("y")
		);
		resolvePromiseFromResolve(res, promise, "setCameraExposurePosition Failed");
	}

	@ReactMethod
	public void setCameraTorchOn(boolean isOn, Promise promise) {
		int res = RtcEngineDelegate.INSTANCE.setCameraTorchOn(isOn);
		resolvePromiseFromResolve(res, promise, "setCameraTorchOn Failed");
	}

	@ReactMethod
	public void setCameraAutoFocusFaceModeEnabled(boolean enabled, Promise promise) {
		int res = RtcEngineDelegate.INSTANCE.setCameraAutoFocusFaceModeEnabled(enabled);
		resolvePromiseFromResolve(res, promise, "setCameraAutoFocusFaceModeEnabled Failed");

	}

	@ReactMethod
	public void getCallId(Promise promise) {
		String res = RtcEngineDelegate.INSTANCE.getCallId();
		resolvePromiseFromResolve(res, promise);
	}

	@ReactMethod
	public void setLog(String filePath, int level, int size, Promise promise) {
		try {
			int res = 0;
			res = RtcEngineDelegate.INSTANCE.setLogFileSize(size);
			if (res != 0) throw new ReactNativeAgoraException("setLogFileSize Failed", res);
			res = RtcEngineDelegate.INSTANCE.setLogFilter(level);
			if (res != 0) throw new ReactNativeAgoraException("setLogFilter Failed", res);
			res = RtcEngineDelegate.INSTANCE.setLogFile(filePath);
			resolvePromiseFromResolve(res, promise, "setLogFile Failed");
		} catch (Exception e) {
			promise.reject(e);
		}
	}


	@ReactMethod
	public void enableAudio() {
		RtcEngineDelegate.INSTANCE.enableAudio();
	}

	@ReactMethod
	public void disableAudio() {
		RtcEngineDelegate.INSTANCE.disableAudio();
	}

	@ReactMethod
	public void muteAllRemoteAudioStreams(boolean muted) {
		RtcEngineDelegate.INSTANCE.muteAllRemoteAudioStreams(muted);
	}

	@ReactMethod
	public void muteRemoteAudioStream(int uid, boolean muted) {
		RtcEngineDelegate.INSTANCE.muteRemoteAudioStream(uid, muted);
	}

	@ReactMethod
	public void setDefaultMuteAllRemoteAudioStreams(boolean muted) {
		RtcEngineDelegate.INSTANCE.setDefaultMuteAllRemoteAudioStreams(muted);
	}

	@ReactMethod
	public void adjustRecordingSignalVolume(int volume) {
		int res = RtcEngineDelegate.INSTANCE.adjustRecordingSignalVolume(volume);
		if (res != 0) {
			sendError(getReactApplicationContext(), res, "adjustRecordingSignalVolume Failed");
		}
	}

	@ReactMethod
	public void adjustPlaybackSignalVolume(int volume) {
		int res = RtcEngineDelegate.INSTANCE.adjustPlaybackSignalVolume(volume);
		if (res != 0) {
			sendError(getReactApplicationContext(), res, "adjustPlaybackSignalVolume Failed");
		}
	}

	@ReactMethod
	public void enableAudioVolumeIndication(int interval, int smooth) {
		RtcEngineDelegate.INSTANCE.enableAudioVolumeIndication(interval, smooth, false);
	}

	@ReactMethod
	public void enableLocalAudio(boolean enabled) {
		RtcEngineDelegate.INSTANCE.enableLocalAudio(enabled);
	}

	@ReactMethod
	public void muteLocalAudioStream(boolean enabled) {
		RtcEngineDelegate.INSTANCE.muteLocalAudioStream(enabled);
	}

	@ReactMethod
	public void methodisSpeakerphoneEnabled(Callback callback) {
		WritableMap map = Arguments.createMap();
		map.putBoolean("status", RtcEngineDelegate.INSTANCE.isSpeakerphoneEnabled());
		callback.invoke(map);
	}

	@ReactMethod
	public void enableInEarMonitoring(boolean enabled) {
		int res = RtcEngineDelegate.INSTANCE.enableInEarMonitoring(enabled);
		if (res != 0) {
			sendError(getReactApplicationContext(), res, "enableInEarMonitoring Failed");
		}
	}

	@ReactMethod
	public void setInEarMonitoringVolume(int volume) {
		int res = RtcEngineDelegate.INSTANCE.setInEarMonitoringVolume(volume);
		if (res != 0) {
			sendError(getReactApplicationContext(), res, "setInEarMonitoringVolume Failed");
		}
	}

	@ReactMethod
	public void setLocalVoicePitch(double pitch) {
		int res = RtcEngineDelegate.INSTANCE.setLocalVoicePitch(pitch);
		if (res != 0) {
			sendError(getReactApplicationContext(), res, "setLocalVoicePitch Failed");
		}
	}

	@ReactMethod
	public void setLocalVoiceEqualization(int band, int gain) {
		int res = RtcEngineDelegate.INSTANCE.setLocalVoiceEqualization(band, gain);
		if (res != 0) {
			sendError(getReactApplicationContext(), res, "setLocalVoiceEqualization Failed");
		}
	}

	@ReactMethod
	public void setLocalVoiceReverb(int reverb, int value) {
		int res = RtcEngineDelegate.INSTANCE.setLocalVoiceReverb(reverb, value);
		if (res != 0) {
			sendError(getReactApplicationContext(), res, "setLocalVoiceReverb Failed");
		}
	}

	@ReactMethod
	public void startAudioMixing(ReadableMap options) {
		int res = RtcEngineDelegate.INSTANCE.startAudioMixing(
				options.getString("filepath"),
				options.getBoolean("loopback"),
				options.getBoolean("replace"),
				options.getInt("cycle")
		);
		if (res != 0) {
			sendError(getReactApplicationContext(), res, "startAudioMixing Failed");
		}
	}

	@ReactMethod
	public void stopAudioMixing() {
		int res = RtcEngineDelegate.INSTANCE.stopAudioMixing();
		if (res != 0) {
			sendError(getReactApplicationContext(), res, "stopAudioMixing Failed");
		}
	}

	@ReactMethod
	public void pauseAudioMixing() {
		int res = RtcEngineDelegate.INSTANCE.pauseAudioMixing();
		if (res != 0) {
			sendError(getReactApplicationContext(), res, "pauseAudioMixing Failed");
		}
	}

	@ReactMethod
	public void resumeAudioMixing() {
		int res = RtcEngineDelegate.INSTANCE.resumeAudioMixing();
		if (res != 0) {
			sendError(getReactApplicationContext(), res, "resumeAudioMixing Failed");
		}
	}

	@ReactMethod
	public void adjustAudioMixingVolume(int volume) {
		int res = RtcEngineDelegate.INSTANCE.adjustAudioMixingVolume(volume);
		if (res != 0) {
			sendError(getReactApplicationContext(), res, "adjustAudioMixingVolume Failed");
		}
	}

	@ReactMethod
	public void adjustAudioMixingPlayoutVolume(int volume) {
		int res = RtcEngineDelegate.INSTANCE.adjustAudioMixingPlayoutVolume(volume);
		if (res != 0) {
			sendError(getReactApplicationContext(), res, "adjustAudioMixingPlayoutVolume Failed");
		}
	}

	@ReactMethod
	public void adjustAudioMixingPublishVolume(int volume) {
		int res = RtcEngineDelegate.INSTANCE.adjustAudioMixingPublishVolume(volume);
		if (res != 0) {
			sendError(getReactApplicationContext(), res, "adjustAudioMixingPublishVolume Failed");
		}
	}

	@ReactMethod
	public void getAudioMixingPlayoutVolume(Promise promise) {
		int res = RtcEngineDelegate.INSTANCE.getAudioMixingPlayoutVolume();
		resolvePromiseFromNegativeResolve(res, promise, "getAudioMixingPlayoutVolume Failed");
	}

	@ReactMethod
	public void getAudioMixingPublishVolume(Promise promise) {
		int res = RtcEngineDelegate.INSTANCE.getAudioMixingPlayoutVolume();
		resolvePromiseFromNegativeResolve(res, promise, "getAudioMixingPublishVolume Failed");
	}

	@ReactMethod
	public void getAudioMixingDuration(Promise promise) {
		int res = RtcEngineDelegate.INSTANCE.getAudioMixingDuration();
		resolvePromiseFromResolve(res, promise, "getAudioMixingDuration Failed");
	}

	@ReactMethod
	public void getAudioMixingCurrentPosition(Promise promise) {
		int res = RtcEngineDelegate.INSTANCE.getAudioMixingCurrentPosition();
		resolvePromiseFromResolve(res, promise, "getAudioMixingCurrentPosition Failed");
	}

	@ReactMethod
	public void setAudioMixingPosition(int pos, Promise promise) {
		int res = RtcEngineDelegate.INSTANCE.setAudioMixingPosition(pos);
		resolvePromiseFromResolve(res, promise, "setAudioMixingPosition Failed");
	}

	@ReactMethod
	public void startAudioRecording(ReadableMap options, Promise promise) {
		try {
			int res = RtcEngineDelegate.INSTANCE
					.startAudioRecording(
							options.getString("filepath"),
							options.getInt("quality")
					);
			resolvePromiseFromResolve(res, promise, "startAudioRecording Failed");
		} catch (Exception e) {
			promise.reject("131007", e);
		}
	}

	@ReactMethod
	public void stopAudioRecording(Promise promise) {
		int res = RtcEngineDelegate.INSTANCE
				.stopAudioRecording();
		resolvePromiseFromResolve(res, promise, "stopAudioRecording Failed");
	}

	@ReactMethod
	public void stopEchoTest(Promise promise) {
		int res = RtcEngineDelegate.INSTANCE
				.stopEchoTest();
		resolvePromiseFromResolve(res, promise, "stopEchoTest Failed");

	}

	@ReactMethod
	public void enableLastmileTest(Promise promise) {
		int res = RtcEngineDelegate.INSTANCE
				.enableLastmileTest();
		resolvePromiseFromResolve(res, promise, "enableLastmileTest Failed");
	}

	@ReactMethod
	public void disableLastmileTest(Promise promise) {
		try {
			int res = RtcEngineDelegate.INSTANCE
					.disableLastmileTest();
			resolvePromiseFromResolve(res, promise, "disableLastmileTest Failed");
		} catch (Exception e) {
			promise.reject("131022", e);
		}
	}

	@ReactMethod
	public void setRecordingAudioFrameParameters(ReadableMap options, Promise promise) {
		try {
			int res = RtcEngineDelegate.INSTANCE
					.setRecordingAudioFrameParameters(
							options.getInt("sampleRate"),
							options.getInt("channel"),
							options.getInt("mode"),
							options.getInt("samplesPerCall")
					);
			resolvePromiseFromResolve(res, promise, "setRecordingAudioFrameParameters Failed");
		} catch (Exception e) {
			promise.reject("131023", e);
		}
	}

	@ReactMethod
	public void setPlaybackAudioFrameParameters(ReadableMap options, Promise promise) {
		try {
			int res = RtcEngineDelegate.INSTANCE
					.setPlaybackAudioFrameParameters(
							options.getInt("sampleRate"),
							options.getInt("channel"),
							options.getInt("mode"),
							options.getInt("samplesPerCall")
					);
			resolvePromiseFromResolve(res, promise, "setPlaybackAudioFrameParameters Failed");
		} catch (Exception e) {
			promise.reject("131024", e);
		}
	}

	@ReactMethod
	public void setMixedAudioFrameParameters(WritableMap options, Promise promise) {
		try {
			int res = RtcEngineDelegate.INSTANCE
					.setMixedAudioFrameParameters(
							options.getInt("sampleRate"),
							options.getInt("samplesPerCall")
					);
			resolvePromiseFromResolve(res, promise, "setMixedAudioFrameParameters Failed");
		} catch (Exception e) {
			promise.reject("131025", e);
		}
	}

	public AgoraImage createAgoraImage(ReadableMap options) {
		AgoraImage image = new AgoraImage();
		image.url = options.getString("url");
		image.height = options.getInt("height");
		image.width = options.getInt("width");
		image.x = options.getInt("x");
		image.y = options.getInt("y");
		return image;
	}

	@ReactMethod
	public void addVideoWatermark(ReadableMap options, Promise promise) {
		try {
			int res = RtcEngineDelegate.INSTANCE
					.addVideoWatermark(createAgoraImage(options));
			resolvePromiseFromResolve(res, promise, "addVideoWatermark Failed");
		} catch (Exception e) {
			promise.reject("131026", e);
		}
	}

	@ReactMethod
	public void clearVideoWatermarks(Promise promise) {
		try {
			int res = RtcEngineDelegate.INSTANCE
					.clearVideoWatermarks();
			resolvePromiseFromResolve(res, promise, "clearVideoWatermarks Failed");
		} catch (Exception e) {
			promise.reject("131027", e);
		}
	}

	@ReactMethod
	public void setLocalPublishFallbackOption(int option, Promise promise) {
		try {
			int res = RtcEngineDelegate.INSTANCE
					.setLocalPublishFallbackOption(option);
			resolvePromiseFromResolve(res, promise, "setLocalPublishFallbackOption Failed");
		} catch (Exception e) {
			promise.reject("131028", e);
		}
	}

	@ReactMethod
	public void setRemoteSubscribeFallbackOption(int option, Promise promise) {
		try {
			int res = RtcEngineDelegate.INSTANCE
					.setRemoteSubscribeFallbackOption(option);
			resolvePromiseFromResolve(res, promise, "setRemoteSubscribeFallbackOption Failed");
		} catch (Exception e) {
			promise.reject("131029", e);
		}
	}

	@ReactMethod
	public void enableDualStreamMode(boolean enabled, Promise promise) {
		try {
			int res = RtcEngineDelegate.INSTANCE
					.enableDualStreamMode(enabled);
			resolvePromiseFromResolve(res, promise, "enableDualStreamMode Failed");
		} catch (Exception e) {
			promise.reject("131028", e);
		}
	}


	@ReactMethod
	public void setRemoteVideoStreamType(ReadableMap options, Promise promise) {
		try {
			int res = RtcEngineDelegate.INSTANCE
					.setRemoteVideoStreamType(
							options.getInt("uid"),
							options.getInt("streamType")
					);
			resolvePromiseFromResolve(res, promise, "setRemoteVideoStreamType Failed");
		} catch (Exception e) {
			promise.reject("131029", e);
		}
	}

	@ReactMethod
	public void setRemoteDefaultVideoStreamType(ReadableMap options, Promise promise) {
		try {
			int res = RtcEngineDelegate.INSTANCE
					.setRemoteDefaultVideoStreamType(
							options.getInt("streamType")
					);
			resolvePromiseFromResolve(res, promise, "setRemoteDefaultVideoStreamType Failed");
		} catch (Exception e) {
			promise.reject("-1", e);
		}
	}

	@ReactMethod
	public void sendMediaData(String data, final Promise promise) {
		if (null == mediaObserver) {
			promise.reject("-1", "sendMediaData failed");
		} else {
			mediaObserver.setMetadata(data.getBytes(Charset.forName("UTF-8")));
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			promise.resolve(map);
		}
	}

	@ReactMethod
	public void registerMediaMetadataObserver(final Promise promise) {
		try {
			mediaObserver = new MediaObserver(getReactApplicationContext());
			int res = RtcEngineDelegate.INSTANCE
					.registerMediaMetadataObserver(mediaObserver, IMetadataObserver.VIDEO_METADATA);
			if (res < 0) {
				throw new ReactNativeAgoraException("registerMediaMetadataObserver Failed", res);
			}
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putInt("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject("-1", e);
		}
	}

	public LiveInjectStreamConfig.AudioSampleRateType getAudioSampleRateEnum(int val) {
		LiveInjectStreamConfig.AudioSampleRateType type = LiveInjectStreamConfig.AudioSampleRateType.TYPE_32000;
		switch (Integer.valueOf(val)) {
			case 32000:
				type = LiveInjectStreamConfig.AudioSampleRateType.TYPE_32000;
				break;
			case 44100:
				type = LiveInjectStreamConfig.AudioSampleRateType.TYPE_44100;
				break;
			case 48000:
				type = LiveInjectStreamConfig.AudioSampleRateType.TYPE_48000;
				break;
		}
		return type;
	}

	public LiveTranscoding.AudioSampleRateType getLiveTranscodingAudioSampleRateEnum(int val) {
		LiveTranscoding.AudioSampleRateType type = LiveTranscoding.AudioSampleRateType.TYPE_32000;
		switch (Integer.valueOf(val)) {
			case 32000:
				type = LiveTranscoding.AudioSampleRateType.TYPE_32000;
				break;
			case 44100:
				type = LiveTranscoding.AudioSampleRateType.TYPE_44100;
				break;
			case 48000:
				type = LiveTranscoding.AudioSampleRateType.TYPE_48000;
				break;
		}
		return type;
	}


	public LiveTranscoding.VideoCodecProfileType getLiveTranscodingVideoCodecProfileEnum(int val) {
		LiveTranscoding.VideoCodecProfileType type = LiveTranscoding.VideoCodecProfileType.BASELINE;
		switch (val) {
			case 66:
				type = LiveTranscoding.VideoCodecProfileType.BASELINE;
				break;
			case 77:
				type = LiveTranscoding.VideoCodecProfileType.MAIN;
				break;
			case 100:
				type = LiveTranscoding.VideoCodecProfileType.HIGH;
				break;
		}
		return type;
	}


	@ReactMethod
	public void addInjectStreamUrl(ReadableMap options, Promise promise) {
		try {
			LiveInjectStreamConfig injectstream = new LiveInjectStreamConfig();
			ReadableMap config = options.getMap("config");
			ReadableMap size = config.getMap("size");
			injectstream.width = size.getInt("width");
			injectstream.height = size.getInt("height");
			injectstream.videoGop = config.getInt("videoGop");
			injectstream.videoBitrate = config.getInt("videoBitrate");
			injectstream.videoFramerate = config.getInt("videoFramerate");
			injectstream.audioBitrate = config.getInt("audioBitrate");
			injectstream.audioSampleRate = getAudioSampleRateEnum(config.getInt("audioSampleRate"));
			injectstream.audioChannels = config.getInt("audioChannels");

			int res = RtcEngineDelegate.INSTANCE
					.addInjectStreamUrl(
							options.getString("url"),
							injectstream
					);
			resolvePromiseFromResolve(res, promise, "addInjectStreamUrl Failed");
		} catch (Exception e) {
			promise.reject("131031", e);
		}
	}

	@ReactMethod
	public void removeInjectStreamUrl(ReadableMap options, Promise promise) {
		try {
			int res = RtcEngineDelegate.INSTANCE
					.removeInjectStreamUrl(options.getString("url"));
			resolvePromiseFromResolve(res, promise, "removeInjectStreamUrl Failed");
		} catch (Exception e) {
			promise.reject("131032", e);
		}
	}

	@ReactMethod
	public void addPublishStreamUrl(ReadableMap options, Promise promise) {
		try {
			int res = RtcEngineDelegate.INSTANCE
					.addPublishStreamUrl(
							options.getString("url"),
							options.getBoolean("enable")
					);
			resolvePromiseFromResolve(res, promise, "addPublishStreamUrl Failed");
		} catch (Exception e) {
			promise.reject("131033", e);
		}
	}

	@ReactMethod
	public void removePublishStreamUrl(ReadableMap options, Promise promise) {
		try {
			int res = RtcEngineDelegate.INSTANCE
					.removePublishStreamUrl(options.getString("url"));
			resolvePromiseFromResolve(res, promise, "removePublishStreamUrl Failed");
		} catch (Exception e) {
			promise.reject("131034", e);
		}
	}

	@ReactMethod
	public void setLiveTranscoding(ReadableMap options, Promise promise) {
		try {
			LiveTranscoding transcoding = new LiveTranscoding();
			if (options.hasKey("size") && null != options.getMap("size")) {
				ReadableMap size = options.getMap("size");
				transcoding.width = size.getInt("width");
				transcoding.height = size.getInt("height");
			}
			if (options.hasKey("videoBitrate")) {
				transcoding.videoBitrate = options.getInt("videoBitrate");
			}
			if (options.hasKey("videoFramerate")) {
				transcoding.videoFramerate = options.getInt("videoFramerate");
			}
			if (options.hasKey("lowLatency")) {
				transcoding.lowLatency = options.getBoolean("lowLatency");
			}
			if (options.hasKey("videoGop")) {
				transcoding.videoGop = options.getInt("videoGop");
			}
			if (options.hasKey("videoCodecProfile")) {
				transcoding.videoCodecProfile = getLiveTranscodingVideoCodecProfileEnum(options.getInt("videoCodecProfile"));
			}
			if (options.hasKey("transcodingUsers")) {
				ArrayList<LiveTranscoding.TranscodingUser> users = new ArrayList<LiveTranscoding.TranscodingUser>();
				ReadableArray transcodingUsers = options.getArray("transcodingUsers");
				for (int i = 0; i < transcodingUsers.size(); i++) {
					ReadableMap _map = transcodingUsers.getMap(i);
					LiveTranscoding.TranscodingUser user = new LiveTranscoding.TranscodingUser();
					user.uid = _map.getInt("uid");
					ReadableMap backgroundColor = _map.getMap("backgroundColor");
					user.x = backgroundColor.getInt("x");
					user.y = backgroundColor.getInt("y");
					user.width = backgroundColor.getInt("width");
					user.height = backgroundColor.getInt("height");
					user.zOrder = _map.getInt("zOrder");
					user.alpha = _map.getInt("alpha");
					user.audioChannel = _map.getInt("audioChannel");
					users.add(user);
				}
				transcoding.setUsers(users);
			}
			if (options.hasKey("transcodingExtraInfo")) {
				transcoding.userConfigExtraInfo = options.getString("transcodingExtraInfo");
			}
			if (options.hasKey("watermark")) {
				ReadableMap watermark = options.getMap("watermark");
				WritableMap map = Arguments.createMap();
				map.putString("url", watermark.getString("url"));
				map.putString("x", watermark.getString("x"));
				map.putString("y", watermark.getString("y"));
				map.putString("width", watermark.getString("width"));
				map.putString("height", watermark.getString("height"));
				transcoding.watermark = createAgoraImage(map);
			}
			if (options.hasKey("backgroundImage")) {
				ReadableMap watermark = options.getMap("backgroundImage");
				WritableMap map = Arguments.createMap();
				map.putString("url", watermark.getString("url"));
				map.putString("x", watermark.getString("x"));
				map.putString("y", watermark.getString("y"));
				map.putString("width", watermark.getString("width"));
				map.putString("height", watermark.getString("height"));
				transcoding.backgroundImage = createAgoraImage(map);
			}
			if (options.hasKey("backgroundColor")) {
				ReadableMap backgroundColor = options.getMap("backgroundColor");
				transcoding.setBackgroundColor(
						backgroundColor.getInt("red"),
						backgroundColor.getInt("green"),
						backgroundColor.getInt("blue")
				);
			}
			if (options.hasKey("audioSampleRate")) {
				transcoding.audioSampleRate = getLiveTranscodingAudioSampleRateEnum(options.getInt("audioSampleRate"));
			}
			if (options.hasKey("audioBitrate")) {
				transcoding.audioChannels = options.getInt("audioBitrate");
			}
			if (options.hasKey("audioChannels")) {
				transcoding.audioChannels = options.getInt("audioChannel");
			}
			int res = RtcEngineDelegate.INSTANCE.setLiveTranscoding(transcoding);
			if (res != 0) {
				sendError(getReactApplicationContext(), res, "setLiveTranscoding Failed");
			}
		} catch (Exception e) {
			promise.reject(e);
		}
	}

	@ReactMethod
	public void getEffectsVolume(Promise promise) {
		IAudioEffectManager manager = RtcEngineDelegate.INSTANCE.getAudioEffectManager();
		double res = manager.getEffectsVolume();
		resolvePromiseFromResolve(res, promise, "getEffectsVolume Failed");
	}

	@ReactMethod
	public void setEffectsVolume(double volume, Promise promise) {
		IAudioEffectManager manager = RtcEngineDelegate.INSTANCE.getAudioEffectManager();
		int res = manager.setEffectsVolume(volume);
		resolvePromiseFromResolve(res, promise, "setEffectsVolume Failed");
	}


	@ReactMethod
	public void setVolumeOfEffect(int soundId, double volume, Promise promise) {
		IAudioEffectManager manager = RtcEngineDelegate.INSTANCE.getAudioEffectManager();
		int res = manager.setVolumeOfEffect(soundId, volume);
		resolvePromiseFromResolve(res, promise, "setVolumeOfEffect Failed");
	}

	@ReactMethod
	public void playEffect(ReadableMap options, Promise promise) {
		try {
			IAudioEffectManager manager = RtcEngineDelegate.INSTANCE.getAudioEffectManager();
			int res = manager.playEffect(
					options.getInt("soundid"),
					options.getString("filepath"),
					options.getInt("loopcount"),
					options.getDouble("pitch"),
					options.getDouble("pan"),
					options.getDouble("gain"),
					options.getBoolean("publish")
			);
			resolvePromiseFromResolve(res, promise, "playEffect Failed");
		} catch (Exception e) {
			promise.reject(e);
		}
	}


	@ReactMethod
	public void stopEffect(int soundId, Promise promise) {
		IAudioEffectManager manager = RtcEngineDelegate.INSTANCE.getAudioEffectManager();
		int res = manager.stopEffect(soundId);
		resolvePromiseFromResolve(res, promise, "stopEffect Failed");
	}

	@ReactMethod
	public void stopAllEffects(Promise promise) {
		IAudioEffectManager manager = RtcEngineDelegate.INSTANCE.getAudioEffectManager();
		int res = manager.stopAllEffects();
		resolvePromiseFromResolve(res, promise, "stopAllEffects Failed");
	}

	@ReactMethod
	public void preloadEffect(int soundId, String filePath, Promise promise) {
		IAudioEffectManager manager = RtcEngineDelegate.INSTANCE.getAudioEffectManager();
		int res = manager.preloadEffect(soundId, filePath);
		resolvePromiseFromResolve(res, promise, "preloadEffect Failed");
	}

	@ReactMethod
	public void unloadEffect(int soundId, Promise promise) {
		IAudioEffectManager manager = RtcEngineDelegate.INSTANCE.getAudioEffectManager();
		int res = manager.unloadEffect(soundId);
		resolvePromiseFromResolve(res, promise, "unloadEffect Failed");
	}

	@ReactMethod
	public void pauseEffect(int soundId, Promise promise) {
		IAudioEffectManager manager = RtcEngineDelegate.INSTANCE.getAudioEffectManager();
		int res = manager.pauseEffect(soundId);
		resolvePromiseFromResolve(res, promise, "pauseEffect Failed");
	}

	@ReactMethod
	public void pauseAllEffects(Promise promise) {
		IAudioEffectManager manager = RtcEngineDelegate.INSTANCE.getAudioEffectManager();
		int res = manager.pauseAllEffects();
		resolvePromiseFromResolve(res, promise, "pauseAllEffects Failed");
	}

	@ReactMethod
	public void resumeEffect(int soundId, Promise promise) {
		IAudioEffectManager manager = RtcEngineDelegate.INSTANCE.getAudioEffectManager();
		int res = manager.resumeEffect(soundId);
		resolvePromiseFromResolve(res, promise, "resumeEffect Failed");
	}

	@ReactMethod
	public void resumeAllEffects(int soundId, Promise promise) {
		IAudioEffectManager manager = RtcEngineDelegate.INSTANCE.getAudioEffectManager();
		int res = manager.resumeAllEffects();
		resolvePromiseFromResolve(res, promise, "resumeAllEffects Failed");
	}

	// set local video render mode
	@ReactMethod
	public void setLocalRenderMode(int mode) {
		RtcEngineDelegate.INSTANCE.setLocalRenderMode(mode);
	}

	@ReactMethod
	public void setLocalVideoMirrorMode(int mode, Promise promise) {
		int res = RtcEngineDelegate.INSTANCE.setLocalVideoMirrorMode(mode);
		resolvePromiseFromResolve(res, promise, "setLocalVideoMirrorMode Failed");
	}

	@ReactMethod
	public void setBeautyEffectOptions(boolean enabled, ReadableMap options, Promise promise) {
		BeautyOptions beautyOption = new BeautyOptions();
		beautyOption.lighteningContrastLevel = options.getInt("lighteningContrastLevel");
		beautyOption.lighteningLevel = (float) options.getDouble("lighteningLevel");
		beautyOption.smoothnessLevel = (float) options.getDouble("smoothnessLevel");
		beautyOption.rednessLevel = (float) options.getDouble("rednessLevel");
		int res = RtcEngineDelegate.INSTANCE.setBeautyEffectOptions(true, beautyOption);
		resolvePromiseFromResolve(res, promise, "setBeautyEffectOptions Failed");
	}

	@ReactMethod
	public void setLocalVoiceChanger(int voiceChanger, Promise promise) {
		int res = RtcEngineDelegate.INSTANCE.setLocalVoiceChanger(voiceChanger);
		resolvePromiseFromResolve(res, promise, "setLocalVoiceChanger Failed");
	}

	@ReactMethod
	public void setLocalVoiceReverbPreset(int preset, Promise promise) {
		int res = RtcEngineDelegate.INSTANCE.setLocalVoiceReverbPreset(preset);
		resolvePromiseFromResolve(res, promise, "setLocalVoiceReverbPreset Failed");
	}

	@ReactMethod
	public void enableSoundPositionIndication(boolean enabled, Promise promise) {
		int res = RtcEngineDelegate.INSTANCE.enableSoundPositionIndication(enabled);
		resolvePromiseFromResolve(res, promise, "enableSoundPositionIndication Failed");
	}

	@ReactMethod
	public void setRemoteVoicePosition(int uid, int pan, int gain, Promise promise) {
		int res = RtcEngineDelegate.INSTANCE.setRemoteVoicePosition(uid, pan, gain);
		resolvePromiseFromResolve(res, promise, "setRemoteVoicePosition Failed");
	}

	@ReactMethod
	public void startLastmileProbeTest(ReadableMap config, Promise promise) {
		LastmileProbeConfig probeConfig = new LastmileProbeConfig();
		probeConfig.probeUplink = config.getBoolean("probeUplink");
		probeConfig.probeDownlink = config.getBoolean("probeDownlink");
		probeConfig.expectedDownlinkBitrate = config.getInt("expectedDownlinkBitrate");
		probeConfig.expectedUplinkBitrate = config.getInt("expectedUplinkBitrate");
		int res = RtcEngineDelegate.INSTANCE.startLastmileProbeTest(probeConfig);
		resolvePromiseFromResolve(res, promise, "startLastmileProbeTest Failed");
	}

	@ReactMethod
	public void stopLastmileProbeTest(Promise promise) {
		int res = RtcEngineDelegate.INSTANCE.stopLastmileProbeTest();
		resolvePromiseFromResolve(res, promise, "stopLastmileProbeTest Failed");

	}

	@ReactMethod
	public void setRemoteUserPriority(int uid, int userPrority, Promise promise) {
		int res = RtcEngineDelegate.INSTANCE.setRemoteUserPriority(uid, userPrority);
		resolvePromiseFromResolve(res, promise, "setRemoteUserPriority Failed");
	}

	@ReactMethod
	public void startEchoTestWithInterval(int interval, Promise promise) {
		int res = RtcEngineDelegate.INSTANCE.startEchoTest(interval);
		resolvePromiseFromResolve(res, promise, "startEchoTestWithInterval Failed");
	}

	@ReactMethod
	public void setCameraCapturerConfiguration(ReadableMap options, Promise promise) {
		try {
			CameraCapturerConfiguration.CAPTURER_OUTPUT_PREFERENCE preference = CameraCapturerConfiguration.CAPTURER_OUTPUT_PREFERENCE.CAPTURER_OUTPUT_PREFERENCE_AUTO;
			switch (options.getInt("preference")) {
				case 0: {
					preference = CameraCapturerConfiguration.CAPTURER_OUTPUT_PREFERENCE.CAPTURER_OUTPUT_PREFERENCE_AUTO;
					break;
				}
				case 1: {
					preference = CameraCapturerConfiguration.CAPTURER_OUTPUT_PREFERENCE.CAPTURER_OUTPUT_PREFERENCE_PERFORMANCE;
					break;
				}
				case 2: {
					preference = CameraCapturerConfiguration.CAPTURER_OUTPUT_PREFERENCE.CAPTURER_OUTPUT_PREFERENCE_PREVIEW;
					break;
				}
			}
			CameraCapturerConfiguration config = new CameraCapturerConfiguration(preference, CameraCapturerConfiguration.CAMERA_DIRECTION.CAMERA_FRONT);

			int res = RtcEngineDelegate.INSTANCE.setCameraCapturerConfiguration(config);
			resolvePromiseFromResolve(res, promise, "setCameraCapturerConfiguration Failed");
		} catch (Exception e) {
			promise.reject(e);
		}
	}

	private void resolvePromiseFromResolve(String res, Promise promise) {
		WritableMap map = Arguments.createMap();
		map.putBoolean("success", true);
		map.putString("value", res);
		promise.resolve(map);
	}

	private void resolvePromiseFromResolve(boolean res, Promise promise) {
		WritableMap map = Arguments.createMap();
		map.putBoolean("success", true);
		map.putBoolean("value", res);
		promise.resolve(map);
	}

	private void resolvePromiseFromResolve(double res, Promise promise) {
		WritableMap map = Arguments.createMap();
		map.putBoolean("success", true);
		map.putDouble("value", res);
		promise.resolve(map);
	}


	private void resolvePromiseFromResolve(int res, Promise promise) {
		WritableMap map = Arguments.createMap();
		map.putBoolean("success", true);
		map.putInt("value", res);
		promise.resolve(map);
	}

	private void resolvePromiseFromResolve(double res, Promise promise, String error) {
		if (res == 0) {
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putDouble("value", res);
			promise.resolve(map);
		} else {
			promise.reject(new ReactNativeAgoraException(error, (int) res));
		}
	}

	private void resolvePromiseFromResolve(int res, Promise promise, String error) {
		if (res == 0) {
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putInt("value", res);
			promise.resolve(map);
		} else {
			promise.reject(new ReactNativeAgoraException(error, res));
		}
	}

	private void resolvePromiseFromNegativeResolve(int res, Promise promise, String error) {
		if (res >= 0) {
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putInt("value", res);
			promise.resolve(map);
		} else {
			promise.reject(new ReactNativeAgoraException(error, res));
		}
	}

	private void sendError(ReactApplicationContext reactApplicationContext, int code, String message) {
		WritableMap err = Arguments.createMap();
		err.putBoolean("success", false);
		err.putInt("code", code);
		err.putString("message", message);
		sendError(reactApplicationContext, err);
	}

	private void sendError(ReactApplicationContext reactApplicationContext, WritableMap err) {
		RtcEngineEventHandler.sendEvent(reactApplicationContext, "error", err);
	}

}
