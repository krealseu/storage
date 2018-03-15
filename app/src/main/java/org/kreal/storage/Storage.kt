package org.kreal.storage

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.storage.StorageManager
import android.support.v4.provider.DocumentFile
import java.io.File

/**
 * Created by lthee on 2018/3/13.
 */
class Storage(private val context: Context) {
    private val grantUris: ArrayList<Uri> = ArrayList()
    private val roots: HashMap<String, DocumentFile> = HashMap()
    private val internal = "/storage/emulated"
    var devs: ArrayList<String> = ArrayList()

    init {
        loadStorageDevs(context)
        loadGrantUri(context.contentResolver)
    }

    private fun loadStorageDevs(context: Context) {
        devs.clear()
        val sm: StorageManager = context.getSystemService(Context.STORAGE_SERVICE) as StorageManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sm.storageVolumes.forEach {
                if (it.uuid == null)
                    devs.add("sdcard")
                else devs.add(it.uuid)
            }
        } else {
            val file = File(internal)
            if (file.isDirectory)
                file.listFiles().forEach {
                    if (it.name == "0")
                        devs.add("sdcard")
                    else devs.add(it.name)
                }
        }
    }

    private fun loadGrantUri(contentResolver: ContentResolver) {
        grantUris.clear()
        roots.clear()
        roots["sdcard"] = DocumentFile.fromFile(File("/sdcard"))
        contentResolver.persistedUriPermissions.forEach {
            val path = it.uri.path
            if (path.startsWith("/tree/") and path.endsWith(":")) {
                grantUris.add(it.uri)
                val key = path.substring(6, path.length - 1)
                roots[key] = DocumentFile.fromTreeUri(context, it.uri)
            }
        }
    }

    fun getDocumentFile(path: String): DocumentFile? {
        if (!path.startsWith('/'))
            return null
        val secondPos = path.indexOf('/', 1)
        val key = if (secondPos == -1) {
            path.substring(1)
        } else {
            path.substring(1, secondPos)
        }
        val name = if (secondPos == -1) "" else path.substring(secondPos)
        if (roots.containsKey(key)) {
            val root = roots[key] ?: return null
            return findChild(root, name)
        }
        return null
    }

    fun reload() {
        loadStorageDevs(context)
        loadGrantUri(context.contentResolver)
    }

    fun isGrant(dev: String) = roots.containsKey(dev)

    private fun findChild(documentFile: DocumentFile, path: String): DocumentFile? {
        val names = path.split('/')
        var result: DocumentFile = documentFile
        names.forEach {
            if (it != "")
                result = result.findFile(it) ?: return null
        }
        return result
    }
}