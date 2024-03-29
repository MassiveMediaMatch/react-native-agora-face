//
//  RCTAgoraVideoView.h
//  RCTAgora
//
//  Created by 邓博 on 2017/6/30.
//  Copyright © 2017年 Syan. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AgoraConst.h"

@interface RCTAgoraVideoView : UIView

@property (strong, nonatomic) AgoraRtcEngineKit *rtcEngine;
@property (nonatomic) BOOL showLocalVideo;
@property (nonatomic) NSUInteger remoteUid;
@property (nonatomic, strong) NSString *channelId;
@property (nonatomic) NSInteger renderMode;

@end
