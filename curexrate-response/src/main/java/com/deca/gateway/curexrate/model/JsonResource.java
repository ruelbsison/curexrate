package com.deca.gateway.curexrate.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.io.Serializable;
import org.bson.types.ObjectId;

@Document(collection = "json_resource")
public class JsonResource implements Serializable {

    private static final long serialVersionUID = 950340958092341L;

	@Id
	protected ObjectId id;

    @Indexed
    private String resourceId;

    private String json;

    public static class Builder {
        
        JsonResource jsonResource = new JsonResource();

        public Builder(String resourceId, String json) {
            jsonResource.id = new ObjectId();
            jsonResource.resourceId = resourceId;
            jsonResource.json = json;
        }

        public JsonResource build() {
			return jsonResource;
		}
		
		// public Builder setResourceId(String resourceId) {
		// 	jsonResource.resourceId = resourceId;
		// 	return this;
		// }
    }

    public ObjectId getId() {
		return id;
	}

    public String getResourceId() {
		return resourceId;
	}

    public String getJson() {
        return json;
    }

    @Override
	public String toString() {
		return "[resourceId=" + this.resourceId + ",json=" + this.json + "]";
	}
}
