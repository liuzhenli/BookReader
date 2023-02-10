package com.liuzhenli.common.utils

import android.content.res.AssetManager
import com.liuzhenli.common.exception.ApiCodeException
import android.annotation.TargetApi
import android.os.Build
import android.provider.DocumentsContract
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.webkit.MimeTypeMap
import com.liuzhenli.common.utils.FileUtils.Fileter
import com.liuzhenli.common.utils.L
import com.liuzhenli.common.utils.filepicker.util.FileUtils.listDirs
import com.liuzhenli.common.utils.filepicker.util.FileUtils.listFiles
import com.orhanobut.logger.Logger
import org.json.JSONObject
import splitties.init.appCtx
import java.io.*
import java.lang.Exception
import java.lang.StringBuilder
import java.nio.charset.Charset
import java.text.DecimalFormat
import java.util.*

object FileUtils {
    private const val TAG = "FileUtils"
    private val filters = arrayOf(".txt", ".pdf", ".epub")
    private const val BUFFER = 1024 * 8


    fun createFileIfNotExist(root: File, vararg subDirFiles: String): File {
        val filePath = getPath(root, *subDirFiles)
        return createFileIfNotExist(filePath)
    }

    fun createFolderIfNotExist(root: File, vararg subDirs: String): File {
        val filePath = getPath(root, *subDirs)
        return createFolderIfNotExist(filePath)
    }

    fun createFolderIfNotExist(filePath: String): File {
        val file = File(filePath)
        //如果文件夹不存在，就创建它
        if (!file.exists()) {
            file.mkdirs()
        }
        return file
    }

    @Synchronized
    fun createFileIfNotExist(filePath: String): File {
        val file = File(filePath)
        try {
            if (!file.exists()) {
                //创建父类文件夹
                file.parent?.let {
                    createFolderIfNotExist(it)
                }
                //创建文件
                file.createNewFile()
            }
        } catch (e: IOException) {
            e.printOnDebug()
        }
        return file
    }

    fun createFileWithReplace(filePath: String): File {
        val file = File(filePath)
        if (!file.exists()) {
            //创建父类文件夹
            file.parent?.let {
                createFolderIfNotExist(it)
            }
            //创建文件
            file.createNewFile()
        } else {
            file.delete()
            file.createNewFile()
        }
        return file
    }


    fun getFontDir(font: String): File? {
        val file = File(getFontPath(font))
        return if (file.exists()) {
            file
        } else null
    }

    fun getFontPath(font: String): String {
        return Constant.FONT_PATH + font
    }

    fun getTtsPath(filename: String): String {
        return Constant.TTS_PATH + filename
    }

    fun getImgPath(img: String): String {
        return Constant.IMG_PATH + img
    }

    fun getUpdatePath(fileName: String): String {
        return Constant.UPDATE_PATH + fileName
    }

    //复制文件
    @JvmStatic
    @Throws(IOException::class)
    fun copyFile(sourceFile: File?, targetFile: File?) {
        BufferedInputStream(FileInputStream(sourceFile)).use { inBuff ->
            BufferedOutputStream(
                FileOutputStream(targetFile)
            ).use { outBuff ->
                val buffer = ByteArray(BUFFER)
                var length: Int
                while (inBuff.read(buffer).also { length = it } != -1) {
                    outBuff.write(buffer, 0, length)
                }
                outBuff.flush()
            }
        }
    }

    @JvmStatic
    @Throws(IOException::class)
    fun copyFromAssets(assets: AssetManager, source: String?, dest: String, isCover: Boolean) {
        val file = File(dest)
        if (!file.parentFile.exists()) file.parentFile.mkdirs()
        if (isCover || !file.exists()) {
            var `is`: InputStream? = null
            var fos: FileOutputStream? = null
            try {
                `is` = assets.open(source!!)
                fos = FileOutputStream(dest)
                val buffer = ByteArray(1024)
                var size = 0
                while (`is`.read(buffer, 0, 1024).also { size = it } >= 0) {
                    fos.write(buffer, 0, size)
                }
            } finally {
                if (fos != null) {
                    try {
                        fos.close()
                    } finally {
                        `is`!!.close()
                    }
                }
            }
        }
    }

