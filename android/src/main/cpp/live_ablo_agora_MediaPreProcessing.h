/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class live_ablo_agora_MediaPreProcessing */

#ifndef _Included_live_ablo_agora_MediaPreProcessing
#define _Included_live_ablo_agora_MediaPreProcessing
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     live_ablo_agora_MediaPreProcessing
 * Method:    setCallback
 * Signature: (Lio/agora/advancedvideo/rawdata/MediaPreProcessing/ProgressCallback;)V
 */
JNIEXPORT void JNICALL Java_live_ablo_agora_data_MediaPreProcessing_setCallback
  (JNIEnv *, jclass, jobject);

/*
 * Class:     live_ablo_agora_MediaPreProcessing
 * Method:    setVideoCaptureByteBuffer
 * Signature: (Ljava/nio/ByteBuffer;)V
 */
JNIEXPORT void JNICALL Java_live_ablo_agora_data_MediaPreProcessing_setVideoCaptureByteBuffer
  (JNIEnv *, jclass, jobject);

/*
 * Class:     live_ablo_agora_MediaPreProcessing
 * Method:    setAudioRecordByteBuffer
 * Signature: (Ljava/nio/ByteBuffer;)V
 */
JNIEXPORT void JNICALL Java_live_ablo_agora_data_MediaPreProcessing_setAudioRecordByteBuffer
  (JNIEnv *, jclass, jobject);

/*
 * Class:     live_ablo_agora_MediaPreProcessing
 * Method:    setAudioPlayByteBuffer
 * Signature: (Ljava/nio/ByteBuffer;)V
 */
JNIEXPORT void JNICALL Java_live_ablo_agora_data_MediaPreProcessing_setAudioPlayByteBuffer
  (JNIEnv *, jclass, jobject);

/*
 * Class:     live_ablo_agora_MediaPreProcessing
 * Method:    setBeforeAudioMixByteBuffer
 * Signature: (Ljava/nio/ByteBuffer;)V
 */
JNIEXPORT void JNICALL Java_live_ablo_agora_data_MediaPreProcessing_setBeforeAudioMixByteBuffer
  (JNIEnv *, jclass, jobject);

/*
 * Class:     live_ablo_agora_MediaPreProcessing
 * Method:    setAudioMixByteBuffer
 * Signature: (Ljava/nio/ByteBuffer;)V
 */
JNIEXPORT void JNICALL Java_live_ablo_agora_data_MediaPreProcessing_setAudioMixByteBuffer
  (JNIEnv *, jclass, jobject);

/*
 * Class:     live_ablo_agora_MediaPreProcessing
 * Method:    setVideoDecodeByteBuffer
 * Signature: (ILjava/nio/ByteBuffer;)V
 */
JNIEXPORT void JNICALL Java_live_ablo_agora_data_MediaPreProcessing_setVideoDecodeByteBuffer
  (JNIEnv *, jclass, jint, jobject);

/*
 * Class:     live_ablo_agora_MediaPreProcessing
 * Method:    releasePoint
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_live_ablo_agora_data_MediaPreProcessing_releasePoint
  (JNIEnv *, jclass);

#ifdef __cplusplus
}
#endif
#endif
