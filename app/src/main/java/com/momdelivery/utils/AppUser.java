package com.momdelivery.utils;

import com.momdelivery.network.response.login.UserDatum;
import com.momdelivery.network.response.on_going.OrderDatum;

public class AppUser {
    UserDatum userDatum;
    OrderDatum orderDatum;

    public OrderDatum getOrderDatum() {
        return orderDatum;
    }

    public void setOrderDatum(OrderDatum orderDatum) {
        this.orderDatum = orderDatum;
    }

    public UserDatum getUserDatum() {
        return userDatum;
    }

    public void setUserDatum(UserDatum userDatum) {
        this.userDatum = userDatum;
    }
}
