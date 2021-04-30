package com.sport.training.domain.service;

import com.sport.training.domain.dto.CreditCardDTO;
import com.sport.training.exception.FinderException;

public interface CreditCardService {
	
	 public void verifyCreditCard(CreditCardDTO creditCardDTO) throws FinderException;

}
