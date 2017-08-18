package org.godotengine.godot;

import android.util.Log;
import com.godot.game.R;
import android.app.Activity;

import com.amazon.ags.api.AGResponseCallback;
import com.amazon.ags.api.AmazonGamesCallback;
import com.amazon.ags.api.AmazonGamesClient;
import com.amazon.ags.api.AmazonGamesFeature;
import com.amazon.ags.api.AmazonGamesStatus;
import com.amazon.ags.api.achievements.AchievementsClient;
import com.amazon.ags.api.achievements.UpdateProgressResponse;
import com.amazon.ags.api.AGResponseHandle;
import com.amazon.ags.api.leaderboards.LeaderboardsClient;
import com.amazon.ags.api.leaderboards.SubmitScoreResponse;

import java.util.EnumSet;

public class AmazonGameCircle extends Godot.SingletonBase{

    int deviceID;
    private Activity activity;

    AmazonGamesClient agsClient;
    final String TAG = "Godot: ";

    AmazonGamesCallback callback = new AmazonGamesCallback() {
        @Override
        public void onServiceNotReady(AmazonGamesStatus status) {
            GodotLib.calldeferred(deviceID, "sign_in_failed", new Object[]{});
    
            Log.i(TAG, "AmazonGameCircle: not ready " + String.valueOf(status));
        }
        @Override
        public void onServiceReady(AmazonGamesClient amazonGamesClient) {
            agsClient = amazonGamesClient;
            GodotLib.calldeferred(deviceID, "signed_in", new Object[]{});
            Log.i(TAG, "AmazonGameCircle: signed in");
        }
    };

    //list of features your game uses (in this example, achievements and leaderboards)
    EnumSet<AmazonGamesFeature> myGameFeatures = EnumSet.of(AmazonGamesFeature.Achievements, AmazonGamesFeature.Leaderboards);

    static public Godot.SingletonBase initialize(Activity p_activity) { return new AmazonGameCircle(p_activity);}

    public AmazonGameCircle(Activity p_activity) {

        this.activity = p_activity;

        registerClass("AmazonGameCircle", new String[]{
        "init",
        "increase_achievement",
        "show_achievements",
        "show_leaderboards",
        "submit_leaderboard",
        "release"});
	}


    public void init(int deviceId) {
        Log.i(TAG, "AmazonGameCircle: init");
        deviceID = deviceId;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                AmazonGamesClient.initialize(activity, callback, myGameFeatures);
                Log.i(TAG, "AmazonGameCircle: init run");
            }
        });
    }

    public void release() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (agsClient != null)
                {
                	agsClient.release();
                }

            }
        });
    }

    public void increase_achievement(final String achievementId,final float percent) {
	      if (agsClient == null){Log.i(TAG, "AmazonGameCircle: agsClient=null increase_achievement"); return;}
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                AchievementsClient acClient = agsClient.getAchievementsClient();

                AGResponseHandle<UpdateProgressResponse> handle = acClient.updateProgress(achievementId, percent);

                // Optional callback to receive notification of success/failure.
                handle.setCallback(new AGResponseCallback<UpdateProgressResponse>() {

                    @Override
                    public void onComplete(UpdateProgressResponse result) {
                        if (result.isError()) {
                            // Add optional error handling here.  Not strictly required
                            // since retries and on-device request caching are automatic.
                        }
                        else
                        {
                            // Continue game flow.
                        }
                    }
                });

            }
        });
    }

    public void show_achievements() {
	      if (agsClient == null){Log.i(TAG, "AmazonGameCircle: agsClient=null show_achievement"); return;}
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                AchievementsClient acClient = agsClient.getAchievementsClient();

                acClient.showAchievementsOverlay();

            }
        });
    }

    public void submit_leaderboard(final String leaderboardId,final float score) {
	      if (agsClient == null){Log.i(TAG, "AmazonGameCircle: agsClient=null submit_leaderboard"); return;}
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                LeaderboardsClient lbClient = agsClient.getLeaderboardsClient();
                AGResponseHandle<SubmitScoreResponse> handle = lbClient.submitScore(leaderboardId, (long)score);

                // Optional callback to receive notification of success/failure.
                handle.setCallback(new AGResponseCallback<SubmitScoreResponse>() {

                    @Override
                    public void onComplete(SubmitScoreResponse result)
                    {
                        if (result.isError())
                        {
                        }
                        else
                        {
                                // Continue game flow.
                        }
                    }
                });
            }
        });
    }

    public void show_leaderboards() 
    {
        if (agsClient == null){Log.i(TAG, "AmazonGameCircle: agsClient=null show_leaderboards"); return;}
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                LeaderboardsClient lbClient = agsClient.getLeaderboardsClient();

                lbClient.showLeaderboardsOverlay();

            }
        });
    }
}
