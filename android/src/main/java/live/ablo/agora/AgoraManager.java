package live.ablo.agora;

import android.content.Context;
import android.util.Log;
import android.view.SurfaceView;

import com.facebook.react.bridge.ReadableMap;

import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.BeautyOptions;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;

import static io.agora.rtc.video.VideoEncoderConfiguration.FRAME_RATE;
import static io.agora.rtc.video.VideoEncoderConfiguration.ORIENTATION_MODE;


/**
 * Created by Leon on 2017/4/9.
 */

public class AgoraManager {

	private static AgoraManager sAgoraManager;

	private RtcEngine mRtcEngine;

	private int mLocalUid = 0;

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
			@Override
			public boolean onRecordFrame(byte[] samples, int numOfSamples, int bytesPerSample, int channels, int samplesPerSec) {
				return observer.onRecordFrame(samples, numOfSamples, bytesPerSample, channels, samplesPerSec);
			}

			@Override
			public boolean onPlaybackFrame(byte[] samples, int numOfSamples, int bytesPerSample, int channels, int samplesPerSec) {
				return observer.onPlaybackFrame(samples, numOfSamples, bytesPerSample, channels, samplesPerSec);
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
	public int init(Context context, IRtcEngineEventHandler mRtcEventHandler, ReadableMap options) {
		//create rtcEngine instance and setup rtcEngine eventHandler
		try {
			this.mRtcEngine = RtcEngine.create(context, options.getString("appid"), mRtcEventHandler);
			if (options.hasKey("secret") && null != options.getString("secret")) {
				mRtcEngine.setEncryptionSecret(options.getString("secret"));
				if (options.hasKey("secretMode") && null != options.getString("secretMode")) {
					mRtcEngine.setEncryptionMode(options.getString("secretMode"));
				}
			}
			if (options.hasKey("channelProfile")) {
				mRtcEngine.setChannelProfile(options.getInt("channelProfile"));
			}
			if (options.hasKey("dualStream")) {
				mRtcEngine.enableDualStreamMode(options.getBoolean("dualStream"));
			}
			if (options.hasKey("mode")) {
				Integer mode = options.getInt("mode");
				switch (mode) {
					case 0: {
						mRtcEngine.enableAudio();
						mRtcEngine.disableVideo();
						break;
					}
					case 1: {
						mRtcEngine.enableVideo();
						mRtcEngine.disableAudio();
						break;
					}
				}
			} else {
				mRtcEngine.enableVideo();
				mRtcEngine.enableAudio();
			}

			if (options.hasKey("beauty") && null != options.getMap("beauty")) {
				ReadableMap beauty = options.getMap("beauty");
				BeautyOptions beautyOption = new BeautyOptions();
				beautyOption.lighteningContrastLevel = beauty.getInt("lighteningContrastLevel");
				beautyOption.lighteningLevel = (float) beauty.getDouble("lighteningLevel");
				beautyOption.smoothnessLevel = (float) beauty.getDouble("smoothnessLevel");
				beautyOption.rednessLevel = (float) beauty.getDouble("rednessLevel");
				mRtcEngine.setBeautyEffectOptions(true, beautyOption);
			}

			if (options.hasKey("voice") && null != options.getMap("voice")) {
				ReadableMap voice = options.getMap("voice");
				final String voiceType = voice.getString("type");
				final Integer voiceValue = voice.getInt("value");
				if (voiceType.equals("changer")) {
					mRtcEngine.setLocalVoiceChanger(voiceValue);
				}
				if (voiceType.equals("reverbPreset")) {
					mRtcEngine.setLocalVoiceReverbPreset(voiceValue);
				}
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

			if (options.hasKey("clientRole")) {
				mRtcEngine.setClientRole(options.getInt("clientRole"));
			}
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
	public SurfaceView setupRemoteVideo(final int uid, final Integer mode, Context context) {
		SurfaceView surfaceView = RtcEngine.CreateRendererView(context);
		mRtcEngine.setupRemoteVideo(new VideoCanvas(surfaceView, mode, uid));
		return surfaceView;
	}
	public int joinChannel(ReadableMap options) {
		String token = options.hasKey("token") ? options.getString("token") : null;
		String channelName = options.hasKey("channelName") ? options.getString("channelName") : null;
		String optionalInfo = options.hasKey("optionalInfo") ? options.getString("optionalInfo") : null;
		int uid = options.hasKey("uid") ? options.getInt("uid") : 0;
		this.mLocalUid = uid;
		return mRtcEngine.joinChannel(token, channelName, optionalInfo, uid);
	}

	public RtcEngine getEngine() {
		return mRtcEngine;
	}
}
