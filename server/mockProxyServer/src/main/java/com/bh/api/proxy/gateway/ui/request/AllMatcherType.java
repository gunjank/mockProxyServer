package com.bh.api.proxy.gateway.ui.request;

public class AllMatcherType {

	public interface MatcherType{
		
	}
	
	public enum NumberMatcherType implements MatcherType{
		
		EQUAL_TO_NUMBER,
		GREATER_THAN_NUMBER,
		LESS_THAN_NUMBER,
		GREATER_THAN_EQUAL_TO_NUMBER,
		LESS_THAN_EQUAL_TO_NUMBER,
		NUMBER_REGEX // for any range, start en, collection etc.
	}

	public enum StringMatcherType implements MatcherType{
		ALL_ALPAHBETS,
		ANY,
		IS_ALPHA_NUMERIC, 
		IS_NUMBER, 
		EQUALS_CASE_SENSITIVE, 
		EQUALS_CASE_INSENSITIVE, 
		STARTS_WITH, 
		ENDS_WITH, 
		IS_EMPTY, 
		IS_NULL, 
		CONTAINS_STRING,
		CONTAINS_STRING_REGEX
	}

	public enum BooleanMatcherType implements MatcherType{
		IS_TRUE, 
		IS_FALSE
	}

	public enum DateMatcherType implements MatcherType{
		IS_VALID_DATE_IN_MM_DD_YYYY, 
		IS_VALID_DATE_IN_FORMAT,
		GREATER_THEN_EQUAL_CURRENT_DATE, 
		LESS_THEN_EQUAL_CURRENT_DATE, 
		GREATER_THEN_CURRENT_DATE, 
		LESS_THEN_CURRENT_DATE, 
		GREATER_THEN_DATE_IN_MM_DD_YYYY, 
		LESS_THEN_DATE_IN_MM_DD_YYYY,
		MATCHES_DATE_IN_MM_DD_YYYY
		
	}
	
	public enum StringArrayMatcherType implements MatcherType{
		CONTAINS_ITEM,
		LENGTH_OF,
		CONTAINS_ITEM_REGEX
	}
	
	public enum NumberArrayMatcherType implements MatcherType{
		CONTAINS_ITEM,
		LENGTH_OF,
		CONTAINS_ITEM_GREATER_THAN,
		CONTAINS_ITEM_EQUAL_TO,
		CONTAINS_ITEM_GREATER_THAN_EQUAL,
		CONTAINS_ITEM_LESS_THAN_EQUAL,
		CONTAINS_ITEM_REGEX
	}
	
	public enum BooleanArrayMatcherType implements MatcherType{
		CONTAINS_ALL_ITEM_TRUE,
		CONTAINS_ALL_ITEM_FALSE,
		CONTAINS_ANY_ITEM_TRUE,
		CONTAINS_ANY_ITEM_FALSE
	}

	public enum CustomMatcherType implements MatcherType{
		PHONENUMBER, 
		EMAIL, 
		SSN
	}
}
