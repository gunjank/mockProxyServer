package com.bh.api.proxy.gateway.model;

import static org.junit.Assert.assertEquals;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.bh.api.proxy.gateway.ui.request.AllMatcherType.BooleanArrayMatcherType;
import com.bh.api.proxy.gateway.ui.request.AllMatcherType.BooleanMatcherType;
import com.bh.api.proxy.gateway.ui.request.AllMatcherType.CustomMatcherType;
import com.bh.api.proxy.gateway.ui.request.AllMatcherType.DateMatcherType;
import com.bh.api.proxy.gateway.ui.request.AllMatcherType.MatcherType;
import com.bh.api.proxy.gateway.ui.request.AllMatcherType.NumberArrayMatcherType;
import com.bh.api.proxy.gateway.ui.request.AllMatcherType.NumberMatcherType;
import com.bh.api.proxy.gateway.ui.request.AllMatcherType.StringArrayMatcherType;
import com.bh.api.proxy.gateway.ui.request.AllMatcherType.StringMatcherType;

public class MockModeller implements Serializable{

	private static final long serialVersionUID = 1L;

	public Node buildNode(String path, String templateKey, AttributeType attributeType, Type requestType, Type responseType, String type, String tobeMatchedValue) {
		// setting default matcher type as any..
		MatcherType matcherType = StringMatcherType.ANY;
		Function<Object, Boolean> ruleFunction = (ob) -> {
			return false;
		};
		try {
			if (AttributeType.STRING.equals(attributeType)) {
				matcherType = StringMatcherType.valueOf(type);
				ruleFunction = getCoreMatchersForString(matcherType, tobeMatchedValue);
			} else if (AttributeType.NUMBER.equals(attributeType)) {
				matcherType = NumberMatcherType.valueOf(type);
				ruleFunction = getCoreMatchersForNumbers(matcherType, tobeMatchedValue);
			} else if (AttributeType.BOOLEAN.equals(attributeType)) {
				matcherType = BooleanMatcherType.valueOf(type);
				ruleFunction = getCoreMatchersForBoolean(matcherType, tobeMatchedValue);
			} else if (AttributeType.DATE.equals(attributeType)) {
				matcherType = DateMatcherType.valueOf(type);
				ruleFunction = getCoreMatchersForDate(matcherType, tobeMatchedValue);
			} else if (AttributeType.CUSTOM.equals(attributeType)) {
				matcherType = CustomMatcherType.valueOf(type);
				ruleFunction = getCoreMatchersForCustom(matcherType, tobeMatchedValue);
			} else if (AttributeType.STRING_ARRAY.equals(attributeType)) {
				matcherType = StringArrayMatcherType.valueOf(type);
				ruleFunction = getCoreMatchersForStringArray(matcherType, tobeMatchedValue);
			} else if (AttributeType.NUMBER_ARRAY.equals(attributeType)) {
				matcherType = NumberArrayMatcherType.valueOf(type);
				ruleFunction = getCoreMatchersForNumberArray(matcherType, tobeMatchedValue);
			} else if (AttributeType.BOOLEAN_ARRAY.equals(attributeType)) {
				matcherType = BooleanArrayMatcherType.valueOf(type);
				ruleFunction = getCoreMatchersForBooleanArray(matcherType, tobeMatchedValue);
			} else {
				// not supported matcher type..
			}
		} catch (Exception e) {

		}

		Node node = new Node(path, templateKey, attributeType, requestType, responseType, matcherType, tobeMatchedValue);
		node.setToBeMatchedValueForViewer(tobeMatchedValue);
		node.setRuleFunction(ruleFunction);
		return node;
	}

