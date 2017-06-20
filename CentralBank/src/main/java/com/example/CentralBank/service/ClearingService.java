package com.example.CentralBank.service;

import com.example.CentralBank.model.Mt102Model;
import com.example.service.mt102.Mt102;

public interface ClearingService {

    public String processMT102(Mt102 mt102);
    
    /**
     * Clearing obracun kada ima dovoljno mt102 naloga
     * */
    public String performClear();
    
    public String forwardMt102(Mt102Model mt102model);
    
    /**
     * Poruka o zaduzenju
     * */
    public String sendMt900(Mt102Model mt102model);
}