    /**
     * 读取表情配置文件
     */
    @JvmStatic
    fun getFacesFile(context: Context): List<String?>? {
        var br: BufferedReader? = null
        try {
            val list: MutableList<String?> = ArrayList()
            val `in` = context.resources.assets.open("faces")
            br = BufferedReader(
                InputStreamReader(
                    `in`,
                    "UTF-8"
                )
            )
            var str: String? = null
            while (br.readLine().also { str = it } != null) {
                list.add(str)
            }
            return list
        } catch (e: Throwable) {
            e.printStackTrace()
        } finally {
            assert(br != null)
            try {
                br!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return null
    }

    @JvmStatic
    fun read(file: File?): Array<File>? {
        // 是文件夹
        return if (file != null && file.isDirectory) {
            // 设置过滤条件
            file.listFiles(FileFilter { pathname -> // 是文件夹，true不过滤
                if (pathname.isDirectory) {
                    return@FileFilter true
                }
                var flag = false
                if (pathname.isFile) {
                    val fileName = pathname.name.lowercase(Locale.getDefault())
                    for (i in filters.indices) {
                        if (fileName.endsWith(filters[i])) {
                            flag = true
                            break
                        }
                    }
                }
                flag
            })
        } else null
    }

    fun readString(file: File, begin: Int, size: Int): String? {
        try {
            val dataInputStream = DataInputStream(FileInputStream(file))
            Logger.d("MyBook", "文件总长度：" + file.length())
            val out = ByteArrayOutputStream()
            // 从什么位置开始读
            dataInputStream.skip(begin.toLong())
            // 读多少数据
            val b = ByteArray(size)
            var i = -1
            if (dataInputStream.read(b, 0, b.size).also { i = it } != -1) {
                out.write(b, 0, i)
                // randomAccessFile.skipBytes(1024);
                val string = out.toString("UTF-8")
                Logger.d("MyBook", "begin：$begin")
                Logger.d("MyBook", "size：$size")
                // Log.d("MyBook", "读取的文本：" + string);
                return string
            }
            out.close()
            dataInputStream.close()
        } catch (e: Exception) {
            Logger.d("MyBook", e.message)
        }
        return null
    }

    fun readString0(file: File?, begin: Int, size: Int): String? {
        try {
            val randomAccessFile = RandomAccessFile(file, "r")
            Logger.d("MyBook", "文件总长度：" + randomAccessFile.length())
            val out = ByteArrayOutputStream()
            // 从什么位置开始读
            randomAccessFile.seek(begin.toLong())
            // 读多少数据
            val b = ByteArray(size)
            var i = -1
            if (randomAccessFile.read(b, 0, b.size).also { i = it } != -1) {
                out.write(b, 0, i)
                val string = out.toString("UTF-8")
                Logger.d("MyBook", "begin：$begin")
                Logger.d("MyBook", "size：$size")
                Logger.d("MyBook", "FilePointer：" + randomAccessFile.filePointer)
                // Log.d("MyBook", "读取的文本：" + string);
                return string
            }
            out.close()
            randomAccessFile.close()
        } catch (e: Exception) {
            Logger.d("MyBook", e.message)
        }
        return null
    }

    val logFile: File
        get() {
            val file = File(Constant.LOG_PATH + "log.txt")
            if (!file.exists()) {
                createFile(file)
            }
            return file
        }

    @JvmStatic
    val errorLogFile: File
        get() {
            val file = File(Constant.LOG_PATH + "errorlog.txt")
            if (!file.exists()) {
                createFile(file)
            }
            return file
        }

    fun getBookDir(bookId: String): File {
        return File(Constant.BASE_PATH + bookId)
    }

    /**
     * root cache path
     */
    @JvmStatic
    fun createRootPath(context: Context): String {
        var cacheRootPath = ""
        cacheRootPath = if (isSdCardAvailable) {
            // /sdcard/Android/data/<application package>/cache
            val externalCacheDir = context.externalCacheDir
            if (externalCacheDir == null) {
                context.cacheDir.path
            } else {
                externalCacheDir.path
            }
        } else {
            // /data/data/<application package>/cache
            context.cacheDir.path
        }
        return cacheRootPath
    }

    val isSdCardAvailable: Boolean
        get() = Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()

    /**
     * 创建WiFi传书目录
     * create download dir
     * /data/data/<application package>/Download
    </application> */
    @JvmStatic
    fun createDownloadBookPath(context: Context): String {
        var cacheRootPath = ""
        cacheRootPath = if (isSdCardAvailable) {
            val externalFileDir =
                context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            if (externalFileDir == null) {
                context.filesDir.path
            } else {
                externalFileDir.path
            }
        } else {
            context.filesDir.path
        }
        return cacheRootPath
    }

    fun createDocumentBookPath(context: Context): String {
        var cacheRootPath = ""
        cacheRootPath = if (isSdCardAvailable) {
            val externalFileDir =
                context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            if (externalFileDir == null) {
                context.filesDir.path
            } else {
                externalFileDir.path
            }
        } else {
            context.filesDir.path
        }
        return cacheRootPath
    }

    /**
     * 递归创建文件夹
     *
     * @return 创建失败返回""
     */
    @JvmStatic
    fun createDir(dirPath: String): String {
        try {
            val file = File(dirPath)
            if (file.parentFile.exists()) {
                file.mkdir()
                return file.absolutePath
            } else {
                createDir(file.parentFile.absolutePath)
                file.mkdir()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return dirPath
    }

    /**
     * 递归创建文件夹
     *
     * @return 创建失败返回""
     */
    fun createFile(file: File): String {
        try {
            if (file.parentFile.exists()) {
                file.createNewFile()
                return file.absolutePath
            } else {
                createDir(file.parentFile.absolutePath)
                file.createNewFile()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    @JvmStatic
    @Throws(IOException::class)
    fun createFile(fullPath: String?): File {
        val file = File(fullPath)
        file.createNewFile()
        return file
    }

    fun getImageCachePath(path: String): String {
        return createDir(path)
    }

    /**
     * 获取图片缓存目录
     *
     * @return 创建失败, 返回""
     */
    fun getImageCachePath(context: Context): String {
        return createDir(createRootPath(context) + File.separator + "img" + File.separator)
    }

    /**
     * 获取图片裁剪缓存目录
     *
     * @return 创建失败, 返回""
     */
    fun getImageCropCachePath(context: Context): String {
        return createDir(createRootPath(context) + File.separator + "imgCrop" + File.separator)
    }

    /**
     * 将内容写入文件
     *
     * @param filePath eg:/mnt/sdcard/demo.txt
     * @param content  内容
     */
    @Synchronized
    fun writeFile(filePath: String?, content: String, isAppend: Boolean) {
//        Logger.i("save:" + filePath);
        try {
            val fout = FileOutputStream(filePath, isAppend)
            val bytes = content.toByteArray()
            fout.write(bytes)
            fout.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun writeFromBuffer(fullPath: String, buffer: ByteArray?): File {
        val file: File
        var output: OutputStream? = null
        try {
            val path = fullPath.substring(0, fullPath.lastIndexOf('/'))
            createDir(path)
            deleteFile(fullPath)
            file = createFile(fullPath)
            output = FileOutputStream(file)
            output.write(buffer)
            output.flush()
        } catch (e: Exception) {
            e.printStackTrace()
            throw ApiCodeException(22212, "保存文件失败,请检查手机扩展卡是否可用")
        } finally {
            try {
                output?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return file
    }

    fun writeFile(filePathAndName: String?, fileContent: String?) {
        try {
            val outStream: OutputStream = FileOutputStream(filePathAndName)
            val out = OutputStreamWriter(outStream)
            out.write(fileContent)
            out.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * 写入文件
     *
     * @param in
     * @param file
     */
    @Throws(IOException::class)
    fun writeFile(`in`: InputStream, file: File?) {
        if (!file!!.parentFile.exists()) file.parentFile.mkdirs()
        if (file != null && file.exists()) file.delete()
        val out = FileOutputStream(file)
        val buffer = ByteArray(1024 * 128)
        var len = -1
        while (`in`.read(buffer).also { len = it } != -1) {
            out.write(buffer, 0, len)
        }
        out.flush()
        out.close()
        `in`.close()
    }

    /**
     * 打开Asset下的文件
     *
     * @param context
     * @param fileName
     * @return
     */
    fun openAssetFile(context: Context, fileName: String?): InputStream? {
        val am = context.assets
        var `is`: InputStream? = null
        try {
            `is` = am.open(fileName!!)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return `is`
    }

    /**
     * 获取Raw下的文件内容
     *
     * @param resId raw res id
     * @return 文件内容
     */
    fun getFileFromRaw(context: Context?, resId: Int): String? {
        if (context == null) {
            return null
        }
        var br: BufferedReader? = null
        val s = StringBuilder()
        return try {
            val `in` = InputStreamReader(context.resources.openRawResource(resId))
            br = BufferedReader(`in`)
            var line: String?
            while (br.readLine().also { line = it } != null) {
                s.append(line)
            }
            s.toString()
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } finally {
            try {
                br?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 文件拷贝
     *
     * @param src  源文件
     * @param desc 目的文件
     */
    fun fileChannelCopy(src: File?, desc: File?) {
        var fi: FileInputStream? = null
        var fo: FileOutputStream? = null
        try {
            fi = FileInputStream(src)
            fo = FileOutputStream(desc)
            val `in` = fi.channel //得到对应的文件通道
            val out = fo.channel //得到对应的文件通道
            `in`.transferTo(0, `in`.size(), out) //连接两个通道，并且从in通道读取，然后写入out通道
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                fo?.close()
                fi?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 转换文件大小
     *
     * @param fileLen 单位B
     * @return
     */
    @JvmStatic
    fun formatFileSizeToString(fileLen: Long): String {
        val df = DecimalFormat("#.00")
        var fileSizeString = ""
        fileSizeString = if (fileLen < 1024) {
            df.format(fileLen.toDouble()) + "B"
        } else if (fileLen < 1048576) {
            df.format(fileLen.toDouble() / 1024) + "K"
        } else if (fileLen < 1073741824) {
            df.format(fileLen.toDouble() / 1048576) + "M"
        } else {
            df.format(fileLen.toDouble() / 1073741824) + "G"
        }
        return fileSizeString
    }

    /**
     * 删除指定文件
     *
     * @param file
     */
    @Throws(IOException::class)
    fun deleteFile(file: File?): Boolean {
        return deleteFileOrDirectory(file)
    }

    @JvmStatic
    @Throws(IOException::class)
    fun deleteFile(filePath: String?): Boolean {
        return deleteFile(File(filePath))
    }

    /**
     * 删除指定文件，如果是文件夹，则递归删除
     *
     * @param file
     * @return
     * @throws IOException
     */
    @JvmStatic
    @Throws(IOException::class)
    fun deleteFileOrDirectory(file: File?): Boolean {
        try {
            if (file != null && file.isFile) {
                return file.delete()
            }
            if (file != null && file.isDirectory) {
                val childFiles = file.listFiles()
                // 删除空文件夹
                if (childFiles == null || childFiles.size == 0) {
                    return file.delete()
                }
                // 递归删除文件夹下的子文件
                for (i in childFiles.indices) {
                    deleteFileOrDirectory(childFiles[i])
                }
                return file.delete()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 查询某个文件下的子文件个数
     */
    fun getFileChildCount(file: File?): Int {
        return if (file != null && file.exists()) {
            file.list().size
        } else 0
    }

    /***
     * 获取文件扩展名
     *
     * @param filename 文件名
     * @return
     */
    fun getExtensionName(filename: String?): String? {
        if (filename != null && filename.length > 0) {
            val dot = filename.lastIndexOf('.')
            if (dot > -1 && dot < filename.length - 1) {
                return filename.substring(dot + 1)
            }
        }
        return filename
    }

    /**
     * 获取文件内容
     *
     * @param path
     * @return
     */
    fun getFileOutputString(path: String?): String? {
        try {
            val bufferedReader = BufferedReader(FileReader(path), 8192)
            val sb = StringBuilder()
            var line: String? = null
            while (bufferedReader.readLine().also { line = it } != null) {
                sb.append("\n").append(line)
            }
            bufferedReader.close()
            return sb.toString()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    fun getFromAssets(fileName: String?, context: Context): String? {
        try {
            val inputReader = InputStreamReader(
                context.resources.assets.open(
                    fileName!!
                )
            )
            val bufReader = BufferedReader(inputReader)
            var line: String? = ""
            var Result: String? = ""
            while (bufReader.readLine().also { line = it } != null) Result += line
            return Result
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getPath(rootPath: String, vararg subDirFiles: String): String {
        val path = StringBuilder(rootPath)
        subDirFiles.forEach {
            if (it.isNotEmpty()) {
                if (!path.endsWith(File.separator)) {
                    path.append(File.separator)
                }
                path.append(it)
            }
        }
        return path.toString()
    }

    fun getPath(root: File, vararg subDirFiles: String): String {
        val path = StringBuilder(root.absolutePath)
        subDirFiles.forEach {
            if (it.isNotEmpty()) {
                path.append(File.separator).append(it)
            }
        }
        return path.toString()
    }

    /**
     * 根据Uri获取真实的文件路径
     */
    @JvmStatic
    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun getPath(context: Context, uri: Uri): String? {
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val split = id.split(":").toTypedArray()
                val type = split[0]
                if ("raw".equals(
                        type,
                        ignoreCase = true
                    )
                ) { //处理某些机型（比如Goole Pixel ）ID是raw:/storage/emulated/0/Download/c20f8664da05ab6b4644913048ea8c83.mp4
                    return split[1]
                }
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
                )
                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(
                    split[1]
                )
                return getDataColumn(context, contentUri, selection, selectionArgs)
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {

            // Return the remote address
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
                context,
                uri,
                null,
                null
            )
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    fun getDataColumn(
        context: Context, uri: Uri?, selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        val column = "_data"
        val projection = arrayOf(
            column
        )
        try {
            context.contentResolver.query(
                uri!!, projection, selection, selectionArgs,
                null
            ).use { cursor ->
                if (cursor != null && cursor.moveToFirst()) {
                    val index = cursor.getColumnIndexOrThrow(column)
                    return cursor.getString(index)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

    @JvmStatic
    fun list(
        dir: File, nametxt: String, ext: String,
        type: String, fs: MutableList<File>
    ): List<File>? {
        listFile(dir, nametxt, type, ext, fs)
        val all = dir.listFiles()
        // 递归获得当前目录的所有子目录
        for (i in all.indices) {
            val d = all[i]
            if (d.isDirectory) {
                list(d, nametxt, ext, type, fs)
            }
        }
        return null
    }

    /**
     * @param dir     根目�?
     * @param nametxt 文件名中包含的关键字
     * @param type    文件夹的类型
     * @param ext     后缀�?
     * @param fs      返回的结�?
     * @return
     */
    private fun listFile(
        dir: File, nametxt: String, type: String,
        ext: String, fs: MutableList<File>
    ): List<File> {
        val all = dir.listFiles(Fileter(ext))
        for (i in all.indices) {
            val d = all[i]
            if (d.name.lowercase(Locale.getDefault())
                    .indexOf(nametxt.lowercase(Locale.getDefault())) >= 0
            ) {
                if (type == "1") {
                    fs.add(d)
                } else if (d.isDirectory && type == "2") {
                    fs.add(d)
                } else if (!d.isDirectory && type == "3") {
                    fs.add(d)
                }
            }
        }
        return fs
    }

    @JvmStatic
    fun getCapturePath(img: String): String {
        return Constant.CAPTURE_PATH + img
    }

    @JvmStatic
    val sdCardPath: String
        get() {
            var sdCardDirectory = Environment.getExternalStorageDirectory().absolutePath
            try {
                sdCardDirectory = File(sdCardDirectory).canonicalPath
            } catch (ioe: IOException) {
                L.e(TAG, "Could not get SD directory", ioe)
            }
            return sdCardDirectory
        }

    @JvmStatic
    fun readFileToBuffer(fullPath: String?, encoding: String?): String? {
        var reader: InputStreamReader? = null
        var bufferedReader: BufferedReader? = null
        try {
            val file = File(fullPath)
            val buffer = StringBuilder()
            var txt: String? = ""
            if (file.isFile && file.exists()) {
                reader = InputStreamReader(FileInputStream(file), encoding)
                bufferedReader = BufferedReader(reader)
                while (bufferedReader.readLine().also { txt = it } != null) {
                    buffer.append(txt)
                }
                return buffer.toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                reader?.close()
                bufferedReader?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return null
    }

    fun isContentFile(uri: Uri?): Boolean {
        return uri != null && TextUtils.equals(uri.scheme, "content")
    }

    fun isContentFile(path: String?): Boolean {
        return path != null && path.startsWith("content://")
    }

    fun isContentFile(file: File?): Boolean {
        return file != null && file.absolutePath.startsWith("content://")
    }

    internal class Fileter(private val ext: String) : FilenameFilter {
        override fun accept(dir: File, name: String): Boolean {
            return name.endsWith(ext)
        }
    }


    /**
     * 判断文件或目录是否存在
     */
    fun exist(path: String): Boolean {
        val file = File(path)
        return file.exists()
    }


    /**
     * 删除文件或目录
     */
    @JvmOverloads
    fun delete(path: String, deleteRootDir: Boolean = true): Boolean {
        val file = File(path)

        return if (file.exists()) {
            delete(file, deleteRootDir)
        } else false
    }


    /**
     * 删除文件或目录
     */
    @JvmOverloads
    fun delete(file: File, deleteRootDir: Boolean = false): Boolean {
        var result = false
        if (file.isFile) {
            //是文件
            result = deleteResolveEBUSY(file)
        } else {
            //是目录
            val files = file.listFiles() ?: return false
            if (files.isEmpty()) {
                result = deleteRootDir && deleteResolveEBUSY(file)
            } else {
                for (f in files) {
                    delete(f, deleteRootDir)
                    result = deleteResolveEBUSY(f)
                }
            }
            if (deleteRootDir) {
                result = deleteResolveEBUSY(file)
            }
        }
        return result
    }

    /**
     * bug: open failed: EBUSY (Device or resource busy)
     * fix: http://stackoverflow.com/questions/11539657/open-failed-ebusy-device-or-resource-busy
     */
    private fun deleteResolveEBUSY(file: File): Boolean {
        // Before you delete a Directory or File: rename it!
        val to = File(file.absolutePath + System.currentTimeMillis())

        file.renameTo(to)
        return to.delete()
    }

    /**
     * 获取文件或网址的名称（包括后缀）
     */
    fun getName(pathOrUrl: String?): String {
        if (pathOrUrl == null) {
            return ""
        }
        val pos = pathOrUrl.lastIndexOf('/')
        return if (0 <= pos) {
            pathOrUrl.substring(pos + 1)
        } else {
            System.currentTimeMillis().toString() + "." + getExtension(pathOrUrl)
        }
    }


    /**
     * 获取文件后缀,不包括“.”
     */
    fun getExtension(pathOrUrl: String): String {
        val dotPos = pathOrUrl.lastIndexOf('.')
        return if (0 <= dotPos) {
            pathOrUrl.substring(dotPos + 1)
        } else {
            "ext"
        }
    }


    /**
     * 移动文件或目录
     */
    fun move(src: String, tar: String): Boolean {
        return move(File(src), File(tar))
    }

    /**
     * 移动文件或目录
     */
    fun move(src: File, tar: File): Boolean {
        return rename(src, tar)
    }

    /**
     * 文件重命名
     */
    fun rename(oldPath: String, newPath: String): Boolean {
        return rename(File(oldPath), File(newPath))
    }

    /**
     * 文件重命名
     */
    fun rename(src: File, tar: File): Boolean {
        return src.renameTo(tar)
    }

    /**
     * 读取文本文件, 失败将返回空串
     */
    @JvmOverloads
    fun readText(filepath: String, charset: String = "utf-8"): String {
        try {
            val data = readBytes(filepath)
            if (data != null) {
                return String(data, Charset.forName(charset)).trim { it <= ' ' }
            }
        } catch (ignored: UnsupportedEncodingException) {
        }

        return ""
    }

    /**
     * 读取文件内容, 失败将返回空串
     */
    fun readBytes(filepath: String): ByteArray? {
        var fis: FileInputStream? = null
        try {
            fis = FileInputStream(filepath)
            val outputStream = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            while (true) {
                val len = fis.read(buffer, 0, buffer.size)
                if (len == -1) {
                    break
                } else {
                    outputStream.write(buffer, 0, len)
                }
            }
            val data = outputStream.toByteArray()
            outputStream.close()
            return data
        } catch (e: IOException) {
            return null
        } finally {
            closeSilently(fis)
        }
    }

    fun closeSilently(c: Closeable?) {
        if (c == null) {
            return
        }
        try {
            c.close()
        } catch (ignored: IOException) {
        }
    }

    fun getCachePath(): String {
        return appCtx.externalCache.absolutePath
    }

    /**
     * 获取文件名（不包括扩展名）
     */
    fun getNameExcludeExtension(path: String): String {
        return try {
            var fileName = File(path).name
            val lastIndexOf = fileName.lastIndexOf(".")
            if (lastIndexOf != -1) {
                fileName = fileName.substring(0, lastIndexOf)
            }
            fileName
        } catch (e: Exception) {
            ""
        }

    }

    /**
     * 列出指定目录下的所有子目录及所有文件
     */
    @JvmOverloads
    fun listDirsAndFiles(
        startDirPath: String,
        allowExtensions: Array<String>? = null
    ): Array<File>? {
        val dirs: Array<File>?
        val files: Array<File>? = if (allowExtensions == null) {
            listFiles(startDirPath)
        } else {
            listFiles(startDirPath, allowExtensions)
        }
        dirs = listDirs(startDirPath)
        if (files == null) {
            return null
        }
        return dirs + files
    }

    /**
     * 获取文件的MIME类型
     */
    fun getMimeType(pathOrUrl: String): String {
        val ext = getExtension(pathOrUrl)
        val map = MimeTypeMap.getSingleton()
        return map.getMimeTypeFromExtension(ext) ?: "*/*"
    }

    /**
     * 保存文本内容
     */
    @JvmOverloads
    fun writeText(filepath: String, content: String, charset: String = "utf-8"): Boolean {
        return try {
            writeBytes(filepath, content.toByteArray(charset(charset)))
        } catch (e: UnsupportedEncodingException) {
            false
        }

    }

    /**
     * 保存文件内容
     */
    fun writeBytes(filepath: String, data: ByteArray): Boolean {
        val file = File(filepath)
        var fos: FileOutputStream? = null
        return try {
            if (!file.exists()) {
                file.parentFile?.mkdirs()
                file.createNewFile()
            }
            fos = FileOutputStream(filepath)
            fos.write(data)
            true
        } catch (e: IOException) {
            false
        } finally {
            closeSilently(fos)
        }
    }
}