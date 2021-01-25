package com.xiuxiu.confinement_nurse.ui.base.mvp;


public interface PageStateViewer extends Viewer {

    public void onRequestPageError(int code);

    public void onRequestPageNetError();

    public void onRequestPageSuccess();

    public void onRequestPageEmpty();

    void onRequestLoading();

    void onRequestFinish();
}
