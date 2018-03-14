package org.kreal.storage

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val st = Storage(this)
        val devs = st.devs
        val documentFile = st.getDocumentFile("/${devs[0]}/Download") ?: return
        Log.i("asd", documentFile.name)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        contentResolver.takePersistableUriPermission(data.data, Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    }

}
