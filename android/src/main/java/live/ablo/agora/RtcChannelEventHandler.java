package live.ablo.agora;

import android.graphics.Rect;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import io.agora.rtc.IRtcChannelEventHandler;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcChannel;

import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;
import static live.ablo.agora.AgoraConst.AGActiveSpeaker;
import static live.ablo.agora.AgoraConst.AGApiCallExecute;
import static live.ablo.agora.AgoraConst.AGAudioEffectFinish;
import static live.ablo.agora.AgoraConst.AGAudioMixingStateChanged;
import static live.ablo.agora.AgoraConst.AGAudioRouteChanged;
import static live.ablo.agora.AgoraConst.AGAudioTransportStatsOfUid;
import static live.ablo.agora.AgoraConst.AGAudioVolumeIndication;
import static live.ablo.agora.AgoraConst.AGCameraExposureAreaChanged;
import static live.ablo.agora.AgoraConst.AGCameraFocusAreaChanged;
import static live.ablo.agora.AgoraConst.AGClientRoleChanged;
import static live.ablo.agora.AgoraConst.AGConnectionLost;
import static live.ablo.agora.AgoraConst.AGConnectionStateChanged;
import static live.ablo.agora.AgoraConst.AGError;
import static live.ablo.agora.AgoraConst.AGFirstLocalAudioFrame;
import static live.ablo.agora.AgoraConst.AGFirstLocalVideoFrame;
import static live.ablo.agora.AgoraConst.AGFirstRemoteAudioDecoded;
import static live.ablo.agora.AgoraConst.AGFirstRemoteAudioFrame;
import static live.ablo.agora.AgoraConst.AGFirstRemoteVideoDecoded;
import static live.ablo.agora.AgoraConst.AGFirstRemoteVideoFrame;
import static live.ablo.agora.AgoraConst.AGJoinChannelSuccess;
import static live.ablo.agora.AgoraConst.AGLastmileProbeResult;
import static live.ablo.agora.AgoraConst.AGLastmileQuality;
import static live.ablo.agora.AgoraConst.AGLeaveChannel;
import static live.ablo.agora.AgoraConst.AGLocalPublishFallbackToAudioOnly;
import static live.ablo.agora.AgoraConst.AGLocalVideoChanged;
import static live.ablo.agora.AgoraConst.AGLocalVideoStats;
import static live.ablo.agora.AgoraConst.AGMediaEngineLoaded;
import static live.ablo.agora.AgoraConst.AGMediaEngineStartCall;
import static live.ablo.agora.AgoraConst.AGMicrophoneEnabled;
import static live.ablo.agora.AgoraConst.AGNetworkQuality;
import static live.ablo.agora.AgoraConst.AGNetworkTypeChanged;
import static live.ablo.agora.AgoraConst.AGOccurStreamMessageError;
import static live.ablo.agora.AgoraConst.AGReceiveStreamMessage;
import static live.ablo.agora.AgoraConst.AGRejoinChannelSuccess;
import static live.ablo.agora.AgoraConst.AGRemoteAudioStats;
import static live.ablo.agora.AgoraConst.AGRemoteSubscribeFallbackToAudioOnly;
import static live.ablo.agora.AgoraConst.AGRemoteVideoStats;
import static live.ablo.agora.AgoraConst.AGRequestToken;
import static live.ablo.agora.AgoraConst.AGRtcStats;
import static live.ablo.agora.AgoraConst.AGRtmpStreamingStateChanged;
import static live.ablo.agora.AgoraConst.AGStreamInjectedStatus;
import static live.ablo.agora.AgoraConst.AGStreamPublished;
import static live.ablo.agora.AgoraConst.AGStreamUnpublish;
import static live.ablo.agora.AgoraConst.AGTokenPrivilegeWillExpire;
import static live.ablo.agora.AgoraConst.AGTranscodingUpdate;
import static live.ablo.agora.AgoraConst.AGUserEnableLocalVideo;
import static live.ablo.agora.AgoraConst.AGUserEnableVideo;
import static live.ablo.agora.AgoraConst.AGUserJoined;
import static live.ablo.agora.AgoraConst.AGUserMuteAudio;
import static live.ablo.agora.AgoraConst.AGUserMuteVideo;
import static live.ablo.agora.AgoraConst.AGUserOffline;
import static live.ablo.agora.AgoraConst.AGVideoSizeChanged;
import static live.ablo.agora.AgoraConst.AGVideoTransportStatsOfUid;
import static live.ablo.agora.AgoraConst.AGWarning;
import static live.ablo.agora.AgoraConst.AG_PREFIX;
import static live.ablo.agora.AgoraConst.AGonFacePositionChanged;

public class RtcChannelEventHandler extends IRtcChannelEventHandler {

	private final ReactApplicationContext reactApplicationContext;

	RtcChannelEventHandler(ReactApplicationContext reactApplicationContext) {
		this.reactApplicationContext = reactApplicationContext;
	}

