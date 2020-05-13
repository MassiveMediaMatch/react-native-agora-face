package live.ablo.agora;

import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;

import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;
import io.agora.rtc.video.VideoEncoderConfiguration.FRAME_RATE;
import io.agora.rtc.video.VideoEncoderConfiguration.ORIENTATION_MODE;

public enum RtcEngineDelegate {
	INSTANCE;

	private final String TAG = RtcEngineDelegate.class.getSimpleName();

	private RtcEngine rtcEngine;

	public void init(ReactApplicationContext context, ReadableMap options, IRtcEngineEventHandler eventHandler) {
		try {
			this.rtcEngine = RtcEngine.create(context, options.getString("appid"), eventHandler);


			if (options.hasKey("channelProfile")) {
				this.rtcEngine.setChannelProfile(options.getInt("channelProfile"));
			}
			if (options.hasKey("dualStream")) {
				this.rtcEngine.enableDualStreamMode(options.getBoolean("dualStream"));
			}
			if (options.hasKey("mode")) {
				int mode = options.getInt("mode");
				switch (mode) {
					case 0: {
						this.rtcEngine.enableAudio();
						this.rtcEngine.disableVideo();
						break;
					}
					case 1: {
						this.rtcEngine.enableVideo();
						this.rtcEngine.disableAudio();
						break;
					}
				}
			} else {
				this.rtcEngine.enableVideo();
				this.rtcEngine.enableAudio();
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
				this.rtcEngine.setVideoEncoderConfiguration(encoderConfig);
			}

			if (options.hasKey("audioProfile") &&
					options.hasKey("audioScenario")) {
				this.rtcEngine.setAudioProfile(options.getInt("audioProfile"), options.getInt("audioScenario"));
			}

			if (options.hasKey("clientRole")) {
				this.rtcEngine.setClientRole(options.getInt("clientRole"));
			}
		} catch (Exception e) {
			Log.e(TAG, Log.getStackTraceString(e));
			throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
		}
	}

	public void registerAudioFrameObserver(final IAudioFrameObserver observer) {
		int res = this.rtcEngine.registerAudioFrameObserver(new io.agora.rtc.IAudioFrameObserver() {
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
			throw new RuntimeException("registerAudioFrameObserver Failed");
		}
	}

	public void unRegisterAudioFrameObserver() {
		int res = this.rtcEngine.registerAudioFrameObserver(null);
		if (res < 0) {
			throw new RuntimeException("unRegisterAudioFrameObserver Failed");
		}
	}

	public int setRecordingAudioFrameParameters(int sampleRate, int channel, int mode, int samplesPerCall) {
		return this.rtcEngine.setRecordingAudioFrameParameters(sampleRate, channel, mode, samplesPerCall);
	}

	public int setEnableSpeakerphone(boolean enabled) {
		return this.rtcEngine.setEnableSpeakerphone(enabled);
	}

	public int setDefaultAudioRouteToSpeakerphone(boolean enabled) {
		return this.rtcEngine.setDefaultAudioRoutetoSpeakerphone(enabled);
	}

	public int renewToken(String token) {
		return this.rtcEngine.renewToken(token);
	}

	public int setClientRole(int role) {
		return this.rtcEngine.setClientRole(role);
	}

	public int enableWebSdkInteroperability(boolean enabled) {
		return this.rtcEngine.enableWebSdkInteroperability(enabled);
	}

	public int getConnectionState() {
		return this.rtcEngine.getConnectionState();
	}

	public int joinChannel(ReadableMap options) {
		String token = options.hasKey("token") ? options.getString("token") : null;
		String channelName = options.hasKey("channelName") ? options.getString("channelName") : null;
		String optionalInfo = options.hasKey("optionalInfo") ? options.getString("optionalInfo") : null;
		int uid = options.hasKey("uid") ? options.getInt("uid") : 0;
		return this.rtcEngine.joinChannel(token, channelName, optionalInfo, uid);
	}

	public int enableLastmileTest() {
		return this.rtcEngine.enableLastmileTest();
	}

	public int disableLastmileTest() {
		return this.rtcEngine.disableLastmileTest();
	}

	public int startPreview() {
		return this.rtcEngine.startPreview();
	}

	public int stopPreview() {
		return this.rtcEngine.stopPreview();
	}

	public int leaveChannel() {
		return this.rtcEngine.leaveChannel();
	}

	public int setLocalRenderMode(final Integer renderMode) {
		return this.rtcEngine.setLocalRenderMode(renderMode);
	}

	public int setRemoteRenderMode(final Integer uid, final Integer renderMode) {
		return this.rtcEngine.setRemoteRenderMode(uid, renderMode);
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

	public void setupRemoteVideo(VideoCanvas videoCanvas) {
		this.rtcEngine.setupRemoteVideo(videoCanvas);
	}

	public void enableVideo() {
		this.rtcEngine.enableVideo();
	}

	public void setupLocalVideo(VideoCanvas localVideoCanvas) {
		this.rtcEngine.setupLocalVideo(localVideoCanvas);
	}
}


