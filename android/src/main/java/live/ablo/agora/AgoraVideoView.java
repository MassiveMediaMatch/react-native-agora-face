package live.ablo.agora;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;


/**
 * Created by DB on 2017/6/27.
 */

public class AgoraVideoView extends LinearLayout {
	private boolean showLocalVideo;
	private Integer renderMode = 1;
	private Integer remoteUid;
	private String channelId;
	private boolean zOrderMediaOverlay;
	private int localViewId = -1;
	private int remoteViewId = -1;

	public AgoraVideoView(Context context) {
		super(context);
	}

	public AgoraVideoView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AgoraVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public void setupRemoteView() {
		if (remoteUid != null && remoteUid != 0 && channelId != null) {
			SurfaceView surfaceView = AgoraManager.getInstance().setupRemoteVideo(remoteUid, channelId, getRenderMode(), getContext());
			surfaceView.setZOrderMediaOverlay(getZOrderMediaOverlay());
			surfaceView.setId(getRemoteViewId());
			addView(surfaceView);
		}
	}

	public void setupLocalView() {
		SurfaceView surfaceView = AgoraManager.getInstance().setupLocalVideo(getRenderMode(), getContext());
		surfaceView.setZOrderMediaOverlay(getZOrderMediaOverlay());
		surfaceView.setId(getLocalViewId());
		addView(surfaceView);
	}

	public boolean isShowLocalVideo() {
		return showLocalVideo;
	}

	public void setShowLocalVideo(boolean showLocalVideo) {
		this.showLocalVideo = showLocalVideo;
	}

	public Integer getRenderMode() {
		return renderMode;
	}

	public void setRenderMode(Integer renderMode) {
		this.renderMode = renderMode;

	}

	public Integer getRemoteUid() {
		return remoteUid;
	}

	public void setRemoteUid(Integer remoteUid) {
		this.remoteUid = remoteUid;
	}

	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public boolean getZOrderMediaOverlay() {
		return zOrderMediaOverlay;
	}

	public void setZOrderMediaOverlay(boolean zOrderMediaOverlay) {
		this.zOrderMediaOverlay = zOrderMediaOverlay;
		SurfaceView surfaceView;
		if (remoteUid != null) {
			surfaceView = findViewById(getRemoteViewId());
		} else {
			surfaceView = findViewById(getLocalViewId());
		}
		if (surfaceView != null) {
			surfaceView.setZOrderMediaOverlay(zOrderMediaOverlay);
		}
	}

	protected void onVisibilityChanged(View changedView, int visibility) {
		super.onVisibilityChanged(changedView, visibility);
		if (changedView == this) {
			if (null != remoteUid) {
				AgoraManager.getInstance().getEngine().setRemoteRenderMode(remoteUid, renderMode, io.agora.rtc.Constants.VIDEO_MIRROR_MODE_AUTO);
			} else {
				AgoraManager.getInstance().getEngine().setLocalRenderMode(renderMode, io.agora.rtc.Constants.VIDEO_MIRROR_MODE_AUTO);
			}
		}
	}

	public int getLocalViewId() {
		if (localViewId < 0) {
			localViewId = View.generateViewId();
		}
		return localViewId;
	}

	public int getRemoteViewId() {
		if (remoteViewId < 0) {
			remoteViewId = View.generateViewId();
		}
		return remoteViewId;
	}
}
