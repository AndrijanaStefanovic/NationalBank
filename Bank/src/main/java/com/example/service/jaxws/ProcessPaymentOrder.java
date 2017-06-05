
package com.example.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "processPaymentOrder", namespace = "http://service.example.com/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "processPaymentOrder", namespace = "http://service.example.com/")
public class ProcessPaymentOrder {

    @XmlElement(name = "arg0", namespace = "")
    private com.example.service.paymentorder.PaymentOrder arg0;

    /**
     * 
     * @return
     *     returns PaymentOrder
     */
    public com.example.service.paymentorder.PaymentOrder getArg0() {
        return this.arg0;
    }

    /**
     * 
     * @param arg0
     *     the value for the arg0 property
     */
    public void setArg0(com.example.service.paymentorder.PaymentOrder arg0) {
        this.arg0 = arg0;
    }

}
