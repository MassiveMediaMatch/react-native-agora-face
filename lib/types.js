"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.AudioScenario = exports.AudioProfile = exports.ClientRole = exports.ChannelProfile = void 0;
// https://docs.agora.io/en/Video/API%20Reference/react_native/enums/channelprofile.html
var ChannelProfile;
(function (ChannelProfile) {
    ChannelProfile[ChannelProfile["COMMUNICATION"] = 0] = "COMMUNICATION";
    ChannelProfile[ChannelProfile["LIVE_BROACASTING"] = 1] = "LIVE_BROACASTING";
    ChannelProfile[ChannelProfile["GAME"] = 2] = "GAME";
})(ChannelProfile = exports.ChannelProfile || (exports.ChannelProfile = {}));
//
var ClientRole;
(function (ClientRole) {
    ClientRole[ClientRole["BROADCASTER"] = 1] = "BROADCASTER";
    ClientRole[ClientRole["AUDIENCE"] = 2] = "AUDIENCE";
})(ClientRole = exports.ClientRole || (exports.ClientRole = {}));
var AudioProfile;
(function (AudioProfile) {
    AudioProfile[AudioProfile["DEFAULT"] = 0] = "DEFAULT";
    AudioProfile[AudioProfile["SPEECH_STANDARD"] = 1] = "SPEECH_STANDARD";
    AudioProfile[AudioProfile["MUSIC_STANDARD"] = 2] = "MUSIC_STANDARD";
    AudioProfile[AudioProfile["MUSIC_STANDARD_STEREO"] = 3] = "MUSIC_STANDARD_STEREO";
    AudioProfile[AudioProfile["MUSIC_HIGH_QUALITY"] = 4] = "MUSIC_HIGH_QUALITY";
    AudioProfile[AudioProfile["MUSIC_HIGH_QUALITY_STEREO"] = 5] = "MUSIC_HIGH_QUALITY_STEREO";
})(AudioProfile = exports.AudioProfile || (exports.AudioProfile = {}));
var AudioScenario;
(function (AudioScenario) {
    AudioScenario[AudioScenario["DEFAULT"] = 0] = "DEFAULT";
    AudioScenario[AudioScenario["CHATROOM_ENTERTAINMENT"] = 1] = "CHATROOM_ENTERTAINMENT";
    AudioScenario[AudioScenario["EDUCATION"] = 2] = "EDUCATION";
    AudioScenario[AudioScenario["GAME_STREAMING"] = 3] = "GAME_STREAMING";
    AudioScenario[AudioScenario["SHOWROOM"] = 4] = "SHOWROOM";
    AudioScenario[AudioScenario["CHATROOM_GAMING"] = 5] = "CHATROOM_GAMING";
    AudioScenario[AudioScenario["IOT"] = 6] = "IOT";
    AudioScenario[AudioScenario["MEETING"] = 7] = "MEETING";
})(AudioScenario = exports.AudioScenario || (exports.AudioScenario = {}));
//# sourceMappingURL=types.js.map