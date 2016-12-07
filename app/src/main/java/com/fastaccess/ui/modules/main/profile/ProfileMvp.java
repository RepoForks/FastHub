package com.fastaccess.ui.modules.main.profile;

import com.fastaccess.ui.base.mvp.BaseMvp;

/**
 * Created by Kosh on 03 Dec 2016, 7:59 AM
 */

public interface ProfileMvp {

    interface View extends BaseMvp.FAView {}

    interface Presenter extends BaseMvp.FAPresenter<View> {}
}
