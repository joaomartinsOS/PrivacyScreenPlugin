/**
 * cordova is available under the MIT License (2008).
 * See http://opensource.org/licenses/alphabetical for full text.
 *
 * Copyright (c) Matt Kane 2010
 * Copyright (c) 2011, IBM Corporation
 * Copyright (c) 2012-2017, Adobe Systems
 */
        var exec = cordova.require("cordova/exec");
        function PrivacyScreenPlugin() {}

PrivacyScreenPlugin.prototype.changePrivacy = function (successCallback, errorCallback, IsToShowFlag) {
            console.log("PrivacyScreenPlugin: executing changePrivacy, trying to set flag as "+IsToShowFlag);
            if (errorCallback == null) {
                errorCallback = function () {
                };
            }

            if (typeof errorCallback != "function") {
                console.log("PrivacyScreenPlugin.changePrivacy failure: failure parameter not a function");
                return;
            }

            if (typeof successCallback != "function") {
                console.log("PrivacyScreenPlugin.changePrivacy failure: success callback parameter must be a function");
                return;
            }


            exec(
                function(result) {
                    successCallback(result);
                },
                function(error) {
                    errorCallback(error);
                },
                'PrivacyScreenPlugin',
                'changePrivacy',
                {"IsToShowFlag": IsToShowFlag}
            );
        };

PrivacyScreenPlugin.prototype.getPreferencesValue = function (successCallback, errorCallback) {
            console.log("PrivacyScreenPlugin: executing getPreferencesValue");
            if (errorCallback == null) {
                errorCallback = function () {
                };
            }

            if (typeof errorCallback != "function") {
                console.log("PrivacyScreenPlugin.getPreferencesValue failure: failure parameter not a function");
                return;
            }

            if (typeof successCallback != "function") {
                console.log("PrivacyScreenPlugin.getPreferencesValue failure: success callback parameter must be a function");
                return;
            }
            exec(
                function(result) {
                    successCallback(result);
                },
                function(error) {
                    errorCallback(error);
                },
                'PrivacyScreenPlugin',
                'getPreferencesValue',
                false
            );
        };

// Installation constructor that binds RemoveSecureFlag to window
PrivacyScreenPlugin.install = function() {
  if (!window.plugins) {
    window.plugins = {};
  }
  window.plugins.PrivacyScreenPlugin = new PrivacyScreenPlugin();
  return window.plugins.PrivacyScreenPlugin;
};
cordova.addConstructor(PrivacyScreenPlugin.install);
