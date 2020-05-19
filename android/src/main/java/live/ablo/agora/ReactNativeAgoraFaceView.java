package live.ablo.agora;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;

public class ReactNativeAgoraFaceView extends FrameLayout {
	private SurfaceView mLocalView;
	private SurfaceView mRemoteView;
	private int renderMode;
	private Integer remoteUid;

	public ReactNativeAgoraFaceView(@NonNull Context context) {
		super(context);
	}

	public ReactNativeAgoraFaceView(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public ReactNativeAgoraFaceView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public void setupLocalVideo() {
		RtcEngineDelegate.INSTANCE.enableVideo();
		mLocalView = RtcEngine.CreateRendererView(getContext());
		mLocalView.setZOrderMediaOverlay(true);
		addView(mLocalView);
		VideoCanvas localVideoCanvas = new VideoCanvas(mLocalView, VideoCanvas.RENDER_MODE_HIDDEN, 0);
		RtcEngineDelegate.INSTANCE.setupLocalVideo(localVideoCanvas);
	}

	private void setupRemoteVideo(int uid) {
		mRemoteView = RtcEngine.CreateRendererView(getContext());
		addView(mRemoteView);
		RtcEngineDelegate.INSTANCE.setupRemoteVideo(new VideoCanvas(mRemoteView, VideoCanvas.RENDER_MODE_HIDDEN, uid));
	}

	protected void onVisibilityChanged(View changedView, int visibility) {
		super.onVisibilityChanged(changedView, visibility);
		if (changedView == this) {
			if (null != remoteUid) {
				RtcEngineDelegate.INSTANCE.setRemoteRenderMode(remoteUid, renderMode);
			} else {
				RtcEngineDelegate.INSTANCE.setLocalRenderMode(renderMode);
			}
		}
	}

	public void setZOrderMediaOverlay(boolean zOrderMediaOverlay) {
		if (remoteUid != null) {
			if (mRemoteView != null) {
				mRemoteView.setZOrderMediaOverlay(zOrderMediaOverlay);
			}
		} else {
			if (mLocalView != null) {
				mLocalView.setZOrderMediaOverlay(zOrderMediaOverlay);
			}
		}
	}

	public int getRenderMode() {
		return renderMode;
	}

	public void setRenderMode(int renderMode) {
		this.renderMode = renderMode;
	}

	public int getRemoteUid() {
		return remoteUid;
	}

	public void setRemoteUid(int remoteUid) {
		this.remoteUid = remoteUid;
		if (remoteUid != 0) {
			setupRemoteVideo(remoteUid);
		}
	}
}
