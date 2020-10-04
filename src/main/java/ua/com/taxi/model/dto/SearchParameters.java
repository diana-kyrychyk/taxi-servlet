package ua.com.taxi.model.dto;

import java.time.LocalDate;

public class SearchParameters {
    public static final String SORT_BY_DATE = "BY_DATE";
    public static final String SORT_BY_FARE = "BY_FARE";
    public static final String DEFAULT_SORT_TYPE = SORT_BY_DATE;
    public static final int DEFAULT_PAGE_SIZE = 8;

    private Integer selectedPassenger;
    private LocalDate startDate;
    private LocalDate endDate;

    private String sortType;

    private int pageSize = DEFAULT_PAGE_SIZE;
    private int pageNumber;
    private int totalPages;


    public Integer getSelectedPassenger() {
        return selectedPassenger;
    }

    public void setSelectedPassenger(Integer selectedPassenger) {
        this.selectedPassenger = selectedPassenger;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "SearchParameters{" +
                "selectedPassenger=" + selectedPassenger +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", sortType='" + sortType + '\'' +
                ", pageSize=" + pageSize +
                ", pageNumber=" + pageNumber +
                ", totalPages=" + totalPages +
                '}';
    }
}
