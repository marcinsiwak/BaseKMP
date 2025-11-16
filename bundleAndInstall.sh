./gradlew clean bundleRelease

bundletool build-apks --connected-device \
--bundle=./composeApp/build/outputs/bundle/release/composeApp-release.aab \
--output=./composeApp-release.apks

bundletool install-apks --apks=./composeApp-release.apks
rm ./composeApp-release.apks

