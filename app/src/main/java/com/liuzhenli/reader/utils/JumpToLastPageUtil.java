//package com.liuzhenli.reader.utils;
//
//import android.content.Context;
//import android.content.Intent;
//import android.text.TextUtils;
//
//import com.liuzhenli.common.BitIntentDataManager;
//import com.liuzhenli.common.constant.AppConstant;
//import com.liuzhenli.common.utils.AppSharedPreferenceHelper;
//import com.micoredu.reader.help.book.BookHelp;
//
//import java.util.Map;
//
//import static com.liuzhenli.common.BitIntentDataManager.DATA_KEY;
//
///**
// * describe:open last view page
// *
// * @author Liuzhenli 848808263@qq.com
// * @since on 2021/6/21 8:13 PM
// */
//public class JumpToLastPageUtil {
//    /**
//     * open last page
//     */
//    public static void openLastPage(Context context) {
//        Map<String, Object> lastPageInfo = AppSharedPreferenceHelper.getLastPageInfo();
//        if (lastPageInfo != null && lastPageInfo.size() >= 2 && lastPageInfo.get("timeStamp") != null) {
//            long timeStamp = (long) lastPageInfo.get("timeStamp");
//            if (System.currentTimeMillis() - timeStamp >= 24 * 60 * 60 * 1000) {
//                return;
//            }
//            //jump target page
//            String pageName = (String) lastPageInfo.get("pageName");
//            if (TextUtils.equals(pageName, ReaderActivity.class.getSimpleName())) {
//                Book bookShelf = (Book) lastPageInfo.get("bookShelf");
//                if (bookShelf == null) {
//                    return;
//                }
//                Intent intent = new Intent(context, ReaderActivity.class);
//                intent.putExtra("openFrom", AppConstant.BookOpenFrom.OPEN_FROM_APP);
//                intent.putExtra("inBookshelf", BookHelp.isInBookShelf(bookShelf.getBookUrl()));
//                String key = String.valueOf(System.currentTimeMillis());
//                String bookKey = "book" + key;
//                intent.putExtra(DATA_KEY, bookKey);
//                BitIntentDataManager.getInstance().putData(bookKey, bookShelf);
//                context.startActivity(intent);
//            }
//
//        }
//    }
//}
