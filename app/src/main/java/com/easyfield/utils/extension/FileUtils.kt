package com.lezasolutions.callenza.utils.extension

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import isDownloadsDocument
import isExternalStorageDocument
import isMediaDocument
import java.io.File

/**
 * Functions for dealing with files.
 */

@SuppressLint("Range")
fun File.toImageContentUri(context: Context): Uri? {
    val filePath = absolutePath
    val cursor = context.contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        arrayOf(MediaStore.Images.Media._ID),
        MediaStore.Images.Media.DATA + "=? ",
        arrayOf(filePath),
        null
    )
    return if (cursor != null && cursor.moveToFirst()) {
        val id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID))
        cursor.close()
        Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id)
    } else {
        if (exists()) {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.DATA, filePath)
            context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
            )
        } else {
            null
        }
    }
}

/**
 * Gets the actual path of the [Uri].
 */
fun Uri.getRealPath(context: Context): String? {

    // DocumentProvider
    if (DocumentsContract.isDocumentUri(context, this)) {
        // ExternalStorageProvider
        if (isExternalStorageDocument()) {
            val docId = DocumentsContract.getDocumentId(this)
            val split = docId.split(":".toRegex())
            val type = split[0]

            if ("primary".equals(type, ignoreCase = true)) {
                return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            }

            // TODO handle non-primary volumes
        } else if (isDownloadsDocument()) {

            val id = DocumentsContract.getDocumentId(this)
            val contentUri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"),
                java.lang.Long.valueOf(id)
            )

            return context.getDataColumn(contentUri, null, null)
        } else if (isMediaDocument()) {
            val docId = DocumentsContract.getDocumentId(this)
            val split = docId.split(":".toRegex())

            val contentUri: Uri? = when (split[0]) {
                "image" -> {
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                }
                "video" -> {
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                }
                "audio" -> {
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                else -> return null
            }

            val selection = "_id=?"
            val selectionArgs = arrayOf(split[1])

            return contentUri?.let { context.getDataColumn(it, selection, selectionArgs) }
        }// MediaProvider
        // DownloadsProvider
    } else if ("content".equals(scheme, ignoreCase = true)) {
        return context.getDataColumn(this, null, null)
    } else if ("file".equals(scheme, ignoreCase = true)) {
        return path
    }// File
    // MediaStore (and general)

    return null
}

/**
 * Get the value of the data column for this Uri. This is useful for
 * MediaStore Uris, and other file-based ContentProviders.

 * @param uri           The Uri to query.
 * *
 * @param selection     (Optional) Filter used in the query.
 * *
 * @param selectionArgs (Optional) Selection arguments used in the query.
 * *
 * @return The value of the _data column, which is typically a file path.
 */
fun Context.getDataColumn(uri: Uri, selection: String?, selectionArgs: Array<String>?): String? {

    var cursor: Cursor? = null
    val column = "_data"
    val projection = arrayOf(column)

    try {
        cursor = this.contentResolver.query(uri, projection, selection, selectionArgs, null)
        if (cursor != null && cursor.moveToFirst()) {
            val column_index = cursor.getColumnIndexOrThrow(column)
            return cursor.getString(column_index)
        }
    } finally {
        cursor?.close()
    }
    return null
}