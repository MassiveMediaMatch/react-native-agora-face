package live.ablo.agora;

import android.content.Context;
import android.util.Log;
import android.view.SurfaceView;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;

import java.util.HashMap;
import java.util.Map;

import com.facebook.react.bridge.ReadableMap;

import io.agora.rtc.AudioFrame;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.RtcChannel;
import io.agora.rtc.RtcEngineConfig;
import io.agora.rtc.audio.AudioParams;
import io.agora.rtc.models.ChannelMediaOptions;
import io.agora.rtc.internal.EncryptionConfig;
import io.agora.rtc.video.BeautyOptions;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;

import static io.agora.rtc.video.VideoEncoderConfiguration.FRAME_RATE;
import static io.agora.rtc.video.VideoEncoderConfiguration.ORIENTATION_MODE;
import static live.ablo.agora.AgoraConst.AGInit;


/**
 * Created by Leon on 2017/4/9.
 */

public class AgoraManager {

	private static AgoraManager sAgoraManager;

	private RtcEngine mRtcEngine;

	private int mLocalUid = 0;

	private Map<String, RtcChannel> agoraChannels = new HashMap<String, RtcChannel>();


	private AgoraManager() {

	}

	public static AgoraManager getInstance() {
		if (sAgoraManager == null) {
			synchronized (AgoraManager.class) {
				if (sAgoraManager == null) {
					sAgoraManager = new AgoraManager();
				}
			}
		}
		return sAgoraManager;
	}

	public void registerAudioFrameObserver(final IAudioFrameObserver observer) throws ReactNativeAgoraException {
		int res = mRtcEngine.registerAudioFrameObserver(new io.agora.rtc.IAudioFrameObserver() {
			/*@Override
			public boolean onRecordFrame(byte[] samples, int numOfSamples, int bytesPerSample, int channels, int samplesPerSec) {
				return observer.onRecordFrame(samples, numOfSamples, bytesPerSample, channels, samplesPerSec);
			}

			@Override
			public boolean onPlaybackFrame(byte[] samples, int numOfSamples, int bytesPerSample, int channels, int samplesPerSec) {
				return observer.onPlaybackFrame(samples, numOfSamples, bytesPerSample, channels, samplesPerSec);
			}

			@Override
			public boolean onPlaybackFrameBeforeMixing(byte[] samples, int numOfSamples, int bytesPerSample, int channels, int samplesPerSec, int uid) {
				return false;
			}

			@Override
			public boolean onMixedFrame(byte[] samples, int numOfSamples, int bytesPerSample, int channels, int samplesPerSec) {
				return false;
			}

			@Override
			public boolean onPlaybackFrameBeforeMixingEx(byte[] samples, int numOfSamples, int bytesPerSample, int channels, int samplesPerSec, int uid, String channelId) {
				return false;
			}
			*/

			@Override
			public boolean onRecordFrame(AudioFrame audioFrame) {
				return observer.onRecordFrame(audioFrame.samples, audioFrame.numOfSamples, audioFrame.bytesPerSample, audioFrame.channels, audioFrame.samplesPerSec);
			}

			@Override
			public boolean onPlaybackFrame(AudioFrame audioFrame) {
				return observer.onPlaybackFrame(audioFrame.samples, audioFrame.numOfSamples, audioFrame.bytesPerSample, audioFrame.channels, audioFrame.samplesPerSec);
			}

			@Override
			public boolean onPlaybackFrameBeforeMixing(AudioFrame audioFrame, int uid) {
				return false;
			}

			@Override
			public boolean onMixedFrame(AudioFrame audioFrame) {
				return false;
			}

			@Override
			public boolean isMultipleChannelFrameWanted() {
				return false;
			}

			@Override
			public boolean onPlaybackFrameBeforeMixingEx(AudioFrame audioFrame, int uid, String channelId) {
				return false;
			}

			@Override
			public int getObservedAudioFramePosition() {
				return io.agora.rtc.IAudioFrameObserver.POSITION_RECORD;
			}

			@Override
			public AudioParams getRecordAudioParams() {
				return null;
			}

			@Override
			public AudioParams getPlaybackAudioParams() {
				return null;
			}

			@Override
			public AudioParams getMixedAudioParams() {
				return null;
			}
		});
		if (res < 0) {
			throw new ReactNativeAgoraException("registerAudioFrameObserver Failed", res);
		}
	}

