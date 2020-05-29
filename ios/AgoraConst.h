//
//  MyAgoraRtcEngineKit.h
//  RCTAgora
//
//  Created by 邓博 on 2017/6/30.
//  Copyright © 2017年 Syan. All rights reserved.
//

#import <AgoraRtcKit/AgoraRtcEngineKit.h>

typedef NS_ENUM(NSUInteger, AgoraRtcQualityReportFormat) {
    AgoraRtc_QualityReportFormat_Json = 0,
    AgoraRtc_QualityReportFormat_Html = 1,
};

typedef NS_ENUM(NSUInteger, AgoraRtcAppType) {
    AgoraRtc_APP_TYPE_NATIVE = 0,
    AgoraRtc_APP_TYPE_COCOS = 1,
    AgoraRtc_APP_TYPE_UNITY = 2,
    AgoraRtc_APP_TYPE_ELECTRON = 3,
    AgoraRtc_APP_TYPE_FLUTTER = 4,
    AgoraRtc_APP_TYPE_UNREAL = 5,
    AgoraRtc_APP_TYPE_XAMARIN = 6,
    AgoraRtc_APP_TYPE_APICLOUD = 7,
    AgoraRtc_APP_TYPE_REACTNATIVE = 8
};


@protocol AgoraRtcEngineExtensionDelegate <AgoraRtcEngineDelegate>
@optional
- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine audioTransportQualityOfUid:(NSUInteger)uid delay:(NSUInteger)delay lost:(NSUInteger)lost;
- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine videoTransportQualityOfUid:(NSUInteger)uid delay:(NSUInteger)delay lost:(NSUInteger)lost;
@end


@interface AgoraRtcEngineKit (AgoraExtension)

+ (instancetype _Nonnull)sharedEngineWithAppId:(NSString * _Nonnull)appId
                             extensionDelegate:(id<AgoraRtcEngineExtensionDelegate> _Nullable)delegate;

/** Sets the profile to control the RTC engine.
 *
 *  @param profile SDK profile in JSON format.
 *  @param merge Whether to merge the profile data with the original value.
 */
- (int)setProfile:(NSString * _Nonnull)profile
            merge:(BOOL)merge;

/** Set wrapper frame type by language wrapper.
 *
 *  @param appType wrapper frame type.
 */
- (int)setAppType:(AgoraRtcAppType)appType;

/** END OF COMMON METHODS */

/** BEGIN OF AUDIO METHODS */


/**
 *  Enable recap
 *
 *  @param interval &le; 0: Disabled, > 0: Interval in ms.
 */
- (int)enableRecap:(NSInteger)interval;

/**
 *  Start playing recap conversation
 *
 */
- (int)playRecap;

- (int)enableAudioQualityIndication:(BOOL)enabled;
- (int)enableTransportQualityIndication:(BOOL)enabled;

- (int)setVideoProfileEx:(NSInteger)width
               andHeight:(NSInteger)height
            andFrameRate:(NSInteger)frameRate
              andBitrate:(NSInteger)andBitrate;

- (int)sendReportData:(NSData * _Nonnull)data
                 type:(NSInteger)type;
/** END OF AUDIO METHODS */

/** Queries internal states
 * @param parameters
 *     json string, array type
 * @return a json string
 */
- (NSString * _Nullable)getParameters:(NSString * _Nonnull)parameters;

/**
 *  Generates a URL linking to the call quality reports. @param channel      The channel name specified in the joinChannel method.
 *  @param listenerUid  The uid of the listener.
 *  @param speakerUid   The uid of the speaker.
 *  @param reportFormat The format of the report.
                        AgoraRtc_QualityReportFormat_Json (0): JSON.: Returns the quality report data in Json.
                        AgoraRtc_QualityReportFormat_Html (1): HTML.: Returns a report in HTML format, displayed on a web browser or WebVIEW components.
 *
 *  @return 0 when executed successfully. return minus value when failed. return AgoraRtc_Error_Invalid_Argument (-2)：Invalid argument. return AgoraRtc_Error_Buffer_Too_Small (-6)：The buffer length is too small.
 */
- (NSString * _Nullable)makeQualityReportUrl:(NSString * _Nonnull) channel
                                 listenerUid:(NSUInteger)listenerUid
                                 speakerrUid:(NSUInteger)speakerUid
                                reportFormat:(AgoraRtcQualityReportFormat)reportFormat;

/*********************************************************
 * Large group conference call (experiment) - END
 *********************************************************/
@end

static NSString * _Nullable AG_PREFIX = @"ag_rtc";

static NSString * _Nullable RCTAgoraErrorDomain = @"RCTAgoraErrorDomain";

static NSString * _Nullable AGWarning = @"warning";
static NSString * _Nullable AGError = @"error";
static NSString * _Nullable AGApiCallExecute = @"apiCallExecute";
static NSString * _Nullable AGJoinChannelSuccess = @"joinChannelSuccess";
static NSString * _Nullable AGRejoinChannelSuccess = @"rejoinChannelSuccess";
static NSString * _Nullable AGLeaveChannel = @"leaveChannel";
static NSString * _Nullable AGClientRoleChanged = @"clientRoleChanged";
static NSString * _Nullable AGLocalUserRegistered = @"localUserRegistered";
static NSString * _Nullable AGUserInfoUpdated = @"userInfoUpdated";
static NSString * _Nullable AGUserJoined = @"userJoined";
static NSString * _Nullable AGUserOffline = @"userOffline";
static NSString * _Nullable AGConnectionStateChanged = @"connectionStateChanged";
static NSString * _Nullable AGConnectionLost = @"connectionLost";
static NSString * _Nullable AGTokenPrivilegeWillExpire = @"tokenPrivilegeWillExpire";
static NSString * _Nullable AGRequestToken = @"requestToken";

