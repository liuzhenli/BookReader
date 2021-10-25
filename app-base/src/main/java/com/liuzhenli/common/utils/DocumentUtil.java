package com.liuzhenli.common.utils;

import static com.liuzhenli.common.utils.Constant.FileSuffix.EPUB;
import static com.liuzhenli.common.utils.Constant.FileSuffix.PDF;
import static com.liuzhenli.common.utils.Constant.FileSuffix.TXT;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.documentfile.provider.DocumentFile;

import com.liuzhenli.common.utils.filepicker.entity.FileItem;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by PureDark on 2016/9/24.
 */

@SuppressWarnings({"unused", "WeakerAccess"})
public class DocumentUtil {

    private static Pattern FilePattern = Pattern.compile("[\\\\/:*?\"<>|]");

    public static boolean isFileExist(Context context, String fileName, String rootPath, String... subDirs) {
        Uri rootUri;
        if (rootPath.startsWith("content")) {
            rootUri = Uri.parse(rootPath);
        } else {
            rootUri = Uri.parse(Uri.decode(rootPath));
        }
        return isFileExist(context, fileName, rootUri, subDirs);
    }

    public static boolean isFileExist(Context context, String fileName, Uri rootUri, String... subDirs) {
        DocumentFile root;
        if ("content".equals(rootUri.getScheme())) {
            root = DocumentFile.fromTreeUri(context, rootUri);
        } else {
            root = DocumentFile.fromFile(new File(rootUri.getPath()));
        }
        return isFileExist(fileName, root, subDirs);
    }

    public static boolean isFileExist(String fileName, DocumentFile root, String... subDirs) {
        DocumentFile parent = getDirDocument(root, subDirs);
        if (parent == null)
            return false;
        fileName = filenameFilter(Uri.decode(fileName));
        DocumentFile file = parent.findFile(fileName);
        if (file != null && file.exists())
            return true;
        return false;
    }

    public static DocumentFile createDirIfNotExist(Context context, String rootPath, String... subDirs) {
        Uri rootUri;
        if (rootPath.startsWith("content"))
            rootUri = Uri.parse(rootPath);
        else
            rootUri = Uri.parse(Uri.decode(rootPath));
        return createDirIfNotExist(context, rootUri, subDirs);
    }

    public static DocumentFile createDirIfNotExist(Context context, Uri rootUri, String... subDirs) {
        DocumentFile root;
        if ("content".equals(rootUri.getScheme()))
            root = DocumentFile.fromTreeUri(context, rootUri);
        else
            root = DocumentFile.fromFile(new File(rootUri.getPath()));
        return createDirIfNotExist(root, subDirs);
    }

