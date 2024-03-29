//
//  RCTAgoraVideoView.m
//  RCTAgora
//
//  Created by 邓博 on 2017/6/30.
//  Copyright © 2017年 Syan. All rights reserved.
//

#import "RCTAgoraVideoView.h"
#import <React/RCTBridgeModule.h>

@implementation RCTAgoraVideoView

- (instancetype)init{
  
  if (self == [super init]) {
	  self.rtcEngine = [AgoraConst share].rtcEngine;
	  self.renderMode = AgoraVideoRenderModeHidden;
  }
  
  return self;
}

- (void)setRenderMode:(NSInteger)renderMode {
  _renderMode = renderMode;
}

- (void)setShowLocalVideo:(BOOL)showLocalVideo {
  _showLocalVideo = showLocalVideo;
  AgoraRtcVideoCanvas *canvas = [[AgoraRtcVideoCanvas alloc] init];
  if (_showLocalVideo) {
    canvas.uid = [AgoraConst share].localUid;
    canvas.view = self;
    canvas.renderMode = _renderMode;
    [_rtcEngine setupLocalVideo:canvas];
  }
}

-(void)setRemoteUid:(NSUInteger)remoteUid {
  _remoteUid = remoteUid;
  AgoraRtcVideoCanvas *canvas = [[AgoraRtcVideoCanvas alloc] init];
  if (_remoteUid != 0 && _channelId) {
    canvas.uid = _remoteUid;
	canvas.channelId = _channelId;
    canvas.view = self;
    canvas.renderMode = _renderMode;
    [_rtcEngine setupRemoteVideo:canvas];
    return;
  }
}

- (void)setChannelId:(NSString *)channelId {
	_channelId = channelId;
	AgoraRtcVideoCanvas *canvas = [[AgoraRtcVideoCanvas alloc] init];
	if (_channelId && _remoteUid != 0) {
	  canvas.uid = _remoteUid;
	  canvas.channelId = _channelId;
	  canvas.view = self;
	  canvas.renderMode = _renderMode;
	  [_rtcEngine setupRemoteVideo:canvas];
	  return;
	}
}

-(void) willMoveToSuperview:(UIView *)newSuperview {
  [super willMoveToSuperview:newSuperview];
  if (_remoteUid > 0) {
    [_rtcEngine setRemoteRenderMode:_remoteUid mode:_renderMode];
  } else {
    [_rtcEngine setLocalRenderMode:_renderMode];
  }
}

@end
