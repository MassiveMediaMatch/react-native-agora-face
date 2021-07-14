package live.ablo.agora;

import android.os.Handler;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.agora.rtc.Constants;
import io.agora.rtc.IAudioEffectManager;
import io.agora.rtc.IMetadataObserver;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.internal.EncryptionConfig;
import io.agora.rtc.internal.LastmileProbeConfig;
import io.agora.rtc.live.LiveInjectStreamConfig;
import io.agora.rtc.live.LiveTranscoding;
import io.agora.rtc.video.AgoraImage;
import io.agora.rtc.video.BeautyOptions;
import io.agora.rtc.video.CameraCapturerConfiguration;
import io.agora.rtc.video.VideoEncoderConfiguration;

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
	private static final String ErrorCodeEncryptedStreamNotAllowedPublished = "ErrorCodeEncryptedStreamNotAllowedPublished";
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

	private final RtcEventHandler engineEventHandler;
	private MediaObserver mediaObserver;

	public AgoraModule(ReactApplicationContext reactContext) {
		super(reactContext);
		engineEventHandler = new RtcEventHandler(reactContext);
	}

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
		constants.put(ChannelProfileCommunication, Constants.CHANNEL_PROFILE_COMMUNICATION);
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
		AgoraManager.getInstance().init(getReactApplicationContext(), engineEventHandler, options);
	}

	@ReactMethod
	public void enableEncryption(boolean enable, String key, Promise promise) {
		EncryptionConfig config = new EncryptionConfig();
		config.encryptionKey = key;
		config.encryptionMode = EncryptionConfig.EncryptionMode.AES_128_XTS;
		int res = AgoraManager.getInstance().getEngine().enableEncryption(enable, config);
		resolvePromiseFromResolve(res, promise);
	}

	@ReactMethod
	public void toggleFaceDetection(boolean enabled, Promise promise) {
		int res = AgoraManager.getInstance().getEngine().enableFaceDetection(enabled);
		resolvePromiseFromResolve(res, promise, "enable facedetection");
	}

	@ReactMethod
	public void takeScreenshot(int uid, final Promise promise) {
		File outputDir = getReactApplicationContext().getCacheDir();
		try {
			final File outputFile = File.createTempFile("screenshot", ".jpeg", outputDir);
			FaceDetector.getInstance().takeScreenshot(outputFile.toString(), uid);

			// generating the screenshot happens async
			// perhaps we got lucky and the async task already finished
			if (outputFile.exists() && outputFile.length() > 0) {
				promise.resolve(outputFile.toString());
				return;
			}

			// file not ready yet, we need to check with an interval
			// until the file is written
			final Handler h = new Handler();
			h.postDelayed(new Runnable()
			{
				private long counter = 0;

				@Override
				public void run()
				{
					counter++;
					if (outputFile.exists() && outputFile.length() > 0) {
						promise.resolve(outputFile.toString());
						return;
					}
					if (counter > 10) {
						// waited too long, this did not work
						promise.reject(new Error("Waited too long for a screenshot to generate"));
					}
					h.postDelayed(this, 500);
				}
			}, 500);
		} catch (IOException e) {
			promise.reject(e);
		}
	}

	@ReactMethod
	public void toggleFaceDetectionBlurring(boolean enabled, Promise promise) {
		FaceDetector.getInstance().setBlurOnNoFaceDetected(enabled);
		resolvePromiseFromResolve(0, promise);
	}

	@ReactMethod
	public void toggleBlurring(boolean enabled, Promise promise) {
		FaceDetector.getInstance().setBlurring(enabled);
		resolvePromiseFromResolve(0, promise);
	}

	@ReactMethod
	public void toggleFaceDetectionDataEvents(boolean enabled, Promise promise) {
		FaceDetector.getInstance().setSendFaceDetectionDataEvents(enabled);
		resolvePromiseFromResolve(0, promise);
	}

	@ReactMethod
	public void toggleFaceDetectionStatusEvents(boolean enabled, Promise promise) {
		FaceDetector.getInstance().setSendFaceDetectionStatusEvent(enabled);
		resolvePromiseFromResolve(0, promise);
	}

	@ReactMethod
	public void renewToken(String token,
						   Promise promise) {
		int res = AgoraManager.getInstance().getEngine().renewToken(token);
		resolvePromiseFromResolve(res, promise, "renew token");
	}

	@ReactMethod
	public void enableWebSdkInteroperability(boolean enabled, Promise promise) {
		int res = AgoraManager.getInstance().getEngine().enableWebSdkInteroperability(enabled);
		resolvePromiseFromResolve(res, promise, "enableWebSdkInteroperability Failed");
	}

	@ReactMethod
	public void getConnectionState(Promise promise) {
		int res = AgoraManager.getInstance().getEngine().getConnectionState();
		resolvePromiseFromResolve(res, promise, "getConnectionState Failed");
	}

	@ReactMethod
	public void setClientRole(int role) {
		int res = AgoraManager.getInstance().getEngine().setClientRole(role);
		if (res != 0) {
			sendError(getReactApplicationContext(), res, "setClientRole Failed");
		}
	}

	@ReactMethod
	public void joinChannel(ReadableMap options) {
		int res = AgoraManager.getInstance().joinChannel(options);
		if (res != 0) {
			sendError(getReactApplicationContext(), res, "joinChannel Failed");
		}
	}

	@ReactMethod
	public void switchChannel(ReadableMap options) {
		int res = AgoraManager.getInstance().switchChannel(options);
		if (res != 0) {
			sendError(getReactApplicationContext(), res, "switchChannel Failed");
		}
	}

	@ReactMethod
	public void setVideoEncoderConfiguration(ReadableMap options) {
		int res = AgoraManager.getInstance().setVideoEncoderConfiguration(options);
		if (res != 0) {
			sendError(getReactApplicationContext(), res, "setVideoEncoderConfiguration Failed");
		}
	}

	@ReactMethod
	public void leaveChannel(Promise promise) {
		int res = AgoraManager.getInstance().getEngine().leaveChannel();
		if (res != 0) {
			sendError(getReactApplicationContext(), res, "leaveChannel Failed");
		}
	}

	@ReactMethod
	public void destroy() {
		AgoraManager.getInstance().destroy();
		FaceDetector.getInstance().destroy();
	}

	@ReactMethod
	public void startPreview() {
		AgoraManager.getInstance().getEngine().startPreview();
	}

	@ReactMethod
	public void stopPreview() {
		AgoraManager.getInstance().getEngine().stopPreview();
	}

	@ReactMethod
	public void setEnableSpeakerphone(boolean enabled) {
		int res = AgoraManager.getInstance().getEngine().setEnableSpeakerphone(enabled);
		if (res != 0) {
			sendError(getReactApplicationContext(), res, "setEnableSpeakerphone Failed");
		}
	}

	@ReactMethod
	public void setDefaultAudioRouteToSpeakerphone(boolean enabled) {
		int res = AgoraManager.getInstance().getEngine().setDefaultAudioRoutetoSpeakerphone(enabled);
		if (res != 0) {
			sendError(getReactApplicationContext(), res, "$1");
		}
	}

	@ReactMethod
	public void enableVideo() {
		AgoraManager.getInstance().getEngine().enableVideo();
	}

	@ReactMethod
	public void disableVideo() {
		AgoraManager.getInstance().getEngine().disableVideo();
	}

	@ReactMethod
	public void enableLocalVideo(boolean enabled) {
		AgoraManager.getInstance().getEngine().enableLocalVideo(enabled);
	}

	@ReactMethod
	public void muteLocalVideoStream(boolean muted) {
		AgoraManager.getInstance().getEngine().muteLocalVideoStream(muted);
	}

	@ReactMethod
	public void muteAllRemoteVideoStreams(boolean muted) {
		AgoraManager.getInstance().getEngine().muteAllRemoteVideoStreams(muted);
	}

	@ReactMethod
	public void muteRemoteVideoStream(int uid, boolean muted) {
		AgoraManager.getInstance().getEngine().muteRemoteVideoStream(uid, muted);
	}

	@ReactMethod
	public void setDefaultMuteAllRemoteVideoStreams(boolean muted) {
		AgoraManager.getInstance().getEngine().setDefaultMuteAllRemoteVideoStreams(muted);
	}

	@ReactMethod
	public void switchCamera(Promise promise) {
		int res = AgoraManager.getInstance().getEngine().switchCamera();
		resolvePromiseFromResolve(res, promise, "switchCamera Failed");
	}

	@ReactMethod
	public void isCameraZoomSupported(Promise promise) {
		boolean res = AgoraManager.getInstance().getEngine().isCameraZoomSupported();
		resolvePromiseFromResolve(res, promise);
	}


	@ReactMethod
	public void isCameraTorchSupported(Promise promise) {
		boolean res = AgoraManager.getInstance().getEngine().isCameraTorchSupported();
		resolvePromiseFromResolve(res, promise);
	}

	@ReactMethod
	public void isCameraFocusSupported(Promise promise) {
		boolean res = AgoraManager.getInstance().getEngine().isCameraFocusSupported();
		resolvePromiseFromResolve(res, promise);
	}

	@ReactMethod
	public void isCameraExposurePositionSupported(Promise promise) {
		boolean res = AgoraManager.getInstance().getEngine().isCameraExposurePositionSupported();
		resolvePromiseFromResolve(res, promise);
	}


	@ReactMethod
	public void isCameraAutoFocusFaceModeSupported(Promise promise) {
		boolean res = AgoraManager.getInstance().getEngine().isCameraAutoFocusFaceModeSupported();
		resolvePromiseFromResolve(res, promise);

	}

	@ReactMethod
	public void setCameraZoomFactor(float factor, Promise promise) {
		int res = AgoraManager.getInstance().getEngine().setCameraZoomFactor(factor);
		resolvePromiseFromResolve(res, promise, "setCameraZoomFactor Failed");
	}

	@ReactMethod
	public void getCameraMaxZoomFactor(Promise promise) {
		double res = AgoraManager.getInstance().getEngine().getCameraMaxZoomFactor();
		resolvePromiseFromResolve(res, promise);
	}

	@ReactMethod
	public void setCameraFocusPositionInPreview(ReadableMap options, Promise promise) {
		int res = AgoraManager.getInstance().getEngine().setCameraFocusPositionInPreview(
				(float) options.getDouble("x"),
				(float) options.getDouble("y")
		);
		resolvePromiseFromResolve(res, promise, "setCameraFocusPositionInPreview Failed");

	}

	@ReactMethod
	public void setCameraExposurePosition(ReadableMap options, Promise promise) {
		int res = AgoraManager.getInstance().getEngine().setCameraExposurePosition(
				(float) options.getDouble("x"),
				(float) options.getDouble("y")
		);
		resolvePromiseFromResolve(res, promise, "setCameraExposurePosition Failed");
	}

	@ReactMethod
	public void setCameraTorchOn(boolean isOn, Promise promise) {
		int res = AgoraManager.getInstance().getEngine().setCameraTorchOn(isOn);
		resolvePromiseFromResolve(res, promise, "setCameraTorchOn Failed");
	}

	@ReactMethod
	public void setCameraAutoFocusFaceModeEnabled(boolean enabled, Promise promise) {
		int res = AgoraManager.getInstance().getEngine().setCameraAutoFocusFaceModeEnabled(enabled);
		resolvePromiseFromResolve(res, promise, "setCameraAutoFocusFaceModeEnabled Failed");

	}

	@ReactMethod
	public void getCallId(Promise promise) {
		String res = AgoraManager.getInstance().getEngine().getCallId();
		resolvePromiseFromResolve(res, promise);
	}

	@ReactMethod
	public void setLog(String filePath, int level, int size, Promise promise) {
		try {
			int res = 0;
			res = AgoraManager.getInstance().getEngine().setLogFileSize(size);
			if (res != 0) throw new ReactNativeAgoraException("setLogFileSize Failed", res);
			res = AgoraManager.getInstance().getEngine().setLogFilter(level);
			if (res != 0) throw new ReactNativeAgoraException("setLogFilter Failed", res);
			res = AgoraManager.getInstance().getEngine().setLogFile(filePath);
			resolvePromiseFromResolve(res, promise, "setLogFile Failed");
		} catch (Exception e) {
			promise.reject(e);
		}
	}


	@ReactMethod
	public void enableAudio() {
		AgoraManager.getInstance().getEngine().enableAudio();
	}

	@ReactMethod
	public void disableAudio() {
		AgoraManager.getInstance().getEngine().disableAudio();
	}

	@ReactMethod
	public void muteAllRemoteAudioStreams(boolean muted) {
		AgoraManager.getInstance().getEngine().muteAllRemoteAudioStreams(muted);
	}

	@ReactMethod
	public void muteRemoteAudioStream(int uid, boolean muted) {
		AgoraManager.getInstance().getEngine().muteRemoteAudioStream(uid, muted);
	}

	@ReactMethod
	public void setDefaultMuteAllRemoteAudioStreams(boolean muted) {
		AgoraManager.getInstance().getEngine().setDefaultMuteAllRemoteAudioStreams(muted);
	}

	@ReactMethod
	public void adjustRecordingSignalVolume(int volume) {
		int res = AgoraManager.getInstance().getEngine().adjustRecordingSignalVolume(volume);
		if (res != 0) {
			sendError(getReactApplicationContext(), res, "adjustRecordingSignalVolume Failed");
		}
	}

	@ReactMethod
	public void adjustPlaybackSignalVolume(int volume) {
		int res = AgoraManager.getInstance().getEngine().adjustPlaybackSignalVolume(volume);
		if (res != 0) {
			sendError(getReactApplicationContext(), res, "adjustPlaybackSignalVolume Failed");
		}
	}

	@ReactMethod
	public void enableAudioVolumeIndication(int interval, int smooth) {
		AgoraManager.getInstance().getEngine().enableAudioVolumeIndication(interval, smooth, false);
	}

	@ReactMethod
	public void enableLocalAudio(boolean enabled) {
		AgoraManager.getInstance().getEngine().enableLocalAudio(enabled);
	}

	@ReactMethod
	public void muteLocalAudioStream(boolean enabled) {
		AgoraManager.getInstance().getEngine().muteLocalAudioStream(enabled);
	}

	@ReactMethod
	public void isSpeakerphoneEnabled(Callback callback) {
		WritableMap map = Arguments.createMap();
		map.putBoolean("status", AgoraManager.getInstance().getEngine().isSpeakerphoneEnabled());
		callback.invoke(map);
	}

	@ReactMethod
	public void enableInEarMonitoring(boolean enabled) {
		int res = AgoraManager.getInstance().getEngine().enableInEarMonitoring(enabled);
		if (res != 0) {
			sendError(getReactApplicationContext(), res, "enableInEarMonitoring Failed");
		}
	}

	@ReactMethod
	public void setInEarMonitoringVolume(int volume) {
		int res = AgoraManager.getInstance().getEngine().setInEarMonitoringVolume(volume);
		if (res != 0) {
			sendError(getReactApplicationContext(), res, "setInEarMonitoringVolume Failed");
		}
	}

	@ReactMethod
	public void setLocalVoicePitch(double pitch) {
		int res = AgoraManager.getInstance().getEngine().setLocalVoicePitch(pitch);
		if (res != 0) {
			sendError(getReactApplicationContext(), res, "setLocalVoicePitch Failed");
		}
	}

	@ReactMethod
	public void setLocalVoiceEqualization(int band, int gain) {
		int res = AgoraManager.getInstance().getEngine().setLocalVoiceEqualization(band, gain);
		if (res != 0) {
			sendError(getReactApplicationContext(), res, "setLocalVoiceEqualization Failed");
		}
	}

	@ReactMethod
	public void setLocalVoiceReverb(int reverb, int value) {
		int res = AgoraManager.getInstance().getEngine().setLocalVoiceReverb(reverb, value);
		if (res != 0) {
			sendError(getReactApplicationContext(), res, "setLocalVoiceReverb Failed");
		}
	}

	@ReactMethod
	public void startAudioMixing(ReadableMap options) {
		int res = AgoraManager.getInstance().getEngine().startAudioMixing(
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
		int res = AgoraManager.getInstance().getEngine().stopAudioMixing();
		if (res != 0) {
			sendError(getReactApplicationContext(), res, "stopAudioMixing Failed");
		}
	}

	@ReactMethod
	public void pauseAudioMixing() {
		int res = AgoraManager.getInstance().getEngine().pauseAudioMixing();
		if (res != 0) {
			sendError(getReactApplicationContext(), res, "pauseAudioMixing Failed");
		}
	}

	@ReactMethod
	public void resumeAudioMixing() {
		int res = AgoraManager.getInstance().getEngine().resumeAudioMixing();
		if (res != 0) {
			sendError(getReactApplicationContext(), res, "resumeAudioMixing Failed");
		}
	}

	@ReactMethod
	public void adjustAudioMixingVolume(int volume) {
		int res = AgoraManager.getInstance().getEngine().adjustAudioMixingVolume(volume);
		if (res != 0) {
			sendError(getReactApplicationContext(), res, "adjustAudioMixingVolume Failed");
		}
	}

	@ReactMethod
	public void adjustAudioMixingPlayoutVolume(int volume) {
		int res = AgoraManager.getInstance().getEngine().adjustAudioMixingPlayoutVolume(volume);
		if (res != 0) {
			sendError(getReactApplicationContext(), res, "adjustAudioMixingPlayoutVolume Failed");
		}
	}

	@ReactMethod
	public void adjustAudioMixingPublishVolume(int volume) {
		int res = AgoraManager.getInstance().getEngine().adjustAudioMixingPublishVolume(volume);
		if (res != 0) {
			sendError(getReactApplicationContext(), res, "adjustAudioMixingPublishVolume Failed");
		}
	}

	@ReactMethod
	public void getAudioMixingPlayoutVolume(Promise promise) {
		int res = AgoraManager.getInstance().getEngine().getAudioMixingPlayoutVolume();
		resolvePromiseFromNegativeResolve(res, promise, "getAudioMixingPlayoutVolume Failed");
	}

	@ReactMethod
	public void getAudioMixingPublishVolume(Promise promise) {
		int res = AgoraManager.getInstance().getEngine().getAudioMixingPlayoutVolume();
		resolvePromiseFromNegativeResolve(res, promise, "getAudioMixingPublishVolume Failed");
	}

	@ReactMethod
	public void getAudioMixingDuration(Promise promise) {
		int res = AgoraManager.getInstance().getEngine().getAudioMixingDuration();
		resolvePromiseFromResolve(res, promise, "getAudioMixingDuration Failed");
	}

	@ReactMethod
	public void getAudioMixingCurrentPosition(Promise promise) {
		int res = AgoraManager.getInstance().getEngine().getAudioMixingCurrentPosition();
		resolvePromiseFromResolve(res, promise, "getAudioMixingCurrentPosition Failed");
	}

	@ReactMethod
	public void setAudioMixingPosition(int pos, Promise promise) {
		int res = AgoraManager.getInstance().getEngine().setAudioMixingPosition(pos);
		resolvePromiseFromResolve(res, promise, "setAudioMixingPosition Failed");
	}

	@ReactMethod
	public void startAudioRecording(ReadableMap options, Promise promise) {
		try {
			int res = AgoraManager.getInstance().getEngine()
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
		int res = AgoraManager.getInstance().getEngine()
				.stopAudioRecording();
		resolvePromiseFromResolve(res, promise, "stopAudioRecording Failed");
	}

	@ReactMethod
	public void stopEchoTest(Promise promise) {
		int res = AgoraManager.getInstance().getEngine()
				.stopEchoTest();
		resolvePromiseFromResolve(res, promise, "stopEchoTest Failed");

	}

	@ReactMethod
	public void enableLastmileTest(Promise promise) {
		int res = AgoraManager.getInstance().getEngine()
				.enableLastmileTest();
		resolvePromiseFromResolve(res, promise, "enableLastmileTest Failed");
	}

	@ReactMethod
	public void disableLastmileTest(Promise promise) {
		try {
			int res = AgoraManager.getInstance().getEngine()
					.disableLastmileTest();
			resolvePromiseFromResolve(res, promise, "disableLastmileTest Failed");
		} catch (Exception e) {
			promise.reject("131022", e);
		}
	}

	@ReactMethod
	public void setRecordingAudioFrameParameters(ReadableMap options, Promise promise) {
		try {
			int res = AgoraManager.getInstance().getEngine()
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
			int res = AgoraManager.getInstance().getEngine()
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
			int res = AgoraManager.getInstance().getEngine()
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
			int res = AgoraManager.getInstance().getEngine()
					.addVideoWatermark(createAgoraImage(options));
			resolvePromiseFromResolve(res, promise, "addVideoWatermark Failed");
		} catch (Exception e) {
			promise.reject("131026", e);
		}
	}

	@ReactMethod
	public void clearVideoWatermarks(Promise promise) {
		try {
			int res = AgoraManager.getInstance().getEngine()
					.clearVideoWatermarks();
			resolvePromiseFromResolve(res, promise, "clearVideoWatermarks Failed");
		} catch (Exception e) {
			promise.reject("131027", e);
		}
	}

	@ReactMethod
	public void setLocalPublishFallbackOption(int option, Promise promise) {
		try {
			int res = AgoraManager.getInstance().getEngine()
					.setLocalPublishFallbackOption(option);
			resolvePromiseFromResolve(res, promise, "setLocalPublishFallbackOption Failed");
		} catch (Exception e) {
			promise.reject("131028", e);
		}
	}

	@ReactMethod
	public void setRemoteSubscribeFallbackOption(int option, Promise promise) {
		try {
			int res = AgoraManager.getInstance().getEngine()
					.setRemoteSubscribeFallbackOption(option);
			resolvePromiseFromResolve(res, promise, "setRemoteSubscribeFallbackOption Failed");
		} catch (Exception e) {
			promise.reject("131029", e);
		}
	}

	@ReactMethod
	public void enableDualStreamMode(boolean enabled, Promise promise) {
		try {
			int res = AgoraManager.getInstance().getEngine()
					.enableDualStreamMode(enabled);
			resolvePromiseFromResolve(res, promise, "enableDualStreamMode Failed");
		} catch (Exception e) {
			promise.reject("131028", e);
		}
	}


	@ReactMethod
	public void setRemoteVideoStreamType(ReadableMap options, Promise promise) {
		try {
			int res = AgoraManager.getInstance().getEngine()
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
			int res = AgoraManager.getInstance().getEngine()
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
			mediaObserver.setMetadata(data.getBytes(StandardCharsets.UTF_8));
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			promise.resolve(map);
		}
	}

	@ReactMethod
	public void registerMediaMetadataObserver(final Promise promise) {
		try {
			mediaObserver = new MediaObserver(getReactApplicationContext());
			int res = AgoraManager.getInstance().getEngine()
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

			int res = AgoraManager.getInstance().getEngine()
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
			int res = AgoraManager.getInstance().getEngine()
					.removeInjectStreamUrl(options.getString("url"));
			resolvePromiseFromResolve(res, promise, "removeInjectStreamUrl Failed");
		} catch (Exception e) {
			promise.reject("131032", e);
		}
	}

	@ReactMethod
	public void addPublishStreamUrl(ReadableMap options, Promise promise) {
		try {
			int res = AgoraManager.getInstance().getEngine()
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
			int res = AgoraManager.getInstance().getEngine()
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
			int res = AgoraManager.getInstance().getEngine().setLiveTranscoding(transcoding);
			if (res != 0) {
				sendError(getReactApplicationContext(), res, "setLiveTranscoding Failed");
			}
		} catch (Exception e) {
			promise.reject(e);
		}
	}

	@ReactMethod
	public void getEffectsVolume(Promise promise) {
		IAudioEffectManager manager = AgoraManager.getInstance().getEngine().getAudioEffectManager();
		double res = manager.getEffectsVolume();
		resolvePromiseFromResolve(res, promise, "getEffectsVolume Failed");
	}

	@ReactMethod
	public void setEffectsVolume(double volume, Promise promise) {
		IAudioEffectManager manager = AgoraManager.getInstance().getEngine().getAudioEffectManager();
		int res = manager.setEffectsVolume(volume);
		resolvePromiseFromResolve(res, promise, "setEffectsVolume Failed");
	}


	@ReactMethod
	public void setVolumeOfEffect(int soundId, double volume, Promise promise) {
		IAudioEffectManager manager = AgoraManager.getInstance().getEngine().getAudioEffectManager();
		int res = manager.setVolumeOfEffect(soundId, volume);
		resolvePromiseFromResolve(res, promise, "setVolumeOfEffect Failed");
	}

	@ReactMethod
	public void playEffect(ReadableMap options, Promise promise) {
		try {
			IAudioEffectManager manager = AgoraManager.getInstance().getEngine().getAudioEffectManager();
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
		IAudioEffectManager manager = AgoraManager.getInstance().getEngine().getAudioEffectManager();
		int res = manager.stopEffect(soundId);
		resolvePromiseFromResolve(res, promise, "stopEffect Failed");
	}

	@ReactMethod
	public void stopAllEffects(Promise promise) {
		IAudioEffectManager manager = AgoraManager.getInstance().getEngine().getAudioEffectManager();
		int res = manager.stopAllEffects();
		resolvePromiseFromResolve(res, promise, "stopAllEffects Failed");
	}

	@ReactMethod
	public void preloadEffect(int soundId, String filePath, Promise promise) {
		IAudioEffectManager manager = AgoraManager.getInstance().getEngine().getAudioEffectManager();
		int res = manager.preloadEffect(soundId, filePath);
		resolvePromiseFromResolve(res, promise, "preloadEffect Failed");
	}

	@ReactMethod
	public void unloadEffect(int soundId, Promise promise) {
		IAudioEffectManager manager = AgoraManager.getInstance().getEngine().getAudioEffectManager();
		int res = manager.unloadEffect(soundId);
		resolvePromiseFromResolve(res, promise, "unloadEffect Failed");
	}

	@ReactMethod
	public void pauseEffect(int soundId, Promise promise) {
		IAudioEffectManager manager = AgoraManager.getInstance().getEngine().getAudioEffectManager();
		int res = manager.pauseEffect(soundId);
		resolvePromiseFromResolve(res, promise, "pauseEffect Failed");
	}

	@ReactMethod
	public void pauseAllEffects(Promise promise) {
		IAudioEffectManager manager = AgoraManager.getInstance().getEngine().getAudioEffectManager();
		int res = manager.pauseAllEffects();
		resolvePromiseFromResolve(res, promise, "pauseAllEffects Failed");
	}

	@ReactMethod
	public void resumeEffect(int soundId, Promise promise) {
		IAudioEffectManager manager = AgoraManager.getInstance().getEngine().getAudioEffectManager();
		int res = manager.resumeEffect(soundId);
		resolvePromiseFromResolve(res, promise, "resumeEffect Failed");
	}

	@ReactMethod
	public void resumeAllEffects(int soundId, Promise promise) {
		IAudioEffectManager manager = AgoraManager.getInstance().getEngine().getAudioEffectManager();
		int res = manager.resumeAllEffects();
		resolvePromiseFromResolve(res, promise, "resumeAllEffects Failed");
	}

	// set local video render mode
	@ReactMethod
	public void setLocalRenderMode(int mode) {
		AgoraManager.getInstance().getEngine().setLocalRenderMode(mode);
	}

	@ReactMethod
	public void setLocalVideoMirrorMode(int mode, Promise promise) {
		int res = AgoraManager.getInstance().getEngine().setLocalVideoMirrorMode(mode);
		resolvePromiseFromResolve(res, promise, "setLocalVideoMirrorMode Failed");
	}

	@ReactMethod
	public void setBeautyEffectOptions(boolean enabled, ReadableMap options, Promise promise) {
		BeautyOptions beautyOption = new BeautyOptions();
		beautyOption.lighteningContrastLevel = options.getInt("lighteningContrastLevel");
		beautyOption.lighteningLevel = (float) options.getDouble("lighteningLevel");
		beautyOption.smoothnessLevel = (float) options.getDouble("smoothnessLevel");
		beautyOption.rednessLevel = (float) options.getDouble("rednessLevel");
		int res = AgoraManager.getInstance().getEngine().setBeautyEffectOptions(true, beautyOption);
		resolvePromiseFromResolve(res, promise, "setBeautyEffectOptions Failed");
	}

	@ReactMethod
	public void setLocalVoiceChanger(int voiceChanger, Promise promise) {
		int res = AgoraManager.getInstance().getEngine().setLocalVoiceChanger(voiceChanger);
		resolvePromiseFromResolve(res, promise, "setLocalVoiceChanger Failed");
	}

	@ReactMethod
	public void setLocalVoiceReverbPreset(int preset, Promise promise) {
		int res = AgoraManager.getInstance().getEngine().setLocalVoiceReverbPreset(preset);
		resolvePromiseFromResolve(res, promise, "setLocalVoiceReverbPreset Failed");
	}

	@ReactMethod
	public void enableSoundPositionIndication(boolean enabled, Promise promise) {
		int res = AgoraManager.getInstance().getEngine().enableSoundPositionIndication(enabled);
		resolvePromiseFromResolve(res, promise, "enableSoundPositionIndication Failed");
	}

	@ReactMethod
	public void setRemoteVoicePosition(int uid, int pan, int gain, Promise promise) {
		int res = AgoraManager.getInstance().getEngine().setRemoteVoicePosition(uid, pan, gain);
		resolvePromiseFromResolve(res, promise, "setRemoteVoicePosition Failed");
	}

	@ReactMethod
	public void startLastmileProbeTest(ReadableMap config, Promise promise) {
		LastmileProbeConfig probeConfig = new LastmileProbeConfig();
		probeConfig.probeUplink = config.getBoolean("probeUplink");
		probeConfig.probeDownlink = config.getBoolean("probeDownlink");
		probeConfig.expectedDownlinkBitrate = config.getInt("expectedDownlinkBitrate");
		probeConfig.expectedUplinkBitrate = config.getInt("expectedUplinkBitrate");
		int res = AgoraManager.getInstance().getEngine().startLastmileProbeTest(probeConfig);
		resolvePromiseFromResolve(res, promise, "startLastmileProbeTest Failed");
	}

	@ReactMethod
	public void stopLastmileProbeTest(Promise promise) {
		int res = AgoraManager.getInstance().getEngine().stopLastmileProbeTest();
		resolvePromiseFromResolve(res, promise, "stopLastmileProbeTest Failed");

	}

	@ReactMethod
	public void setRemoteUserPriority(int uid, int userPrority, Promise promise) {
		int res = AgoraManager.getInstance().getEngine().setRemoteUserPriority(uid, userPrority);
		resolvePromiseFromResolve(res, promise, "setRemoteUserPriority Failed");
	}

	@ReactMethod
	public void startEchoTestWithInterval(int interval, Promise promise) {
		int res = AgoraManager.getInstance().getEngine().startEchoTest(interval);
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

			int res = AgoraManager.getInstance().getEngine().setCameraCapturerConfiguration(config);
			resolvePromiseFromResolve(res, promise, "setCameraCapturerConfiguration Failed");
		} catch (Exception e) {
			promise.reject(e);
		}
	}

	@ReactMethod
	public void setChannelProfile(int channel, Promise promise) {
		int res = AgoraManager.getInstance().getEngine().setChannelProfile(channel);
		resolvePromiseFromResolve(res, promise, "setChannelProfile Failed");
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
		RtcEventHandler.sendEvent(reactApplicationContext, "error", err);
	}

}