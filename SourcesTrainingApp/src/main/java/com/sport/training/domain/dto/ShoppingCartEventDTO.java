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
    private String eventId;
	@NotBlank(message = "invalid event date")
    private Date eventDate;
	@NotBlank(message = "invalid Description")
    private String activityDescription;
	@Positive(message = "invalid credit cost")
    private int creditCost;

    // ======================================
    // =            Constructors            =
    // ======================================
    public ShoppingCartEventDTO(final String eventId, final Date eventDate, final String activityDescription, final int creditCost) {
        this.eventId = eventId;
        this.eventDate = eventDate;
        this.activityDescription = activityDescription;
        this.creditCost = creditCost;
    }

    // ======================================
    // =         Getters and Setters        =
    // ======================================
    public String getEventId() {
        return eventId;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public String getActivityDescription() {
        return activityDescription;
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
        buf.append(",description=").append(getActivityDescription());
        buf.append(",creditCost=").append(getCreditCost());
        buf.append('}');
        return buf.toString();
    }
}

