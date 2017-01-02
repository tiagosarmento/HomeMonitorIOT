/**
 * Author: Tiago Sarmento Santos
 * Github: https://github.com/tiagosarmento/HomeMonitorIOT
 *
 * Software License Agreement
 * The present software is open-source and it is owned by this project contributors. Feel free to
 * use it on your own and to improve it for your needs. You may not combine this software with
 * "viral" open-source software in order to form a larger program. This software is being done as
 * an hobby and a DIY project. It is provided as is and with all possible faults associated.
 * The software contributors shall not, under any circumstances, be liable for special, incidental
 * or consequential damages for any reason whatsoever.
 */
package com.tiasan.homemonitoriot;

import android.app.AlertDialog;
import android.content.Context;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ErrorHandler {

    // Set Global data
    private String  gTag     = "DBG - ErrorHandler";
    private Context gContext = null;

    /**
     * @func ErrorHandler Constructor
     * @param cContext
     */
    ErrorHandler(Context cContext) {
        Log.d(gTag, "Constructor");
        this.gContext = cContext;
    }

    void showErrorMsg(String sMsg) {
        Log.d(gTag, "showErrorMsg");
        AlertDialog.Builder adBuilder = new AlertDialog.Builder(this.gContext);

        // Create our AlerDialog contents as a Linear Layout to have a fancy format
        LinearLayout llinear = new LinearLayout(this.gContext);
        llinear.setOrientation(LinearLayout.VERTICAL);
        llinear.setPadding(10,10,10,10);

        // Generic failure message
        final TextView tvErrorMsg = new TextView(this.gContext);
        tvErrorMsg.setText("Error message:\n");
        tvErrorMsg.append("    " + sMsg);
        llinear.addView(tvErrorMsg);

        // Error Report
        final TextView tvErrorReport = new TextView(this.gContext);
        tvErrorReport.setText("\nPlease report error to:");
        llinear.addView(tvErrorReport);

        // Contact
        final TextView tvMsgContact = new TextView(this.gContext);
        final SpannableString ssGitHub = new SpannableString("tiagosarmentosantos@gmail.com");
        Linkify.addLinks(ssGitHub, Linkify.EMAIL_ADDRESSES);
        tvMsgContact.setText(ssGitHub);
        tvMsgContact.setMovementMethod(LinkMovementMethod.getInstance());
        llinear.addView(tvMsgContact);
        // Code page
        final TextView tvMsgGitRepo = new TextView(this.gContext);
        final SpannableString ssGmail = new SpannableString("https://github.com/tiagosarmento/HomeMonitorIOT");
        Linkify.addLinks(ssGmail, Linkify.WEB_URLS);
        tvMsgGitRepo.setText(ssGmail);
        tvMsgGitRepo.setMovementMethod(LinkMovementMethod.getInstance());
        llinear.addView(tvMsgGitRepo);

        // Add contents to AlertDialog builder
        adBuilder.setView(llinear);
        adBuilder.setTitle("Application Failed");
        adBuilder.setPositiveButton("Dismiss", null);

        // Show AlertDialog
        AlertDialog dialog = adBuilder.create();
        dialog.show();
    }
}
