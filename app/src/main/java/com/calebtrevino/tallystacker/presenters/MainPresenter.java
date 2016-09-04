package com.calebtrevino.tallystacker.presenters;

import android.content.Intent;
import android.os.Bundle;

public interface MainPresenter {
    public void initializeViews();

    public void initializeMainLayout(Intent argument);

    public void saveState(Bundle outState);

    public void restoreState(Bundle savedState);

}
