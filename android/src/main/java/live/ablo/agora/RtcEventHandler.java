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

import io.agora.rtc.IRtcEngineEventHandler;
import live.ablo.agora.data.MediaDataObserverPlugin;

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

public class RtcEventHandler extends IRtcEngineEventHandler {
	private MediaDataObserverPlugin plugin;
	private final ReactApplicationContext reactApplicationContext;

	RtcEventHandler(ReactApplicationContext reactApplicationContext) {
		this.reactApplicationContext = reactApplicationContext;
	}

	public void setMediaDataPlugin(MediaDataObserverPlugin plugin) {
		this.plugin = plugin;
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

	public void onFacePositionChanged(final int imageWidth, final int imageHeight, final IRtcEngineEventHandler.AgoraFacePositionInfo[] faces) {
		super.onFacePositionChanged(imageWidth, imageHeight, faces);
		FaceDetector.getInstance().faceDataChanged(faces);
		if (FaceDetector.getInstance().sendFaceDetectionDataEvents()) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					WritableMap map = Arguments.createMap();
					WritableArray list = Arguments.createArray();
					for (AgoraFacePositionInfo info : faces) {
						WritableMap face = Arguments.createMap();
						face.putInt("faceX", info.x);
						face.putInt("faceY", info.y);
						face.putInt("faceDistance", info.distance);
						face.putInt("faceHeight", info.height);
						face.putInt("faceWidth", info.width);
						face.putInt("width", imageWidth);
						face.putInt("height", imageHeight);
						list.pushMap(face);
					}
					map.putArray("faces", list);
					sendEvent(reactApplicationContext, AGonFacePositionChanged, map);
				}
			});
		}
	}

	@Override
	public void onWarning(final int code) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putString("message", "AgoraWarning");
				map.putInt("errorCode", code);
				sendEvent(reactApplicationContext, AGWarning, map);
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
				sendEvent(reactApplicationContext, AGError, map);
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
					sendEvent(reactApplicationContext, AGError, map);
				} else {
					sendEvent(reactApplicationContext, AGApiCallExecute, map);
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
				sendEvent(reactApplicationContext, AGJoinChannelSuccess, map);
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
				sendEvent(reactApplicationContext, AGRejoinChannelSuccess, map);
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
				map.putMap("stats", statsMap);
				sendEvent(reactApplicationContext, AGLeaveChannel, map);
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
				sendEvent(reactApplicationContext, AGClientRoleChanged, map);
			}
		});
	}

	@Override
	public void onUserJoined(final int uid, final int elapsed) {
		if (this.plugin != null) {
			this.plugin.addDecodeBuffer(uid);
		}
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putInt("uid", uid);
				map.putInt("elapsed", elapsed);
				sendEvent(reactApplicationContext, AGUserJoined, map);
			}
		});
	}

	@Override
	public void onUserOffline(final int uid, final int reason) {
		if (this.plugin != null) {
			this.plugin.removeDecodeBuffer(uid);
		}
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putInt("uid", uid);
				map.putInt("reason", reason);
				sendEvent(reactApplicationContext, AGUserOffline, map);
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
				sendEvent(reactApplicationContext, AGConnectionStateChanged, map);
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
				sendEvent(reactApplicationContext, AGConnectionLost, map);
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
				sendEvent(reactApplicationContext, AGTokenPrivilegeWillExpire, map);
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
				sendEvent(reactApplicationContext, AGRequestToken, map);
			}
		});
	}

	@Override
	public void onMicrophoneEnabled(final boolean enabled) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putBoolean("enabled", enabled);
				sendEvent(reactApplicationContext, AGMicrophoneEnabled, map);
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
					arr.pushMap(obj);
				}

				WritableMap map = Arguments.createMap();
				map.putArray("speakers", arr);
				map.putInt("totalVolume", totalVolume);
				sendEvent(reactApplicationContext, AGAudioVolumeIndication, map);
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
				sendEvent(reactApplicationContext, AGActiveSpeaker, map);
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
				sendEvent(reactApplicationContext, AGFirstLocalAudioFrame, map);
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
				sendEvent(reactApplicationContext, AGFirstRemoteAudioFrame, map);
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
				sendEvent(reactApplicationContext, AGFirstLocalVideoFrame, map);
			}
		});
	}

	/**
	 * onFirstRemoteVideoDecoded
	 */
	@Override
	public void onFirstRemoteVideoDecoded(final int uid, final int width, final int height, final int elapsed) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putInt("uid", uid);
				map.putInt("width", width);
				map.putInt("height", height);
				map.putInt("elapsed", elapsed);
				sendEvent(reactApplicationContext, AGFirstRemoteVideoDecoded, map);
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
				sendEvent(reactApplicationContext, AGFirstRemoteVideoFrame, map);
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
				sendEvent(reactApplicationContext, AGUserMuteAudio, map);
			}
		});
	}

	@Override
	public void onUserMuteVideo(final int uid, final boolean muted) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putBoolean("muted", muted);
				map.putInt("uid", uid);
				sendEvent(reactApplicationContext, AGUserMuteVideo, map);
			}
		});
	}

	@Override
	public void onUserEnableVideo(final int uid, final boolean enabled) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putBoolean("enabled", enabled);
				map.putInt("uid", uid);
				sendEvent(reactApplicationContext, AGUserEnableVideo, map);
			}
		});
	}

	@Override
	public void onUserEnableLocalVideo(final int uid, final boolean enabled) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap map = Arguments.createMap();
				map.putBoolean("enabled", enabled);
				map.putInt("uid", uid);
				sendEvent(reactApplicationContext, AGUserEnableLocalVideo, map);
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
				sendEvent(reactApplicationContext, AGVideoSizeChanged, map);
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
				sendEvent(reactApplicationContext, AGRtmpStreamingStateChanged, map);
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
				sendEvent(reactApplicationContext, AGNetworkTypeChanged, map);
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
				sendEvent(reactApplicationContext, AGFirstRemoteAudioDecoded, map);
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
				sendEvent(reactApplicationContext, AGLocalPublishFallbackToAudioOnly, map);
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
				sendEvent(reactApplicationContext, AGRemoteSubscribeFallbackToAudioOnly, map);
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
				sendEvent(reactApplicationContext, AGAudioRouteChanged, map);
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
				sendEvent(reactApplicationContext, AGCameraFocusAreaChanged, map);
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
				sendEvent(reactApplicationContext, AGCameraExposureAreaChanged, map);
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
				WritableMap map = Arguments.createMap();
				map.putMap("stats", statsMap);
				sendEvent(reactApplicationContext, AGRemoteAudioStats, map);
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
				sendEvent(reactApplicationContext, AGRtcStats, map);
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
				sendEvent(reactApplicationContext, AGLastmileQuality, map);
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
				sendEvent(reactApplicationContext, AGNetworkQuality, map);
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
				WritableMap map = Arguments.createMap();
				map.putMap("stats", statsMap);
				sendEvent(reactApplicationContext, AGLocalVideoStats, map);
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
				statsMap.putInt("rendererOutputFrameRate", stats.rendererOutputFrameRate);
				statsMap.putInt("rxStreamType", stats.rxStreamType);
				WritableMap map = Arguments.createMap();
				map.putMap("stats", statsMap);
				sendEvent(reactApplicationContext, AGRemoteVideoStats, map);
			}
		});
	}

	@Override
	public void onRemoteAudioTransportStats(final int uid,
											final int delay,
											final int lost,
											final int rxKBitRate) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap statsMap = Arguments.createMap();
				statsMap.putInt("uid", uid);
				statsMap.putInt("delay", delay);
				statsMap.putInt("lost", lost);
				statsMap.putInt("rxKBitRate", rxKBitRate);
				WritableMap map = Arguments.createMap();
				map.putMap("stats", statsMap);
				sendEvent(reactApplicationContext, AGAudioTransportStatsOfUid, map);
			}
		});
	}

	@Override
	public void onRemoteVideoTransportStats(final int uid,
											final int delay,
											final int lost,
											final int rxKBitRate) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WritableMap statsMap = Arguments.createMap();
				statsMap.putInt("uid", uid);
				statsMap.putInt("delay", delay);
				statsMap.putInt("lost", lost);
				statsMap.putInt("rxKBitRate", rxKBitRate);
				WritableMap map = Arguments.createMap();
				map.putMap("stats", statsMap);
				sendEvent(reactApplicationContext, AGVideoTransportStatsOfUid, map);
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
				sendEvent(reactApplicationContext, AGAudioMixingStateChanged, map);
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
				sendEvent(reactApplicationContext, AGAudioEffectFinish, map);
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
				map.putInt("code", errorCode);
				sendEvent(reactApplicationContext, AGStreamPublished, map);
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
				sendEvent(reactApplicationContext, AGStreamUnpublish, map);
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
				sendEvent(reactApplicationContext, AGTranscodingUpdate, map);
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
				sendEvent(reactApplicationContext, AGStreamInjectedStatus, map);
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
				String msg = new String(data, StandardCharsets.UTF_8);
				WritableMap map = Arguments.createMap();
				map.putInt("uid", uid);
				map.putInt("streamId", streamId);
				map.putString("data", msg);
				sendEvent(reactApplicationContext, AGReceiveStreamMessage, map);
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
				map.putInt("error", error);
				map.putInt("missed", missed);
				map.putInt("cached", cached);
				sendEvent(reactApplicationContext, AGOccurStreamMessageError, map);
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
				sendEvent(reactApplicationContext, AGMediaEngineLoaded, map);
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
				sendEvent(reactApplicationContext, AGMediaEngineStartCall, map);
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
				sendEvent(reactApplicationContext, AGLastmileProbeResult, map);
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
				sendEvent(reactApplicationContext, AGLocalVideoChanged, map);
			}
		});
	}
}
