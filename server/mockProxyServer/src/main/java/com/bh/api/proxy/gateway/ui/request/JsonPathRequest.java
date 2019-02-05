package com.bh.api.proxy.gateway.ui.request;

import java.io.Serializable;

import com.bh.api.proxy.gateway.model.MockModeller.AttributeType;
import com.bh.api.proxy.gateway.ui.ValidationException;
import com.bh.api.proxy.gateway.ui.request.AllMatcherType.BooleanArrayMatcherType;
import com.bh.api.proxy.gateway.ui.request.AllMatcherType.BooleanMatcherType;
import com.bh.api.proxy.gateway.ui.request.AllMatcherType.CustomMatcherType;
import com.bh.api.proxy.gateway.ui.request.AllMatcherType.DateMatcherType;
import com.bh.api.proxy.gateway.ui.request.AllMatcherType.MatcherType;
import com.bh.api.proxy.gateway.ui.request.AllMatcherType.NumberArrayMatcherType;
import com.bh.api.proxy.gateway.ui.request.AllMatcherType.NumberMatcherType;
import com.bh.api.proxy.gateway.ui.request.AllMatcherType.StringArrayMatcherType;
import com.bh.api.proxy.gateway.ui.request.AllMatcherType.StringMatcherType;

public class JsonPathRequest implements Serializable{

	private static final long serialVersionUID = 1L;

	String jsonPath;
	String templateKey;
	String toBeMatchedString;
	String type;
	AttributeType attributeType;
	Class<? extends MatcherType> matcherType;

	public String getJsonPath() {
		return jsonPath;
	}

	public AttributeType getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(AttributeType attributeType) {
		this.attributeType = attributeType;
	}

	public void setJsonPath(String jsonPath) {
		this.jsonPath = jsonPath;
	}

	public String getTemplateKey() {
		return templateKey;
	}

	public void setTemplateKey(String templateKey) {
		this.templateKey = templateKey;
	}

	public String getToBeMatchedString() {
		return toBeMatchedString;
	}

	public void setToBeMatchedString(String toBeMatchedString) {
		this.toBeMatchedString = toBeMatchedString;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	// validation method to set the matchers
	public void init() throws ValidationException {
		if (AttributeType.STRING.equals(attributeType)) {
			if (StringMatcherType.valueOf(type) != null) {
				this.matcherType = StringMatcherType.class;
			} else {
				throw new ValidationException("INVALID_MATCHER_FOUND", "INVALID_MATCHER_EXCEPTION");
			}
		} else if (AttributeType.NUMBER.equals(attributeType)) {
			if (NumberMatcherType.valueOf(type) != null) {
				this.matcherType = NumberMatcherType.class;
			} else {
				throw new ValidationException("INVALID_MATCHER_FOUND", "INVALID_MATCHER_EXCEPTION");
			}
		} else if (AttributeType.BOOLEAN.equals(attributeType)) {
			if (BooleanMatcherType.valueOf(type) != null) {
				this.matcherType = BooleanMatcherType.class;
			} else {
				throw new ValidationException("INVALID_MATCHER_FOUND", "INVALID_MATCHER_EXCEPTION");
			}
		} else if (AttributeType.DATE.equals(attributeType)) {
			if (DateMatcherType.valueOf(type) != null) {
				this.matcherType = DateMatcherType.class;
			} else {
				throw new ValidationException("INVALID_MATCHER_FOUND", "INVALID_MATCHER_EXCEPTION");
			}
		} else if (AttributeType.STRING_ARRAY.equals(attributeType)) {
			if (StringArrayMatcherType.valueOf(type) != null) {
				this.matcherType = StringArrayMatcherType.class;
			} else {
				throw new ValidationException("INVALID_MATCHER_FOUND", "INVALID_MATCHER_EXCEPTION");
			}
		} else if (AttributeType.NUMBER_ARRAY.equals(attributeType)) {
			if (NumberArrayMatcherType.valueOf(type) != null) {
				this.matcherType = NumberArrayMatcherType.class;
			} else {
				throw new ValidationException("INVALID_MATCHER_FOUND", "INVALID_MATCHER_EXCEPTION");
			}
		} else if (AttributeType.BOOLEAN_ARRAY.equals(attributeType)) {
			if (BooleanArrayMatcherType.valueOf(type) != null) {
				this.matcherType = BooleanArrayMatcherType.class;
			} else {
				throw new ValidationException("INVALID_MATCHER_FOUND", "INVALID_MATCHER_EXCEPTION");
			}
		} else if (AttributeType.CUSTOM.equals(attributeType)) {
			if (CustomMatcherType.valueOf(type) != null) {
				this.matcherType = CustomMatcherType.class;
			} else {
				throw new ValidationException("INVALID_MATCHER_FOUND", "INVALID_MATCHER_EXCEPTION");
			}
		} else {
			throw new ValidationException("INVALID_MATCHER_FOUND", "INVALID_MATCHER_EXCEPTION");
		}
	}
}