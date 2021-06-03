package com.sport.training.domain.dto;

import java.time.LocalDateTime;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

/**
 * This class is a client view of an item of the Shopping Cart.
 * A shopping cart is made of several items.
 * This class only transfers data from a distant service to a client.
 */
public class ShoppingCartEventDTO {

    // ======================================
    // =             Attributes             =
    // ======================================
	@NotBlank(message = "id must be defined")
    private Long eventId;
	@NotBlank(message = "invalid Name")
    private String eventName;
	@NotBlank(message = "invalid event datetime")
    private LocalDateTime eventDateTime;
	@NotBlank(message = "invalid Description")
    private String eventDescription;
	@Positive(message = "invalid credit cost")
    private int creditCost;

    // ======================================
    // =            Constructors            =
    // ======================================
    public ShoppingCartEventDTO(final Long eventId, final String eventName,final LocalDateTime localDateTime, final String eventDescription, final int creditCost) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDateTime = localDateTime;
        this.eventDescription = eventDescription;
        this.creditCost = creditCost;
    }

    // ======================================
    // =         Getters and Setters        =
    // ======================================
    public Long getEventId() {
        return eventId;
    }
    
    public String getEventName() {
        return eventName;
    }

    public LocalDateTime getEventDateTime() {
        return eventDateTime;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public double getCreditCost() {
        return creditCost;
    }

    @Override
	public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append("EventDTO{");
        buf.append("eventId=").append(getEventId());
        buf.append(",eventDateTime=").append(getEventDateTime());
        buf.append(",description=").append(getEventDescription());
        buf.append(",creditCost=").append(getCreditCost());
        buf.append('}');
        return buf.toString();
    }
}

