package live.ablo.agora;

import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import io.agora.rtc.Constants;
import io.agora.rtc.IAudioEffectManager;
import io.agora.rtc.IMetadataObserver;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.internal.LastmileProbeConfig;
import io.agora.rtc.live.LiveInjectStreamConfig;
import io.agora.rtc.live.LiveTranscoding;
import io.agora.rtc.video.AgoraImage;
import io.agora.rtc.video.BeautyOptions;
import io.agora.rtc.video.CameraCapturerConfiguration;
import io.agora.rtc.video.VideoEncoderConfiguration;

import static live.ablo.agora.AgoraConst.AG_PREFIX;

public class AgoraModule extends ReactContextBaseJavaModule {

	private static final String FPS1 = "FPS1";
	private static final String FPS7 = "FPS7";
	private static final String FPS10 = "FPS10";
	private static final String FPS15 = "FPS15";
	private static final String FPS24 = "FPS24";
	private static final String FPS30 = "FPS30";
	private static final String FPS60 = "FPS60";
	private static final String Adaptative = "Adaptative";
	private static final String FixedLandscape = "FixedLandscape";
	private static final String FixedPortrait = "FixedPortrait";
	private static final String Host = "Host";
	private static final String Audience = "Audience";
	private static final String UserOfflineReasonQuit = "UserOfflineReasonQuit";
	private static final String UserOfflineReasonDropped = "UserOfflineReasonDropped";
	private static final String UserOfflineReasonBecomeAudience = "UserOfflineReasonBecomeAudience";
	private static final String AudioSampleRateType32000 = "AudioSampleRateType32000";
	private static final String AudioSampleRateType44100 = "AudioSampleRateType44100";
	private static final String AudioSampleRateType48000 = "AudioSampleRateType48000";
	private static final String CodecTypeBaseLine = "CodecTypeBaseLine";
	private static final String CodecTypeMain = "CodecTypeMain";
	private static final String CodecTypeHigh = "CodecTypeHigh";
	private static final String QualityLow = "QualityLow";
	private static final String QualityMedium = "QualityMedium";
	private static final String QualityHigh = "QualityHigh";
	private static final String Disconnected = "Disconnected";
	private static final String Connecting = "Connecting";
	private static final String Connected = "Connected";
	private static final String Reconnecting = "Reconnecting";
	private static final String ConnectionFailed = "ConnectionFailed";
	private static final String ConnectionChangedConnecting = "ConnectionChangedConnecting";
	private static final String ConnectionChangedJoinSuccess = "ConnectionChangedJoinSuccess";
	private static final String ConnectionChangedInterrupted = "ConnectionChangedInterrupted";
	private static final String ConnectionChangedBannedByServer = "ConnectionChangedBannedByServer";
	private static final String ConnectionChangedJoinFailed = "ConnectionChangedJoinFailed";
	private static final String ConnectionChangedLeaveChannel = "ConnectionChangedLeaveChannel";
	private static final String AudioOutputRoutingDefault = "AudioOutputRoutingDefault";
	private static final String AudioOutputRoutingHeadset = "AudioOutputRoutingHeadset";
	private static final String AudioOutputRoutingEarpiece = "AudioOutputRoutingEarpiece";
	private static final String AudioOutputRoutingHeadsetNoMic = "AudioOutputRoutingHeadsetNoMic";
	private static final String AudioOutputRoutingSpeakerphone = "AudioOutputRoutingSpeakerphone";
	private static final String AudioOutputRoutingLoudspeaker = "AudioOutputRoutingLoudspeaker";
	private static final String AudioOutputRoutingHeadsetBluetooth = "AudioOutputRoutingHeadsetBluetooth";
	private static final String NetworkQualityUnknown = "NetworkQualityUnknown";
	private static final String NetworkQualityExcellent = "NetworkQualityExcellent";
	private static final String NetworkQualityGood = "NetworkQualityGood";
	private static final String NetworkQualityPoor = "NetworkQualityPoor";
	private static final String NetworkQualityBad = "NetworkQualityBad";
	private static final String NetworkQualityVBad = "NetworkQualityVBad";
	private static final String NetworkQualityDown = "NetworkQualityDown";
	private static final String AudioProfileDefault = "AudioProfileDefault";
	private static final String AudioProfileSpeechStandard = "AudioProfileSpeechStandard";
	private static final String AudioProfileMusicStandard = "AudioProfileMusicStandard";
	private static final String AgoraAudioProfileMusicStandardStereo = "AudioProfileMusicStandardStereo";
	private static final String AudioProfileMusicHighQuality = "AudioProfileMusicHighQuality";
	private static final String AudioProfileMusicHighQualityStereo = "AudioProfileMusicHighQualityStereo";
	private static final String AudioScenarioDefault = "AudioScenarioDefault";
	private static final String AudioScenarioChatRoomEntertainment = "AudioScenarioChatRoomEntertainment";
	private static final String AudioScenarioEducation = "AudioScenarioEducation";
	private static final String AudioScenarioGameStreaming = "AudioScenarioGameStreaming";
	private static final String AudioScenarioShowRoom = "AudioScenarioShowRoom";
	private static final String AudioScenarioChatRoomGaming = "AudioScenarioChatRoomGaming";
	private static final String AudioEqualizationBand31 = "AudioEqualizationBand31";
	private static final String AudioEqualizationBand62 = "AudioEqualizationBand62";
	private static final String AudioEqualizationBand125 = "AudioEqualizationBand125";
	private static final String AudioEqualizationBand250 = "AudioEqualizationBand250";
	private static final String AudioEqualizationBand500 = "AudioEqualizationBand500";
	private static final String AudioEqualizationBand1K = "AudioEqualizationBand1K";
	private static final String AudioEqualizationBand2K = "AudioEqualizationBand2K";
	private static final String AudioEqualizationBand4K = "AudioEqualizationBand4K";
	private static final String AudioEqualizationBand8K = "AudioEqualizationBand8K";
	private static final String AudioEqualizationBand16K = "AudioEqualizationBand16K";
	private static final String AudioRawFrameOperationModeReadOnly = "AudioRawFrameOperationModeReadOnly";
	private static final String AudioRawFrameOperationModeWriteOnly = "AudioRawFrameOperationModeWriteOnly";
	private static final String AudioRawFrameOperationModeReadWrite = "AudioRawFrameOperationModeReadWrite";
	private static final String VideoStreamTypeHigh = "VideoStreamTypeHigh";
	private static final String VideoStreamTypeLow = "VideoStreamTypeLow";
	private static final String VideoMirrorModeAuto = "VideoMirrorModeAuto";
	private static final String VideoMirrorModeEnabled = "VideoMirrorModeEnabled";
	private static final String VideoMirrorModeDisabled = "VideoMirrorModeDisabled";
	private static final String ChannelProfileCommunication = "ChannelProfileCommunication";
	private static final String ChannelProfileLiveBroadcasting = "ChannelProfileLiveBroadcasting";
	private static final String ChannelProfileGame = "ChannelProfileGame";
	private static final String ErrorCodeNoError = "ErrorCodeNoError";
	private static final String ErrorCodeFailed = "ErrorCodeFailed";
	private static final String ErrorCodeInvalidArgument = "ErrorCodeInvalidArgument";
	private static final String ErrorCodeTimedOut = "ErrorCodeTimedOut";
	private static final String ErrorCodeAlreadyInUse = "ErrorCodeAlreadyInUse";
	//    private static String ErrorCodeAbort = "ErrorCodeAbort";
	private static final String ErrorCodeEncryptedStreamNotAllowedPublished = "ErrorCodeEncryptedStreamNotAllowedPublished";
	//    private static String ErrorCodeResourceLimited = "ErrorCodeResourceLimited";
	private static final String InjectStreamStatusStartSuccess = "InjectStreamStatusStartSuccess";
	private static final String InjectStreamStatusStartAlreadyExist = "InjectStreamStatusStartAlreadyExist";
	private static final String InjectStreamStatusStartUnauthorized = "InjectStreamStatusStartUnauthorized";
	private static final String InjectStreamStatusStartTimeout = "InjectStreamStatusStartTimeout";
	private static final String InjectStreamStatusStartFailed = "InjectStreamStatusStartFailed";
	private static final String InjectStreamStatusStopSuccess = "InjectStreamStatusStopSuccess";
	private static final String InjectStreamStatusStopNotFound = "InjectStreamStatusStopNotFound";
	private static final String InjectStreamStatusStopUnauthorized = "InjectStreamStatusStopUnauthorized";
	private static final String InjectStreamStatusStopTimeout = "InjectStreamStatusStopTimeout";
	private static final String InjectStreamStatusStopFailed = "InjectStreamStatusStopFailed";
	private static final String InjectStreamStatusBroken = "InjectStreamStatusBroken";
	private static final String AgoraAudioMode = "AudioMode";
	private static final String AgoraVideoMode = "VideoMode";
	private MediaObserver mediaObserver = null;
	private IRtcEngineEventHandler mRtcEventHandler;

