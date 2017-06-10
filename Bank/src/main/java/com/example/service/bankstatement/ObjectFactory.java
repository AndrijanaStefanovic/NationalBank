package com.example.service.bankstatement;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.example.service.bankstatement package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.example.service.bankstatement
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link BankStatement }
     * 
     */
    public BankStatement createBankStatement() {
        return new BankStatement();
    }

    /**
     * Create an instance of {@link BankStatement.BankStatementItem }
     * 
     */
    public BankStatement.BankStatementItem createBankStatementBankStatementItem() {
        return new BankStatement.BankStatementItem();
    }

    /**
     * Create an instance of {@link TCompanyData }
     * 
     */
    public TCompanyData createTCompanyData() {
        return new TCompanyData();
    }

}
