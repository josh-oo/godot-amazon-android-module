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

![alt text](https://raw.githubusercontent.com/JoshNinetySeven/godot-amazon-android-module/master/documentation/screenshot_1.png)

API Reference
-------------

**AmazonGameCircle:**

The singleton "AmazonGameCircle" will be available on gdscript.

The following methods are available:

 	void init(int deviceId);
	void increase_achievement(String achievementId,float percent);
	void show_achievementst();
	void submit_leaderboard(String leaderboardId,float score);
	void show_leaderboards();
	void release();
	
The following snippet is necessary to use GameCircle:

	#_notification is called by godot every time, the window get or lose its focus.
	func _notification(what):
		if what == MainLoop.NOTIFICATION_WM_FOCUS_OUT:
			GameCircle.release()
		if what == MainLoop.NOTIFICATION_WM_FOCUS_IN:
			GameCircle.init(get_instance_ID())
Example:

```
func game_start():
	if(Globals.has_singleton("AmazonGameCircle")):
		GameCircle = Globals.get_singleton("AmazonGameCircle")
		GameCircle.init(get_instance_ID())

func post_score(amazonLeaderboardId,score):
	if(GameCircle != null):
		GameCircle.submit_leaderboard(amazonLeaderboardId,score)

func show_leaderboards():
	if(GameCircle != null):
		GameCircle.show_leaderboards()

func unlock_achievement(amazonAchievementId):
	if(GameCircle != null):
		GameCircle.increase_achievement(amazonAchievementId,100.0)

...

#Callbacks (defined in same script as the calling functions)

func signed_in():
	pass

func sign_in_failed():
	pass
```

**AmazonIAP**

The following methods are available:

 	void init(int deviceId);
	void consume_iap(String sku);

Example:

```
func game_start():
	if(Globals.has_singleton("AmazonIAP")):
		IAP = Globals.get_singleton("AmazonIAP")
		IAP.init(get_instance_ID())

func consume_item(sku):
	if(IAP != null):
		IAP.consume_iap(sku)

...

#Callbacks (defined in same script as the calling functions)

func purchase_success(par1,par2,sku):
	pass
```
