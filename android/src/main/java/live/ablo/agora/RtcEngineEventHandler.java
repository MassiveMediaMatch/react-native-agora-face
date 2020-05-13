package live.ablo.agora;

import android.graphics.Rect;
import android.support.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.nio.charset.Charset;

import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.models.UserInfo;

import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;
import static live.ablo.agora.AgoraConst.*;

public class RtcEngineEventHandler extends IRtcEngineEventHandler {

	private final ReactApplicationContext reactContext;

	public RtcEngineEventHandler(ReactApplicationContext reactContext) {
		this.reactContext = reactContext;
	}

	private void sendEvent(ReactContext reactContext,
						   String eventName,
						   @Nullable WritableMap params) {
		StringBuffer agoraEvtName = new StringBuffer(AG_PREFIX);
		agoraEvtName.append(eventName);
		reactContext
				.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
				.emit(agoraEvtName.toString(), params);
	}

	@Override
	public void onWarning(final int code) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putString("message", "AgoraWarning");
				map.putInt("errorCode", code);
				sendEvent(reactContext, AGWarning, map);
			}
		});
	}

	@Override
	public void onError(final int code) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putString("message", "AgoraError");
				map.putInt("errorCode", code);
				sendEvent(reactContext, AGError, map);
			}
		});
	}

	@Override
	public void onApiCallExecuted(final int code, final String api, final String result) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putInt("errorCode", code);
				map.putString("api", api);
				map.putString("result", result);
				if (code != 0) {
					sendEvent(reactContext, AGError, map);
				} else {
					sendEvent(reactContext, AGApiCallExecute, map);
				}
			}
		});
	}

	@Override
	public void onJoinChannelSuccess(final String channel, final int uid, final int elapsed) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putString("channel", channel);
				map.putInt("uid", uid);
				map.putInt("elapsed", elapsed);
				sendEvent(reactContext, AGJoinChannelSuccess, map);
			}
		});
	}

	@Override
	public void onRejoinChannelSuccess(final String channel, final int uid, final int elapsed) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putString("channel", channel);
				map.putInt("uid", uid);
				map.putInt("elapsed", elapsed);
				sendEvent(reactContext, AGRejoinChannelSuccess, map);
			}
		});
	}

	@Override
	public void onLeaveChannel(final RtcStats stats) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap statsMap = Arguments.createMap();
				statsMap.putInt("duration", stats.totalDuration);
				statsMap.putInt("txBytes", stats.txBytes);
				statsMap.putInt("rxBytes", stats.rxBytes);
				statsMap.putInt("txAudioBytes", stats.txAudioBytes);
				statsMap.putInt("txVideoBytes", stats.txVideoBytes);
				statsMap.putInt("rxAudioBytes", stats.rxAudioBytes);
				statsMap.putInt("rxVideoBytes", stats.rxVideoBytes);
				statsMap.putInt("txKBitRate", stats.txKBitRate);
				statsMap.putInt("rxKBitRate", stats.rxKBitRate);
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
				map.putMap("stats", statsMap);
				sendEvent(reactContext, AGLeaveChannel, map);
			}
		});
	}

	@Override
	public void onClientRoleChanged(final int oldRole, final int newRole) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putInt("oldRole", oldRole);
				map.putInt("newRole", newRole);
				sendEvent(reactContext, AGClientRoleChanged, map);
			}
		});
	}

	@Override
	public void onLocalUserRegistered(final int uid, final String userAccount) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putInt("uid", uid);
				map.putString("userAccount", userAccount);
				sendEvent(reactContext, AGLocalUserRegistered, map);
			}
		});
	}

	@Override
	public void onUserInfoUpdated(final int uid, final UserInfo peer) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putInt("uid", uid);
				WritableMap peerInfo = Arguments.createMap();
				peerInfo.putInt("uid", peer.uid);
				peerInfo.putString("userAccount", peer.userAccount);
				map.putMap("peer", peerInfo);
				sendEvent(reactContext, AGUserInfoUpdated, map);
			}
		});
	}

	@Override
	public void onUserJoined(final int uid, final int elapsed) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putInt("uid", uid);
				map.putInt("elapsed", elapsed);
				sendEvent(reactContext, AGUserJoined, map);
			}
		});
	}

	@Override
	public void onUserOffline(final int uid, final int reason) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putInt("uid", uid);
				map.putInt("reason", reason);
				sendEvent(reactContext, AGUserOffline, map);
			}
		});
	}

	@Override
	public void onConnectionStateChanged(final int state, final int reason) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putInt("state", state);
				map.putInt("reason", reason);
				sendEvent(reactContext, AGConnectionStateChanged, map);
			}
		});
	}


	@Override
	public void onConnectionLost() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putString("message", "connectionLost");
				sendEvent(reactContext, AGConnectionLost, map);
			}
		});
	}

	@Override
	public void onTokenPrivilegeWillExpire(final String token) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putString("token", token);
				sendEvent(reactContext, AGTokenPrivilegeWillExpire, map);
			}
		});
	}

	@Override
	public void onRequestToken() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putString("message", "RequestToken");
				sendEvent(reactContext, AGRequestToken, map);
			}
		});
	}

	@Override
	public void onAudioVolumeIndication(final AudioVolumeInfo[] speakers, final int totalVolume) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {

				WritableArray arr = Arguments.createArray();
				for (int i = 0; i < speakers.length; i++) {
					WritableMap obj = Arguments.createMap();
					obj.putInt("uid", speakers[i].uid);
					obj.putInt("volume", speakers[i].volume);
					obj.putInt("vad", speakers[i].vad);
					arr.pushMap(obj);
				}

				WritableMap map = Arguments.createMap();
				map.putArray("speakers", arr);
				map.putInt("totalVolume", totalVolume);
				sendEvent(reactContext, AGAudioVolumeIndication, map);
			}
		});
	}

	@Override
	public void onActiveSpeaker(final int uid) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putInt("uid", uid);
				sendEvent(reactContext, AGActiveSpeaker, map);
			}
		});
	}

	@Override
	public void onFirstLocalAudioFrame(final int elapsed) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putInt("elapsed", elapsed);
				sendEvent(reactContext, AGFirstLocalAudioFrame, map);
			}
		});
	}

	@Override
	public void onFirstRemoteAudioFrame(final int uid, final int elapsed) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putInt("uid", uid);
				map.putInt("elapsed", elapsed);
				sendEvent(reactContext, AGFirstRemoteAudioFrame, map);
			}
		});
	}

	@Override
	public void onFirstLocalVideoFrame(final int width, final int height, final int elapsed) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putInt("width", width);
				map.putInt("height", height);
				map.putInt("elapsed", elapsed);
				sendEvent(reactContext, AGFirstLocalVideoFrame, map);
			}
		});
	}

	@Override
	public void onFirstRemoteVideoFrame(final int uid, final int width, final int height, final int elapsed) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putInt("uid", uid);
				map.putInt("width", width);
				map.putInt("height", height);
				map.putInt("elapsed", elapsed);
				sendEvent(reactContext, AGFirstRemoteVideoFrame, map);
			}
		});
	}

	@Override
	public void onUserMuteAudio(final int uid, final boolean muted) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putBoolean("muted", muted);
				map.putInt("uid", uid);
				sendEvent(reactContext, AGUserMuteAudio, map);
			}
		});
	}

	@Override
	public void onVideoSizeChanged(final int uid, final int width, final int height, final int rotation) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putInt("uid", uid);
				map.putInt("width", width);
				map.putInt("height", height);
				map.putInt("rotation", rotation);
				sendEvent(reactContext, AGVideoSizeChanged, map);
			}
		});
	}

	@Override
	public void onRtmpStreamingStateChanged(final String url, final int state, final int errCode) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putString("url", url);
				map.putInt("state", state);
				map.putInt("errorCode", errCode);
				sendEvent(reactContext, AGRtmpStreamingStateChanged, map);
			}
		});
	}

	@Override
	public void onNetworkTypeChanged(final int type) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putInt("type", type);
				sendEvent(reactContext, AGNetworkTypeChanged, map);
			}
		});
	}


	@Override
	public void onLocalAudioStateChanged(final int state, final int errCode) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putInt("state", state);
				map.putInt("errorCode", errCode);
				sendEvent(reactContext, AGLocalAudioStateChanged, map);
			}
		});
	}

	@Override
	public void onRemoteAudioStateChanged(final int uid,
										  final int state,
										  final int reason,
										  final int elapsed) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putInt("uid", uid);
				map.putInt("state", state);
				map.putInt("reason", reason);
				map.putInt("elapsed", elapsed);
				sendEvent(reactContext, AGRemoteAudioStateChanged, map);
			}
		});
	}

	@Override
	public void onFirstRemoteAudioDecoded(final int uid, final int elapsed) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putInt("uid", uid);
				map.putInt("elapsed", elapsed);
				sendEvent(reactContext, AGFirstRemoteAudioDecoded, map);
			}
		});
	}

	@Override
	public void onRemoteVideoStateChanged(final int uid,
										  final int state,
										  final int reason,
										  final int elapsed) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putInt("uid", uid);
				map.putInt("state", state);
				map.putInt("reason", reason);
				map.putInt("elapsed", elapsed);
				sendEvent(reactContext, AGRemoteVideoStateChanged, map);
			}
		});
	}

	@Override
	public void onLocalPublishFallbackToAudioOnly(final boolean isFallbackOrRecover) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putBoolean("isFallbackOrRecover", isFallbackOrRecover);
				sendEvent(reactContext, AGLocalPublishFallbackToAudioOnly, map);
			}
		});
	}

	@Override
	public void onRemoteSubscribeFallbackToAudioOnly(final int uid, final boolean isFallbackOrRecover) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putBoolean("isFallbackOrRecover", isFallbackOrRecover);
				map.putInt("uid", uid);
				sendEvent(reactContext, AGRemoteSubscribeFallbackToAudioOnly, map);
			}
		});
	}

	@Override
	public void onAudioRouteChanged(final int routing) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putInt("routing", routing);
				sendEvent(reactContext, AGAudioRouteChanged, map);
			}
		});
	}

	@Override
	public void onCameraFocusAreaChanged(final Rect rect) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap rectMap = Arguments.createMap();
				rectMap.putInt("top", rect.top);
				rectMap.putInt("right", rect.right);
				rectMap.putInt("bottom", rect.bottom);
				rectMap.putInt("left", rect.left);
				WritableMap map = Arguments.createMap();
				map.putMap("rect", rectMap);
				sendEvent(reactContext, AGCameraFocusAreaChanged, map);
			}
		});
	}

	@Override
	public void onCameraExposureAreaChanged(final Rect rect) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap rectMap = Arguments.createMap();
				rectMap.putInt("top", rect.top);
				rectMap.putInt("right", rect.right);
				rectMap.putInt("bottom", rect.bottom);
				rectMap.putInt("left", rect.left);
				WritableMap map = Arguments.createMap();
				map.putMap("rect", rectMap);
				sendEvent(reactContext, AGCameraExposureAreaChanged, map);
			}
		});
	}

	@Override
	public void onRemoteAudioStats(final RemoteAudioStats stats) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap statsMap = Arguments.createMap();
				statsMap.putInt("uid", stats.uid);
				statsMap.putInt("quality", stats.quality);
				statsMap.putInt("networkTransportDelay", stats.networkTransportDelay);
				statsMap.putInt("jitterBufferDelay", stats.jitterBufferDelay);
				statsMap.putInt("audioLossRate", stats.audioLossRate);
				statsMap.putInt("totalFrozenTime", stats.totalFrozenTime);
				statsMap.putInt("frozenRate", stats.frozenRate);
				statsMap.putInt("numChannels", stats.numChannels);
				statsMap.putInt("receivedSampleRate", stats.receivedSampleRate);
				statsMap.putInt("receivedBitrate", stats.receivedBitrate);
				WritableMap map = Arguments.createMap();
				map.putMap("stats", statsMap);
				sendEvent(reactContext, AGRemoteAudioStats, map);
			}
		});
	}

	@Override
	public void onRtcStats(final RtcStats stats) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap statsMap = Arguments.createMap();
				statsMap.putInt("duration", stats.totalDuration);
				statsMap.putInt("txBytes", stats.txBytes);
				statsMap.putInt("rxBytes", stats.rxBytes);
				statsMap.putInt("txAudioBytes", stats.txAudioBytes);
				statsMap.putInt("txVideoBytes", stats.txVideoBytes);
				statsMap.putInt("rxAudioBytes", stats.rxAudioBytes);
				statsMap.putInt("rxVideoBytes", stats.rxVideoBytes);
				statsMap.putInt("txKBitRate", stats.txKBitRate);
				statsMap.putInt("rxKBitRate", stats.rxKBitRate);
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
				map.putMap("stats", statsMap);
				sendEvent(reactContext, AGRtcStats, map);
			}
		});
	}

	@Override
	public void onLastmileQuality(final int quality) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putInt("quality", quality);
				sendEvent(reactContext, AGLastmileQuality, map);
			}
		});
	}

	@Override
	public void onNetworkQuality(final int uid, final int txQuality, final int rxQuality) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putInt("uid", uid);
				map.putInt("txQuality", txQuality);
				map.putInt("rxQuality", rxQuality);
				sendEvent(reactContext, AGNetworkQuality, map);
			}
		});
	}


	@Override
	public void onLocalVideoStats(final LocalVideoStats stats) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap statsMap = Arguments.createMap();
				statsMap.putInt("sentBitrate", stats.sentBitrate);
				statsMap.putInt("sentFrameRate", stats.sentFrameRate);
				statsMap.putInt("encoderOutputFrameRate", stats.encoderOutputFrameRate);
				statsMap.putInt("rendererOutputFrameRate", stats.rendererOutputFrameRate);
				statsMap.putInt("targetBitrate", stats.targetBitrate);
				statsMap.putInt("targetFrameRate", stats.targetFrameRate);
				statsMap.putInt("qualityAdaptIndication", stats.qualityAdaptIndication);
				statsMap.putInt("encodedBitrate", stats.encodedBitrate);
				statsMap.putInt("encodedFrameWidth", stats.encodedFrameWidth);
				statsMap.putInt("encodedFrameHeight", stats.encodedFrameHeight);
				statsMap.putInt("encodedFrameCount", stats.encodedFrameCount);
				statsMap.putInt("codecType", stats.codecType);
				WritableMap map = Arguments.createMap();
				map.putMap("stats", statsMap);
				sendEvent(reactContext, AGLocalVideoStats, map);
			}
		});
	}

	@Override
	public void onRemoteVideoStats(final RemoteVideoStats stats) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap statsMap = Arguments.createMap();
				statsMap.putInt("uid", stats.uid);
				statsMap.putInt("width", stats.width);
				statsMap.putInt("height", stats.height);
				statsMap.putInt("receivedBitrate", stats.receivedBitrate);
				statsMap.putInt("decoderOutputFrameRate", stats.decoderOutputFrameRate);
				statsMap.putInt("rendererOutputFrameRate", stats.rendererOutputFrameRate);
				statsMap.putInt("packetLossRate", stats.packetLossRate);
				statsMap.putInt("rxStreamType", stats.rxStreamType);
				statsMap.putInt("totalFrozenTime", stats.totalFrozenTime);
				statsMap.putInt("frozenRate", stats.frozenRate);
				WritableMap map = Arguments.createMap();
				map.putMap("stats", statsMap);
				sendEvent(reactContext, AGRemoteVideoStats, map);
			}
		});
	}

	@Override
	public void onAudioMixingStateChanged(final int state, final int errorCode) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putInt("state", state);
				map.putInt("errorCode", errorCode);
				sendEvent(reactContext, AGAudioMixingStateChanged, map);
			}
		});
	}

	@Override
	public void onAudioEffectFinished(final int soundId) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putInt("soundid", soundId);
				sendEvent(reactContext, AGAudioEffectFinish, map);
			}
		});
	}

	@Override
	public void onStreamPublished(final String url, final int errorCode) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putString("url", url);
				map.putInt("errorCode", errorCode);
				sendEvent(reactContext, AGStreamPublished, map);
			}
		});
	}

	@Override
	public void onStreamUnpublished(final String url) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putString("url", url);
				sendEvent(reactContext, AGStreamUnpublish, map);
			}
		});
	}

	@Override
	public void onTranscodingUpdated() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putString("message", "AGTranscodingUpdate");
				sendEvent(reactContext, AGTranscodingUpdate, map);
			}
		});
	}

	@Override
	public void onStreamInjectedStatus(final String url, final int uid, final int status) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putInt("uid", uid);
				map.putString("url", url);
				map.putInt("status", status);
				sendEvent(reactContext, AGStreamInjectedStatus, map);
			}
		});
	}

	/**
	 * onStreamMessage
	 */
	@Override
	public void onStreamMessage(final int uid, final int streamId, final byte[] data) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				String msg = new String(data, Charset.forName("UTF-8"));
				WritableMap map = Arguments.createMap();
				map.putInt("uid", uid);
				map.putInt("streamId", streamId);
				map.putString("data", msg);
				sendEvent(reactContext, AGReceiveStreamMessage, map);
			}
		});
	}

	@Override
	public void onStreamMessageError(final int uid, final int streamId, final int error, final int missed, final int cached) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putInt("uid", uid);
				map.putInt("streamId", streamId);
				map.putInt("errorCode", error);
				map.putInt("missed", missed);
				map.putInt("cached", cached);
				sendEvent(reactContext, AGOccurStreamMessageError, map);
			}
		});
	}

	@Override
	public void onMediaEngineLoadSuccess() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putString("message", "MediaEngineLoaded");
				sendEvent(reactContext, AGMediaEngineLoaded, map);
			}
		});
	}

	@Override
	public void onMediaEngineStartCallSuccess() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putString("message", "MediaEngineStartCall");
				sendEvent(reactContext, AGMediaEngineStartCall, map);
			}
		});
	}

	@Override
	public void onLastmileProbeResult(LastmileProbeResult result) {
		super.onLastmileProbeResult(result);
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putString("message", "LastmileProbeTestResult");
				sendEvent(reactContext, AGLastmileProbeResult, map);
			}
		});
	}

	@Override
	public void onLocalVideoStateChanged(final int localVideoState, final int error) {
		super.onLocalVideoStateChanged(localVideoState, error);
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putString("message", "LocalVideoChanged");
				map.putInt("state", localVideoState);
				sendEvent(reactContext, AGLocalVideoChanged, map);
			}
		});
	}

	@Override
	public void onChannelMediaRelayEvent(final int code) {
		super.onChannelMediaRelayEvent(code);
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putInt("errorCode", code);
				sendEvent(reactContext, AGReceivedChannelMediaRelay, map);
			}
		});
	}

	@Override
	public void onChannelMediaRelayStateChanged(final int state, final int code) {
		super.onChannelMediaRelayStateChanged(state, code);
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putInt("state", state);
				map.putInt("errorCode", code);
				sendEvent(reactContext, AGMediaRelayStateChanged, map);
			}
		});
	}

	@Override
	public void onLocalAudioStats(final LocalAudioStats rtcStats) {
		super.onLocalAudioStats(rtcStats);
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putInt("numChannels", rtcStats.numChannels);
				map.putInt("sentSampleRate", rtcStats.sentSampleRate);
				map.putInt("sentBitrate", rtcStats.sentBitrate);
				sendEvent(reactContext, AGLocalAudioStats, map);
			}
		});
	}
}
