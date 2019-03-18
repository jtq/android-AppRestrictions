/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.apprestrictions;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.UserManager;
import android.widget.CheckBox;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

/**
 * This is the main user interface of the App Restrictions sample app.  It demonstrates the use
 * of the App Restriction feature, which is available on Android 4.3 and above tablet devices
 * with the multiuser feature.
 *
 * When launched under the primary User account, you can toggle between standard app restriction
 * types and custom.  When launched under a restricted profile, this activity displays app
 * restriction settings, if available.
 *
 * Follow these steps to exercise the feature:
 * 1. If this is the primary user, go to Settings > Users.
 * 2. Create a restricted profile, if one doesn't exist already.
 * 3. Open the profile settings, locate the sample app, and tap the app restriction settings
 *    icon. Configure app restrictions for the app.
 * 4. In the lock screen, switch to the user's restricted profile, launch this sample app,
 *    and see the configured app restrictions displayed.
 */
public class MainActivity extends Activity {
    private Bundle mRestrictionsBundle;

    // Checkbox to indicate whether custom or standard app restriction types are selected.
    private CheckBox mCustomConfig;

    public static final String CUSTOM_CONFIG_KEY = "custom_config";

    private TextView mJsonValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Sets up  user interface elements.
        setContentView(R.layout.main);

        mJsonValue = /*(TextView)*/ findViewById(R.id.restrictions_json_contents);
    }

    @Override
    protected void onResume() {
        super.onResume();

        JSONObject jsonObject;
        String stringifiedJson;

        // If app restrictions are set for this package, when launched from a restricted profile,
        // the settings are available in the returned Bundle as key/value pairs.
        mRestrictionsBundle =
                ((UserManager) getSystemService(Context.USER_SERVICE))
                        .getApplicationRestrictions(getPackageName());
        try {
            jsonObject = _convertBundleToJSON(mRestrictionsBundle);
            stringifiedJson = jsonObject.toString(2);
        }
        catch (JSONException e) {
            stringifiedJson = "# Application restrictions not found: " + e.toString();
        }

        mJsonValue.setText(stringifiedJson);

        /*if (mRestrictionsBundle == null) {
            mRestrictionsBundle = new Bundle();
        }*/

        // Reads and displays values from a boolean type restriction entry, if available.
        // An app can utilize these settings to restrict its content under a restricted profile.
        /*final String booleanRestrictionValue =
                mRestrictionsBundle.containsKey(GetRestrictionsReceiver.KEY_BOOLEAN) ?
                        mRestrictionsBundle.getBoolean(GetRestrictionsReceiver.KEY_BOOLEAN) + "":
                        getString(R.string.na);
        mBooleanEntryValue.setText(booleanRestrictionValue);*/
    }

    // Convert a Bundle into JSONObject
    private JSONObject _convertBundleToJSON(Bundle bundle) throws JSONException
    {
        JSONObject json = new JSONObject();

        if (bundle != null)
        {
            Set<String> keys = bundle.keySet();
            if (keys != null)
            {
                for (String key : keys)
                {
                    json.put(key, JSONObject.wrap(bundle.get(key)));
                }
            }
        }

        return json;
    }
}
