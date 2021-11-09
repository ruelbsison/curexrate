package com.deca.gateway.curexrate.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import org.bson.types.ObjectId;

//@Document(collection = "jsonResource")
public class JsonResource implements Serializable {

    private static final long serialVersionUID = 543530384723981L;

    private String id;

    private String json;

    public JsonResource() {

    }

    public JsonResource(String id, String json) {
        this.id = id;
        this.json = json;
    }

    public String getId() {
        return id;
    }

    public String getJson() {
        return json;
    }

    @Override
	public String toString() {
		return "[id=" + this.id + ",json=" + this.json + "]";
	}
}
