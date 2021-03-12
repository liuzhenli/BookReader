package com.liuzhenli.reader;

import com.liuzhenli.common.AppComponent;
import com.liuzhenli.common.scope.ActivityScope;
import com.liuzhenli.reader.ui.activity.BookDetailActivity;
import com.liuzhenli.reader.ui.activity.ChangeSourceActivity;
import com.liuzhenli.reader.ui.activity.DatabaseTableListActivity;
import com.liuzhenli.reader.ui.activity.ImportLocalBookActivity;
import com.liuzhenli.reader.ui.activity.LoginActivity;
import com.liuzhenli.reader.ui.activity.SearchActivity;
import com.liuzhenli.reader.ui.activity.SettingActivity;
import com.liuzhenli.reader.ui.fragment.BookCategoryFragment;
import com.liuzhenli.reader.ui.fragment.BookShelfFragment;
import com.liuzhenli.reader.ui.fragment.DiscoverFragment;
import com.liuzhenli.reader.ui.fragment.LocalFileFragment;
import com.liuzhenli.reader.ui.fragment.LocalTxtFragment;


import dagger.Component;

/**
 * Description:
 *
 * @author liuzhenli 3/10/21
 * Email: 848808263@qq.com
 */
@ActivityScope
@Component(dependencies = AppComponent.class)
public interface ReadBookComponent {
    void inject(BookShelfFragment fragment);

    void inject(DiscoverFragment discoverFragment);

    void inject(BookCategoryFragment bookCategoryFragment);

    void inject(LocalFileFragment localFileFragment);

    void inject(LocalTxtFragment localTxtFragment);

    void inject(SettingActivity settingActivity);

    void inject(BookDetailActivity bookDetailActivity);

    void inject(ChangeSourceActivity changeSourceActivity);

    void inject(SearchActivity searchActivity);

    void inject(ImportLocalBookActivity importLocalBookActivity);

    void inject(DatabaseTableListActivity activity);

    void inject(LoginActivity loginActivity);
}
