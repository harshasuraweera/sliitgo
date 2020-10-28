package com.evoxlk.sliitgo;

public class TempBookMark {

    String bookMarkUrl, bookmarkTitle;


    public TempBookMark(String bookMarkUrl, String bookmarkTitle) {
        this.bookMarkUrl = bookMarkUrl;
        this.bookmarkTitle = bookmarkTitle;
    }


    public String getBookMarkUrl() {
        return bookMarkUrl;
    }

    public void setBookMarkUrl(String bookMarkUrl) {
        this.bookMarkUrl = bookMarkUrl;
    }

    public String getBookmarkTitle() {
        return bookmarkTitle;
    }

    public void setBookmarkTitle(String bookmarkTitle) {
        this.bookmarkTitle = bookmarkTitle;
    }
}