    public static DocumentFile createDirIfNotExist(@NonNull DocumentFile root, String... subDirs) {
        DocumentFile parent = root;
        try {
            for (String subDir1 : subDirs) {
                String subDirName = filenameFilter(Uri.decode(subDir1));
                DocumentFile subDir = parent.findFile(subDirName);
                if (subDir == null) {
                    subDir = parent.createDirectory(subDirName);
                }
                parent = subDir;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return parent;
    }

    public static DocumentFile createFileIfNotExist(Context context, String fileName, String rootPath, String... subDirs) {
        Uri rootUri;
        if (rootPath.startsWith("content"))
            rootUri = Uri.parse(rootPath);
        else
            rootUri = Uri.parse(Uri.decode(rootPath));
        return createFileIfNotExist(context, "", fileName, rootUri, subDirs);
    }

    public static DocumentFile createFileIfNotExist(Context context, String fileName, Uri rootUri, String... subDirs) {
        return createFileIfNotExist(context, "", fileName, rootUri, subDirs);
    }

    public static DocumentFile createFileIfNotExist(Context context, String mimeType, String fileName, String rootPath, String... subDirs) {
        Uri rootUri;
        if (rootPath.startsWith("content"))
            rootUri = Uri.parse(rootPath);
        else
            rootUri = Uri.parse(Uri.decode(rootPath));
        return createFileIfNotExist(context, mimeType, fileName, rootUri, subDirs);
    }

    public static DocumentFile createFileIfNotExist(Context context, String mimeType, String fileName, Uri rootUri, String... subDirs) {
        DocumentFile parent = createDirIfNotExist(context, rootUri, subDirs);
        if (parent == null)
            return null;
        fileName = filenameFilter(Uri.decode(fileName));
        DocumentFile file = parent.findFile(fileName);
        if (file == null) {
            file = parent.createFile(mimeType, fileName);
        }
        return file;
    }

    public static boolean deleteFile(Context context, String fileName, String rootPath, String... subDirs) {
        Uri rootUri;
        if (rootPath.startsWith("content"))
            rootUri = Uri.parse(rootPath);
        else
            rootUri = Uri.parse(Uri.decode(rootPath));
        return deleteFile(context, fileName, rootUri, subDirs);
    }

    public static boolean deleteFile(Context context, String fileName, Uri rootUri, String... subDirs) {
        DocumentFile root;
        if ("content".equals(rootUri.getScheme()))
            root = DocumentFile.fromTreeUri(context, rootUri);
        else
            root = DocumentFile.fromFile(new File(rootUri.getPath()));
        return deleteFile(fileName, root, subDirs);
    }

    public static boolean deleteFile(String fileName, DocumentFile root, String... subDirs) {
        DocumentFile parent = getDirDocument(root, subDirs);
        if (parent == null)
            return false;
        fileName = filenameFilter(Uri.decode(fileName));
        DocumentFile file = parent.findFile(fileName);
        return file != null && file.exists() && file.delete();
    }

    public static boolean writeBytes(Context context, byte[] data, String fileName, String rootPath, String... subDirs) {
        DocumentFile parent = getDirDocument(context, rootPath, subDirs);
        if (parent == null)
            return false;
        DocumentFile file = parent.findFile(fileName);
        return writeBytes(context, data, file.getUri());
    }

    public static boolean writeBytes(Context context, byte[] data, String fileName, Uri rootUri, String... subDirs) {
        DocumentFile parent = getDirDocument(context, rootUri, subDirs);
        if (parent == null)
            return false;
        fileName = filenameFilter(Uri.decode(fileName));
        DocumentFile file = parent.findFile(fileName);
        return writeBytes(context, data, file.getUri());
    }

    public static boolean writeBytes(Context context, byte[] data, String fileName, DocumentFile root, String... subDirs) {
        DocumentFile parent = getDirDocument(root, subDirs);
        if (parent == null)
            return false;
        fileName = filenameFilter(Uri.decode(fileName));
        DocumentFile file = parent.findFile(fileName);
        return writeBytes(context, data, file.getUri());
    }

    public static boolean writeBytes(Context context, byte[] data, DocumentFile file) {
        return writeBytes(context, data, file.getUri());
    }

    public static boolean writeBytes(Context context, byte[] data, Uri fileUri) {
        try {
            OutputStream out = context.getContentResolver().openOutputStream(fileUri, "wt"); //Write file need open with truncate mode, the mode truncate file upon opening (to zero bytes)
            out.write(data);
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean writeFromInputStream(Context context, InputStream inStream, DocumentFile file) {
        return writeFromInputStream(context, inStream, file.getUri());
    }

    public static boolean writeFromInputStream(Context context, InputStream inStream, Uri fileUri) {
        try {
            OutputStream out = context.getContentResolver().openOutputStream(fileUri);
            int byteread;
            byte[] buffer = new byte[1024];
            while ((byteread = inStream.read(buffer)) > 0) {
                out.write(buffer, 0, byteread);
            }
            inStream.close();
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static byte[] readBytes(Context context, String fileName, String rootPath, String... subDirs) {
        DocumentFile parent = getDirDocument(context, rootPath, subDirs);
        if (parent == null)
            return null;
        DocumentFile file = parent.findFile(fileName);
        if (file == null)
            return null;
        return readBytes(context, file.getUri());
    }

    public static byte[] readBytes(Context context, String fileName, Uri rootUri, String... subDirs) {
        DocumentFile parent = getDirDocument(context, rootUri, subDirs);
        if (parent == null)
            return null;
        fileName = filenameFilter(Uri.decode(fileName));
        DocumentFile file = parent.findFile(fileName);
        if (file == null)
            return null;
        return readBytes(context, file.getUri());
    }

    public static byte[] readBytes(Context context, String fileName, DocumentFile root, String... subDirs) {
        DocumentFile parent = getDirDocument(root, subDirs);
        if (parent == null)
            return null;
        fileName = filenameFilter(Uri.decode(fileName));
        DocumentFile file = parent.findFile(fileName);
        if (file == null)
            return null;
        return readBytes(context, file.getUri());
    }

    public static byte[] readBytes(Context context, DocumentFile file) {
        if (file == null)
            return null;
        return readBytes(context, file.getUri());
    }

    public static byte[] readBytes(Context context, Uri fileUri) {
        try {
            InputStream fis = context.getContentResolver().openInputStream(fileUri);
            int len = fis.available();
            byte[] buffer = new byte[len];
            fis.read(buffer);
            fis.close();
            return buffer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static DocumentFile getDirDocument(Context context, String rootPath, String... subDirs) {
        Uri rootUri;
        if (rootPath.startsWith("content"))
            rootUri = Uri.parse(rootPath);
        else
            rootUri = Uri.parse(Uri.decode(rootPath));
        return getDirDocument(context, rootUri, subDirs);
    }

    public static DocumentFile getDirDocument(Context context, Uri rootUri, String... subDirs) {
        DocumentFile root;
        if ("content".equals(rootUri.getScheme()))
            root = DocumentFile.fromTreeUri(context, rootUri);
        else
            root = DocumentFile.fromFile(new File(rootUri.getPath()));
        return getDirDocument(root, subDirs);
    }

    public static DocumentFile getDirDocument(DocumentFile root, String... subDirs) {
        DocumentFile parent = root;
        for (int i = 0; i < subDirs.length; i++) {
            String subDirName = Uri.decode(subDirs[i]);
            DocumentFile subDir = parent.findFile(subDirName);
            if (subDir != null)
                parent = subDir;
            else
                return null;
        }
        return parent;
    }

    public static OutputStream getFileOutputSteam(Context context, String fileName, String rootPath, String... subDirs) {
        DocumentFile parent = getDirDocument(context, rootPath, subDirs);
        if (parent == null)
            return null;
        DocumentFile file = parent.findFile(fileName);
        if (file == null)
            return null;
        return getFileOutputSteam(context, file.getUri());
    }

    public static OutputStream getFileOutputSteam(Context context, String fileName, Uri rootUri, String... subDirs) {
        DocumentFile parent = getDirDocument(context, rootUri, subDirs);
        if (parent == null)
            return null;
        DocumentFile file = parent.findFile(fileName);
        if (file == null)
            return null;
        return getFileOutputSteam(context, file.getUri());
    }

    public static OutputStream getFileOutputSteam(Context context, String fileName, DocumentFile root, String... subDirs) {
        DocumentFile parent = getDirDocument(root, subDirs);
        if (parent == null)
            return null;
        DocumentFile file = parent.findFile(fileName);
        if (file == null)
            return null;
        return getFileOutputSteam(context, file.getUri());
    }

    public static OutputStream getFileOutputSteam(Context context, DocumentFile file) {
        return getFileOutputSteam(context, file.getUri());
    }

    public static OutputStream getFileOutputSteam(Context context, Uri fileUri) {
        try {
            OutputStream out = context.getContentResolver().openOutputStream(fileUri);
            return out;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static InputStream getFileInputSteam(Context context, String fileName, String rootPath, String... subDirs) {
        DocumentFile parent = getDirDocument(context, rootPath, subDirs);
        if (parent == null)
            return null;
        DocumentFile file = parent.findFile(fileName);
        if (file == null)
            return null;
        return getFileInputSteam(context, file.getUri());
    }

    public static InputStream getFileInputSteam(Context context, String fileName, Uri rootUri, String... subDirs) {
        DocumentFile parent = getDirDocument(context, rootUri, subDirs);
        if (parent == null)
            return null;
        fileName = filenameFilter(Uri.decode(fileName));
        DocumentFile file = parent.findFile(fileName);
        if (file == null)
            return null;
        return getFileInputSteam(context, file.getUri());
    }

    public static InputStream getFileInputSteam(Context context, String fileName, DocumentFile root, String... subDirs) {
        DocumentFile parent = getDirDocument(root, subDirs);
        if (parent == null)
            return null;
        DocumentFile file = parent.findFile(fileName);
        if (file == null)
            return null;
        return getFileInputSteam(context, file.getUri());
    }

    public static InputStream getFileInputSteam(Context context, DocumentFile file) {
        return getFileInputSteam(context, file.getUri());
    }

    public static InputStream getFileInputSteam(Context context, Uri fileUri) {
        try {
            InputStream in = context.getContentResolver().openInputStream(fileUri);
            return in;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String filenameFilter(String str) {
        return str == null ? null : FilePattern.matcher(str).replaceAll("_");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static ArrayList<FileItem> listFile(Context context, Uri uri) {
        Uri childUri = DocumentsContract.buildChildDocumentsUriUsingTree(uri, DocumentsContract.getDocumentId(uri));
        String[] protection = {DocumentsContract.Document.COLUMN_DOCUMENT_ID,
                DocumentsContract.Document.COLUMN_DISPLAY_NAME,
                DocumentsContract.Document.COLUMN_LAST_MODIFIED,
                DocumentsContract.Document.COLUMN_SIZE,
                DocumentsContract.Document.COLUMN_MIME_TYPE};

        Cursor cursor = context.getContentResolver().query(childUri, protection, null, null, DocumentsContract.Document.COLUMN_DISPLAY_NAME);
        int columnIndex = cursor.getColumnIndex(DocumentsContract.Document.COLUMN_DOCUMENT_ID);
        int columnName = cursor.getColumnIndex(DocumentsContract.Document.COLUMN_DISPLAY_NAME);
        int columnSizeIndex = cursor.getColumnIndex(DocumentsContract.Document.COLUMN_SIZE);
        int columnTypeIndex = cursor.getColumnIndex(DocumentsContract.Document.COLUMN_MIME_TYPE);
        int columnLastModifiedIndex = cursor.getColumnIndex(DocumentsContract.Document.COLUMN_LAST_MODIFIED);
        ArrayList<FileItem> data = new ArrayList<>();
        if (cursor.moveToNext()) {
            while (cursor.moveToNext()) {
                String fileName = cursor.getString(columnName);
                if (!fileName.startsWith(".")) {
                    FileItem lf = new FileItem();
                    lf.name = fileName;
                    lf.size = cursor.getLong(columnSizeIndex);
                    String fileType = cursor.getString(columnTypeIndex);
                    if (TextUtils.equals(DocumentsContract.Document.MIME_TYPE_DIR, fileType)) {
                        lf.fileType = Constant.FileSuffix.DIRECTORY;
                    } else if (fileName.endsWith(EPUB)) {
                        lf.fileType = EPUB;
                    } else if (fileName.endsWith(PDF)) {
                        lf.fileType = PDF;
                    } else if (fileName.endsWith(TXT)) {
                        lf.fileType = TXT;
                    } else {
                        lf.fileType = Constant.FileSuffix.OTHER;
                    }
                    lf.time = new Date(cursor.getLong(columnLastModifiedIndex));
                    lf.uri = DocumentsContract.buildDocumentUriUsingTree(childUri, cursor.getString(columnIndex));
                    lf.path = lf.uri.toString();
                    data.add(lf);
                }
            }
            cursor.close();
            Collections.sort(data, (lhs, rhs) -> ((lhs.name).toLowerCase()).compareTo((rhs.name).toLowerCase()));
        }
        return data;
    }

}