	/**
	 * 
	 * @param matcherType
	 * @param tobeMatchedValue
	 * @return
	 */
	private Function<Object, Boolean> getCoreMatchersForBooleanArray(MatcherType matcherType, String tobeMatchedValue) {
		if (BooleanArrayMatcherType.CONTAINS_ALL_ITEM_TRUE.equals(matcherType)) {
			return (ob) -> {
				try {
					List<Boolean> booleanList = (List<Boolean>) ob;
					return !(booleanList.stream().filter(item -> !item).findFirst().isPresent());
				} catch (Throwable e) {
					return false;
				}
			};
		} else if (BooleanArrayMatcherType.CONTAINS_ALL_ITEM_FALSE.equals(matcherType)) {
			return (ob) -> {
				try {
					List<Boolean> booleanList = (List<Boolean>) ob;
					return !(booleanList.stream().filter(item -> item).findFirst().isPresent());
				} catch (Throwable e) {
					return false;
				}
			};
		} else if (BooleanArrayMatcherType.CONTAINS_ANY_ITEM_TRUE.equals(matcherType)) {
			return (ob) -> {
				try {
					List<Boolean> booleanList = (List<Boolean>) ob;
					return booleanList.stream().filter(item -> item).findFirst().isPresent();
				} catch (Throwable e) {
					return false;
				}
			};
		} else if (BooleanArrayMatcherType.CONTAINS_ANY_ITEM_FALSE.equals(matcherType)) {
			return (ob) -> {
				try {
					List<Boolean> booleanList = (List<Boolean>) ob;
					return booleanList.stream().filter(item -> !item).findFirst().isPresent();
				} catch (Throwable e) {
					return false;
				}
			};
		} else {
			return (ob) -> {
				return false;
			};
		}
	}

	/**
	 * Right now only integer is supported..
	 * @param matcherType
	 * @param tobeMatchedValue
	 * @return
	 */
	private Function<Object, Boolean> getCoreMatchersForNumberArray(MatcherType matcherType, String tobeMatchedValue) {

		if (NumberArrayMatcherType.CONTAINS_ITEM.equals(matcherType)) {
			return (ob) -> {
				try {
					
					return true;
				} catch (Throwable e) {
					return false;
				}
			};
		} else if (NumberArrayMatcherType.LENGTH_OF.equals(matcherType)) {
			return (ob) -> {
				try {

					return true;
				} catch (Throwable e) {
					return false;
				}
			};
		} else if (NumberArrayMatcherType.CONTAINS_ITEM_GREATER_THAN.equals(matcherType)) {
			return (ob) -> {
				try {

					return true;
				} catch (Throwable e) {
					return false;
				}
			};
		} else if (NumberArrayMatcherType.CONTAINS_ITEM_GREATER_THAN_EQUAL.equals(matcherType)) {
			return (ob) -> {
				try {

					return true;
				} catch (Throwable e) {
					return false;
				}
			};
		} else if (NumberArrayMatcherType.CONTAINS_ITEM_LESS_THAN_EQUAL.equals(matcherType)) {
			return (ob) -> {
				try {

					return true;
				} catch (Throwable e) {
					return false;
				}
			};
		} else if (NumberArrayMatcherType.CONTAINS_ITEM_REGEX.equals(matcherType)) {
			return (ob) -> {
				try {

					return true;
				} catch (Throwable e) {
					return false;
				}
			};
		} else {
			return (ob) -> {
				return false;
			};
		}
	}

	/**
	 * 
	 * @param matcherType
	 * @param tobeMatchedValue
	 * @return
	 */
	private Function<Object, Boolean> getCoreMatchersForStringArray(MatcherType matcherType, String tobeMatchedValue) {

		if (StringArrayMatcherType.CONTAINS_ITEM.equals(matcherType)) {
			return (ob) -> {
				try {
					List<String> list = (List<String>)ob;
					Optional<String> stringMatchFound = list.stream().filter(item -> item.contains(tobeMatchedValue)).findFirst();
					return stringMatchFound.isPresent();
				} catch (Throwable e) {
					return false;
				}
			};
		} else if (StringArrayMatcherType.LENGTH_OF.equals(matcherType)) {
			return (ob) -> {
				try {
					List<String> list = (List<String>)ob;
					return (list.size() == Integer.parseInt(tobeMatchedValue));
				} catch (Throwable e) {
					return false;
				}
			};
		} else if (StringArrayMatcherType.CONTAINS_ITEM_REGEX.equals(matcherType)) {
			return (ob) -> {
				try {
					List<String> list = (List<String>)ob;
					Optional<String> stringMatchFound = list.stream().filter(item -> item.matches(tobeMatchedValue)).findFirst();
					return stringMatchFound.isPresent();
					
				} catch (Throwable e) {
					return false;
				}
			};
		} else {
			return (ob) -> {
				return false;
			};
		}
	}

