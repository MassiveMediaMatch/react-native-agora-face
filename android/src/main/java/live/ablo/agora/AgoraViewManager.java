package live.ablo.agora;

import android.view.SurfaceView;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

/**
 * Created by DB on 2017/6/23.
 */

public class AgoraViewManager extends SimpleViewManager<AgoraVideoView> {

	public static final String REACT_CLASS = "RCTAgoraVideoView";

	@Override
	public String getName() {
		return REACT_CLASS;
	}

	@Override
	protected AgoraVideoView createViewInstance(ThemedReactContext reactContext) {
		return new AgoraVideoView(reactContext);
	}

	@ReactProp(name = "mode")
	public void setRenderMode(final AgoraVideoView agoraVideoView, Integer renderMode) {
		agoraVideoView.setRenderMode(renderMode);
	}

	@ReactProp(name = "showLocalVideo")
	public void setShowLocalVideo(final AgoraVideoView agoraVideoView, boolean showLocalVideo) {
		agoraVideoView.setShowLocalVideo(showLocalVideo);
		if (showLocalVideo) {
			SurfaceView surfaceView = AgoraManager.getInstance().setupLocalVideo(agoraVideoView.getRenderMode());
			surfaceView.setZOrderMediaOverlay(agoraVideoView.getZOrderMediaOverlay());
			agoraVideoView.addView(surfaceView);
		}
	}

	@ReactProp(name = "zOrderMediaOverlay")
	public void setZOrderMediaOverlay(final AgoraVideoView agoraVideoView, boolean zOrderMediaOverlay) {
		agoraVideoView.setZOrderMediaOverlay(zOrderMediaOverlay);
		Integer remoteUid = agoraVideoView.getRemoteUid();
		if (remoteUid != null) {
			SurfaceView surfaceView = agoraVideoView.findViewById(remoteUid);
			if (surfaceView != null) {
				surfaceView.setZOrderMediaOverlay(zOrderMediaOverlay);
			}
		} else {
			SurfaceView surfaceView = agoraVideoView.findViewById(0);
			if (surfaceView != null) {
				surfaceView.setZOrderMediaOverlay(zOrderMediaOverlay);
			}
		}
	}

	@ReactProp(name = "remoteUid")
	public void setRemoteUid(final AgoraVideoView agoraVideoView, final int remoteUid) {
		agoraVideoView.setRemoteUid(remoteUid);
		if (remoteUid != 0) {
			SurfaceView surfaceView = AgoraManager.getInstance().setupRemoteVideo(remoteUid, agoraVideoView.getRenderMode());
			surfaceView.setZOrderMediaOverlay(agoraVideoView.getZOrderMediaOverlay());
			agoraVideoView.addView(surfaceView);
		}
	}

}
