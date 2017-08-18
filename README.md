# godot-amazon-android-module
Simple basic module to use AmazonGameCircle and Amazon IAP in Godot

This is an Amazon-SDK module for Godot Engine (https://github.com/okamstudio/godot).
Amazon SDK originally available on https://developer.amazon.com/services-and-apis

How to use
----------
Drop the "amazongamecircle" / "amazoniap" directory inside the "modules" directory on the Godot source. Recompile for your android. (Described in http://docs.godotengine.org/en/stable/development/compiling/compiling_for_android.html#building-the-export-templates)

Configuring your game
---------------------
To enable the module on Android, add the path to the module to the "modules" property on the [android] section of your engine.cfg file. It should look like this:

	[android]
	modules="org/godotengine/godot/AmazonGameCircle,org/godotengine/godot/AmazonIAP"
  
Then go to your Amazon-Developer Console, create a new SecurityProfile for your App and drop the generated api_key.txt in the root directory of your Godot project.
Go to "Export" and switch to the "Resources" Tab. Paste "**\*.txt**" in the first textfield to include .txt files to your exported .apk.


