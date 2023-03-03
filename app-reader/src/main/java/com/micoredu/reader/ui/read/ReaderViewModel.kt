package com.micoredu.reader.ui.read

import android.content.Intent
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.airbnb.mvrx.MavericksViewModel
import com.liuzhenli.common.exception.NoStackTraceException
import com.liuzhenli.common.utils.*
import com.liuzhenli.common.BaseActivity
import com.micoredu.reader.bean.Book
import com.micoredu.reader.bean.BookChapter
import com.micoredu.reader.bean.BookProgress
import com.micoredu.reader.bean.SearchResult
import com.micoredu.reader.constant.BookType
import com.micoredu.reader.dao.appDb
import com.micoredu.reader.help.AppWebDav
import com.micoredu.reader.help.book.BookHelp
import com.micoredu.reader.help.book.ContentProcessor
import com.micoredu.reader.help.book.isLocal
import com.micoredu.reader.help.book.removeType
import com.micoredu.reader.model.ReadAloud
import com.micoredu.reader.model.ReadBook
import com.micoredu.reader.model.localBook.LocalBook
import com.micoredu.reader.model.webBook.WebBook
import com.micoredu.reader.page.entities.TextChapter
import com.micoredu.reader.page.provider.ImageProvider
import com.micoredu.reader.service.BaseReadAloudService
import com.micoredu.reader.utils.toStringArray
import com.microedu.lib.reader.R
import kotlinx.coroutines.Dispatchers
import splitties.init.appCtx
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class ReaderViewModel(initialState: ReaderState) :
    MavericksViewModel<ReaderState>(initialState) {
    var isInitFinish = false
    var searchContentQuery = ""
    var searchResultList: List<SearchResult>? = null
    var searchResultIndex: Int = 0
    lateinit var mFragment: BaseActivity<*>

    fun initData(context: BaseActivity<*>, bundle: Intent?) {
        this.mFragment = context
        ReadBook.inBookshelf = bundle?.getBooleanExtra("inBookshelf", true) == true
        ReadBook.tocChanged = bundle?.getBooleanExtra("tocChanged", false) == true
        val bookUrl = bundle?.getStringExtra("bookUrl")
        val book = when {
            bookUrl.isNullOrEmpty() -> appDb.bookDao.lastReadBook
            else -> appDb.bookDao.getBook(bookUrl)
        } ?: ReadBook.book
        when {
            book != null -> initBook(book)
            else -> ReadBook.upMsg(context.getString(R.string.no_book))
        }
    }


    private fun initBook(book: Book) {
        val isSameBook = ReadBook.book?.bookUrl == book.bookUrl
        if (isSameBook) ReadBook.upData(book) else ReadBook.resetData(book)
        isInitFinish = true
        if (ReadBook.chapterSize == 0) {
            if (book.tocUrl.isEmpty()) {
                loadBookInfo(book)
            } else {
                loadChapterList(book)
            }
        } else if (book.isLocal
            && LocalBook.getLastModified(book).getOrDefault(0L) > book.latestChapterTime
        ) {
            loadChapterList(book)
        } else if (isSameBook) {
            if (ReadBook.curTextChapter != null) {
                ReadBook.callBack?.upContent(resetPageOffset = false)
            } else {
                ReadBook.loadContent(resetPageOffset = true)
            }
        } else {
            if (ReadBook.durChapterIndex > ReadBook.chapterSize - 1) {
                ReadBook.durChapterIndex = ReadBook.chapterSize - 1
            }
            ReadBook.loadContent(resetPageOffset = false)
        }
        if (!isSameBook || !BaseReadAloudService.isRun) {
            syncBookProgress(book)
        }
        if (!book.isLocal && ReadBook.bookSource == null) {
            autoChangeSource(book.name, book.author)
        }
    }


    /**
     * 加载目录
     */
    fun loadChapterList(book: Book, callback: (() -> Unit)? = null) = withState {
        if (book.isLocal) {
            suspend {
                LocalBook.getChapterList(book)
            }.execute(Dispatchers.IO, retainValue = ReaderState::getBookChapter) { response ->
                book.latestChapterTime = System.currentTimeMillis()
                appDb.bookChapterDao.delByBook(book.bookUrl)
                response()?.toTypedArray()?.let { res -> appDb.bookChapterDao.insert(*res) }
                appDb.bookDao.update(book)
                ReadBook.chapterSize = response()?.size ?: 0
                ReadBook.upMsg(null)
                ReadBook.loadContent(resetPageOffset = true)
                copy(getBookChapter = response)
            }

        } else {
            ReadBook.bookSource?.let {
                val oldBook = book.copy()
                WebBook.getChapterList(viewModelScope, it, book, true)
                    .onSuccess(Dispatchers.IO) { cList ->
                        if (oldBook.bookUrl == book.bookUrl) {
                            appDb.bookDao.update(book)
                        } else {
                            appDb.bookDao.insert(book)
                            BookHelp.updateCacheFolder(oldBook, book)
                        }
                        appDb.bookChapterDao.delByBook(oldBook.bookUrl)
                        appDb.bookChapterDao.insert(*cList.toTypedArray())
                        ReadBook.chapterSize = cList.size
                        ReadBook.upMsg(null)
                        ReadBook.loadContent(resetPageOffset = true)
                    }.onError {
                        ReadBook.upMsg(mFragment.getString(R.string.error_load_toc))
                    }.onFinally {
                        callback?.invoke()
                    }
            }
        }
    }


    /**
     * 加载详情页
     */
    private fun loadBookInfo(book: Book) = withState {
        if (book.isLocal) {
            loadChapterList(book)
        } else {
            ReadBook.bookSource?.let { source ->
                WebBook.getBookInfo(viewModelScope, source, book, canReName = false)
                    .onSuccess {
                        loadChapterList(book)
                    }.onError {
                        ReadBook.upMsg("详情页出错: ${it.localizedMessage}")
                    }
            }
        }
    }


    /**
     * 同步进度
     */
    fun syncBookProgress(
        book: Book,
        alertSync: ((progress: BookProgress) -> Unit)? = null
    ) = withState {
        if (!AppConfig.syncBookProgress) return@withState
        suspend {
            AppWebDav.getBookProgress(book)
        }.execute(Dispatchers.IO, retainValue = ReaderState::syncBookProgress) { progress ->
            if ((progress()?.durChapterIndex ?: 0) < book.durChapterIndex ||
                (progress()?.durChapterIndex == book.durChapterIndex
                        && (progress()?.durChapterPos ?: 0) < book.durChapterPos)
            ) {
                progress()?.let {
                    alertSync?.invoke(it)
                }
            } else {
                progress()?.let {
                    ReadBook.setProgress(it)
                }
                AppLog.put("自动同步阅读进度成功")
            }
            copy(syncBookProgress = progress)
        }
    }


    /**
     * 换源
     */
    fun changeTo(book: Book, toc: List<BookChapter>) = withState {

        suspend {
            ReadBook.upMsg(mFragment.getString(R.string.loading))
            ReadBook.book?.migrateTo(book, toc)
            book.removeType(BookType.updateError)
            appDb.bookDao.insert(book)
            appDb.bookChapterDao.insert(*toc.toTypedArray())
            ReadBook.resetData(book)
            ReadBook.upMsg(null)
            ReadBook.loadContent(resetPageOffset = true)
        }.execute(Dispatchers.IO, retainValue = ReaderState::changeBookSource) { res ->
            copy(changeBookSource = res)
        }
//       onError {
//            context.toast("换源失败\n${it.localizedMessage}")
//            ReadBook.upMsg(null)
//        }.onFinally {
//            postEvent(EventBus.SOURCE_CHANGED, book.bookUrl)
//        }
    }

    /**
     * 自动换源
     */
    private fun autoChangeSource(name: String, author: String) = withState {
        if (!AppConfig.autoChangeSource) return@withState
        suspend execute@{
            val sources = appDb.bookSourceDao.allTextEnabled
            sources.forEach { source ->
                WebBook.preciseSearchAwait(source, name, author).getOrNull()?.let { book ->
                    if (book.tocUrl.isEmpty()) {
                        WebBook.getBookInfoAwait(source, book)
                    }
                    val toc = WebBook.getChapterListAwait(source, book).getOrThrow()
                    val chapter = toc.getOrElse(book.durChapterIndex) {
                        toc.last()
                    }
                    val nextChapter = toc.getOrElse(chapter.index) {
                        toc.first()
                    }
                    kotlin.runCatching {
                        WebBook.getContentAwait(
                            bookSource = source,
                            book = book,
                            bookChapter = chapter,
                            nextChapterUrl = nextChapter.url
                        )
                        changeTo(book, toc)
                        return@execute
                    }
                }
            }
            throw NoStackTraceException("没有合适书源")
        }.execute(Dispatchers.IO, retainValue = ReaderState::autoChangeBookSource) {

            copy(autoChangeBookSource = it)
        }
//        execute {

//        }.onStart {
//            ReadBook.upMsg(context.getString(R.string.source_auto_changing))
//        }.onError {
//            AppLog.put("自动换源失败\n${it.localizedMessage}", it)
//            context.toast("自动换源失败\n${it.localizedMessage}")
//        }.onFinally {
//            ReadBook.upMsg(null)
//        }
    }

    fun openChapter(index: Int, durChapterPos: Int = 0, success: (() -> Unit)? = null) {
        if (index < ReadBook.chapterSize) {
            ReadBook.clearTextChapter()
            ReadBook.callBack?.upContent()
            ReadBook.durChapterIndex = index
            ReadBook.durChapterPos = durChapterPos
            ReadBook.saveRead()
            ReadBook.loadContent(resetPageOffset = true) {
                success?.invoke()
            }
        }
    }

    fun removeFromBookshelf() = withState {
        suspend {
            ReadBook.book?.delete()
            true
        }.execute(Dispatchers.IO, retainValue = ReaderState::removeFromBookshelf) {
            copy(removeFromBookshelf = it)
        }
    }

    fun upBookSource() = withState {
        suspend {
            ReadBook.book?.let { book ->
                ReadBook.bookSource = appDb.bookSourceDao.getBookSource(book.origin)
            }
            true
        }.execute(Dispatchers.IO, retainValue = ReaderState::upBookSource) {
            copy(upBookSource = it)
        }
    }

    fun refreshContentDur(book: Book) = withState {
        suspend {
            appDb.bookChapterDao.getChapter(book.bookUrl, ReadBook.durChapterIndex)
                ?.let { chapter ->
                    BookHelp.delContent(book, chapter)
                    ReadBook.loadContent(ReadBook.durChapterIndex, resetPageOffset = false)
                }
        }.execute(Dispatchers.IO, retainValue = ReaderState::refreshContentDur) {
            copy(refreshContentDur = it)
        }
    }

    fun refreshContentAfter(book: Book) = withState {
        suspend {
            appDb.bookChapterDao.getChapterList(
                book.bookUrl,
                ReadBook.durChapterIndex,
                book.totalChapterNum
            ).forEach { chapter ->
                BookHelp.delContent(book, chapter)
            }
            ReadBook.loadContent(false)
        }.execute(Dispatchers.IO, retainValue = ReaderState::voidRequest) { copy(voidRequest = it) }
    }

    fun refreshContentAll(book: Book) {
        suspend {
            BookHelp.clearCache(book)
            ReadBook.loadContent(false)
        }.execute(
            Dispatchers.IO,
            retainValue = ReaderState::voidRequest
        ) { copy(voidRequest = it) }
    }

    /**
     * 保存内容
     */
    fun saveContent(book: Book, content: String) = withState {
        suspend {
            appDb.bookChapterDao.getChapter(book.bookUrl, ReadBook.durChapterIndex)
                ?.let { chapter ->
                    BookHelp.saveText(book, chapter, content)
                    ReadBook.loadContent(ReadBook.durChapterIndex, resetPageOffset = false)
                }
        }.execute(Dispatchers.IO, retainValue = ReaderState::saveContent) {
            copy(saveContent = it)
        }
    }

    /**
     * 反转内容
     */
    fun reverseContent(book: Book) = withState {
        suspend execute@{
            val chapter = appDb.bookChapterDao.getChapter(book.bookUrl, ReadBook.durChapterIndex)
                ?: return@execute
            val content = BookHelp.getContent(book, chapter) ?: return@execute
            val stringBuilder = StringBuilder()
            content.toStringArray().forEach {
                stringBuilder.insert(0, it)
            }
            BookHelp.saveText(book, chapter, stringBuilder.toString())
            ReadBook.loadContent(ReadBook.durChapterIndex, resetPageOffset = false)
        }.execute(Dispatchers.IO, retainValue = ReaderState::reverseContent) {
            copy(reverseContent = it)
        }
    }

    /**
     * 内容搜索跳转
     */
    fun searchResultPositions(
        textChapter: TextChapter,
        searchResult: SearchResult
    ): Array<Int> {
        // calculate search result's pageIndex
        val pages = textChapter.pages
        val content = textChapter.getContent()
        val queryLength = searchContentQuery.length

        var count = 0
        var index = content.indexOf(searchContentQuery)
        while (count != searchResult.resultCountWithinChapter) {
            index = content.indexOf(searchContentQuery, index + queryLength)
            count += 1
        }
        val contentPosition = index
        var pageIndex = 0
        var length = pages[pageIndex].text.length
        while (length < contentPosition && pageIndex + 1 < pages.size) {
            pageIndex += 1
            length += pages[pageIndex].text.length
        }

        // calculate search result's lineIndex
        val currentPage = pages[pageIndex]
        val curTextLines = currentPage.lines
        var lineIndex = 0
        var curLine = curTextLines[lineIndex]
        length = length - currentPage.text.length + curLine.text.length
        if (curLine.isParagraphEnd) length++
        while (length < contentPosition && lineIndex + 1 < curTextLines.size) {
            lineIndex += 1
            curLine = curTextLines[lineIndex]
            length += curLine.text.length
            if (curLine.isParagraphEnd) length++
        }

        // charIndex
        val currentLine = currentPage.lines[lineIndex]
        var curLineLength = currentLine.text.length
        if (currentLine.isParagraphEnd) curLineLength++
        length -= curLineLength

        val charIndex = contentPosition - length
        var addLine = 0
        var charIndex2 = 0
        // change line
        if ((charIndex + queryLength) > curLineLength) {
            addLine = 1
            charIndex2 = charIndex + queryLength - curLineLength - 1
        }
        // changePage
        if ((lineIndex + addLine + 1) > currentPage.lines.size) {
            addLine = -1
            charIndex2 = charIndex + queryLength - curLineLength - 1
        }
        return arrayOf(pageIndex, lineIndex, charIndex, addLine, charIndex2)
    }

    fun refreshImage(src: String) = withState {
        suspend {
            ReadBook.book?.let { book ->
                val vFile = BookHelp.getImage(book, src)
                ImageProvider.bitmapLruCache.remove(vFile.absolutePath)
                vFile.delete()
            }
        }.execute(dispatcher = Dispatchers.IO, retainValue = ReaderState::refreshImage) {
            copy(refreshImage = it)
        }
//        execute {
//
//        }.onFinally {
//            ReadBook.loadContent(false)
//        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    fun saveImage(src: String?, uri: Uri) {
        src ?: return
        val book = ReadBook.book ?: return

        suspend {
            val image = BookHelp.getImage(book, src)
            FileInputStream(image).use { input ->
                if (uri.isContentScheme()) {
                    DocumentFile.fromTreeUri(appCtx, uri)?.let { doc ->
                        val imageDoc = DocumentUtils.createFileIfNotExist(doc, image.name)!!
                        appCtx.contentResolver.openOutputStream(imageDoc.uri)!!.use { output ->
                            input.copyTo(output)
                        }
                    }
                } else {
                    val dir = File(uri.path ?: uri.toString())
                    val file = FileUtils.createFileIfNotExist(dir, image.name)
                    FileOutputStream(file).use { output ->
                        input.copyTo(output)
                    }
                }
            }
        }.execute(dispatcher = Dispatchers.IO, ReaderState::saveImage) {
            copy(saveImage = it)
        }
//        onError {
//            AppLog.put("保存图片出错\n${it.localizedMessage}", it)
//            context.toast("保存图片出错\n${it.localizedMessage}")
//        }
    }

    /**
     * 替换规则变化
     */
    fun replaceRuleChanged() = withState {
        suspend {
            ReadBook.book?.let {
                ContentProcessor.get(it.name, it.origin).upReplaceRules()
                ReadBook.loadContent(resetPageOffset = false)
            }
        }.execute(Dispatchers.IO, retainValue = ReaderState::replaceRuleChanged) {
            copy(replaceRuleChanged = it)
        }
    }

    fun disableSource() = withState {
        suspend {
            ReadBook.bookSource?.let {
                it.enabled = false
                appDb.bookSourceDao.update(it)
            }
        }.execute(Dispatchers.IO, retainValue = ReaderState::disableSource) {
            copy(disableSource = it)
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (BaseReadAloudService.pause) {
            ReadAloud.stop(mFragment)
        }
    }

}