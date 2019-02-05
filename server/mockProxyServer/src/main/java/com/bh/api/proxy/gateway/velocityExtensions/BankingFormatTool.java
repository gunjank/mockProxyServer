package com.bh.api.proxy.gateway.velocityExtensions;

import org.apache.velocity.tools.Scope;
import org.apache.velocity.tools.config.DefaultKey;
import org.apache.velocity.tools.config.ValidScope;
import org.apache.velocity.tools.generic.FormatConfig;

@DefaultKey("banking")
@ValidScope(Scope.APPLICATION)
public class BankingFormatTool extends FormatConfig{

//https://gist.github.com/josefeg/5781824
//place holder to put all banking specific helpers, aba routing, credit card number, expiration date, date in general, etc..		
	public Number cc_number()
    {
        return System.currentTimeMillis();
    }
	
	public Number bank_number()
    {
        return System.currentTimeMillis();
    }
	
	public Number aba_number()
    {
        return System.currentTimeMillis();
    }
	
	public Number expiration_date()
    {
        return System.currentTimeMillis();
    }
	
	public Number start_date()
    {
        return System.currentTimeMillis();
    }
	
	public Number current_date()
    {
        return System.currentTimeMillis();
    }
	
}
