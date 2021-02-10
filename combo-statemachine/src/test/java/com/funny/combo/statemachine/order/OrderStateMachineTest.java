package com.funny.combo.statemachine.order;

import com.funny.combo.statemachine.Action;
import com.funny.combo.statemachine.Condition;
import com.funny.combo.statemachine.StateMachine;
import com.funny.combo.statemachine.builder.StateMachineBuilder;
import com.funny.combo.statemachine.builder.StateMachineBuilderFactory;
import org.junit.Assert;
import org.junit.Test;

public class OrderStateMachineTest {
    static String MACHINE_ID = "orderStateMachine";

    @Test
    public void testExternalNormal(){
        StateMachineBuilder<OrderStates, OrderEvents, OrderContext> builder = StateMachineBuilderFactory.create();

        builder.externalTransition()
                .from(OrderStates.WAIT_PAY)
                .to(OrderStates.WAIT_DELIVERY)
                .on(OrderEvents.PAYED)
                .when(checkPayed())
                .perform(doPayNotify());


        builder.externalTransition()
                .from(OrderStates.WAIT_DELIVERY)
                .to(OrderStates.WAIT_CONFIRM)
                .on(OrderEvents.DELIVERY)
                .when(checkPayed())
                .perform(doPayNotify());

        builder.externalTransition()
                .from(OrderStates.WAIT_CONFIRM)
                .to(OrderStates.FINISH)
                .on(OrderEvents.CONFIRM)
                .when(checkPayed())
                .perform(doPayNotify());

        StateMachine<OrderStates, OrderEvents, OrderContext> stateMachine = builder.build(MACHINE_ID);
        OrderContext orderContext = new OrderContext();
        orderContext.setOrderId("1123");
        OrderStates target = stateMachine.fireEvent(OrderStates.WAIT_PAY, OrderEvents.PAYED,orderContext);
        Assert.assertEquals(OrderStates.WAIT_DELIVERY, target);


        OrderStates targetbb = stateMachine.fireEvent(OrderStates.WAIT_DELIVERY, OrderEvents.DELIVERY,orderContext);
        Assert.assertEquals(OrderStates.WAIT_CONFIRM, targetbb);


        OrderStates targetcc = stateMachine.fireEvent(OrderStates.WAIT_CONFIRM, OrderEvents.CONFIRM,orderContext);
        Assert.assertEquals(OrderStates.FINISH, targetcc);
    }

    private Condition<OrderContext> checkPayed() {
        return (ctx) -> {return true;};
    }

    private Action<OrderStates, OrderEvents, OrderContext> doPayNotify() {
        return (from, to, event, ctx)->{
            System.out.println(ctx.getOrderId()+" is operating from:"+ from.getOrderStatusName()+" to:"+to.getOrderStatusName()+" on:"+event.getEventName());
        };
    }

}
