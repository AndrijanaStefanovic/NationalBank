package com.example.Bank.service.jaxws;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "processMT910", namespace = "http://service.Bank.example.com/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "processMT910", namespace = "http://service.Bank.example.com/")
public class ProcessMT910 {

    @XmlElement(name = "arg0", namespace = "")
    private com.example.service.mt910.Mt910 arg0;

    /**
     *
     * @return
     *     returns PaymentOrder
     */
    public  com.example.service.mt910.Mt910 getArg0() {
        return this.arg0;
    }

    /**
     *
     * @param arg0
     *     the value for the arg0 property
     */
    public void setArg0( com.example.service.mt910.Mt910 arg0) {
        this.arg0 = arg0;
    }
}