	public void unRegisterAudioFrameObserver() throws ReactNativeAgoraException {
		int res = mRtcEngine.registerAudioFrameObserver(null);
		if (res < 0) {
			throw new ReactNativeAgoraException("unRegisterAudioFrameObserver Failed", res);
		}
	}

	public int setRecordingAudioFrameParameters(int sampleRate, int channel, int mode, int samplesPerCall) {
		return mRtcEngine.setRecordingAudioFrameParameters(sampleRate, channel, mode, samplesPerCall);
	}

	private FRAME_RATE getVideoEncoderEnum(int val) {
		FRAME_RATE type = FRAME_RATE.FRAME_RATE_FPS_1;
		switch (val) {
			case 1:
				type = FRAME_RATE.FRAME_RATE_FPS_1;
				break;
			case 7:
				type = FRAME_RATE.FRAME_RATE_FPS_7;
				break;
			case 10:
				type = FRAME_RATE.FRAME_RATE_FPS_10;
				break;
			case 15:
				type = FRAME_RATE.FRAME_RATE_FPS_15;
				break;
			case 24:
				type = FRAME_RATE.FRAME_RATE_FPS_24;
				break;
			case 30:
				type = FRAME_RATE.FRAME_RATE_FPS_30;
				break;
		}
		return type;
	}

	private ORIENTATION_MODE getOrientationModeEnum(int val) {
		ORIENTATION_MODE type = ORIENTATION_MODE.ORIENTATION_MODE_ADAPTIVE;
		switch (val) {
			case 0:
				type = ORIENTATION_MODE.ORIENTATION_MODE_ADAPTIVE;
				break;
			case 1:
				type = ORIENTATION_MODE.ORIENTATION_MODE_FIXED_LANDSCAPE;
				break;
			case 2:
				type = ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT;
				break;
		}
		return type;
	}

	/**
	 * initialize rtc engine
	 */
	public int init(Context context, RtcEventHandler rtcEventHandler, ReadableMap options) {
		//create rtcEngine instance and setup rtcEngine eventHandler
		Log.v("Agora", "init :" + options.toString());
		try {
			RtcEngineConfig rtcEngineConfig = new RtcEngineConfig();
			rtcEngineConfig.mAppId = options.getString("appid");
			rtcEngineConfig.mContext = context;
			rtcEngineConfig.mEventHandler = rtcEventHandler;
			this.mRtcEngine = RtcEngine.create(rtcEngineConfig);
			if (options.hasKey("toggleFaceDetection")) {
				FaceDetector.getInstance().enableFaceDetection(options.getBoolean("toggleFaceDetection"));
				if (!options.getBoolean("toggleFaceDetection")) {
					FaceDetector.getInstance().setBlurOnNoFaceDetected(false);
				}
			}
			if (options.hasKey("toggleFaceDetectionBlurring")) {
				FaceDetector.getInstance().setBlurOnNoFaceDetected(options.getBoolean("toggleFaceDetectionBlurring"));
			}
			if (options.hasKey("toggleFaceDetectionDataEvents")) {
				FaceDetector.getInstance().setSendFaceDetectionDataEvents(options.getBoolean("toggleFaceDetectionDataEvents"));
			}
			if (options.hasKey("toggleFaceDetectionStatusEvents")) {
				FaceDetector.getInstance().setSendFaceDetectionStatusEvent(options.getBoolean("toggleFaceDetectionStatusEvents"));
			}
			if (options.hasKey("channelProfile")) {
				mRtcEngine.setChannelProfile(options.getInt("channelProfile"));
			}
			if (options.hasKey("dualStream")) {
				mRtcEngine.enableDualStreamMode(options.getBoolean("dualStream"));
			}

			if (options.hasKey("videoEncoderConfig") && null != options.getMap("videoEncoderConfig")) {
				ReadableMap config = options.getMap("videoEncoderConfig");
				VideoEncoderConfiguration encoderConfig = new VideoEncoderConfiguration(
						config.getInt("width"),
						config.getInt("height"),
						getVideoEncoderEnum(config.getInt("frameRate")),
						config.getInt("bitrate"),
						getOrientationModeEnum(config.getInt("orientationMode"))
				);
				mRtcEngine.setVideoEncoderConfiguration(encoderConfig);
			}

			if (options.hasKey("audioProfile") &&
					options.hasKey("audioScenario")) {
				mRtcEngine.setAudioProfile(options.getInt("audioProfile"), options.getInt("audioScenario"));
			}

			// if (options.hasKey("clientRole")) {
			// 	mRtcEngine.setClientRole(options.getInt("clientRole"));
			// }

			FaceDetector.getInstance().init(rtcEventHandler);

			WritableMap map = Arguments.createMap();
			RtcEventHandler.sendEvent(rtcEventHandler.getReactApplicationContext(), AGInit, map);

			return mRtcEngine.enableWebSdkInteroperability(true);
		} catch (Exception e) {
			throw new RuntimeException("create rtc engine failed\n" + Log.getStackTraceString(e));
		}
	}

