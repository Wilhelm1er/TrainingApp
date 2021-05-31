package com.sport.training.domain.dto;

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
	@NotBlank(message = "invalid event date")
    private Date eventDate;
	@NotBlank(message = "invalid Description")
    private String eventDescription;
	@Positive(message = "invalid credit cost")
    private int creditCost;

    // ======================================
    // =            Constructors            =
    // ======================================
    public ShoppingCartEventDTO(final Long eventId, final String eventName,final Date eventDate, final String eventDescription, final int creditCost) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDate = eventDate;
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

    public Date getEventDate() {
        return eventDate;
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
        buf.append(",eventDate=").append(getEventDate());
        buf.append(",description=").append(getEventDescription());
        buf.append(",creditCost=").append(getCreditCost());
        buf.append('}');
        return buf.toString();
    }
}

