package com.funny.combo.statemachine.order;

public enum  OrderEvents {

    PAYED(1,"支付"),
    DELIVERY(2,"发货"),
    CONFIRM(3,"确认收货");

    private Integer event;

    private String eventName;

    OrderEvents(Integer event, String eventName) {
        this.event = event;
        this.eventName = eventName;
    }

    public Integer getEvent() {
        return event;
    }

    public void setEvent(Integer event) {
        this.event = event;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}