	/**
	 * setupLocalVideo will render video from local side capture into ui layout
	 */
	public SurfaceView setupLocalVideo(Integer mode, Context context) {
		SurfaceView surfaceView = RtcEngine.CreateRendererView(context);
		mRtcEngine.setupLocalVideo(new VideoCanvas(surfaceView, mode, mLocalUid));
		return surfaceView;
	}

	/**
	 * setupRemoteVideo will render video from remote side capture into ui layout
	 */
	public SurfaceView setupRemoteVideo(final int uid, final String channelId, final Integer mode, Context context) {
		SurfaceView surfaceView = RtcEngine.CreateRendererView(context);
		mRtcEngine.setupRemoteVideo(new VideoCanvas(surfaceView, mode, channelId, uid));
		return surfaceView;
	}

	/**
	 * Create a new agora channel object
	 */
	private RtcChannel getOrCreateChannel(String channelName) {
		if (agoraChannels.containsKey(channelName)) {
			// return channel
			return agoraChannels.get(channelName);
		}

		RtcChannel channel = mRtcEngine.createRtcChannel(channelName);

		// Store channel reference
		agoraChannels.put(channelName, channel);

		// return
		return channel;
	}

	public int joinChannel(ReadableMap options) {
		String token = options.hasKey("token") ? options.getString("token") : null;
		String channelName = options.hasKey("channelName") ? options.getString("channelName") : null;
		String optionalInfo = options.hasKey("optionalInfo") ? options.getString("optionalInfo") : null;
		int uid = options.hasKey("uid") ? options.getInt("uid") : 0;
		int clientRole = options.hasKey("clientRole") ? options.getInt("clientRole") : 2;

		RtcChannel channel = getOrCreateChannel(channelName);

		ChannelMediaOptions mediaOptions = new ChannelMediaOptions();

		if (options.hasKey("channelMediaOptions")) {
			ReadableMap channelMediaOptions = options.getMap("channelMediaOptions");

			boolean autoSubscribeAudio = channelMediaOptions.hasKey("autoSubscribeAudio") ? channelMediaOptions.getBoolean("autoSubscribeAudio") : true;
			boolean autoSubscribeVideo = channelMediaOptions.hasKey("autoSubscribeVideo") ? channelMediaOptions.getBoolean("autoSubscribeVideo") : true;
			boolean publishLocalAudio = channelMediaOptions.hasKey("publishLocalAudio") ? channelMediaOptions.getBoolean("publishLocalAudio") : true;
			boolean publishLocalVideo = channelMediaOptions.hasKey("publishLocalVideo") ? channelMediaOptions.getBoolean("publishLocalVideo") : true;

			mediaOptions.autoSubscribeAudio = autoSubscribeAudio;
			mediaOptions.autoSubscribeVideo = autoSubscribeVideo;
			mediaOptions.publishLocalAudio = publishLocalAudio;
			mediaOptions.publishLocalVideo = publishLocalVideo;
		}

		if (options.hasKey("encryption")) {
			ReadableMap encryptionOptions = options.getMap("encryption");
			EncryptionConfig config = new EncryptionConfig();
			config.encryptionKey = encryptionOptions.hasKey("key") ? encryptionOptions.getString("key") : "";
			config.encryptionMode = EncryptionConfig.EncryptionMode.AES_128_XTS;

			channel.enableEncryption(true, config);
		}

		channel.setClientRole(clientRole);

		this.mLocalUid = uid;
		return channel.joinChannel(token, optionalInfo, uid, mediaOptions);
	}

