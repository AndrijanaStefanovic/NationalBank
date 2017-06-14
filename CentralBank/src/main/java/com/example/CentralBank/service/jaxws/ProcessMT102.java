
package com.example.CentralBank.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "processMT102", namespace = "http://service.CentralBank.example.com/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "processMT102", namespace = "http://service.CentralBank.example.com/")
public class ProcessMT102 {

    @XmlElement(name = "arg0", namespace = "")
    private com.example.service.mt102.Mt102 arg0;

    /**
     *
     * @return
     *     returns PaymentOrder
     */
    public  com.example.service.mt102.Mt102 getArg0() {
        return this.arg0;
    }

    /**
     *
     * @param arg0
     *     the value for the arg0 property
     */
    public void setArg0( com.example.service.mt102.Mt102 arg0) {
        this.arg0 = arg0;
    }
}
