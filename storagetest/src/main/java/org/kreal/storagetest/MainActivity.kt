package org.kreal.storagetest

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import org.kreal.storage.Storage
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    private lateinit var storage: Storage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i("asd", ":" + Environment.getExternalStorageState())
        try {
            val runTime = Runtime.getRuntime()
            val process = runTime.exec("mount")
            val bufferedReader = BufferedReader(InputStreamReader(process.inputStream))
            while (true) {
                val line = bufferedReader.readLine() ?: break
                if (line.contains("(fat|fuse|storage)".toRegex()) && !line.contains("(secure|asec|firmware|shell|obb|legacy|data)".toRegex())) {
                    val parts = line.split(' ')
                    if (parts.size >= 2) {
                        val path = parts[2]
                        val file = File(path)
                        if (file.exists() && file.isDirectory)
                            Log.i("asd", path)
                    }
                }
            }
        } catch (e: Exception) {

        }
        Storage(baseContext).getAvailableVolumes().forEach {
            Log.i("asd", it.toString())
        }
        val dd = Storage(baseContext).getDocumentFile("/9016-4EF8/Download") ?: return
        Log.i("asd", dd.name)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        contentResolver.takePersistableUriPermission(data.data, Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        storage.reload()
    }
}