	public int leaveChannel(ReadableMap options) {
		String channelName = options.hasKey("channelName") ? options.getString("channelName") : null;

		if (agoraChannels.containsKey(channelName)) {
			RtcChannel channel = agoraChannels.get(channelName);
			int result = channel.leaveChannel();
			channel.destroy();

			// remove from channel
			agoraChannels.remove(channelName);

			return result;
		}
		return mRtcEngine.leaveChannel();
	}

	public int switchChannel(ReadableMap options) {
		String token = options.hasKey("token") ? options.getString("token") : null;
		String channelName = options.hasKey("channelName") ? options.getString("channelName") : null;

		ChannelMediaOptions mediaOptions = new ChannelMediaOptions();

		if (options.hasKey("channelMediaOptions")) {
			ReadableMap channelMediaOptions = options.getMap("channelMediaOptions");

			boolean autoSubscribeAudio = channelMediaOptions.hasKey("autoSubscribeAudio") ? channelMediaOptions.getBoolean("autoSubscribeAudio") : true;
			boolean autoSubscribeVideo = channelMediaOptions.hasKey("autoSubscribeVideo") ? channelMediaOptions.getBoolean("autoSubscribeVideo") : true;

			mediaOptions.autoSubscribeAudio = autoSubscribeAudio;
			mediaOptions.autoSubscribeVideo = autoSubscribeVideo;
		}
		
		return mRtcEngine.switchChannel(token, channelName, mediaOptions);
	}

	public void muteRemoteVideoStream(ReadableMap options) {
		String channelName = options.hasKey("channelName") ? options.getString("channelName") : null;
		int uid = options.hasKey("uid") ? options.getInt("uid") : 0;
		boolean mute = options.hasKey("mute") ? options.getBoolean("mute") : true;

		if (agoraChannels.containsKey(channelName)) {
			agoraChannels.get(channelName).muteRemoteVideoStream(uid, mute);
		}

		mRtcEngine.muteRemoteVideoStream(uid, mute);
	}

	public void muteRemoteAudioStream(ReadableMap options) {
		String channelName = options.hasKey("channelName") ? options.getString("channelName") : null;
		int uid = options.hasKey("uid") ? options.getInt("uid") : 0;
		boolean mute = options.hasKey("mute") ? options.getBoolean("mute") : true;

		if (agoraChannels.containsKey(channelName)) {
			agoraChannels.get(channelName).muteRemoteAudioStream(uid, mute);
		}

		mRtcEngine.muteRemoteAudioStream(uid, mute);
	}

	public void muteLocalVideoStream(ReadableMap options) {
		String channelName = options.hasKey("channelName") ? options.getString("channelName") : null;
		boolean mute = options.hasKey("mute") ? options.getBoolean("mute") : true;

		if (agoraChannels.containsKey(channelName)) {
			agoraChannels.get(channelName).muteLocalVideoStream(mute);
		}

		mRtcEngine.muteLocalVideoStream(mute);
	}

	public void muteLocalAudioStream(ReadableMap options) {
		String channelName = options.hasKey("channelName") ? options.getString("channelName") : null;
		boolean mute = options.hasKey("mute") ? options.getBoolean("mute") : true;

		if (agoraChannels.containsKey(channelName)) {
			agoraChannels.get(channelName).muteLocalAudioStream(mute);
		}

		mRtcEngine.muteLocalAudioStream(mute);
	}

	public int setVideoEncoderConfiguration(ReadableMap options) {
		VideoEncoderConfiguration encoderConfig = new VideoEncoderConfiguration(
			options.getInt("width"),
			options.getInt("height"),
			getVideoEncoderEnum(options.getInt("framerate")),
			options.getInt("bitrate"),
			getOrientationModeEnum(options.getInt("orientationMode"))
		);
		return mRtcEngine.setVideoEncoderConfiguration(encoderConfig);
	}

	public RtcEngine getEngine() {
		return mRtcEngine;
	}

	public void destroy() {
		RtcEngine.destroy();
	}
}
