package com.micoredu.reader.bean;

public class LocBookShelfBean {
    private Boolean isNew;
    private BookShelfBean bookShelfBean;

    public LocBookShelfBean(Boolean isNew, BookShelfBean bookShelfBean) {
        this.isNew = isNew;
        this.bookShelfBean = bookShelfBean;
    }

    public Boolean getNew() {
        return isNew;
    }

    public void setNew(Boolean aNew) {
        isNew = aNew;
    }

    public BookShelfBean getBookShelfBean() {
        return bookShelfBean;
    }

    public void setBookShelfBean(BookShelfBean bookShelfBean) {
        this.bookShelfBean = bookShelfBean;
    }
}
