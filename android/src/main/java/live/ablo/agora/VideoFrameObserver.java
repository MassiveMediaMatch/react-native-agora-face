package live.ablo.agora;

import android.content.Context;
import android.graphics.Bitmap;

import live.ablo.agora.data.MediaDataVideoObserver;

public class VideoFrameObserver implements MediaDataVideoObserver {

	private final Context context;
	private boolean blur;

	public VideoFrameObserver(Context context) {
		this.context = context;
	}

	@Override
	public void onCaptureVideoFrame(byte[] data, int frameType, int width, int height, int bufferLength, int yStride, int uStride, int vStride, int rotation, long renderTimeMs) {
		if (blur) {
			Bitmap bmp = YUVUtils.blur(context, YUVUtils.i420ToBitmap(width, height, rotation, bufferLength, data, yStride, uStride, vStride), 20);
			System.arraycopy(YUVUtils.bitmapToI420(width, height, bmp), 0, data, 0, bufferLength);
		}
	}

	@Override
	public void onRenderVideoFrame(int uid, byte[] data, int frameType, int width, int height, int bufferLength, int yStride, int uStride, int vStride, int rotation, long renderTimeMs) {
		//Log.i("VideoFrameObserver", String.format("onRenderVideoFrame uid %d width %d height %d bufferLength %d", uid, width, height, bufferLength));
	}

	public void toggleBlurring(boolean enable) {
		blur = enable;
	}
}
