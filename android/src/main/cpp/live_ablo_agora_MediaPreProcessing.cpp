#include <jni.h>
#include <android/log.h>
#include <cstring>
#include "include/IAgoraRtcEngine.h"
#include "include/IAgoraMediaEngine.h"
#include <string.h>
#include "live_ablo_agora_MediaPreProcessing.h"
#include "include/VMUtil.h"

#include <map>

using namespace std;
// Global variables

jobject gCallBack = nullptr;
jclass gCallbackClass = nullptr;
// Method ID at the Java level
jmethodID captureVideoMethodId = nullptr;
jmethodID renderVideoMethodId = nullptr;
jmethodID preEncodeVideoMethodId = nullptr;
void *_javaDirectPlayBufferCapture = nullptr;
map<int, void *> decodeBufferMap;

static JavaVM *gJVM = nullptr;

// Implement the IVideoFrameObserver class and related callbacks
class AgoraVideoFrameObserver : public agora::media::IVideoFrameObserver {


public:
    AgoraVideoFrameObserver() {

    }

    ~AgoraVideoFrameObserver() {

    }

    // Get video frame data from the VideoFrame object, copy to the ByteBuffer, and call Java method via the method ID
    void getVideoFrame(VideoFrame &videoFrame, _jmethodID *jmethodID, void *_byteBufferObject,
                       unsigned int uid) {
        if (_byteBufferObject == nullptr) {
            return;
        }

        int width = videoFrame.width;
        int height = videoFrame.height;
        size_t widthAndHeight = (size_t) videoFrame.yStride * height;
        size_t length = widthAndHeight * 3 / 2;

        AttachThreadScoped ats(gJVM);
        JNIEnv *env = ats.env();

        memcpy(_byteBufferObject, videoFrame.yBuffer, widthAndHeight);
        memcpy((uint8_t *) _byteBufferObject + widthAndHeight, videoFrame.uBuffer,
               widthAndHeight / 4);
        memcpy((uint8_t *) _byteBufferObject + widthAndHeight * 5 / 4, videoFrame.vBuffer,
               widthAndHeight / 4);

        if (uid == 0) {
            env->CallVoidMethod(gCallBack, jmethodID, videoFrame.type, width, height, length,
                                videoFrame.yStride, videoFrame.uStride,
                                videoFrame.vStride, videoFrame.rotation,
                                videoFrame.renderTimeMs);
        } else {
            env->CallVoidMethod(gCallBack, jmethodID, uid, videoFrame.type, width, height,
                                length,
                                videoFrame.yStride, videoFrame.uStride,
                                videoFrame.vStride, videoFrame.rotation,
                                videoFrame.renderTimeMs);
        }
    }

    // Copy video frame data from ByteBuffer to the VideoFrame object
    void writebackVideoFrame(VideoFrame &videoFrame, void *byteBuffer) {
        if (byteBuffer == nullptr) {
            return;
        }

        int width = videoFrame.width;
        int height = videoFrame.height;
        size_t widthAndHeight = (size_t) videoFrame.yStride * height;

        memcpy(videoFrame.yBuffer, byteBuffer, widthAndHeight);
        memcpy(videoFrame.uBuffer, (uint8_t *) byteBuffer + widthAndHeight, widthAndHeight / 4);
        memcpy(videoFrame.vBuffer, (uint8_t *) byteBuffer + widthAndHeight * 5 / 4,
               widthAndHeight / 4);
    }

public:
    // Implement the onCaptureVideoFrame callback
    virtual bool onCaptureVideoFrame(VideoFrame &videoFrame) override {
        // Get captured video frames
        getVideoFrame(videoFrame, captureVideoMethodId, _javaDirectPlayBufferCapture, 0);
        __android_log_print(ANDROID_LOG_DEBUG, "AgoraVideoFrameObserver", "onCaptureVideoFrame");
        // Send the video frames back to the SDK
        writebackVideoFrame(videoFrame, _javaDirectPlayBufferCapture);
        return true;
    }

    // Implement the onRenderVideoFrame callback
    virtual bool onRenderVideoFrame(unsigned int uid, VideoFrame &videoFrame) override {
        __android_log_print(ANDROID_LOG_DEBUG, "AgoraVideoFrameObserver", "onRenderVideoFrame");
        map<int, void *>::iterator it_find;
        it_find = decodeBufferMap.find(uid);

        if (it_find != decodeBufferMap.end()) {
            if (it_find->second != nullptr) {
                // Get the video frame rendered by the SDK
                getVideoFrame(videoFrame, renderVideoMethodId, it_find->second, uid);
                // Send the video frames back to the SDK
                writebackVideoFrame(videoFrame, it_find->second);
            }
        }
        return true;
    }

