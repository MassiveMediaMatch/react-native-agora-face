"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.EncryptionMode = void 0;
var EncryptionMode;
(function (EncryptionMode) {
    EncryptionMode[EncryptionMode["NONE"] = 0] = "NONE";
    EncryptionMode[EncryptionMode["AES_128_XTS"] = 1] = "AES_128_XTS";
    EncryptionMode[EncryptionMode["AES_128_ECB"] = 2] = "AES_128_ECB";
    EncryptionMode[EncryptionMode["AES_256_XTS"] = 3] = "AES_256_XTS";
    EncryptionMode[EncryptionMode["SM4_128_ECB"] = 4] = "SM4_128_ECB";
    EncryptionMode[EncryptionMode["MODE_END"] = 5] = "MODE_END";
})(EncryptionMode = exports.EncryptionMode || (exports.EncryptionMode = {}));
//# sourceMappingURL=types.js.map