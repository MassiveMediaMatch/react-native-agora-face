package live.ablo.agora;

import android.graphics.Bitmap;

import live.ablo.agora.data.MediaDataVideoObserver;

public class VideoFrameObserver implements MediaDataVideoObserver {

	private boolean blur = false;

	@Override
	public void onCaptureVideoFrame(byte[] data, int frameType, int width, int height, int bufferLength, int yStride, int uStride, int vStride, int rotation, long renderTimeMs) {
		if (blur) {
			Bitmap bmp = YUVUtils.pixelate(YUVUtils.i420ToBitmap(width, height, rotation, bufferLength, data, yStride, uStride, vStride), 10);
			System.arraycopy(YUVUtils.bitmapToI420(width, height, bmp), 0, data, 0, bufferLength);
		}
	}

	@Override
	public void onRenderVideoFrame(int uid, byte[] data, int frameType, int width, int height, int bufferLength, int yStride, int uStride, int vStride, int rotation, long renderTimeMs) {
		if (blur) {
			Bitmap bmp = YUVUtils.pixelate(YUVUtils.i420ToBitmap(width, height, rotation, bufferLength, data, yStride, uStride, vStride), 10);
			System.arraycopy(YUVUtils.bitmapToI420(width, height, bmp), 0, data, 0, bufferLength);
		}
	}

	@Override
	public void onPreEncodeVideoFrame(byte[] buf, int videoFrameType, int width, int height, int bufferLength, int yStride, int uStride, int vStride, int rotation, long renderTimeMs) {

	}

	public void toggleBlurring(boolean enable) {
		blur = enable;
	}
}
