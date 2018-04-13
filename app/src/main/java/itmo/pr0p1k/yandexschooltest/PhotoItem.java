package itmo.pr0p1k.yandexschooltest;

public class PhotoItem {
    private String ID;
    private String ShortcutURL;
    private String title;
    private String FullURL;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getShortcutURL() {
        return ShortcutURL;
    }

    public void setShortcutURL(String shortcutURL) {
        ShortcutURL = shortcutURL;
    }

    public String getTitle() {
        return title;
    }

    public String getFullURL() {
        return FullURL;
    }

    public void setFullURL(String fullURL) {
        FullURL = fullURL;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
