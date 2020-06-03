require 'json'

package = JSON.parse(File.read(File.join(__dir__, 'package.json')))

Pod::Spec.new do |s|
    s.name           = "react-native-agora-face"
    s.version        = package["version"]
    s.summary        = package["description"]
    s.homepage       = package['homepage']
    s.license        = package['license']
    s.authors        = package["authors"]
    s.platform       = :ios, "8.0"

    s.source         = { git: '' }
    s.source_files   = 'ios/*.{h,m}'

    s.dependency 'React'
    s.dependency "AgoraRtcEngine_iOS", "3.0.1"
end
