package com.liuzhenli.reader.network;


import com.liuzhenli.reader.module.ApiModule;
import com.liuzhenli.reader.module.AppModule;
import com.liuzhenli.reader.ui.activity.BookCatalogActivity;
import com.liuzhenli.reader.ui.activity.BookDetailActivity;
import com.liuzhenli.reader.ui.activity.ImportLocalBookActivity;
import com.liuzhenli.reader.ui.activity.LoginActivity;
import com.liuzhenli.reader.ui.activity.ReaderActivity;
import com.liuzhenli.reader.ui.activity.SearchActivity;
import com.liuzhenli.reader.ui.fragment.BookCategoryFragment;
import com.liuzhenli.reader.ui.fragment.BookShelfFragment;
import com.liuzhenli.reader.ui.fragment.LocalFileFragment;
import com.liuzhenli.reader.ui.fragment.LocalTxtFragment;
import com.liuzhenli.reader.ui.fragment.RecommendFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Liuzhenli
 * @since 19/7/6
 */
@Singleton
@Component(modules = {AppModule.class, ApiModule.class})
public interface AppComponent {

    //void inject(SplashActivity splashActivity);

    void inject(LoginActivity loginActivity);

    void inject(LocalFileFragment localFileFragment);

    void inject(ImportLocalBookActivity importLocalBookActivity);

    void inject(BookShelfFragment bookShelfFragment);

    void inject(ReaderActivity readActivity);

    void inject(BookCatalogActivity bookCatalogActivity);

    void inject(LocalTxtFragment localTxtFragment);

    void inject(AppComponent appComponent);

    void inject(RecommendFragment recommendFragment);

    void inject(BookCategoryFragment bookCategoryFragment);

    void inject(BookDetailActivity bookDetailActivity);

    void inject(SearchActivity searchActivity);
}
