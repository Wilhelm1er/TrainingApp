package com.sport.training.domain.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

/**
 * This class is a client view of an event of the Shopping Cart.
 * A shopping cart is made of one event.
 * This class only transfers data from a distant service to a client.
 */
public class ShoppingCartEventDTO {

    // ======================================
    // =             Attributes             =
    // ======================================
	@NotBlank(message = "id must be defined")
    private String eventId;
	@NotBlank(message = "invalid Name")
    private String eventName;
	@NotBlank(message = "invalid Description")
    private String activityDescription;
	@Positive(message = "invalid price")
    private int creditCost;

    // ======================================
    // =            Constructors            =
    // ======================================
    public ShoppingCartEventDTO(final String eventId, final String eventName, final String activityDescription, final int creditCost) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.activityDescription = activityDescription;
        this.creditCost = creditCost;
    }

    // ======================================
    // =         Getters and Setters        =
    // ======================================
    public String getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public int getCreditCost() {
        return creditCost;
    }

    public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append("EventDTO{");
        buf.append("eventId=").append(getEventId());
        buf.append(",eventName=").append(getEventName());
        buf.append(",description=").append(getActivityDescription());
        buf.append(",creditCost=").append(getCreditCost());
        buf.append('}');
        return buf.toString();
    }
}