	public AgoraModule(ReactApplicationContext context) {
		super(context);
		mRtcEventHandler = new RtcEventHandler(context);
	}


//    private final static String AGIntervalTest = "startEchoTestWithInterval";

	@Override
	public String getName() {
		return "ReactNativeAgoraFace";
	}

	@Override
	public Map<String, Object> getConstants() {
		final Map<String, Object> constants = new HashMap<>();

		constants.put(Adaptative, VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_ADAPTIVE.getValue());
		constants.put(FixedLandscape, VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_LANDSCAPE.getValue());
		constants.put(FixedPortrait, VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT.getValue());
		constants.put(Host, IRtcEngineEventHandler.ClientRole.CLIENT_ROLE_BROADCASTER);
		constants.put(Audience, IRtcEngineEventHandler.ClientRole.CLIENT_ROLE_AUDIENCE);
		constants.put(ChannelProfileCommunication, Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
		constants.put(ChannelProfileLiveBroadcasting, Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
		constants.put(ChannelProfileGame, Constants.CHANNEL_PROFILE_GAME);
		constants.put(UserOfflineReasonQuit, Constants.USER_OFFLINE_QUIT);
		constants.put(UserOfflineReasonDropped, Constants.USER_OFFLINE_DROPPED);
		constants.put(UserOfflineReasonBecomeAudience, Constants.USER_OFFLINE_BECOME_AUDIENCE);
		constants.put(Disconnected, Constants.CONNECTION_STATE_DISCONNECTED);
		constants.put(Connecting, Constants.CONNECTION_STATE_CONNECTING);
		constants.put(Connected, Constants.CONNECTION_STATE_CONNECTED);
		constants.put(Reconnecting, Constants.CONNECTION_STATE_RECONNECTING);
		constants.put(ConnectionFailed, Constants.CONNECTION_STATE_FAILED);
		constants.put(ConnectionChangedConnecting, Constants.CONNECTION_CHANGED_CONNECTING);
		constants.put(ConnectionChangedJoinSuccess, Constants.CONNECTION_CHANGED_JOIN_SUCCESS);
		constants.put(ConnectionChangedInterrupted, Constants.CONNECTION_CHANGED_INTERRUPTED);
		constants.put(ConnectionChangedBannedByServer, Constants.CONNECTION_CHANGED_BANNED_BY_SERVER);
		constants.put(ConnectionChangedJoinFailed, Constants.CONNECTION_CHANGED_JOIN_FAILED);
		constants.put(ConnectionChangedLeaveChannel, Constants.CONNECTION_CHANGED_LEAVE_CHANNEL);
		constants.put(AudioOutputRoutingDefault, Constants.AUDIO_ROUTE_DEFAULT);
		constants.put(AudioOutputRoutingHeadset, Constants.AUDIO_ROUTE_HEADSET);
		constants.put(AudioOutputRoutingEarpiece, Constants.AUDIO_ROUTE_EARPIECE);
		constants.put(AudioOutputRoutingHeadsetNoMic, Constants.AUDIO_ROUTE_HEADSETNOMIC);
		constants.put(AudioOutputRoutingSpeakerphone, Constants.AUDIO_ROUTE_SPEAKERPHONE);
		constants.put(AudioOutputRoutingLoudspeaker, Constants.AUDIO_ROUTE_LOUDSPEAKER);
		constants.put(AudioOutputRoutingHeadsetBluetooth, Constants.AUDIO_ROUTE_HEADSETBLUETOOTH);
		constants.put(NetworkQualityUnknown, Constants.QUALITY_UNKNOWN);
		constants.put(NetworkQualityExcellent, Constants.QUALITY_EXCELLENT);
		constants.put(NetworkQualityGood, Constants.QUALITY_GOOD);
		constants.put(NetworkQualityPoor, Constants.QUALITY_POOR);
		constants.put(NetworkQualityBad, Constants.QUALITY_BAD);
		constants.put(NetworkQualityVBad, Constants.QUALITY_VBAD);
		constants.put(NetworkQualityDown, Constants.QUALITY_DOWN);
		constants.put(ErrorCodeNoError, Constants.ERR_OK);
		constants.put(ErrorCodeFailed, Constants.ERR_FAILED);
		constants.put(ErrorCodeInvalidArgument, Constants.ERR_INVALID_ARGUMENT);
		constants.put(ErrorCodeTimedOut, Constants.ERR_TIMEDOUT);
		constants.put(ErrorCodeAlreadyInUse, Constants.ERR_ALREADY_IN_USE);
		constants.put(ErrorCodeEncryptedStreamNotAllowedPublished, Constants.ERR_ENCRYPTED_STREAM_NOT_ALLOWED_PUBLISHED);
		constants.put(InjectStreamStatusStartSuccess, Constants.INJECT_STREAM_STATUS_START_SUCCESS);
		constants.put(InjectStreamStatusStartAlreadyExist, Constants.INJECT_STREAM_STATUS_START_ALREADY_EXISTS);
		constants.put(InjectStreamStatusStartUnauthorized, Constants.INJECT_STREAM_STATUS_START_UNAUTHORIZED);
		constants.put(InjectStreamStatusStartTimeout, Constants.INJECT_STREAM_STATUS_START_TIMEDOUT);
		constants.put(InjectStreamStatusStartFailed, Constants.INJECT_STREAM_STATUS_START_FAILED);
		constants.put(InjectStreamStatusStopSuccess, Constants.INJECT_STREAM_STATUS_STOP_SUCCESS);
		constants.put(InjectStreamStatusStopNotFound, Constants.INJECT_STREAM_STATUS_STOP_NOT_FOUND);
		constants.put(InjectStreamStatusStopUnauthorized, Constants.INJECT_STREAM_STATUS_STOP_UNAUTHORIZED);
		constants.put(InjectStreamStatusStopTimeout, Constants.INJECT_STREAM_STATUS_STOP_TIMEDOUT);
		constants.put(InjectStreamStatusStopFailed, Constants.INJECT_STREAM_STATUS_STOP_FAILED);
		constants.put(InjectStreamStatusBroken, Constants.INJECT_STREAM_STATUS_BROKEN);
		constants.put(AudioSampleRateType32000, 32000);
		constants.put(AudioSampleRateType44100, 44100);
		constants.put(AudioSampleRateType48000, 48000);
		constants.put(FPS1, VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_1.getValue());
		constants.put(FPS7, VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_7.getValue());
		constants.put(FPS10, VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_10.getValue());
		constants.put(FPS15, VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15.getValue());
		constants.put(FPS24, VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_24.getValue());
		constants.put(FPS30, VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_30.getValue());
		constants.put(AudioProfileDefault, Constants.AUDIO_PROFILE_DEFAULT);
		constants.put(AudioProfileSpeechStandard, Constants.AUDIO_PROFILE_SPEECH_STANDARD);
		constants.put(AudioProfileMusicStandard, Constants.AUDIO_PROFILE_MUSIC_STANDARD);
		constants.put(AgoraAudioProfileMusicStandardStereo, Constants.AUDIO_PROFILE_MUSIC_STANDARD_STEREO);
		constants.put(AudioProfileMusicHighQuality, Constants.AUDIO_PROFILE_MUSIC_HIGH_QUALITY);
		constants.put(AudioProfileMusicHighQualityStereo, Constants.AUDIO_PROFILE_MUSIC_HIGH_QUALITY_STEREO);
		constants.put(AudioScenarioDefault, Constants.AUDIO_SCENARIO_DEFAULT);
		constants.put(AudioScenarioChatRoomEntertainment, Constants.AUDIO_SCENARIO_CHATROOM_ENTERTAINMENT);
		constants.put(AudioScenarioEducation, Constants.AUDIO_SCENARIO_EDUCATION);
		constants.put(AudioScenarioGameStreaming, Constants.AUDIO_SCENARIO_GAME_STREAMING);
		constants.put(AudioScenarioShowRoom, Constants.AUDIO_SCENARIO_SHOWROOM);
		constants.put(AudioScenarioChatRoomGaming, Constants.AUDIO_SCENARIO_CHATROOM_GAMING);
		constants.put(AudioEqualizationBand31, Constants.AUDIO_EQUALIZATION_BAND_31);
		constants.put(AudioEqualizationBand62, Constants.AUDIO_EQUALIZATION_BAND_62);
		constants.put(AudioEqualizationBand125, Constants.AUDIO_EQUALIZATION_BAND_125);
		constants.put(AudioEqualizationBand250, Constants.AUDIO_EQUALIZATION_BAND_250);
		constants.put(AudioEqualizationBand500, Constants.AUDIO_EQUALIZATION_BAND_500);
		constants.put(AudioEqualizationBand1K, Constants.AUDIO_EQUALIZATION_BAND_1K);
		constants.put(AudioEqualizationBand2K, Constants.AUDIO_EQUALIZATION_BAND_2K);
		constants.put(AudioEqualizationBand4K, Constants.AUDIO_EQUALIZATION_BAND_4K);
		constants.put(AudioEqualizationBand8K, Constants.AUDIO_EQUALIZATION_BAND_8K);
		constants.put(AudioEqualizationBand16K, Constants.AUDIO_EQUALIZATION_BAND_16K);
		constants.put(AudioRawFrameOperationModeReadOnly, Constants.RAW_AUDIO_FRAME_OP_MODE_READ_ONLY);
		constants.put(AudioRawFrameOperationModeWriteOnly, Constants.RAW_AUDIO_FRAME_OP_MODE_WRITE_ONLY);
		constants.put(AudioRawFrameOperationModeReadWrite, Constants.RAW_AUDIO_FRAME_OP_MODE_READ_WRITE);
		constants.put(VideoStreamTypeHigh, Constants.VIDEO_STREAM_HIGH);
		constants.put(VideoStreamTypeLow, Constants.VIDEO_STREAM_LOW);
		constants.put(VideoMirrorModeAuto, Constants.VIDEO_MIRROR_MODE_AUTO);
		constants.put(VideoMirrorModeEnabled, Constants.VIDEO_MIRROR_MODE_ENABLED);
		constants.put(VideoMirrorModeDisabled, Constants.VIDEO_MIRROR_MODE_DISABLED);
		constants.put(CodecTypeBaseLine, 66);
		constants.put(CodecTypeMain, 77);
		constants.put(CodecTypeHigh, 100);
		constants.put(QualityLow, Constants.AUDIO_RECORDING_QUALITY_LOW);
		constants.put(QualityMedium, Constants.AUDIO_RECORDING_QUALITY_MEDIUM);
		constants.put(QualityHigh, Constants.AUDIO_RECORDING_QUALITY_HIGH);
		constants.put(AgoraAudioMode, 0);
		constants.put(AgoraVideoMode, 1);
		return constants;
	}

	@ReactMethod
	public void init(ReadableMap options) {
		AgoraManager.getInstance().init(getReactApplicationContext(), mRtcEventHandler, options);
	}

	@ReactMethod
	public void renewToken(String token,
						   Promise promise) {
		try {
			int res = AgoraManager.getInstance().renewToken(token);
			if (res != 0) throw new ReactNativeAgoraException("renew token failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject("131002", e);
		}
	}

	@ReactMethod
	public void enableWebSdkInteroperability(boolean enabled, Promise promise) {
		try {
			int res = AgoraManager.getInstance().enableWebSdkInteroperability(enabled);
			if (res != 0)
				throw new ReactNativeAgoraException("enableWebSdkInteroperability Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject("131003", e);
		}
	}

	@ReactMethod
	public void getConnectionState(Promise promise) {
		try {
			int res = AgoraManager.getInstance().getConnectionState();
			if (res != 0) throw new ReactNativeAgoraException("getConnectionState Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putInt("state", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject("141001", e);
		}
	}

	@ReactMethod
	public void setClientRole(int role) {
		try {
			int res = AgoraManager.getInstance().setClientRole(role);
			if (res != 0) throw new ReactNativeAgoraException("setClientRole Failed", res);
		} catch (Exception e) {
			WritableMap err = Arguments.createMap();
			err.putBoolean("success", false);
			err.putString("message", e.toString());
			sendEvent(getReactApplicationContext(), "error", err);
		}
	}

	@ReactMethod
	public void joinChannel(ReadableMap options) {
		try {
			int res = AgoraManager.getInstance().joinChannel(options);
			if (res != 0) throw new ReactNativeAgoraException("joinChannel Failed", res);
		} catch (Exception e) {
			WritableMap err = Arguments.createMap();
			err.putBoolean("success", false);
			err.putString("message", e.toString());
			err.putInt("code", ((ReactNativeAgoraException) e).getCode());
			sendEvent(getReactApplicationContext(), "error", err);
		}
	}

	@ReactMethod
	public void leaveChannel(Promise promise) {
		try {
			int res = AgoraManager.getInstance().leaveChannel();
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject("141002", e);
		}
	}

	@ReactMethod
	public void destroy() {
		RtcEngine.destroy();
	}

	@ReactMethod
	public void startPreview() {
		AgoraManager.getInstance().startPreview();
	}

	@ReactMethod
	public void stopPreview() {
		AgoraManager.getInstance().stopPreview();
	}

	@ReactMethod
	public void setEnableSpeakerphone(boolean enabled) {
		try {
			int res = AgoraManager.getInstance().setEnableSpeakerphone(enabled);
			if (res != 0) throw new ReactNativeAgoraException("setEnableSpeakerphone Failed", res);
		} catch (Exception e) {
			WritableMap err = Arguments.createMap();
			err.putBoolean("success", false);
			err.putString("message", e.toString());
			sendEvent(getReactApplicationContext(), "error", err);
		}
	}

	@ReactMethod
	public void setDefaultAudioRouteToSpeakerphone(boolean enabled) {
		try {
			int res = AgoraManager.getInstance().setDefaultAudioRouteToSpeakerphone(enabled);
			if (res != 0)
				throw new ReactNativeAgoraException("setDefaultAudioRouteToSpeakerphone Failed", res);
		} catch (Exception e) {
			WritableMap err = Arguments.createMap();
			err.putBoolean("success", false);
			err.putString("message", e.toString());
			sendEvent(getReactApplicationContext(), "error", err);
		}
	}

	@ReactMethod
	public void enableVideo() {
		AgoraManager.getInstance().mRtcEngine.enableVideo();
	}

	@ReactMethod
	public void disableVideo() {
		AgoraManager.getInstance().mRtcEngine.disableVideo();
	}

	@ReactMethod
	public void enableLocalVideo(boolean enabled) {
		AgoraManager.getInstance().mRtcEngine.enableLocalVideo(enabled);
	}

	@ReactMethod
	public void muteLocalVideoStream(boolean muted) {
		AgoraManager.getInstance().mRtcEngine.muteLocalVideoStream(muted);
	}

	@ReactMethod
	public void muteAllRemoteVideoStreams(boolean muted) {
		AgoraManager.getInstance().mRtcEngine.muteAllRemoteVideoStreams(muted);
	}

	@ReactMethod
	public void muteRemoteVideoStream(int uid, boolean muted) {
		AgoraManager.getInstance().mRtcEngine.muteRemoteVideoStream(uid, muted);
	}

	@ReactMethod
	public void setDefaultMuteAllRemoteVideoStreams(boolean muted) {
		AgoraManager.getInstance().mRtcEngine.setDefaultMuteAllRemoteVideoStreams(muted);
	}

	@ReactMethod
	public void switchCamera(Promise promise) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine.switchCamera();
			if (res != 0) throw new ReactNativeAgoraException("switchCamera Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putInt("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject(e);
		}
	}

	@ReactMethod
	public void isCameraZoomSupported(Promise promise) {
		try {
			boolean res = AgoraManager.getInstance().mRtcEngine.isCameraZoomSupported();
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putBoolean("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject(e);
		}
	}


	@ReactMethod
	public void isCameraTorchSupported(Promise promise) {
		try {
			boolean res = AgoraManager.getInstance().mRtcEngine.isCameraTorchSupported();
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putBoolean("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject(e);
		}
	}

	@ReactMethod
	public void isCameraFocusSupported(Promise promise) {
		try {
			boolean res = AgoraManager.getInstance().mRtcEngine.isCameraFocusSupported();
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putBoolean("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject(e);
		}
	}

	@ReactMethod
	public void isCameraExposurePositionSupported(Promise promise) {
		try {
			boolean res = AgoraManager.getInstance().mRtcEngine.isCameraExposurePositionSupported();
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putBoolean("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject(e);
		}
	}


	@ReactMethod
	public void isCameraAutoFocusFaceModeSupported(Promise promise) {
		try {
			boolean res = AgoraManager.getInstance().mRtcEngine.isCameraAutoFocusFaceModeSupported();
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putBoolean("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject(e);
		}
	}

	@ReactMethod
	public void setCameraZoomFactor(float factor, Promise promise) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine.setCameraZoomFactor(factor);
			if (res != 0) throw new ReactNativeAgoraException("setCameraZoomFactor Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putInt("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject(e);
		}
	}

	@ReactMethod
	public void getCameraMaxZoomFactor(Promise promise) {
		try {
			double res = AgoraManager.getInstance().mRtcEngine.getCameraMaxZoomFactor();
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putDouble("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject(e);
		}
	}

	@ReactMethod
	public void setCameraFocusPositionInPreview(ReadableMap options, Promise promise) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine.setCameraFocusPositionInPreview(
					(float) options.getDouble("x"),
					(float) options.getDouble("y")
			);
			if (res != 0)
				throw new ReactNativeAgoraException("setCameraFocusPositionInPreview Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putInt("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject(e);
		}
	}

	@ReactMethod
	public void setCameraExposurePosition(ReadableMap options, Promise promise) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine.setCameraExposurePosition(
					(float) options.getDouble("x"),
					(float) options.getDouble("y")
			);
			if (res != 0)
				throw new ReactNativeAgoraException("setCameraExposurePosition Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putInt("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject(e);
		}
	}

	@ReactMethod
	public void setCameraTorchOn(boolean isOn, Promise promise) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine.setCameraTorchOn(isOn);
			if (res != 0) throw new ReactNativeAgoraException("setCameraTorchOn Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putInt("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject(e);
		}
	}

	@ReactMethod
	public void setCameraAutoFocusFaceModeEnabled(boolean enabled, Promise promise) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine.setCameraAutoFocusFaceModeEnabled(enabled);
			if (res != 0)
				throw new ReactNativeAgoraException("setCameraAutoFocusFaceModeEnabled Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putInt("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject(e);
		}
	}

	@ReactMethod
	public void getCallId(Promise promise) {
		try {
			String res = AgoraManager.getInstance().mRtcEngine.getCallId();
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putString("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject(e);
		}
	}

	@ReactMethod
	public void setLog(String filePath, int level, int size, Promise promise) {
		try {
			int res = 0;
			res = AgoraManager.getInstance().mRtcEngine.setLogFileSize(size);
			if (res != 0) throw new ReactNativeAgoraException("setLogFileSize Failed", res);
			res = AgoraManager.getInstance().mRtcEngine.setLogFilter(level);
			if (res != 0) throw new ReactNativeAgoraException("setLogFilter Failed", res);
			res = AgoraManager.getInstance().mRtcEngine.setLogFile(filePath);
			if (res != 0) throw new ReactNativeAgoraException("setLogFile Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putInt("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject(e);
		}
	}


	@ReactMethod
	public void enableAudio() {
		AgoraManager.getInstance().mRtcEngine.enableAudio();
	}

	@ReactMethod
	public void disableAudio() {
		AgoraManager.getInstance().mRtcEngine.disableAudio();
	}

	@ReactMethod
	public void muteAllRemoteAudioStreams(boolean muted) {
		AgoraManager.getInstance().mRtcEngine.muteAllRemoteAudioStreams(muted);
	}

	@ReactMethod
	public void muteRemoteAudioStream(int uid, boolean muted) {
		AgoraManager.getInstance().mRtcEngine.muteRemoteAudioStream(uid, muted);
	}

	@ReactMethod
	public void setDefaultMuteAllRemoteAudioStreams(boolean muted) {
		AgoraManager.getInstance().mRtcEngine.setDefaultMuteAllRemoteAudioStreams(muted);
	}

	@ReactMethod
	public void adjustRecordingSignalVolume(int volume) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine.adjustRecordingSignalVolume(volume);
			if (res != 0)
				throw new ReactNativeAgoraException("adjustRecordingSignalVolume Failed", res);
		} catch (Exception e) {
			WritableMap err = Arguments.createMap();
			err.putBoolean("success", false);
			err.putString("message", e.toString());
			sendEvent(getReactApplicationContext(), "error", err);
		}
	}

	@ReactMethod
	public void adjustPlaybackSignalVolume(int volume) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine.adjustPlaybackSignalVolume(volume);
			if (res != 0)
				throw new ReactNativeAgoraException("adjustPlaybackSignalVolume Failed", res);
		} catch (Exception e) {
			WritableMap err = Arguments.createMap();
			err.putBoolean("success", false);
			err.putString("message", e.toString());
			sendEvent(getReactApplicationContext(), "error", err);
		}
	}

	@ReactMethod
	public void enableAudioVolumeIndication(int interval, int smooth) {
		AgoraManager.getInstance().mRtcEngine.enableAudioVolumeIndication(interval, smooth, false);
	}

	@ReactMethod
	public void enableLocalAudio(boolean enabled) {
		AgoraManager.getInstance().mRtcEngine.enableLocalAudio(enabled);
	}

	@ReactMethod
	public void muteLocalAudioStream(boolean enabled) {
		AgoraManager.getInstance().mRtcEngine.muteLocalAudioStream(enabled);
	}

	@ReactMethod
	public void methodisSpeakerphoneEnabled(Callback callback) {
		WritableMap map = Arguments.createMap();
		map.putBoolean("status", AgoraManager.getInstance().mRtcEngine.isSpeakerphoneEnabled());
		callback.invoke(map);
	}

	@ReactMethod
	public void enableInEarMonitoring(boolean enabled) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine.enableInEarMonitoring(enabled);
			if (res != 0) throw new ReactNativeAgoraException("enableInEarMonitoring Failed", res);
		} catch (Exception e) {
			WritableMap err = Arguments.createMap();
			err.putBoolean("success", false);
			err.putString("message", e.toString());
			sendEvent(getReactApplicationContext(), "error", err);
		}
	}

	@ReactMethod
	public void setInEarMonitoringVolume(int volume) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine.setInEarMonitoringVolume(volume);
			if (res != 0)
				throw new ReactNativeAgoraException("setInEarMonitoringVolume Failed", res);
		} catch (Exception e) {
			WritableMap err = Arguments.createMap();
			err.putBoolean("success", false);
			err.putString("message", e.toString());
			sendEvent(getReactApplicationContext(), "error", err);
		}
	}

	@ReactMethod
	public void setLocalVoicePitch(double pitch) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine.setLocalVoicePitch(pitch);
			if (res != 0) throw new ReactNativeAgoraException("setLocalVoicePitch Failed", res);
		} catch (Exception e) {
			WritableMap err = Arguments.createMap();
			err.putBoolean("success", false);
			err.putString("message", e.toString());
			sendEvent(getReactApplicationContext(), "error", err);
		}
	}

	@ReactMethod
	public void setLocalVoiceEqualization(int band, int gain) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine.setLocalVoiceEqualization(band, gain);
			if (res != 0)
				throw new ReactNativeAgoraException("setLocalVoiceEqualization Failed", res);
		} catch (Exception e) {
			WritableMap err = Arguments.createMap();
			err.putBoolean("success", false);
			err.putString("message", e.toString());
			sendEvent(getReactApplicationContext(), "error", err);
		}
	}

	@ReactMethod
	public void setLocalVoiceReverb(int reverb, int value) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine.setLocalVoiceReverb(reverb, value);
			if (res != 0) throw new ReactNativeAgoraException("setLocalVoiceReverb Failed", res);
		} catch (Exception e) {
			WritableMap err = Arguments.createMap();
			err.putBoolean("success", false);
			err.putString("message", e.toString());
			sendEvent(getReactApplicationContext(), "error", err);
		}
	}

	@ReactMethod
	public void startAudioMixing(ReadableMap options) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine.startAudioMixing(
					options.getString("filepath"),
					options.getBoolean("loopback"),
					options.getBoolean("replace"),
					options.getInt("cycle")
			);
			if (res != 0) throw new ReactNativeAgoraException("startAudioMixing Failed", res);
		} catch (Exception e) {
			WritableMap err = Arguments.createMap();
			err.putBoolean("success", false);
			err.putString("message", e.toString());
			sendEvent(getReactApplicationContext(), "error", err);
		}
	}

	@ReactMethod
	public void stopAudioMixing() {
		try {
			int res = AgoraManager.getInstance().mRtcEngine.stopAudioMixing();
			if (res != 0) throw new ReactNativeAgoraException("stopAudioMixing Failed", res);
		} catch (Exception e) {
			WritableMap err = Arguments.createMap();
			err.putBoolean("success", false);
			err.putString("message", e.toString());
			sendEvent(getReactApplicationContext(), "error", err);
		}
	}

	@ReactMethod
	public void pauseAudioMixing() {
		try {
			int res = AgoraManager.getInstance().mRtcEngine.pauseAudioMixing();
			if (res != 0) throw new ReactNativeAgoraException("pauseAudioMixing Failed", res);
		} catch (Exception e) {
			WritableMap err = Arguments.createMap();
			err.putBoolean("success", false);
			err.putString("message", e.toString());
			sendEvent(getReactApplicationContext(), "error", err);
		}
	}

	@ReactMethod
	public void resumeAudioMixing() {
		try {
			int res = AgoraManager.getInstance().mRtcEngine.resumeAudioMixing();
			if (res != 0) throw new ReactNativeAgoraException("resumeAudioMixing Failed", res);
		} catch (Exception e) {
			WritableMap err = Arguments.createMap();
			err.putBoolean("success", false);
			err.putString("message", e.toString());
			sendEvent(getReactApplicationContext(), "error", err);
		}
	}

	@ReactMethod
	public void adjustAudioMixingVolume(int volume) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine.adjustAudioMixingVolume(volume);
			if (res != 0)
				throw new ReactNativeAgoraException("adjustAudioMixingVolume Failed", res);
		} catch (Exception e) {
			WritableMap err = Arguments.createMap();
			err.putBoolean("success", false);
			err.putString("message", e.toString());
			sendEvent(getReactApplicationContext(), "error", err);
		}
	}

	@ReactMethod
	public void adjustAudioMixingPlayoutVolume(int volume) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine.adjustAudioMixingPlayoutVolume(volume);
			if (res != 0)
				throw new ReactNativeAgoraException("adjustAudioMixingPlayoutVolume Failed", res);
		} catch (Exception e) {
			WritableMap err = Arguments.createMap();
			err.putBoolean("success", false);
			err.putString("message", e.toString());
			sendEvent(getReactApplicationContext(), "error", err);
		}
	}

	@ReactMethod
	public void adjustAudioMixingPublishVolume(int volume) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine.adjustAudioMixingPublishVolume(volume);
			if (res != 0)
				throw new ReactNativeAgoraException("adjustAudioMixingPublishVolume Failed", res);
		} catch (Exception e) {
			WritableMap err = Arguments.createMap();
			err.putBoolean("success", false);
			err.putString("message", e.toString());
			sendEvent(getReactApplicationContext(), "error", err);
		}
	}

	@ReactMethod
	public void getAudioMixingPlayoutVolume(Promise promise) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine.getAudioMixingPlayoutVolume();
			if (res < 0)
				throw new ReactNativeAgoraException("getAudioMixingPlayoutVolume Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putInt("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject("-1", e);
		}
	}

	@ReactMethod
	public void getAudioMixingPublishVolume(Promise promise) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine.getAudioMixingPlayoutVolume();
			if (res < 0)
				throw new ReactNativeAgoraException("getAudioMixingPublishVolume Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putInt("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject("-1", e);
		}
	}

	@ReactMethod
	public void getAudioMixingDuration(Promise promise) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine.getAudioMixingDuration();
			if (res != 0) throw new ReactNativeAgoraException("getAudioMixingDuration Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putInt("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject("-1", e);
		}
	}

	@ReactMethod
	public void getAudioMixingCurrentPosition(Promise promise) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine.getAudioMixingCurrentPosition();
			if (res != 0)
				throw new ReactNativeAgoraException("getAudioMixingCurrentPosition Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putInt("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject("131005", e);
		}
	}

	@ReactMethod
	public void setAudioMixingPosition(int pos, Promise promise) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine.setAudioMixingPosition(pos);
			if (res != 0) throw new ReactNativeAgoraException("setAudioMixingPosition Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject("131006", e);
		}
	}

	@ReactMethod
	public void startAudioRecording(ReadableMap options, Promise promise) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine
					.startAudioRecording(
							options.getString("filepath"),
							options.getInt("quality")
					);
			if (res != 0) throw new ReactNativeAgoraException("startAudioRecording Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putInt("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject("131007", e);
		}
	}

	@ReactMethod
	public void stopAudioRecording(Promise promise) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine
					.stopAudioRecording();
			if (res != 0) throw new ReactNativeAgoraException("stopAudioRecording Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putInt("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject("131008", e);
		}
	}

	@ReactMethod
	public void stopEchoTest(Promise promise) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine
					.stopEchoTest();
			if (res != 0) throw new ReactNativeAgoraException("stopEchoTest Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putInt("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject("131010", e);
		}
	}

	@ReactMethod
	public void enableLastmileTest(Promise promise) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine
					.enableLastmileTest();
			if (res != 0) throw new ReactNativeAgoraException("enableLastmileTest Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putInt("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject("131021", e);
		}
	}

	@ReactMethod
	public void disableLastmileTest(Promise promise) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine
					.disableLastmileTest();
			if (res != 0) throw new ReactNativeAgoraException("disableLastmileTest Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putInt("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject("131022", e);
		}
	}

	@ReactMethod
	public void setRecordingAudioFrameParameters(ReadableMap options, Promise promise) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine
					.setRecordingAudioFrameParameters(
							options.getInt("sampleRate"),
							options.getInt("channel"),
							options.getInt("mode"),
							options.getInt("samplesPerCall")
					);
			if (res != 0)
				throw new ReactNativeAgoraException("setRecordingAudioFrameParameters Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putInt("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject("131023", e);
		}
	}

	@ReactMethod
	public void setPlaybackAudioFrameParameters(ReadableMap options, Promise promise) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine
					.setPlaybackAudioFrameParameters(
							options.getInt("sampleRate"),
							options.getInt("channel"),
							options.getInt("mode"),
							options.getInt("samplesPerCall")
					);
			if (res != 0)
				throw new ReactNativeAgoraException("setPlaybackAudioFrameParameters Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putInt("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject("131024", e);
		}
	}

	@ReactMethod
	public void setMixedAudioFrameParameters(WritableMap options, Promise promise) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine
					.setMixedAudioFrameParameters(
							options.getInt("sampleRate"),
							options.getInt("samplesPerCall")
					);
			if (res != 0)
				throw new ReactNativeAgoraException("setMixedAudioFrameParameters Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putInt("value", res);
			promise.resolve(map);
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
			int res = AgoraManager.getInstance().mRtcEngine
					.addVideoWatermark(createAgoraImage(options));
			if (res != 0) throw new ReactNativeAgoraException("addVideoWatermark Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putInt("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject("131026", e);
		}
	}

	@ReactMethod
	public void clearVideoWatermarks(Promise promise) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine
					.clearVideoWatermarks();
			if (res != 0) throw new ReactNativeAgoraException("clearVideoWatermarks Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putInt("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject("131027", e);
		}
	}

	@ReactMethod
	public void setLocalPublishFallbackOption(int option, Promise promise) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine
					.setLocalPublishFallbackOption(option);
			if (res != 0)
				throw new ReactNativeAgoraException("setLocalPublishFallbackOption Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putInt("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject("131028", e);
		}
	}

	@ReactMethod
	public void setRemoteSubscribeFallbackOption(int option, Promise promise) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine
					.setRemoteSubscribeFallbackOption(option);
			if (res != 0)
				throw new ReactNativeAgoraException("setRemoteSubscribeFallbackOption Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putInt("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject("131029", e);
		}
	}

	@ReactMethod
	public void enableDualStreamMode(boolean enabled, Promise promise) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine
					.enableDualStreamMode(enabled);
			if (res != 0) throw new ReactNativeAgoraException("enableDualStreamMode Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putInt("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject("131028", e);
		}
	}


	@ReactMethod
	public void setRemoteVideoStreamType(ReadableMap options, Promise promise) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine
					.setRemoteVideoStreamType(
							options.getInt("uid"),
							options.getInt("streamType")
					);
			if (res != 0)
				throw new ReactNativeAgoraException("setRemoteVideoStreamType Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putInt("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject("131029", e);
		}
	}

	@ReactMethod
	public void setRemoteDefaultVideoStreamType(ReadableMap options, Promise promise) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine
					.setRemoteDefaultVideoStreamType(
							options.getInt("streamType")
					);
			if (res != 0)
				throw new ReactNativeAgoraException("setRemoteDefaultVideoStreamType Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putInt("value", res);
			promise.resolve(map);
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
			int res = AgoraManager.getInstance().mRtcEngine
					.registerMediaMetadataObserver(mediaObserver, IMetadataObserver.VIDEO_METADATA);
			if (res < 0) {
				new ReactNativeAgoraException("registerMediaMetadataObserver Failed", res);
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
		switch (Integer.valueOf(val)) {
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

			int res = AgoraManager.getInstance().mRtcEngine
					.addInjectStreamUrl(
							options.getString("url"),
							injectstream
					);
			if (res != 0) throw new ReactNativeAgoraException("addInjectStreamUrl Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putInt("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject("131031", e);
		}
	}

	@ReactMethod
	public void removeInjectStreamUrl(ReadableMap options, Promise promise) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine
					.removeInjectStreamUrl(options.getString("url"));
			if (res != 0) throw new ReactNativeAgoraException("removeInjectStreamUrl Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putInt("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject("131032", e);
		}
	}

	@ReactMethod
	public void addPublishStreamUrl(ReadableMap options, Promise promise) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine
					.addPublishStreamUrl(
							options.getString("url"),
							options.getBoolean("enable")
					);
			if (res != 0) throw new ReactNativeAgoraException("addPublishStreamUrl Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putInt("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject("131033", e);
		}
	}

	@ReactMethod
	public void removePublishStreamUrl(ReadableMap options, Promise promise) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine
					.removePublishStreamUrl(options.getString("url"));
			if (res != 0) throw new ReactNativeAgoraException("removePublishStreamUrl Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putInt("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject("131034", e);
		}
	}

	@ReactMethod
	public void setLiveTranscoding(ReadableMap options) {
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
			int res = AgoraManager.getInstance().mRtcEngine.setLiveTranscoding(transcoding);
			if (res != 0) throw new ReactNativeAgoraException("setLiveTranscoding Failed", res);
		} catch (Exception e) {
			WritableMap err = Arguments.createMap();
			err.putBoolean("success", false);
			err.putString("message", e.toString());
			sendEvent(getReactApplicationContext(), "error", err);
		}
	}

	@ReactMethod
	public void getEffectsVolume(Promise promise) {
		try {
			IAudioEffectManager manager = AgoraManager.getInstance().mRtcEngine.getAudioEffectManager();
			double res = manager.getEffectsVolume();
			if (res < 0) throw new ReactNativeAgoraException("getEffectsVolume Failed", (int) res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putDouble("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject(e);
		}
	}

	@ReactMethod
	public void setEffectsVolume(double volume, Promise promise) {
		try {
			IAudioEffectManager manager = AgoraManager.getInstance().mRtcEngine.getAudioEffectManager();
			int res = manager.setEffectsVolume(volume);
			if (res != 0) throw new ReactNativeAgoraException("setEffectsVolume Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putInt("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject(e);
		}
	}


	@ReactMethod
	public void setVolumeOfEffect(int soundId, double volume, Promise promise) {
		try {
			IAudioEffectManager manager = AgoraManager.getInstance().mRtcEngine.getAudioEffectManager();
			int res = manager.setVolumeOfEffect(soundId, volume);
			if (res != 0)
				throw new ReactNativeAgoraException("setVolumeOfEffect Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putDouble("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject(e);
		}
	}

	@ReactMethod
	public void playEffect(ReadableMap options, Promise promise) {
		try {
			IAudioEffectManager manager = AgoraManager.getInstance().mRtcEngine.getAudioEffectManager();
			int res = manager.playEffect(
					options.getInt("soundid"),
					options.getString("filepath"),
					options.getInt("loopcount"),
					options.getDouble("pitch"),
					options.getDouble("pan"),
					options.getDouble("gain"),
					options.getBoolean("publish")
			);
			if (res != 0) throw new ReactNativeAgoraException("playEffect Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putInt("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject(e);
		}
	}


	@ReactMethod
	public void stopEffect(int soundId, Promise promise) {
		try {
			IAudioEffectManager manager = AgoraManager.getInstance().mRtcEngine.getAudioEffectManager();
			int res = manager.stopEffect(soundId);
			if (res != 0) throw new ReactNativeAgoraException("stopEffect Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putInt("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject(e);
		}
	}

	@ReactMethod
	public void stopAllEffects(Promise promise) {
		try {
			IAudioEffectManager manager = AgoraManager.getInstance().mRtcEngine.getAudioEffectManager();
			int res = manager.stopAllEffects();
			if (res != 0) throw new ReactNativeAgoraException("stopAllEffects Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putInt("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject(e);
		}
	}

	@ReactMethod
	public void preloadEffect(int soundId, String filePath, Promise promise) {
		try {
			IAudioEffectManager manager = AgoraManager.getInstance().mRtcEngine.getAudioEffectManager();
			int res = manager.preloadEffect(soundId, filePath);
			if (res != 0) throw new ReactNativeAgoraException("preloadEffect Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putInt("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject(e);
		}
	}

	@ReactMethod
	public void unloadEffect(int soundId, Promise promise) {
		try {
			IAudioEffectManager manager = AgoraManager.getInstance().mRtcEngine.getAudioEffectManager();
			int res = manager.unloadEffect(soundId);
			if (res != 0) throw new ReactNativeAgoraException("unloadEffect Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putInt("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject(e);
		}
	}

	@ReactMethod
	public void pauseEffect(int soundId, Promise promise) {
		try {
			IAudioEffectManager manager = AgoraManager.getInstance().mRtcEngine.getAudioEffectManager();
			int res = manager.pauseEffect(soundId);
			if (res != 0) throw new ReactNativeAgoraException("pauseEffect Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putInt("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject(e);
		}
	}

	@ReactMethod
	public void pauseAllEffects(Promise promise) {
		try {
			IAudioEffectManager manager = AgoraManager.getInstance().mRtcEngine.getAudioEffectManager();
			int res = manager.pauseAllEffects();
			if (res != 0) throw new ReactNativeAgoraException("pauseAllEffects Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putInt("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject(e);
		}
	}

	@ReactMethod
	public void resumeEffect(int soundId, Promise promise) {
		try {
			IAudioEffectManager manager = AgoraManager.getInstance().mRtcEngine.getAudioEffectManager();
			int res = manager.resumeEffect(soundId);
			if (res != 0) throw new ReactNativeAgoraException("resumeEffect Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putInt("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject(e);
		}
	}

	@ReactMethod
	public void resumeAllEffects(int soundId, Promise promise) {
		try {
			IAudioEffectManager manager = AgoraManager.getInstance().mRtcEngine.getAudioEffectManager();
			int res = manager.resumeAllEffects();
			if (res != 0) throw new ReactNativeAgoraException("resumeAllEffects Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putInt("value", res);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject(e);
		}
	}

	// set local video render mode
	@ReactMethod
	public void setLocalRenderMode(int mode) {
		AgoraManager.getInstance().mRtcEngine.setLocalRenderMode(mode);
	}

	@ReactMethod
	public void getSdkVersion(Promise promise) {
		try {
			String res = AgoraManager.getInstance().mRtcEngine.getSdkVersion();
			promise.resolve(res);
		} catch (Exception e) {
			promise.reject(e);
		}
	}

	@ReactMethod
	public void setLocalVideoMirrorMode(int mode, Promise promise) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine.setLocalVideoMirrorMode(mode);
			if (res != 0)
				throw new ReactNativeAgoraException("setLocalVideoMirrorMode Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject(e);
		}
	}

	@ReactMethod
	public void setBeautyEffectOptions(boolean enabled, ReadableMap options, Promise promise) {
		try {
			BeautyOptions beautyOption = new BeautyOptions();
			beautyOption.lighteningContrastLevel = options.getInt("lighteningContrastLevel");
			beautyOption.lighteningLevel = (float) options.getDouble("lighteningLevel");
			beautyOption.smoothnessLevel = (float) options.getDouble("smoothnessLevel");
			beautyOption.rednessLevel = (float) options.getDouble("rednessLevel");
			int res = AgoraManager.getInstance().mRtcEngine.setBeautyEffectOptions(true, beautyOption);
			if (res != 0) throw new ReactNativeAgoraException("setBeautyEffectOptions Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject(e);
		}
	}

	@ReactMethod
	public void setLocalVoiceChanger(int voiceChanger, Promise promise) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine.setLocalVoiceChanger(voiceChanger);
			if (res != 0) throw new ReactNativeAgoraException("setLocalVoiceChanger Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject(e);
		}
	}

	@ReactMethod
	public void setLocalVoiceReverbPreset(int preset, Promise promise) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine.setLocalVoiceReverbPreset(preset);
			if (res != 0)
				throw new ReactNativeAgoraException("setLocalVoiceReverbPreset Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject(e);
		}
	}

	@ReactMethod
	public void enableSoundPositionIndication(boolean enabled, Promise promise) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine.enableSoundPositionIndication(enabled);
			if (res != 0)
				throw new ReactNativeAgoraException("enableSoundPositionIndication Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject(e);
		}
	}

	@ReactMethod
	public void setRemoteVoicePosition(int uid, int pan, int gain, Promise promise) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine.setRemoteVoicePosition(uid, pan, gain);
			if (res != 0) throw new ReactNativeAgoraException("setRemoteVoicePosition Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject(e);
		}
	}

	@ReactMethod
	public void startLastmileProbeTest(ReadableMap config, Promise promise) {
		try {
			LastmileProbeConfig probeConfig = new LastmileProbeConfig();
			probeConfig.probeUplink = config.getBoolean("probeUplink");
			probeConfig.probeDownlink = config.getBoolean("probeDownlink");
			probeConfig.expectedDownlinkBitrate = config.getInt("expectedDownlinkBitrate");
			probeConfig.expectedUplinkBitrate = config.getInt("expectedUplinkBitrate");
			int res = AgoraManager.getInstance().mRtcEngine.startLastmileProbeTest(probeConfig);
			if (res != 0) throw new ReactNativeAgoraException("startLastmileProbeTest Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject(e);
		}
	}

	@ReactMethod
	public void stopLastmileProbeTest(Promise promise) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine.stopLastmileProbeTest();
			if (res != 0) throw new ReactNativeAgoraException("stopLastmileProbeTest Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject(e);
		}
	}

	@ReactMethod
	public void setRemoteUserPriority(int uid, int userPrority, Promise promise) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine.setRemoteUserPriority(uid, userPrority);
			if (res != 0) throw new ReactNativeAgoraException("setRemoteUserPriority Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject(e);
		}
	}

	@ReactMethod
	public void startEchoTestWithInterval(int interval, Promise promise) {
		try {
			int res = AgoraManager.getInstance().mRtcEngine.startEchoTest(interval);
			if (res != 0)
				throw new ReactNativeAgoraException("startEchoTestWithInterval Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject(e);
		}
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

			int res = AgoraManager.getInstance().mRtcEngine.setCameraCapturerConfiguration(config);
			if (res != 0)
				throw new ReactNativeAgoraException("setCameraCapturerConfiguration Failed", res);
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			promise.resolve(map);
		} catch (Exception e) {
			promise.reject(e);
		}
	}


	@ReactMethod
	private void sendEvent(ReactContext reactContext,
						   String eventName,
						   @Nullable WritableMap params) {
		Log.w("AGORA", eventName + " - > " + params.toString());
		reactContext
				.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
				.emit(AG_PREFIX + eventName, params);
	}
}
