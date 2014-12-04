package husart.solr.phone.filter;

import org.apache.lucene.analysis.TokenStream;
import org.apache.solr.analysis.BaseTokenFilterFactory;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;

public class PhoneNumberFilterFactory extends BaseTokenFilterFactory {
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
		final boolean skipInvalid = args.containsKey("skipInvalid") ? args.get(
				"skipInvalid").equals("true") : false;
		final boolean skipError = args.containsKey("skipError") ? args.get(
				"skipError").equals("true") : true;
		return new PhoneNumberFilter(ts, region, format, skipInvalid, skipError);
	}
}