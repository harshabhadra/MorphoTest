package com.harshabhadra.morphotest

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var pManager: PackageManager

    companion object {
        private const val AUTHENTICATION_REQUEST = 111
    }

    val responseXml = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pManager = packageManager

        val isInstalled = isPackageInstalled("com.scl.rdservice", pManager)
        val button: Button = findViewById(R.id.button)
        button.setOnClickListener {
            if (isInstalled) {
                val intent: Intent = Intent("in.gov.uidai.rdservice.fp.CAPTURE")
                intent.setPackage("com.scl.rdservice")
                intent.putExtra("PID_OPTIONS", responseXml)
                startActivityForResult(intent, AUTHENTICATION_REQUEST)
            } else {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=com.scl.rdservice")
                    )
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AUTHENTICATION_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val b = data!!.extras
                if (b != null) {
                    val pidData =
                        b.getString("PID_DATA") // in this varaible you will get Pid data String dnc = b.getString("DNC", ""); // you will get value in this variable when your finger print device not connected
                    val dnr = b.getString(
                        "DNR",
                        ""
                    ) // you will get value in this variable when your finger print device not registered.
                    Toast.makeText(this, "Response: $pidData, $dnr", Toast.LENGTH_LONG).show()
                    Log.e("MainActivity", "Response is: $pidData, $dnr")
                }
            }
        }
    }

    private fun isPackageInstalled(
        packageName: String,
        packageManager: PackageManager
    ): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}