	/**
	 * 
	 * @param matcherType
	 * @param tobeMatchedValue
	 * @return
	 */
	private Function<Object, Boolean> getCoreMatchersForCustom(MatcherType matcherType, String tobeMatchedValue) {
		if (CustomMatcherType.PHONENUMBER.equals(matcherType)) {
			return (ob) -> {
				try {
					return ob.toString().matches("((\\(\\d{3}\\) ?)|(\\d{3}-))?\\d{3}-\\d{4}");
				} catch (Throwable e) {
					return false;
				}
			};
		} else if (CustomMatcherType.EMAIL.equals(matcherType)) {
			return (ob) -> {
				try {
					return ob.toString().matches("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
				} catch (Throwable e) {
					return false;
				}
			};
		} else if (CustomMatcherType.SSN.equals(matcherType)) {
			return (ob) -> {
				try {
					return ob.toString().matches("((\\(\\d{3}\\) ?)|(\\d{3}-))?\\d{2}-\\d{4}");
				} catch (Throwable e) {
					return false;
				}
			};
		} else {
			return (ob) -> {
				return false;
			};
		}
	}

	/**
	 * 
	 * @param matcherType
	 * @param toBeMatchedString
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Function<Object, Boolean> getCoreMatchersForNumbers(final MatcherType matcherType, final String toBeMatchedString) {

		if (NumberMatcherType.EQUAL_TO_NUMBER.equals(matcherType)) {
			return (ob) -> {
				try {
					if(ob instanceof Integer) {
						return (Integer.parseInt(ob.toString()) == Integer.parseInt(toBeMatchedString));
					}else if(ob instanceof Double) {
						return (Double.parseDouble(ob.toString()) == Double.parseDouble(toBeMatchedString));
					}else if(ob instanceof List) {
						return !((List)ob).stream().filter(item -> {
							if(item instanceof Integer) {
								return !(Integer.parseInt(item.toString()) == Integer.parseInt(toBeMatchedString));
							}else if(ob instanceof Double) {
								return !(Double.parseDouble(item.toString()) == Double.parseDouble(toBeMatchedString));
							}else {
								return false;
							}
						}).findFirst().isPresent();
					}else {
						return false;
					}
				} catch (Throwable e) {
					return false;
				}
			};
		} else if (NumberMatcherType.LESS_THAN_NUMBER.equals(matcherType)) {
				return (ob) -> {
					try {
						if(ob instanceof Integer) {
							return (Integer.parseInt(ob.toString()) < Integer.parseInt(toBeMatchedString));
						}else if(ob instanceof Double) {
							return (Double.parseDouble(ob.toString()) < Double.parseDouble(toBeMatchedString));
						}else if(ob instanceof List) {
							return !((List)ob).stream().filter(item -> {
								if(item instanceof Integer) {
									return !(Integer.parseInt(item.toString()) < Integer.parseInt(toBeMatchedString));
								}else if(ob instanceof Double) {
									return !(Double.parseDouble(item.toString()) < Double.parseDouble(toBeMatchedString));
								}else {
									return false;
								}
							}).findFirst().isPresent();
						}else {
							return false;
						}
					} catch (Throwable e) {
						return false;
					}
				};
			}else if (NumberMatcherType.GREATER_THAN_NUMBER.equals(matcherType)) {
			return (ob) -> {
				try {
					if(ob instanceof Integer) {
						return (Integer.parseInt(ob.toString()) > Integer.parseInt(toBeMatchedString));
					}else if(ob instanceof Double) {
						return (Double.parseDouble(ob.toString()) > Double.parseDouble(toBeMatchedString));
					}else if(ob instanceof List) {
						return !((List)ob).stream().filter(item -> {
							if(item instanceof Integer) {
								return !(Integer.parseInt(item.toString()) > Integer.parseInt(toBeMatchedString));
							}else if(ob instanceof Double) {
								return !(Double.parseDouble(item.toString()) > Double.parseDouble(toBeMatchedString));
							}else {
								return false;
							}
						}).findFirst().isPresent();
					}else {
						return false;
					}
				} catch (Throwable e) {
					return false;
				}
			};
		} else if (NumberMatcherType.GREATER_THAN_EQUAL_TO_NUMBER.equals(matcherType)) {
			return (ob) -> {
				try {
					if(ob instanceof Integer) {
						return (Integer.parseInt(ob.toString()) >= Integer.parseInt(toBeMatchedString));
					}else if(ob instanceof Double) {
						return (Double.parseDouble(ob.toString()) >= Double.parseDouble(toBeMatchedString));
					}else if(ob instanceof List) {
						return !((List)ob).stream().filter(item -> {
							if(item instanceof Integer) {
								return !(Integer.parseInt(item.toString()) >= Integer.parseInt(toBeMatchedString));
							}else if(ob instanceof Double) {
								return !(Double.parseDouble(item.toString()) >= Double.parseDouble(toBeMatchedString));
							}else {
								return false;
							}
						}).findFirst().isPresent();
					}else {
						return false;
					}
				} catch (Throwable e) {
					return false;
				}
			};
		} else if (NumberMatcherType.NUMBER_REGEX.equals(matcherType)) {
			return (ob) -> {
				try {
					if(ob instanceof Integer || ob instanceof Double) {
						return ob.toString().matches(toBeMatchedString);
					}else if(ob instanceof List) {
						return !((List)ob).stream().filter(item -> {
							if(item instanceof Integer || item instanceof Double) {
								return !(item.toString().matches(toBeMatchedString));
							}else {
								return false;
							}
						}).findFirst().isPresent();
					}else {
						return false;
					}
				} catch (Throwable e) {
					return false;
				}
			};
		} else {
			return (ob) -> {
				return false;
			};
		}

	}

	
	/**
	 * Run matchers rules
	 * @return
	 */
	public BiFunction<Object, String, Boolean> runMatchers(){
		return (ob, path) -> {
			try {
				if(ob instanceof String) {
					return ob.toString().matches(path);
				}else if(ob instanceof List) {
					return !((List)ob).stream().filter(item -> {return !(item.toString().matches(path));}).findFirst().isPresent();
				}else {
					return false;
				}
			}catch(Exception e) {
				return false;
			}
		};
	} 
	/**
	 * Core matchers for String
	 * @param matcherType
	 * @param toBeMatchedString
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Function<Object, Boolean> getCoreMatchersForString(final MatcherType matcherType, final String toBeMatchedString) {

		// extracted value from response(its an object), boolean matcher
		if (StringMatcherType.ALL_ALPAHBETS.equals(matcherType)) {
			return (ob) -> {
				return runMatchers().apply(ob, "[a-zA-Z]+");
			};
		} else if (StringMatcherType.ANY.equals(matcherType)) {
			return (ob) -> {
				return runMatchers().apply(ob, ".+");
			};
		} else if (StringMatcherType.IS_ALPHA_NUMERIC.equals(matcherType)) {
			return (ob) -> {
				return runMatchers().apply(ob, "([A-Za-z]+[0-9]|[0-9]+[A-Za-z])[A-Za-z0-9]*");
			};
		} else if (StringMatcherType.IS_NUMBER.equals(matcherType)) {
			return (ob) -> {
				return runMatchers().apply(ob, "[1-9]+");
			};
		} else if (StringMatcherType.EQUALS_CASE_SENSITIVE.equals(matcherType)) {
			return (ob) -> {
				try {
					if(ob instanceof String) {
						assertEquals(ob.toString(), toBeMatchedString);
						return true;
					}else if(ob instanceof List) {
						return !((List)ob).stream().filter(item -> {
							try {
								assertEquals(item.toString(), toBeMatchedString);
								return false;
							}catch(Exception e) {
								return true;
							}
						}).findFirst().isPresent();
					}else {
						return false;
					}
				}catch(Exception e) {
					return false;
				}
			};
		} else if (StringMatcherType.EQUALS_CASE_INSENSITIVE.equals(matcherType)) {
			return (ob) -> {
				try {
					if(ob instanceof String) {
						return ob.toString().toUpperCase().equals(toBeMatchedString.toUpperCase());
					}else if(ob instanceof List) {
						return !((List)ob).stream().filter(item -> {
							try {
								 return !(ob.toString().toUpperCase().equals(toBeMatchedString.toUpperCase()));
							}catch(Exception e) {
								return true;
							}
						}).findFirst().isPresent();
					}else {
						return false;
					}
				} catch (Throwable e) {
					return false;
				}
			};
		} else if (StringMatcherType.CONTAINS_STRING.equals(matcherType)) {
			return (ob) -> {
				try {

					if(ob instanceof String) {
						return ob.toString().trim().contains(toBeMatchedString);
					}else if(ob instanceof List) {
						return !((List)ob).stream().filter(item -> {
							try {
								 return !(ob.toString().trim().contains(toBeMatchedString));
							}catch(Exception e) {
								return true;
							}
						}).findFirst().isPresent();
					}else {
						return false;
					}
				} catch (Throwable e) {
					return false;
				}
			};
		}

		else if (StringMatcherType.CONTAINS_STRING_REGEX.equals(matcherType)) {
			return (ob) -> {
				try {
					return runMatchers().apply(ob, toBeMatchedString);
				} catch (Throwable e) {
					return false;
				}
			};
		} else {
			// default case, no matcher found, for the selected path..
			return (ob) -> {
				return false;
			};
		}

	}

	/**
	 * 
	 * @param matcherType
	 * @param toBeMatchedString
	 * @return
	 */
	private Function<Object, Boolean> getCoreMatchersForBoolean(MatcherType matcherType, String toBeMatchedString) {

		if (BooleanMatcherType.IS_TRUE.equals(matcherType)) {
			return (ob) -> {
				try {
					Boolean val = (Boolean) ob;
					if (val) {
						return true;
					}
					return false;
				} catch (Throwable e) {
					return false;
				}
			};
		} else if (BooleanMatcherType.IS_FALSE.equals(matcherType)) {
			return (ob) -> {
				try {
					Boolean val = (Boolean) ob;
					if (!val) {
						return true;
					}
					return false;
				} catch (Throwable e) {
					return false;
				}
			};
		} else {
			// default case, no matcher found, for the selected path..
			return (ob) -> {
				return false;
			};
		}
	}

	/**
	 * Right now format of date is not supported., only mm< /:- >dd </:-> yyyy is only
	 * supported.. delimiter supported are - and / and :
	 * 
	 * @param matcherType
	 * @param toBeMatchedString
	 * @return
	 */
	private Function<Object, Boolean> getCoreMatchersForDate(final MatcherType matcherType, final String toBeMatchedString) {

		if (DateMatcherType.IS_VALID_DATE_IN_MM_DD_YYYY.equals(matcherType)) {
			return (ob) -> {
				try {
					String[] splitDate = ob.toString().split("/");
					if (splitDate.length == 3) {
						LocalDate.of(Integer.parseInt(splitDate[2]), Month.of(Integer.parseInt(splitDate[1])), Integer.parseInt(splitDate[0]));
						return true;
					} else {
						splitDate = ob.toString().split("-");
						if (splitDate.length == 3) {
							LocalDate.of(Integer.parseInt(splitDate[2]), Month.of(Integer.parseInt(splitDate[1])), Integer.parseInt(splitDate[0]));
							return true;
						} else {
							splitDate = ob.toString().split(":");
							if (splitDate.length == 3) {
								LocalDate.of(Integer.parseInt(splitDate[2]), Month.of(Integer.parseInt(splitDate[1])), Integer.parseInt(splitDate[0]));
								return true;
							} else {
								// does not match any criteria..
								return false;
							}
						}
					}
				} catch (Throwable e) {
					return false;
				}
			};
		} else if (DateMatcherType.IS_VALID_DATE_IN_FORMAT.equals(matcherType)) {
			return (ob) -> {
				try {
					LocalDate.parse(ob.toString(), DateTimeFormatter.ofPattern(toBeMatchedString));
					return true;
				} catch (Throwable e) {
					return false;
				}
			};
		} else if (DateMatcherType.GREATER_THEN_EQUAL_CURRENT_DATE.equals(matcherType)) {
			return (ob) -> {
				try {
					String[] splitDate = ob.toString().split("/");
					if (splitDate.length == 3) {
						LocalDate date = LocalDate.of(Integer.parseInt(splitDate[2]), Month.of(Integer.parseInt(splitDate[1])), Integer.parseInt(splitDate[0]));
						if (date.compareTo(LocalDate.now()) >= 0) {
							return true;
						}
						return false;
					} else {
						splitDate = ob.toString().split("-");
						if (splitDate.length == 3) {
							LocalDate date = LocalDate.of(Integer.parseInt(splitDate[2]), Month.of(Integer.parseInt(splitDate[1])),
									Integer.parseInt(splitDate[0]));
							if (date.compareTo(LocalDate.now()) >= 0) {
								return true;
							}
							return false;
						} else {
							splitDate = ob.toString().split(":");
							if (splitDate.length == 3) {
								LocalDate date = LocalDate.of(Integer.parseInt(splitDate[2]), Month.of(Integer.parseInt(splitDate[1])),
										Integer.parseInt(splitDate[0]));
								if (date.compareTo(LocalDate.now()) >= 0) {
									return true;
								}
								return false;
							} else {
								// does not match any criteria..
								return false;
							}
						}
					}
				} catch (Throwable e) {
					return false;
				}
			};
		} else if (DateMatcherType.LESS_THEN_EQUAL_CURRENT_DATE.equals(matcherType)) {
			return (ob) -> {
				try {
					String[] splitDate = ob.toString().split("/");
					if (splitDate.length == 3) {
						LocalDate date = LocalDate.of(Integer.parseInt(splitDate[2]), Month.of(Integer.parseInt(splitDate[1])), Integer.parseInt(splitDate[0]));
						if (date.compareTo(LocalDate.now()) <= 0) {
							return true;
						}
						return false;
					} else {
						splitDate = ob.toString().split("-");
						if (splitDate.length == 3) {
							LocalDate date = LocalDate.of(Integer.parseInt(splitDate[2]), Month.of(Integer.parseInt(splitDate[1])),
									Integer.parseInt(splitDate[0]));
							if (date.compareTo(LocalDate.now()) <= 0) {
								return true;
							}
							return false;
						} else {
							splitDate = ob.toString().split(":");
							if (splitDate.length == 3) {
								LocalDate date = LocalDate.of(Integer.parseInt(splitDate[2]), Month.of(Integer.parseInt(splitDate[1])),
										Integer.parseInt(splitDate[0]));
								if (date.compareTo(LocalDate.now()) <= 0) {
									return true;
								}
								return false;
							} else {
								// does not match any criteria..
								return false;
							}
						}
					}
				} catch (Throwable e) {
					return false;
				}
			};
		} else if (DateMatcherType.GREATER_THEN_CURRENT_DATE.equals(matcherType)) {
			return (ob) -> {
				try {
					String[] splitDate = ob.toString().split("/");
					if (splitDate.length == 3) {
						LocalDate date = LocalDate.of(Integer.parseInt(splitDate[2]), Month.of(Integer.parseInt(splitDate[1])), Integer.parseInt(splitDate[0]));
						if (date.compareTo(LocalDate.now()) < 0) {
							return true;
						}
						return false;
					} else {
						splitDate = ob.toString().split("-");
						if (splitDate.length == 3) {
							LocalDate date = LocalDate.of(Integer.parseInt(splitDate[2]), Month.of(Integer.parseInt(splitDate[1])),
									Integer.parseInt(splitDate[0]));
							if (date.compareTo(LocalDate.now()) < 0) {
								return true;
							}
							return false;
						} else {
							splitDate = ob.toString().split(":");
							if (splitDate.length == 3) {
								LocalDate date = LocalDate.of(Integer.parseInt(splitDate[2]), Month.of(Integer.parseInt(splitDate[1])),
										Integer.parseInt(splitDate[0]));
								if (date.compareTo(LocalDate.now()) < 0) {
									return true;
								}
								return false;
							} else {
								// does not match any criteria..
								return false;
							}
						}
					}
				} catch (Throwable e) {
					return false;
				}
			};
		} else if (DateMatcherType.GREATER_THEN_DATE_IN_MM_DD_YYYY.equals(matcherType)) {
			return (ob) -> {
				try {
					String[] splitDateToBeMatched = toBeMatchedString.split("/");
					LocalDate toBeMatchedDate = LocalDate.now();

					if (splitDateToBeMatched.length == 3) {
						toBeMatchedDate = LocalDate.of(Integer.parseInt(splitDateToBeMatched[2]), Month.of(Integer.parseInt(splitDateToBeMatched[1])),
								Integer.parseInt(splitDateToBeMatched[0]));
					} else {
						splitDateToBeMatched = ob.toString().split("-");
						if (splitDateToBeMatched.length == 3) {
							toBeMatchedDate = LocalDate.of(Integer.parseInt(splitDateToBeMatched[2]), Month.of(Integer.parseInt(splitDateToBeMatched[1])),
									Integer.parseInt(splitDateToBeMatched[0]));
						} else {
							splitDateToBeMatched = ob.toString().split(":");
							if (splitDateToBeMatched.length == 3) {
								toBeMatchedDate = LocalDate.of(Integer.parseInt(splitDateToBeMatched[2]), Month.of(Integer.parseInt(splitDateToBeMatched[1])),
										Integer.parseInt(splitDateToBeMatched[0]));
							} else {
								return false;
							}
						}
					}

					String[] splitDate = ob.toString().split("/");
					if (splitDate.length == 3) {
						LocalDate date = LocalDate.of(Integer.parseInt(splitDate[2]), Month.of(Integer.parseInt(splitDate[1])), Integer.parseInt(splitDate[0]));
						if (date.compareTo(toBeMatchedDate) > 0) {
							return true;
						}
						return false;
					} else {
						splitDate = ob.toString().split("-");
						if (splitDate.length == 3) {
							LocalDate date = LocalDate.of(Integer.parseInt(splitDate[2]), Month.of(Integer.parseInt(splitDate[1])),
									Integer.parseInt(splitDate[0]));
							if (date.compareTo(toBeMatchedDate) > 0) {
								return true;
							}
							return false;
						} else {
							splitDate = ob.toString().split(":");
							if (splitDate.length == 3) {
								LocalDate date = LocalDate.of(Integer.parseInt(splitDate[2]), Month.of(Integer.parseInt(splitDate[1])),
										Integer.parseInt(splitDate[0]));
								if (date.compareTo(toBeMatchedDate) > 0) {
									return true;
								}
								return false;
							} else {
								// does not match any criteria..
								return false;
							}
						}
					}

				} catch (Throwable e) {
					return false;
				}
			};
		} else if (DateMatcherType.LESS_THEN_DATE_IN_MM_DD_YYYY.equals(matcherType)) {
			return (ob) -> {
				try {
					String[] splitDateToBeMatched = toBeMatchedString.split("/");
					LocalDate toBeMatchedDate = LocalDate.now();

					if (splitDateToBeMatched.length == 3) {
						toBeMatchedDate = LocalDate.of(Integer.parseInt(splitDateToBeMatched[2]), Month.of(Integer.parseInt(splitDateToBeMatched[1])),
								Integer.parseInt(splitDateToBeMatched[0]));
					} else {
						splitDateToBeMatched = ob.toString().split("-");
						if (splitDateToBeMatched.length == 3) {
							toBeMatchedDate = LocalDate.of(Integer.parseInt(splitDateToBeMatched[2]), Month.of(Integer.parseInt(splitDateToBeMatched[1])),
									Integer.parseInt(splitDateToBeMatched[0]));
						} else {
							splitDateToBeMatched = ob.toString().split(":");
							if (splitDateToBeMatched.length == 3) {
								toBeMatchedDate = LocalDate.of(Integer.parseInt(splitDateToBeMatched[2]), Month.of(Integer.parseInt(splitDateToBeMatched[1])),
										Integer.parseInt(splitDateToBeMatched[0]));
							} else {
								return false;
							}
						}
					}

					String[] splitDate = ob.toString().split("/");
					if (splitDate.length == 3) {
						LocalDate date = LocalDate.of(Integer.parseInt(splitDate[2]), Month.of(Integer.parseInt(splitDate[1])), Integer.parseInt(splitDate[0]));
						if (date.compareTo(toBeMatchedDate) < 0) {
							return true;
						}
						return false;
					} else {
						splitDate = ob.toString().split("-");
						if (splitDate.length == 3) {
							LocalDate date = LocalDate.of(Integer.parseInt(splitDate[2]), Month.of(Integer.parseInt(splitDate[1])),
									Integer.parseInt(splitDate[0]));
							if (date.compareTo(toBeMatchedDate) < 0) {
								return true;
							}
							return false;
						} else {
							splitDate = ob.toString().split(":");
							if (splitDate.length == 3) {
								LocalDate date = LocalDate.of(Integer.parseInt(splitDate[2]), Month.of(Integer.parseInt(splitDate[1])),
										Integer.parseInt(splitDate[0]));
								if (date.compareTo(toBeMatchedDate) < 0) {
									return true;
								}
								return false;
							} else {
								// does not match any criteria..
								return false;
							}
						}
					}

				} catch (Throwable e) {
					return false;
				}
			};
		} else if (DateMatcherType.MATCHES_DATE_IN_MM_DD_YYYY.equals(matcherType)) {
			return (ob) -> {
				try {
					String[] splitDateToBeMatched = toBeMatchedString.split("/");
					LocalDate toBeMatchedDate = LocalDate.now();

					if (splitDateToBeMatched.length == 3) {
						toBeMatchedDate = LocalDate.of(Integer.parseInt(splitDateToBeMatched[2]), Month.of(Integer.parseInt(splitDateToBeMatched[1])),
								Integer.parseInt(splitDateToBeMatched[0]));
					} else {
						splitDateToBeMatched = ob.toString().split("-");
						if (splitDateToBeMatched.length == 3) {
							toBeMatchedDate = LocalDate.of(Integer.parseInt(splitDateToBeMatched[2]), Month.of(Integer.parseInt(splitDateToBeMatched[1])),
									Integer.parseInt(splitDateToBeMatched[0]));
						} else {
							splitDateToBeMatched = ob.toString().split(":");
							if (splitDateToBeMatched.length == 3) {
								toBeMatchedDate = LocalDate.of(Integer.parseInt(splitDateToBeMatched[2]), Month.of(Integer.parseInt(splitDateToBeMatched[1])),
										Integer.parseInt(splitDateToBeMatched[0]));
							} else {
								return false;
							}
						}
					}

					String[] splitDate = ob.toString().split("/");
					if (splitDate.length == 3) {
						LocalDate date = LocalDate.of(Integer.parseInt(splitDate[2]), Month.of(Integer.parseInt(splitDate[1])), Integer.parseInt(splitDate[0]));
						if (date.compareTo(toBeMatchedDate) == 0) {
							return true;
						}
						return false;
					} else {
						splitDate = ob.toString().split("-");
						if (splitDate.length == 3) {
							LocalDate date = LocalDate.of(Integer.parseInt(splitDate[2]), Month.of(Integer.parseInt(splitDate[1])),
									Integer.parseInt(splitDate[0]));
							if (date.compareTo(toBeMatchedDate) == 0) {
								return true;
							}
							return false;
						} else {
							splitDate = ob.toString().split(":");
							if (splitDate.length == 3) {
								LocalDate date = LocalDate.of(Integer.parseInt(splitDate[2]), Month.of(Integer.parseInt(splitDate[1])),
										Integer.parseInt(splitDate[0]));
								if (date.compareTo(toBeMatchedDate) == 0) {
									return true;
								}
								return false;
							} else {
								// does not match any criteria..
								return false;
							}
						}
					}

				} catch (Throwable e) {
					return false;
				}
			};
		} else {
			// default case, no matcher found, for the selected path..
			return (ob) -> {
				return false;
			};
		}
	}

	@FunctionalInterface
	interface ThreeParameterFunction<A, B, C, R> {
		public R apply(A a, B b, C c);
	}

	// extracted value from response(its an object), boolean matcher

	public enum Type {
		JSON, XML, TEXT
	}

	public enum FunctionType {
		JSON_REQUESTMATCHER_WITH_STRING, JSON_REQUESTMATCHER_WITH_MATCHERS
	}

	public enum AttributeType {
		STRING, NUMBER, BOOLEAN, DATE, CUSTOM, STRING_ARRAY, NUMBER_ARRAY, BOOLEAN_ARRAY
	}
}
