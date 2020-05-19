package live.ablo.agora;

import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;

import io.agora.rtc.IAudioEffectManager;
import io.agora.rtc.IMetadataObserver;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcChannel;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.internal.LastmileProbeConfig;
import io.agora.rtc.live.LiveInjectStreamConfig;
import io.agora.rtc.live.LiveTranscoding;
import io.agora.rtc.mediaio.IVideoSink;
import io.agora.rtc.mediaio.IVideoSource;
import io.agora.rtc.models.UserInfo;
import io.agora.rtc.video.AgoraImage;
import io.agora.rtc.video.AgoraVideoFrame;
import io.agora.rtc.video.BeautyOptions;
import io.agora.rtc.video.CameraCapturerConfiguration;
import io.agora.rtc.video.ChannelMediaRelayConfiguration;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;
import io.agora.rtc.video.VideoEncoderConfiguration.FRAME_RATE;
import io.agora.rtc.video.VideoEncoderConfiguration.ORIENTATION_MODE;
import io.agora.rtc.video.WatermarkOptions;

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


	public int setChannelProfile(int profile) {
		return rtcEngine.setChannelProfile(profile);
	}

	public int joinChannel(String token, String channelName, String optionalInfo, int optionalUid) {
		return rtcEngine.joinChannel(token, channelName, optionalInfo, optionalUid);
	}

	public int switchChannel(String token, String channelName) {
		return rtcEngine.switchChannel(token, channelName);
	}

	public int registerLocalUserAccount(String appId, String userAccount) {
		return rtcEngine.registerLocalUserAccount(appId, userAccount);
	}

	public int joinChannelWithUserAccount(String token, String channelName, String userAccount) {
		return rtcEngine.joinChannelWithUserAccount(token, channelName, userAccount);
	}

	public int getUserInfoByUserAccount(String userAccount, UserInfo userInfo) {
		return rtcEngine.getUserInfoByUserAccount(userAccount, userInfo);
	}

	public int getUserInfoByUid(int uid, UserInfo userInfo) {
		return rtcEngine.getUserInfoByUid(uid, userInfo);
	}

	public int enableAudio() {
		return rtcEngine.enableAudio();
	}

	public int disableAudio() {
		return rtcEngine.disableAudio();
	}

	@Deprecated
	public int pauseAudio() {
		return rtcEngine.pauseAudio();
	}

	@Deprecated
	public int resumeAudio() {
		return rtcEngine.resumeAudio();
	}

	public int setAudioProfile(int profile, int scenario) {
		return rtcEngine.setAudioProfile(profile, scenario);
	}

	@Deprecated
	public int setHighQualityAudioParameters(boolean fullband, boolean stereo, boolean fullBitrate) {
		return rtcEngine.setHighQualityAudioParameters(fullband, stereo, fullBitrate);
	}

	public int adjustRecordingSignalVolume(int volume) {
		return rtcEngine.adjustRecordingSignalVolume(volume);
	}

	public int adjustPlaybackSignalVolume(int volume) {
		return rtcEngine.adjustPlaybackSignalVolume(volume);
	}

	public int enableAudioVolumeIndication(int interval, int smooth, boolean report_vad) {
		return rtcEngine.enableAudioVolumeIndication(interval, smooth, report_vad);
	}

	@Deprecated
	public int enableAudioQualityIndication(boolean enabled) {
		return rtcEngine.enableAudioQualityIndication(enabled);
	}

	public int enableLocalAudio(boolean enabled) {
		return rtcEngine.enableLocalAudio(enabled);
	}

	public int muteLocalAudioStream(boolean muted) {
		return rtcEngine.muteLocalAudioStream(muted);
	}

	public int muteRemoteAudioStream(int uid, boolean muted) {
		return rtcEngine.muteRemoteAudioStream(uid, muted);
	}

	public int adjustUserPlaybackSignalVolume(int uid, int volume) {
		return rtcEngine.adjustUserPlaybackSignalVolume(uid, volume);
	}

	public int muteAllRemoteAudioStreams(boolean muted) {
		return rtcEngine.muteAllRemoteAudioStreams(muted);
	}

	public int setDefaultMuteAllRemoteAudioStreams(boolean muted) {
		return rtcEngine.setDefaultMuteAllRemoteAudioStreams(muted);
	}

	public int disableVideo() {
		return rtcEngine.disableVideo();
	}

	@Deprecated
	public int setVideoProfile(int profile, boolean swapWidthAndHeight) {
		return rtcEngine.setVideoProfile(profile, swapWidthAndHeight);
	}

	@Deprecated
	public int setVideoProfile(int width, int height, int frameRate, int bitrate) {
		return rtcEngine.setVideoProfile(width, height, frameRate, bitrate);
	}

	public int setVideoEncoderConfiguration(VideoEncoderConfiguration config) {
		return rtcEngine.setVideoEncoderConfiguration(config);
	}

	public int setCameraCapturerConfiguration(CameraCapturerConfiguration config) {
		return rtcEngine.setCameraCapturerConfiguration(config);
	}

	@Deprecated
	public int setLocalRenderMode(int renderMode) {
		return rtcEngine.setLocalRenderMode(renderMode);
	}

	public int setLocalRenderMode(int renderMode, int mirrorMode) {
		return rtcEngine.setLocalRenderMode(renderMode, mirrorMode);
	}

	@Deprecated
	public int setRemoteRenderMode(int uid, int renderMode) {
		return rtcEngine.setRemoteRenderMode(uid, renderMode);
	}

	public int setRemoteRenderMode(int uid, int renderMode, int mirrorMode) {
		return rtcEngine.setRemoteRenderMode(uid, renderMode, mirrorMode);
	}

	public int enableLocalVideo(boolean enabled) {
		return rtcEngine.enableLocalVideo(enabled);
	}

	public int muteLocalVideoStream(boolean muted) {
		return rtcEngine.muteLocalVideoStream(muted);
	}

	public int muteRemoteVideoStream(int uid, boolean muted) {
		return rtcEngine.muteRemoteVideoStream(uid, muted);
	}

	public int muteAllRemoteVideoStreams(boolean muted) {
		return rtcEngine.muteAllRemoteVideoStreams(muted);
	}

	public int setDefaultMuteAllRemoteVideoStreams(boolean muted) {
		return rtcEngine.setDefaultMuteAllRemoteVideoStreams(muted);
	}

	public int setBeautyEffectOptions(boolean enabled, BeautyOptions options) {
		return rtcEngine.setBeautyEffectOptions(enabled, options);
	}

	public int setDefaultAudioRoutetoSpeakerphone(boolean defaultToSpeaker) {
		return rtcEngine.setDefaultAudioRoutetoSpeakerphone(defaultToSpeaker);
	}

	public boolean isSpeakerphoneEnabled() {
		return rtcEngine.isSpeakerphoneEnabled();
	}

	public int enableInEarMonitoring(boolean enabled) {
		return rtcEngine.enableInEarMonitoring(enabled);
	}

	public int setInEarMonitoringVolume(int volume) {
		return rtcEngine.setInEarMonitoringVolume(volume);
	}

	@Deprecated
	public int useExternalAudioDevice() {
		return rtcEngine.useExternalAudioDevice();
	}

	public int setLocalVoicePitch(double pitch) {
		return rtcEngine.setLocalVoicePitch(pitch);
	}

	public int setLocalVoiceEqualization(int bandFrequency, int bandGain) {
		return rtcEngine.setLocalVoiceEqualization(bandFrequency, bandGain);
	}

	public int setLocalVoiceReverb(int reverbKey, int value) {
		return rtcEngine.setLocalVoiceReverb(reverbKey, value);
	}

	public int setLocalVoiceChanger(int voiceChanger) {
		return rtcEngine.setLocalVoiceChanger(voiceChanger);
	}

	public int setLocalVoiceReverbPreset(int preset) {
		return rtcEngine.setLocalVoiceReverbPreset(preset);
	}

	public int enableSoundPositionIndication(boolean enabled) {
		return rtcEngine.enableSoundPositionIndication(enabled);
	}

	public int setRemoteVoicePosition(int uid, double pan, double gain) {
		return rtcEngine.setRemoteVoicePosition(uid, pan, gain);
	}

	public int startAudioMixing(String filePath, boolean loopback, boolean replace, int cycle) {
		return rtcEngine.startAudioMixing(filePath, loopback, replace, cycle);
	}

	public int stopAudioMixing() {
		return rtcEngine.stopAudioMixing();
	}

	public int pauseAudioMixing() {
		return rtcEngine.pauseAudioMixing();
	}

	public int resumeAudioMixing() {
		return rtcEngine.resumeAudioMixing();
	}

	public int adjustAudioMixingVolume(int volume) {
		return rtcEngine.adjustAudioMixingVolume(volume);
	}

	public int adjustAudioMixingPlayoutVolume(int volume) {
		return rtcEngine.adjustAudioMixingPlayoutVolume(volume);
	}

	public int adjustAudioMixingPublishVolume(int volume) {
		return rtcEngine.adjustAudioMixingPublishVolume(volume);
	}

	public int getAudioMixingPlayoutVolume() {
		return rtcEngine.getAudioMixingPlayoutVolume();
	}

	public int getAudioMixingPublishVolume() {
		return rtcEngine.getAudioMixingPublishVolume();
	}

	public int getAudioMixingDuration() {
		return rtcEngine.getAudioMixingDuration();
	}

	public int getAudioMixingCurrentPosition() {
		return rtcEngine.getAudioMixingCurrentPosition();
	}

	public int setAudioMixingPosition(int pos) {
		return rtcEngine.setAudioMixingPosition(pos);
	}

	public IAudioEffectManager getAudioEffectManager() {
		return rtcEngine.getAudioEffectManager();
	}

	@Deprecated
	public int startAudioRecording(String filePath, int quality) {
		return rtcEngine.startAudioRecording(filePath, quality);
	}

	public int startAudioRecording(String filePath, int sampleRate, int quality) {
		return rtcEngine.startAudioRecording(filePath, sampleRate, quality);
	}

	public int stopAudioRecording() {
		return rtcEngine.stopAudioRecording();
	}

	@Deprecated
	public int startEchoTest() {
		return rtcEngine.startEchoTest();
	}

	public int startEchoTest(int intervalInSeconds) {
		return rtcEngine.startEchoTest(intervalInSeconds);
	}

	public int stopEchoTest() {
		return rtcEngine.stopEchoTest();
	}

	public int startLastmileProbeTest(LastmileProbeConfig config) {
		return rtcEngine.startLastmileProbeTest(config);
	}

	public int stopLastmileProbeTest() {
		return rtcEngine.stopLastmileProbeTest();
	}

	public int setVideoSource(IVideoSource source) {
		return rtcEngine.setVideoSource(source);
	}

	public int setLocalVideoRenderer(IVideoSink render) {
		return rtcEngine.setLocalVideoRenderer(render);
	}

	public int setRemoteVideoRenderer(int uid, IVideoSink render) {
		return rtcEngine.setRemoteVideoRenderer(uid, render);
	}

	public int setExternalAudioSink(boolean enabled, int sampleRate, int channels) {
		return rtcEngine.setExternalAudioSink(enabled, sampleRate, channels);
	}

	public int pullPlaybackAudioFrame(byte[] data, int lengthInByte) {
		return rtcEngine.pullPlaybackAudioFrame(data, lengthInByte);
	}

	public int setExternalAudioSource(boolean enabled, int sampleRate, int channels) {
		return rtcEngine.setExternalAudioSource(enabled, sampleRate, channels);
	}

	public int pushExternalAudioFrame(byte[] data, long timestamp) {
		return rtcEngine.pushExternalAudioFrame(data, timestamp);
	}

	public void setExternalVideoSource(boolean enable, boolean useTexture, boolean pushMode) {
		rtcEngine.setExternalVideoSource(enable, useTexture, pushMode);
	}

	public boolean pushExternalVideoFrame(AgoraVideoFrame frame) {
		return rtcEngine.pushExternalVideoFrame(frame);
	}

	public boolean isTextureEncodeSupported() {
		return rtcEngine.isTextureEncodeSupported();
	}

	public int setPlaybackAudioFrameParameters(int sampleRate, int channel, int mode, int samplesPerCall) {
		return rtcEngine.setPlaybackAudioFrameParameters(sampleRate, channel, mode, samplesPerCall);
	}

	public int setMixedAudioFrameParameters(int sampleRate, int samplesPerCall) {
		return rtcEngine.setMixedAudioFrameParameters(sampleRate, samplesPerCall);
	}

	@Deprecated
	public int addVideoWatermark(AgoraImage watermark) {
		return rtcEngine.addVideoWatermark(watermark);
	}

	public int addVideoWatermark(String watermarkUrl, WatermarkOptions options) {
		return rtcEngine.addVideoWatermark(watermarkUrl, options);
	}

	public int clearVideoWatermarks() {
		return rtcEngine.clearVideoWatermarks();
	}

	public int setRemoteUserPriority(int uid, int userPriority) {
		return rtcEngine.setRemoteUserPriority(uid, userPriority);
	}

	public int setLocalPublishFallbackOption(int option) {
		return rtcEngine.setLocalPublishFallbackOption(option);
	}

	public int setRemoteSubscribeFallbackOption(int option) {
		return rtcEngine.setRemoteSubscribeFallbackOption(option);
	}

	public int enableDualStreamMode(boolean enabled) {
		return rtcEngine.enableDualStreamMode(enabled);
	}

	public int setRemoteVideoStreamType(int uid, int streamType) {
		return rtcEngine.setRemoteVideoStreamType(uid, streamType);
	}

	public int setRemoteDefaultVideoStreamType(int streamType) {
		return rtcEngine.setRemoteDefaultVideoStreamType(streamType);
	}

	public int setEncryptionSecret(String secret) {
		return rtcEngine.setEncryptionSecret(secret);
	}

	public int setEncryptionMode(String encryptionMode) {
		return rtcEngine.setEncryptionMode(encryptionMode);
	}

	public int addInjectStreamUrl(String url, LiveInjectStreamConfig config) {
		return rtcEngine.addInjectStreamUrl(url, config);
	}

	public int removeInjectStreamUrl(String url) {
		return rtcEngine.removeInjectStreamUrl(url);
	}

	public int addPublishStreamUrl(String url, boolean transcodingEnabled) {
		return rtcEngine.addPublishStreamUrl(url, transcodingEnabled);
	}

	public int removePublishStreamUrl(String url) {
		return rtcEngine.removePublishStreamUrl(url);
	}

	public int setLiveTranscoding(LiveTranscoding transcoding) {
		return rtcEngine.setLiveTranscoding(transcoding);
	}

	public int createDataStream(boolean reliable, boolean ordered) {
		return rtcEngine.createDataStream(reliable, ordered);
	}

	public int sendStreamMessage(int streamId, byte[] message) {
		return rtcEngine.sendStreamMessage(streamId, message);
	}

	@Deprecated
	public int setVideoQualityParameters(boolean preferFrameRateOverImageQuality) {
		return rtcEngine.setVideoQualityParameters(preferFrameRateOverImageQuality);
	}

	@Deprecated
	public int setLocalVideoMirrorMode(int mode) {
		return rtcEngine.setLocalVideoMirrorMode(mode);
	}

	public int switchCamera() {
		return rtcEngine.switchCamera();
	}

	public boolean isCameraZoomSupported() {
		return rtcEngine.isCameraZoomSupported();
	}

	public boolean isCameraTorchSupported() {
		return rtcEngine.isCameraTorchSupported();
	}

	public boolean isCameraFocusSupported() {
		return rtcEngine.isCameraFocusSupported();
	}

	public boolean isCameraExposurePositionSupported() {
		return rtcEngine.isCameraExposurePositionSupported();
	}

	public boolean isCameraAutoFocusFaceModeSupported() {
		return rtcEngine.isCameraAutoFocusFaceModeSupported();
	}

	public int setCameraZoomFactor(float factor) {
		return rtcEngine.setCameraZoomFactor(factor);
	}

	public float getCameraMaxZoomFactor() {
		return rtcEngine.getCameraMaxZoomFactor();
	}

	public int setCameraFocusPositionInPreview(float positionX, float positionY) {
		return rtcEngine.setCameraFocusPositionInPreview(positionX, positionY);
	}

	public int setCameraExposurePosition(float positionXinView, float positionYinView) {
		return rtcEngine.setCameraExposurePosition(positionXinView, positionYinView);
	}

	public int setCameraTorchOn(boolean isOn) {
		return rtcEngine.setCameraTorchOn(isOn);
	}

	public int setCameraAutoFocusFaceModeEnabled(boolean enabled) {
		return rtcEngine.setCameraAutoFocusFaceModeEnabled(enabled);
	}

	public String getCallId() {
		return rtcEngine.getCallId();
	}

	public int rate(String callId, int rating, String description) {
		return rtcEngine.rate(callId, rating, description);
	}

	public int complain(String callId, String description) {
		return rtcEngine.complain(callId, description);
	}

	public int setLogFile(String filePath) {
		return rtcEngine.setLogFile(filePath);
	}

	public int setLogFilter(int filter) {
		return rtcEngine.setLogFilter(filter);
	}

	public int setLogFileSize(int fileSizeInKBytes) {
		return rtcEngine.setLogFileSize(fileSizeInKBytes);
	}

	public long getNativeHandle() {
		return rtcEngine.getNativeHandle();
	}

	@Deprecated
	public boolean enableHighPerfWifiMode(boolean enable) {
		return rtcEngine.enableHighPerfWifiMode(enable);
	}

	@Deprecated
	public void monitorHeadsetEvent(boolean monitor) {
		rtcEngine.monitorHeadsetEvent(monitor);
	}

	@Deprecated
	public void monitorBluetoothHeadsetEvent(boolean monitor) {
		rtcEngine.monitorBluetoothHeadsetEvent(monitor);
	}

	@Deprecated
	public void setPreferHeadset(boolean enabled) {
		rtcEngine.setPreferHeadset(enabled);
	}

	public int setParameters(String parameters) {
		return rtcEngine.setParameters(parameters);
	}

	public String getParameter(String parameter, String args) {
		return rtcEngine.getParameter(parameter, args);
	}

	public int registerMediaMetadataObserver(IMetadataObserver observer, int type) {
		return rtcEngine.registerMediaMetadataObserver(observer, type);
	}

	public int startChannelMediaRelay(ChannelMediaRelayConfiguration channelMediaRelayConfiguration) {
		return rtcEngine.startChannelMediaRelay(channelMediaRelayConfiguration);
	}

	public int stopChannelMediaRelay() {
		return rtcEngine.stopChannelMediaRelay();
	}

	public int updateChannelMediaRelay(ChannelMediaRelayConfiguration channelMediaRelayConfiguration) {
		return rtcEngine.updateChannelMediaRelay(channelMediaRelayConfiguration);
	}

	public int startDumpVideoReceiveTrack(int uid, String dumpFile) {
		return rtcEngine.startDumpVideoReceiveTrack(uid, dumpFile);
	}

	public int stopDumpVideoReceiveTrack() {
		return rtcEngine.stopDumpVideoReceiveTrack();
	}

	public RtcChannel createRtcChannel(String channelId) {
		return rtcEngine.createRtcChannel(channelId);
	}

}


