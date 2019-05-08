package gui;

public interface UICallback {
    void startLoading();
    void stopLoading();
    void setProgress(int progressPercent);
    void showError(String message);
}
