package husart.solr.phone.filter;


import java.util.Map;
import org.apache.lucene.analysis.TokenStream;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;

import org.apache.lucene.analysis.util.TokenFilterFactory;
public class PhoneNumberFilterFactory extends TokenFilterFactory {
	
	protected Map<String,String> args;
	
	public PhoneNumberFilterFactory(Map<String,String> args){
		super(args);
		this.args = args;	
	}
	
	@Override
	public TokenStream create(TokenStream ts) {
		
		final String region;
		if (this.args.containsKey("region")) {
			region = args.get("region");
		} else {
			region = "US";
		}
		
		final PhoneNumberFormat format;
		if (this.args.containsKey("format")) {
			switch (args.get("format").toUpperCase()) {
			case "NATIONAL":
				format = PhoneNumberFormat.NATIONAL;
				break;
			case "E164":
				format = PhoneNumberFormat.E164;
				break;

			default:
			case "INTERNATIONAL":
				format = PhoneNumberFormat.INTERNATIONAL;
				break;
			}
		} else {
			format = PhoneNumberFormat.INTERNATIONAL;
		}
		
		return new PhoneNumberFilter(ts, region, format, getBoolean(args, "skipInvalid", false), getBoolean(args, "skipError", true));
	}
}