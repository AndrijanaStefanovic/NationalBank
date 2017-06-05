
package com.example.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "processBankStatementRequestResponse", namespace = "http://service.example.com/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "processBankStatementRequestResponse", namespace = "http://service.example.com/")
public class ProcessBankStatementRequestResponse {

    @XmlElement(name = "return", namespace = "")
    private com.example.service.bankstatement.BankStatement _return;

    /**
     * 
     * @return
     *     returns BankStatement
     */
    public com.example.service.bankstatement.BankStatement getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(com.example.service.bankstatement.BankStatement _return) {
        this._return = _return;
    }

}
