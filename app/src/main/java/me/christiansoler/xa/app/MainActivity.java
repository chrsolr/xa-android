package me.christiansoler.xa.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.Locale;

import me.christiansoler.xa.R;
import me.christiansoler.xa.misc.VolleySingleton;

public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkUpdate();
    }

    private void checkUpdate() {
        try {
            final int versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;

            RequestQueue mQueue = VolleySingleton.getRequestQueque();
            JsonObjectRequest mRequest = new JsonObjectRequest(Request.Method.GET, "http://www.christiansoler.me/api/android/xa/version", null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            try {
                                final int code = response.getInt("version_code");
                                final String url = response.getString("download_url");
                                final String version = response.getString("version_name");

                                if (code > versionCode) {
                                    alertUpdateAvailable(url, version);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });

            mRequest.setRetryPolicy(new DefaultRetryPolicy(
                    5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            mQueue.add(mRequest);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void alertUpdateAvailable(final String url, final String version) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);

        View layout = getLayoutInflater().inflate(R.layout.dialog_update_available, null, false);

        TextView mUpdateVersion = (TextView) layout.findViewById(R.id.tv_update_version);
        mUpdateVersion.setText(String.format(Locale.US, "Version %s is now available", version));

        builder.setView(layout);
        builder.setPositiveButton("Download",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(mIntent);
                dialog.dismiss();
            }
        });
    }
}
