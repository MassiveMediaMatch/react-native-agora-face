package live.ablo.agora;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

public class ReactNativeAgoraFaceViewManager extends SimpleViewManager<ReactNativeAgoraFaceView> {
	@Override
	public String getName() {
		return "ReactNativeAgoraFaceViewManager";
	}

	@Override
	protected ReactNativeAgoraFaceView createViewInstance(ThemedReactContext reactContext) {
		return new ReactNativeAgoraFaceView(reactContext);
	}

	@ReactProp(name = "mode")
	public void setRenderMode(final ReactNativeAgoraFaceView agoraVideoView, Integer renderMode) {
		agoraVideoView.setRenderMode(renderMode);
	}

	@ReactProp(name = "showLocalVideo")
	public void setShowLocalVideo(final ReactNativeAgoraFaceView agoraVideoView, boolean showLocalVideo) {
		agoraVideoView.setupLocalVideo();
	}

	@ReactProp(name = "zOrderMediaOverlay")
	public void setZOrderMediaOverlay(final ReactNativeAgoraFaceView agoraVideoView, boolean zOrderMediaOverlay) {
		agoraVideoView.setZOrderMediaOverlay(zOrderMediaOverlay);
	}

	@ReactProp(name = "remoteUid")
	public void setRemoteUid(final ReactNativeAgoraFaceView agoraVideoView, final int remoteUid) {
		agoraVideoView.setRemoteUid(remoteUid);
	}
}
