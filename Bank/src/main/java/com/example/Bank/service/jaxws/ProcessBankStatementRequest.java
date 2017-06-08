
package com.example.Bank.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "processBankStatementRequest", namespace = "http://service.Bank.example.com/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "processBankStatementRequest", namespace = "http://service.Bank.example.com/")
public class ProcessBankStatementRequest {

    @XmlElement(name = "arg0", namespace = "")
    private com.example.service.bankstatementrequest.BankStatementRequest arg0;

    /**
     * 
     * @return
     *     returns BankStatementRequest
     */
    public com.example.service.bankstatementrequest.BankStatementRequest getArg0() {
        return this.arg0;
    }

    /**
     * 
     * @param arg0
     *     the value for the arg0 property
     */
    public void setArg0(com.example.service.bankstatementrequest.BankStatementRequest arg0) {
        this.arg0 = arg0;
    }

}
