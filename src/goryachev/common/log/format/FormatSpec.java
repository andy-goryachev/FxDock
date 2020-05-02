// Copyright © 2017-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.log.format;


/**
 * Log Format Spec.
 */
public enum FormatSpec
{
	/** catch-all for misconfigured specs, gets outputted as is */
	AS_IS,
	
	/** {{} encodes a { symbol */
	BRACE,
	
	/** {date.FORMAT} encodes a date with the FORMAT spec */
	DATE,
	
	/** {json} outputs a prettyfied JSON representation of an object */
	JSON,
	
	/** {} toString in normal order */
	PLAIN,
	
	/** {%...} encodes a printf-style String.format */
	PRINTF,
	
	/** {unicode} encodes a single code point from an integer or a decimal string */
	UNICODE,
	
	/** {unicode.ex} encodes a single code point from an integer or a hex string */
	UNICODE_HEX
}
