def can_build(plat):
    return plat == 'android'

def configure(env):
	if env['platform'] == 'android':
		env.android_add_dependency("compile files('../../../modules/amazongamecircle/android/lib//gamecirclesdk.jar')")
		env.android_add_dependency("compile files('../../../modules/amazongamecircle/android/lib//AmazonInsights-android-sdk-2.1.26.jar')")
		env.android_add_dependency("compile files('../../../modules/amazongamecircle/android/lib//login-with-amazon-sdk.jar')")
		env.android_add_to_manifest("android/AndroidManifestChunk.xml")
		env.android_add_to_permissions("android/AndroidPermissionsChunk.xml");
		env.android_add_java_dir("android/src")
		env.android_add_res_dir("android/res")
		env.disable_module()
