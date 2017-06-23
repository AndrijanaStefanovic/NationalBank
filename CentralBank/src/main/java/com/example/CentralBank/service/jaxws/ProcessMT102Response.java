package com.example.CentralBank.service.jaxws;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "processMT102Response", namespace = "http://service.CentralBank.example.com/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "processMT102Response", namespace = "http://service.CentralBank.example.com/")
public class ProcessMT102Response {

    @XmlElement(name = "return", namespace = "")
    private String _return;

    /**
     *
     * @return
     *     returns String
     */
    public String getReturn() {
        return this._return;
    }

    /**
     *
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(String _return) {
        this._return = _return;
    }
}
