package live.ablo.agora;

import java.nio.ByteBuffer;

public interface IAudioFrameObserver {
	boolean onRecordFrame(ByteBuffer byteBuffer, int numOfSamples, int bytesPerSample, int channels, int samplesPerSec);

	boolean onPlaybackFrame(ByteBuffer byteBuffer, int numOfSamples, int bytesPerSample, int channels, int samplesPerSec);
}
