package com.train.sports.domain.service;

import com.train.sports.domain.dto.CreditCardDTO;
import com.train.sports.exception.FinderException;

public interface CreditCardService {
	
	 public void verifyCreditCard(CreditCardDTO creditCardDTO) throws FinderException;

}