    // Implement the onPreEncodeVideoFrame callback
    virtual bool onPreEncodeVideoFrame(VideoFrame &videoFrame) override {
        // Get the pre-encoded video frame
        getVideoFrame(videoFrame, preEncodeVideoMethodId, _javaDirectPlayBufferCapture, 0);
        __android_log_print(ANDROID_LOG_DEBUG, "AgoraVideoFrameObserver", "onPreEncodeVideoFrame");
        // Send the video frames back to the SDK
        writebackVideoFrame(videoFrame, _javaDirectPlayBufferCapture);
        return true;
    }

};


// AgoraVideoFrameObserver object
static AgoraVideoFrameObserver s_videoFrameObserver;
// rtcEngine object
static agora::rtc::IRtcEngine *rtcEngine = nullptr;
// Set up the C++ interface
#ifdef __cplusplus
extern "C" {
#endif


int __attribute__((visibility("default")))
loadAgoraRtcEnginePlugin(agora::rtc::IRtcEngine *engine) {
    __android_log_print(ANDROID_LOG_DEBUG, "agora-raw-data-plugin", "loadAgoraRtcEnginePlugin");
    rtcEngine = engine;
    return 0;
}

void __attribute__((visibility("default")))
unloadAgoraRtcEnginePlugin(agora::rtc::IRtcEngine *engine) {
    __android_log_print(ANDROID_LOG_DEBUG, "agora-raw-data-plugin", "unloadAgoraRtcEnginePlugin");

    rtcEngine = nullptr;
}


// For the Java interface file, use the JNI to export corresponding C++.
// The Java_live_ablo_agora_data_MediaPreProcessing_setCallback method corresponds to the setCallback method in the Java interface file.
JNIEXPORT void JNICALL Java_live_ablo_agora_data_MediaPreProcessing_setCallback
        (JNIEnv *env, jclass, jobject callback) {
    if (!rtcEngine) return;

    env->GetJavaVM(&gJVM);
    // Create an AutoPtr instance that uses the IMediaEngine class as the template
    agora::util::AutoPtr<agora::media::IMediaEngine> mediaEngine;
    // The AutoPtr instance calls the queryInterface method to get a pointer to the IMediaEngine instance from the IID.
    // The AutoPtr instance accesses the pointer to the IMediaEngine instance via the arrow operator and calls the registerVideoFrameObserver via the IMediaEngine instance.
    mediaEngine.queryInterface(rtcEngine, agora::INTERFACE_ID_TYPE::AGORA_IID_MEDIA_ENGINE);
    if (mediaEngine) {
        // Register the video frame observer
        int code = mediaEngine->registerVideoFrameObserver(&s_videoFrameObserver);

    }

    if (gCallBack == nullptr) {
        gCallBack = env->NewGlobalRef(callback);
        gCallbackClass = env->GetObjectClass(gCallBack);
        // Get the method ID of callback functions
        captureVideoMethodId = env->GetMethodID(gCallbackClass, "onCaptureVideoFrame",
                                                "(IIIIIIIIJ)V");
        renderVideoMethodId = env->GetMethodID(gCallbackClass, "onRenderVideoFrame",
                                               "(IIIIIIIIIJ)V");

        __android_log_print(ANDROID_LOG_DEBUG, "setCallback", "setCallback done successfully");
    }
}

// C++ implementation of the setVideoCaptureByteBuffer method in the Java interface file
JNIEXPORT void JNICALL
Java_live_ablo_agora_data_MediaPreProcessing_setVideoCaptureByteBuffer
        (JNIEnv *env, jclass, jobject bytebuffer) {
    _javaDirectPlayBufferCapture = env->GetDirectBufferAddress(bytebuffer);
}

// C++ implementation of the setVideoDecodeByteBuffer method in the Java interface file
JNIEXPORT void JNICALL
Java_live_ablo_agora_data_MediaPreProcessing_setVideoDecodeByteBuffer
        (JNIEnv *env, jclass, jint uid, jobject byteBuffer) {
    if (byteBuffer == nullptr) {
        decodeBufferMap.erase(uid);
    } else {
        void *_javaDirectDecodeBuffer = env->GetDirectBufferAddress(byteBuffer);
        decodeBufferMap.insert(make_pair(uid, _javaDirectDecodeBuffer));
        __android_log_print(ANDROID_LOG_DEBUG, "agora-raw-data-plugin",
                            "setVideoDecodeByteBuffer uid: %u, _javaDirectDecodeBuffer: %p",
                            uid, _javaDirectDecodeBuffer);
    }
}


#ifdef __cplusplus
}
#endif
