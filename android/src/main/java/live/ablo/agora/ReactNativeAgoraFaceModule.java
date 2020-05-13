package live.ablo.agora;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;

import io.agora.rtc.RtcEngine;

public class ReactNativeAgoraFaceModule extends ReactContextBaseJavaModule {

	private final String TAG = ReactNativeAgoraFaceModule.class.getSimpleName();

	private final RtcEngineEventHandler engineEventHandler;

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
		resolvePromiseFromResolve(res, promise);
	}

	@ReactMethod
	public void getConnectionState(Promise promise) {
		int res = RtcEngineDelegate.INSTANCE.getConnectionState();
		WritableMap map = Arguments.createMap();
		map.putInt("state", res);
		promise.resolve(map);
	}

	@ReactMethod
	public void joinChannel(ReadableMap options, Promise promise) {
		int res = RtcEngineDelegate.INSTANCE.joinChannel(options);
		resolvePromiseFromResolve(res, promise);
	}

	@ReactMethod
	public void leaveChannel(Promise promise) {
		int res = RtcEngineDelegate.INSTANCE.leaveChannel();
		resolvePromiseFromResolve(res, promise);
	}

	@ReactMethod
	public void setLocalRenderMode(int mode, Promise promise) {
		int res = RtcEngineDelegate.INSTANCE.setLocalRenderMode(mode);
		resolvePromiseFromResolve(res, promise);
	}

	@ReactMethod
	public void setRemoteRenderMode(int uid, int mode, Promise promise) {
		int res = RtcEngineDelegate.INSTANCE.setRemoteRenderMode(uid, mode);
		resolvePromiseFromResolve(res, promise);
	}

	@ReactMethod
	public void destroy() {
		RtcEngine.destroy();
	}

	private void resolvePromiseFromResolve(int res, Promise promise) {
		if (res == 0) {
			WritableMap map = Arguments.createMap();
			map.putBoolean("success", true);
			map.putInt("value", res);
			promise.resolve(map);
		} else {
			promise.reject("-1", Integer.toString(res));
		}
	}

}
