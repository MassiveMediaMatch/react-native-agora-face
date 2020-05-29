package live.ablo.agora;

class VideoFrameProcessor {
	static {
		System.loadLibrary("apm-plugin-packet-processing");
	}

	final void registerProcessing() {
		doRegisterProcessing(true);
	}

	final void unregisterProcessing() {
		doRegisterProcessing(false);
	}

	final void toggleBlurring(boolean blur) {
		doBlurring(blur);
	}

	private native void doRegisterProcessing(boolean enable);

	private native void doBlurring(boolean enable);
}