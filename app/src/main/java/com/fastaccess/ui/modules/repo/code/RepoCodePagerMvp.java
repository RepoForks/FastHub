package com.fastaccess.ui.modules.repo.code;

import com.fastaccess.ui.base.mvp.BaseMvp;

/**
 * Created by Kosh on 31 Dec 2016, 1:35 AM
 */

public interface RepoCodePagerMvp {

    interface View extends BaseMvp.FAView {}

    interface Presenter extends BaseMvp.FAPresenter<View> {}
}
