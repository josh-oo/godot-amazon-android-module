def can_build(plat):
    return plat == 'android'

def configure(env):
	if env['platform'] == 'android':
		env.android_add_dependency("compile files('../../../modules/amazoniap/android/lib//in-app-purchasing-2.0.76.jar')")
		env.android_add_to_manifest("android/AndroidManifestChunk.xml")
		env.android_add_java_dir("android/src")
		env.disable_module()