static NSString * _Nullable AGLocalAudioStateChanged = @"localAudioStateChanged";
static NSString * _Nullable AGRemoteAudioStateChanged = @"remoteAudioStateChanged";
static NSString * _Nullable AGLocalAudioStats = @"localAudioStats";
static NSString * _Nullable AGAudioVolumeIndication = @"audioVolumeIndication";
static NSString * _Nullable AGActiveSpeaker = @"activeSpeaker";
static NSString * _Nullable AGFirstLocalAudioFrame = @"firstLocalAudioFrame";
static NSString * _Nullable AGFirstRemoteAudioFrame = @"firstRemoteAudioFrame";
static NSString * _Nullable AGFirstRemoteAudioDecoded = @"firstRemoteAudioDecoded";
static NSString * _Nullable AGFirstLocalVideoFrame = @"firstLocalVideoFrame";
static NSString * _Nullable AGFirstRemoteVideoFrame = @"firstRemoteVideoFrame";
static NSString * _Nullable AGFirstRemoteVideoDecoded = @"firstRemoteVideoDecoded";
static NSString * _Nullable AGUserMuteAudio = @"userMuteAudio";
static NSString * _Nullable AGVideoSizeChanged = @"videoSizeChanged";
static NSString * _Nullable AGRemoteVideoStateChanged = @"remoteVideoStateChanged";
static NSString * _Nullable AGLocalPublishFallbackToAudioOnly = @"localPublishFallbackToAudioOnly";
static NSString * _Nullable AGRemoteSubscribeFallbackToAudioOnly = @"remoteSubscribeFallbackToAudioOnly";

static NSString * _Nullable AGAudioRouteChanged = @"audioRouteChanged";
static NSString * _Nullable AGCameraFocusAreaChanged = @"cameraFocusAreaChanged";
static NSString * _Nullable AGCameraExposureAreaChanged = @"cameraExposureAreaChanged";

static NSString * _Nullable AGRtcStats = @"rtcStats";
static NSString * _Nullable AGLastmileQuality = @"lastmileQuality";
static NSString * _Nullable AGNetworkQuality = @"networkQuality";
static NSString * _Nullable AGLocalVideoStats = @"localVideoStats";
static NSString * _Nullable AGRemoteVideoStats = @"remoteVideoStats";
static NSString * _Nullable AGRemoteAudioStats = @"remoteAudioStats";

static NSString * _Nullable AGRemoteAudioMixingStart = @"remoteAudioMixingStart";
static NSString * _Nullable AGRemoteAudioMixingFinish = @"remoteAudioMixingFinish";
static NSString * _Nullable AGAudioEffectFinish = @"audioEffectFinish";

static NSString * _Nullable AGStreamPublished = @"streamPublished";
static NSString * _Nullable AGStreamUnpublish = @"streamUnpublish";
static NSString * _Nullable AGTranscodingUpdate = @"transcodingUpdate";

static NSString * _Nullable AGStreamInjectedStatus = @"streamInjectedStatus";

static NSString * _Nullable AGReceiveStreamMessage = @"receiveStreamMessage";
static NSString * _Nullable AGOccurStreamMessageError = @"occurStreamMessageError";

static NSString * _Nullable AGReceivedChannelMediaRelay = @"receivedChannelMediaRelay";
static NSString * _Nullable AGMediaRelayStateChanged = @"mediaRelayStateChanged";

static NSString * _Nullable AGMediaEngineLoaded = @"mediaEngineLoaded";
static NSString * _Nullable AGMediaEngineStartCall = @"mediaEngineStartCall";

static NSString * _Nullable AGIntervalTest = @"startEchoTestWithInterval";
static NSString * _Nullable AGAudioMixingStateChanged = @"audioMixingStateChanged";
static NSString * _Nullable AGLastmileProbeTestResult = @"lastmileProbeTestResult";

static NSString * _Nullable AGRtmpStreamingStateChanged = @"rtmpStreamingStateChanged";
static NSString * _Nullable AGLocalVideoChanged = @"localVideoChanged";
static NSString * _Nullable AGNetworkTypeChanged = @"networkTypeChanged";
static NSString * _Nullable AGMediaMetaDataReceived = @"mediaMetaDataReceived";

static NSString * _Nullable AGOnFacePositionChanged = @"onFacePositionChanged";

typedef NS_ENUM(NSInteger, AgoraModeType) {
  AgoraAudioMode,
  AgoraVideoMode
};

@interface AgoraConst : NSObject

@property (nonatomic, copy) NSString * _Nullable appid;

@property (nonatomic, assign) NSInteger localUid;

@property (strong, nonatomic) AgoraRtcEngineKit * _Nullable rtcEngine;

+ (instancetype _Nonnull )share;

+ (NSArray<NSString*> *_Nonnull) supportEvents;
@end
