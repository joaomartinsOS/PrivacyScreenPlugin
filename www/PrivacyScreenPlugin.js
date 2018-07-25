/**
 * cordova is available under the MIT License (2008).
 * See http://opensource.org/licenses/alphabetical for full text.
 *
 * Copyright (c) Matt Kane 2010
 * Copyright (c) 2011, IBM Corporation
 * Copyright (c) 2012-2017, Adobe Systems
 */


        var exec = cordova.require("cordova/exec");


        /**
         * Constructor.
         *
         * @returns {PrivacyScreenPlugin}
         */
        function PrivacyScreenPlugin() {

            
  }

/**
 * Read code from scanner.
 *
 * @param {Function} successCallback This function will recieve a result object: {
         *        text : '12345-mock',    // The code that was scanned.
         *        format : 'FORMAT_NAME', // Code format.
         *        cancelled : true/false, // Was canceled.
         *    }
 * @param {Function} errorCallback
 * @param config
 */
PrivacyScreenPlugin.prototype.changePrivacy = function (successCallback, errorCallback, config) {

            

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
                config
            );
        };

        

        var privacyScreenPlugin = new PrivacyScreenPlugin();
        module.exports = privacyScreenPlugin;