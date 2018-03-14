package org.kreal.storagetest

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import org.kreal.storage.Storage

class MainActivity : AppCompatActivity() {

    private lateinit var storage: Storage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        storage = Storage(baseContext)
        val devs = storage.devs
        if (devs.size > 1)
            if (!storage.isGrant(devs[1])) {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
                startActivityForResult(intent, 22)
            }
        val documentFile = storage.getDocumentFile("/${devs[1]}/Download") ?: return
        Log.i("asd", documentFile.name)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        contentResolver.takePersistableUriPermission(data.data, Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        storage.reload()
    }
}