	public static void sendEvent(ReactContext reactContext,
								 String eventName, WritableMap params) {
		Log.w("AGORA", eventName + ": " + params.toString());
		reactContext
				.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
				.emit(AG_PREFIX + eventName, params);
	}

	public ReactApplicationContext getReactApplicationContext() {
		return reactApplicationContext;
	}

	@Override
	public void onUserJoined(final RtcChannel rtcChannel, final int uid, final int elapsed) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
			WritableMap map = Arguments.createMap();
			map.putString("channel", rtcChannel.channelId());
			map.putInt("uid", uid);
			map.putInt("elapsed", elapsed);
			sendEvent(reactApplicationContext, AGUserJoined, map);
			}
		});
	}

	@Override
	public void onUserOffline(final RtcChannel rtcChannel, final int uid, final int reason) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
			WritableMap map = Arguments.createMap();
			map.putString("channel", rtcChannel.channelId());
			map.putInt("uid", uid);
			map.putInt("reason", reason);
			sendEvent(reactApplicationContext, AGUserOffline, map);
			}
		});
	}

	@Override
	public void onVideoSizeChanged(final RtcChannel rtcChannel, final int uid, final int width, final int height, final int rotation) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
			WritableMap map = Arguments.createMap();
			map.putString("channel", rtcChannel.channelId());
			map.putInt("uid", uid);
			map.putInt("width", width);
			map.putInt("height", height);
			map.putInt("rotation", rotation);
			sendEvent(reactApplicationContext, AGVideoSizeChanged, map);
			}
		});
	}

	@Override
	public void onRemoteVideoStats(final RtcChannel rtcChannel, final IRtcEngineEventHandler.RemoteVideoStats stats) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
			WritableMap statsMap = Arguments.createMap();
			statsMap.putInt("uid", stats.uid);
			statsMap.putInt("width", stats.width);
			statsMap.putInt("height", stats.height);
			statsMap.putInt("receivedBitrate", stats.receivedBitrate);
			statsMap.putInt("rendererOutputFrameRate", stats.rendererOutputFrameRate);
			statsMap.putInt("rxStreamType", stats.rxStreamType);
			WritableMap map = Arguments.createMap();
			map.putString("channel", rtcChannel.channelId());
			map.putMap("stats", statsMap);
			sendEvent(reactApplicationContext, AGRemoteVideoStats, map);
			}
		});
	}

	@Override
	public void onJoinChannelSuccess(final RtcChannel rtcChannel, final int uid, final int elapsed) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
			WritableMap map = Arguments.createMap();
			map.putString("channel", rtcChannel.channelId());
			map.putInt("uid", uid);
			map.putInt("elapsed", elapsed);
			sendEvent(reactApplicationContext, AGJoinChannelSuccess, map);
			}
		});
	}

	@Override
	public void onLeaveChannel(final RtcChannel rtcChannel, final IRtcEngineEventHandler.RtcStats stats) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
			WritableMap statsMap = Arguments.createMap();
			statsMap.putInt("duration", stats.totalDuration);
			statsMap.putInt("txBytes", stats.txBytes);
			statsMap.putInt("rxBytes", stats.rxBytes);
			// statsMap.putInt("txKBitRate", stats.txKBitRate);
			// statsMap.putInt("rxKBitRate", stats.rxKBitRate);
			statsMap.putInt("txAudioKBitRate", stats.txAudioKBitRate);
			statsMap.putInt("rxAudioKBitRate", stats.rxAudioKBitRate);
			statsMap.putInt("txVideoKBitRate", stats.txVideoKBitRate);
			statsMap.putInt("rxVideoKBitRate", stats.rxVideoKBitRate);
			statsMap.putInt("lastmileDelay", stats.lastmileDelay);
			statsMap.putInt("userCount", stats.users);
			statsMap.putDouble("cpuAppUsage", stats.cpuAppUsage);
			statsMap.putDouble("cpuTotalUsage", stats.cpuTotalUsage);
			statsMap.putInt("txPacketLossRate", stats.txPacketLossRate);
			statsMap.putInt("rxPacketLossRate", stats.rxPacketLossRate);

			WritableMap map = Arguments.createMap();
			map.putString("channel", rtcChannel.channelId());
			map.putMap("stats", statsMap);
			sendEvent(reactApplicationContext, AGLeaveChannel, map);
			}
		});
	}

	@Override
	public void onChannelError(final RtcChannel rtcChannel, final int err) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
			WritableMap map = Arguments.createMap();
			map.putString("channel", rtcChannel.channelId());
			map.putString("message", "AgoraError");
			map.putInt("errorCode", err);
			sendEvent(reactApplicationContext, AGError, map);
			}
		});
	}

	@Override
	public void onChannelWarning(final RtcChannel rtcChannel, final int warn) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
			WritableMap map = Arguments.createMap();
			map.putString("channel", rtcChannel.channelId());
			map.putString("message", "AgoraWarning");
			map.putInt("errorCode", warn);
			sendEvent(reactApplicationContext, AGWarning, map);
			}
		});
	}
}
