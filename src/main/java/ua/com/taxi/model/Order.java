package ua.com.taxi.model;

import java.time.LocalDateTime;

public class Order {

    private Integer id;
    private Address departureAddress;
    private Address arrivalAddress;
    private int passengersCount;
    private Category category;
    private Driver driver;
    private Car car;
    private long fare;
    private long finalFare;
    private LocalDateTime creationDate;
    private User user;
    private long waitingSeconds;
    private OrderStatus status;
    private long discount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Address getDepartureAddress() {
        return departureAddress;
    }

    public void setDepartureAddress(Address departureAddress) {
        this.departureAddress = departureAddress;
    }

    public Address getArrivalAddress() {
        return arrivalAddress;
    }

    public void setArrivalAddress(Address arrivalAddress) {
        this.arrivalAddress = arrivalAddress;
    }

    public int getPassengersCount() {
        return passengersCount;
    }

    public void setPassengersCount(int passengersCount) {
        this.passengersCount = passengersCount;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public long getFare() {
        return fare;
    }

    public void setFare(long fare) {
        this.fare = fare;
    }

    public long getFinalFare() {
        return finalFare;
    }

    public void setFinalFare(long finalFare) {
        this.finalFare = finalFare;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getWaitingSeconds() {
        return waitingSeconds;
    }

    public void setWaitingSeconds(long waitingSeconds) {
        this.waitingSeconds = waitingSeconds;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public long getDiscount() {
        return discount;
    }

    public void setDiscount(long discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", departureAddress=" + departureAddress +
                ", arrivalAddress=" + arrivalAddress +
                ", passengersCount=" + passengersCount +
                ", category=" + category +
                ", driver=" + driver +
                ", car=" + car +
                ", fare=" + fare +
                ", finalFare=" + finalFare +
                ", creationDate=" + creationDate +
                ", user=" + user +
                ", waitingSeconds=" + waitingSeconds +
                ", status=" + status +
                ", discount=" + discount +
                '}';
    }
}
