package com.alibaba.cola.test.order;

public enum  OrderStates {
    WAIT_PAY(2,"待支付"),
    WAIT_DELIVERY(11,"待发货"),
    WAIT_CONFIRM(15,"等待确认收货"),
    FINISH(20,"完成");
    private Integer orderStatus;

    private String orderStatusName;

    OrderStates(Integer orderStatus, String orderStatusName) {
        this.orderStatus = orderStatus;
        this.orderStatusName = orderStatusName;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }

}
