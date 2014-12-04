package husart.solr.phone.filter;

import java.io.IOException;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

public final class PhoneNumberFilter extends TokenFilter {
	private CharTermAttribute charTermAttr;

	final protected String region;
	final protected PhoneNumberFormat format;
	final protected PhoneNumberUtil phoneUtil;
	final protected boolean skipInvalid;
	final protected boolean skipError;

	protected PhoneNumberFilter(TokenStream ts, final String region,
			final PhoneNumberFormat format, final boolean skipInvalid,
			final boolean skipError) {
		super(ts);
		this.charTermAttr = addAttribute(CharTermAttribute.class);

		this.region = region;
		this.format = format;
		this.skipInvalid = skipInvalid;
		this.skipError = skipError;

		phoneUtil = PhoneNumberUtil.getInstance();

	}

	@Override
	public boolean incrementToken() throws IOException {
		if (!input.incrementToken()) {
			return false;
		}

		final PhoneNumber pNumber;

		try {
			pNumber = phoneUtil.parse(charTermAttr.toString(), this.region);
		} catch (NumberParseException e) {
			if (!skipError) {
				charTermAttr.setEmpty();
			}
			return true;
		}
		charTermAttr.setEmpty();

		if (skipInvalid && !phoneUtil.isValidNumber(pNumber)) {
			return true;
		}
		charTermAttr.append(phoneUtil.format(pNumber, this.format));

		return true;
	}
}