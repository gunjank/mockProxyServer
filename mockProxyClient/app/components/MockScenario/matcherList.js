var matcherFunctions = {

  "dataTypeMapping": {
    "STRING":"stringMatchers",
    "NUMBER":"numberMatcherType",
    "BOOLEAN":"booleanMatcherType",
    "DATE":"dateMatcherType",
    "CUSTOM":"customMatcherType",
    "STRING_ARRAY":"stringArrayMatcherType",
    "NUMBER_ARRAY":"numberArrayMatcherType",
    "BOOLEAN_ARRAY":"booleanArrayMatcherType"
  },

  "datatypes": [{
    "key": "STRING",
    "value": "String"
  }, {
    "key": "NUMBER",
    "value": "Number"
  }, {
    "key": "BOOLEAN",
    "value": "Boolean"
  }, {
    "key": "DATE",
    "value": "Date"
  }, {
    "key": "CUSTOM",
    "value": "Custom"
  }, {
    "key": "STRING_ARRAY",
    "value": "String Array"
  }, {
    "key": "NUMBER_ARRAY",
    "value": "Number Array"
  }, {
    "key": "BOOLEAN_ARRAY",
    "value": "Boolean Array"
  }],

  "stringMatchers": [{
    "key": "ALL_ALPAHBETS",
    "value": "All Alphabets"
  }, {
    "key": "ANY",
    "value": "Any characters"
  }, {
    "key": "IS_ALPHA_NUMERIC",
    "value": "Is Alpha numeric"
  }, {
    "key": "IS_NUMBER",
    "value": "Is Number"
  }, {
    "key": "EQUALS_CASE_SENSITIVE",
    "value": "Equals Case sensitive"
  }, {
    "key": "EQUALS_CASE_INSENSITIVE",
    "value": "Equals Case Insensitive"
  }, {
    "key": "STARTS_WITH",
    "value": "Starts with"
  }, {
    "key": "ENDS_WITH",
    "value": "Ends With"
  }, {
    "key": "IS_EMPTY",
    "value": "Is Empty"
  }, {
    "key": "IS_NULL",
    "value": "Is null"
  }, {
    "key": "CONTAINS_STRING",
    "value": "All Alphabets"
  }, {
    "key": "CONTAINS_STRING_REGEX",
    "value": "regex"
  }],

  "numberMatcherType": [{
    "key": "EQUAL_TO_NUMBER",
    "value": "Equal to"
  }, {
    "key": "GREATER_THAN_NUMBER",
    "value": "Greater Than"
  }, {
    "key": "LESS_THAN_NUMBER",
    "value": "Less than"
  }, {
    "key": "GREATER_THAN_EQUAL_TO_NUMBER",
    "value": "Greater then Equal To"
  }, {
    "key": "LESS_THAN_EQUAL_TO_NUMBER",
    "value": "Less then Equal To"
  }, {
    "key": "NUMBER_REGEX",
    "value": "Number Regex"
  }],

  "booleanMatcherType": [{
    "key": "IS_TRUE",
    "value": "Is True"
  }, {
    "key": "IS_FALSE",
    "value": "Is False"
  }],

  "dateMatcherType": [{
    "key": "IS_VALID_DATE_IN_MM_DD_YYYY",
    "value": "Is Valid Date in mm:dd:yyyy"
  }, {
    "key": "IS_VALID_DATE_IN_FORMAT",
    "value": "Is Valid Date for format"
  }, {
    "key": "GREATER_THEN_EQUAL_CURRENT_DATE",
    "value": "Greater than equal current date"
  }, {
    "key": "LESS_THEN_EQUAL_CURRENT_DATE",
    "value": "Less than equal current date"
  }, {
    "key": "GREATER_THEN_CURRENT_DATE",
    "value": "Less than current date"
  }, {
    "key": "LESS_THEN_CURRENT_DATE",
    "value": "Less than current date"
  }, {
    "key": "GREATER_THEN_DATE_IN_MM_DD_YYYY",
    "value": "Greater Date in mm:dd:yyyy"
  }, {
    "key": "LESS_THEN_DATE_IN_MM_DD_YYYY",
    "value": "Less Date in mm:dd:yyyy"
  }, {
    "key": "MATCHES_DATE_IN_MM_DD_YYYY",
    "value": "Equal Date in mm:dd:yyyy"
  }],

  "stringArrayMatcherType": [{
    "key": "CONTAINS_ITEM",
    "value": "contains item"
  }, {
    "key": "LENGTH_OF",
    "value": "Length"
  }, {
    "key": "CONTAINS_ITEM_REGEX",
    "value": "Contains Item for regex"
  }],

  "numberArrayMatcherType": [{
    "key": "CONTAINS_ITEM",
    "value": "Contains item"
  }, {
    "key": "LENGTH_OF",
    "value": "Length"
  }, {
    "key": "CONTAINS_ITEM_GREATER_THAN",
    "value": "Contains Item Greater than"
  }, {
    "key": "CONTAINS_ITEM_EQUAL_TO",
    "value": "contains item equal to"
  }, {
    "key": "CONTAINS_ITEM_GREATER_THAN_EQUAL",
    "value": "Contains Item Greater than equal"
  }, {
    "key": "CONTAINS_ITEM_LESS_THAN_EQUAL",
    "value": "Contains Item less than equal"
  }, {
    "key": "CONTAINS_ITEM_REGEX",
    "value": "Contains Item for regex"
  }],

  "booleanArrayMatcherType": [{
    "key": "CONTAINS_ALL_ITEM_TRUE",
    "value": "Contains all true"
  }, {
    "key": "CONTAINS_ALL_ITEM_FALSE",
    "value": "Contains all false"
  }, {
    "key": "CONTAINS_ANY_ITEM_TRUE",
    "value": "Contains any true"
  }, {
    "key": "CONTAINS_ANY_ITEM_FALSE",
    "value": "Contains all false"
  }],

  "customMatcherType": [{
    "key": "PHONENUMBER",
    "value": "Phone Number"
  }, {
    "key": "EMAIL",
    "value": "Email"
  }, {
    "key": "SSN",
    "value": "SSN"
  }]

}

export {
  matcherFunctions
};
