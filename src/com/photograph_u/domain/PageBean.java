package com.photograph_u.domain;

public class PageBean<T> {
    private int pageSize;//用户传过来
    private int currentPage;//用户传过来
    private int totalRows;//service计算后传过来
    private int totalPages;//计算后获得
    private int startIndex;//计算获得
    private int start;//开始边界提示
    private int end;//结束边界提示
    private T data;

    public PageBean(int mPageSize, int mCurrentPage, int mTotalRows) {
        this.pageSize = mPageSize;
        this.currentPage = mCurrentPage;
        this.totalRows = mTotalRows;
        //计算总页数
        if (mTotalRows % mPageSize == 0) {
            this.totalPages = mTotalRows / mPageSize;
        } else {
            this.totalPages = mTotalRows / mPageSize + 1;
        }
        //计算开始索引
        this.startIndex = (currentPage - 1) * pageSize;
        //计算显示页
        this.start = 1;//开始页
        this.end = 5;//结束页
        if (totalPages < 5) {
            end = totalPages;//最后一页小于5，最后一页即为总页数
        } else {
            this.start = currentPage - 2;
            this.end = currentPage + 2;
            if (start < 1) {
                start = 1;
                end = 5;
            }
            if (end > totalPages) {
                start = totalPages - 4;
                end = totalPages;
            }
        }
        //保证不越界
        if (currentPage < 1) {
            currentPage = 1;
        }
        if (currentPage > pageSize) {
            currentPage = pageSize;
        }
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
